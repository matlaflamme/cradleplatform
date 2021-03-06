package com.cradlerest.web.service;

import com.cradlerest.web.controller.ReferralController;
import com.cradlerest.web.controller.exceptions.AccessDeniedException;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;

import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.repository.*;
import com.cradlerest.web.util.BitmapEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maumay.jflow.vec.Vec;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

import static com.cradlerest.web.util.AuthenticationExt.hasRole;

/**
 * Implements ReferralManagerService
 *
 * Still a lot to be done...
 * @see ReferralController
 */
@Service
public class ReferralManagerServiceImpl implements ReferralManagerService {

	private Logger logger = LoggerFactory.getLogger(ReferralManagerServiceImpl.class);
	private ReferralRepository referralRepository; // Saving referrals
	private HealthCentreRepository healthCentreRepository;
	private ReadingManager readingManager;
	private PatientRepository patientRepository;
	private PatientManagerService patientManagerService;
	private UserRepository userRepository;
	private DiagnosisRepository diagnosisRepository;

	public ReferralManagerServiceImpl(ReferralRepository referralRepository,
									  HealthCentreRepository healthCentreRepository,
									  PatientRepository patientRepository,
									  ReadingManager readingManager,
									  PatientManagerService patientManagerService,
									  UserRepository userRepository,
									  DiagnosisRepository diagnosisRepository) {
		this.referralRepository = referralRepository;
		this.healthCentreRepository = healthCentreRepository;
		this.patientRepository = patientRepository;
		this.readingManager = readingManager;
		this.patientManagerService = patientManagerService;
		this.userRepository = userRepository;
		this.diagnosisRepository = diagnosisRepository;
	}

	/**
	 * @param healthCentreName
	 * @return All referrals from a health centre
	 */
	@Override
	public List<ReferralView> findAllByHealthCentre(String healthCentreName) throws EntityNotFoundException {
		// TODO: findByName will throw a NonUniqueResultException if there are >1 health centre with same name.
		Optional<HealthCentre> healthCentre = healthCentreRepository.findByName(healthCentreName);
		if (healthCentre.isEmpty()) {
			throw new EntityNotFoundException("No health centre with name: " + healthCentreName);
		}
		return Vec.copy(referralRepository.findAllByHealthCentreId(healthCentre.get().getId()))
				.map(this::computeReferralView)
				.toList();
	}

	@Override
	public List<ReferralView> findAllByOrderByTimestampDesc() {
		return Vec.copy(referralRepository.findAllByOrderByTimestampDesc())
				.map(this::computeReferralView)
				.toList();
	}

	@Override
	public List<ReferralView> allReferrals(@NotNull Authentication auth) {
		assert auth.getPrincipal() instanceof UserDetailsImpl;
		var details = (UserDetailsImpl) auth.getPrincipal();

		if (hasRole(auth, UserRole.HEALTH_WORKER)) {
			var hcId = details.getWorksAtHealthCentreId();

			// If a health worker is not assigned to a health centre then return
			// an empty list of referrals.
			if (hcId == null) {
				return new ArrayList<>();
			}

			// Otherwise, return all referrals made to the health centre that they
			// work at.
			return Vec.copy(referralRepository.findAllByHealthCentreId(hcId))
					.map(this::computeReferralView)
					.toList();
		} else if (hasRole(auth, UserRole.VHT)) {
			// If the user is a VHT, return all of the referrals that the VHT has made.
			var username = details.getUsername();
			return Vec.copy(referralRepository.findAllByReferrerUserName(username))
					.map(this::computeReferralView)
					.toList();
		} else {
			return new ArrayList<>();
		}
	}

	private ReferralView computeReferralView(@NotNull Referral r) {
		var optHc = healthCentreRepository.findById(r.getHealthCentreId());
		if (optHc.isEmpty()) {
			throw new RuntimeException("unable to find health center: constraint violation");
		}
		var hc = optHc.get();

		Diagnosis d = null;
		if ( r.getDiagnosisId() != null) {
			var optDiagnosis = diagnosisRepository.findById(r.getDiagnosisId());
			if (optDiagnosis.isPresent()) {
				d = optDiagnosis.get();
			}
		}

		return ReferralView.fromReferral(r, hc, d);
	}



	private InputStream retrieveMediaFromTwilio(Map<String, String[]> parameters) throws Exception {
		String numMediaStr = parameters.get("NumMedia")[0];
		int numMedia = Integer.parseInt(numMediaStr);

		if (numMedia > 0) {
			numMedia = numMedia - 1;
			String mediaUrl = parameters.get(String.format("MediaUrl%d", numMedia))[0];
			URL url = new URL(mediaUrl);
			CloseableHttpClient httpclient = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			HttpGet get = new HttpGet(url.toURI());
			HttpResponse twilioResponse = httpclient.execute(get);
			return twilioResponse.getEntity().getContent();
		}
		else {
			throw new BadRequestException("No media found in message");
		}
	}

	private Referral getReferralFromMessage(ReferralMessage referralMessage) throws Exception {
		validateReferralMessage(referralMessage);

		// Get user
		Optional<User> userDetails = userRepository.findByUsername(referralMessage.getReferrerUserName());
		if (userDetails.isEmpty()){
			throw new EntityNotFoundException("User is invalid");
		}

		var details = userDetails.get();

		// Get Health Centre
		Optional<HealthCentre> healthCentre = healthCentreRepository.findByPhoneNumber(
				referralMessage.getHealthCentrePhoneNumber());
		if (healthCentre.isEmpty()){
			throw new EntityNotFoundException("Health centre is invalid!");
		}

		// Create or update patient information
		Patient patient = referralMessage.getPatient();
		Optional<Patient> patientOptional = patientRepository.findById(referralMessage.getPatientId());
		if (patientOptional.isEmpty()){
			patient.setId(referralMessage.getPatientId());
			patient.setCreatedBy(details.getId());
			patient = patientManagerService.savePatientWithUser(details, patient);
		}
		else {
			patient = patientOptional.get();
		}

		// Create Reading
		ReadingView readingView = referralMessage.getReadingView();
		readingView.setCreatedBy(details.getId());
		readingView.setPatientId(referralMessage.getPatientId());
		Reading reading = readingManager.saveReadingView(null, readingView);

		// Create Referral object
		return new ReferralBuilder()
				.referredByUsername(referralMessage.getReferrerUserName())
				.referredToHealthCentreId(healthCentre.get().getId())
				.timestamp(referralMessage.getTimestamp())
				.readingId(reading.getId())
				.patientId(patient.getId())
				.build();
	}

	/**
	 * Saves a referral sent from Twilio
	 * Corresponding reading and patient is saved in their own tables
	 */
	@Override
	public Referral saveReferral(Map<String, String[]> parameters) throws Exception {

		InputStream referralBytes = retrieveMediaFromTwilio(parameters);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BitmapEncoder.decodeFromBitmap(referralBytes, bos );
		String referralString = bos.toString(StandardCharsets.UTF_8);

		ObjectMapper objectMapper = new ObjectMapper();
		ReferralMessage referralMessage = objectMapper.readValue(referralString, ReferralMessage.class);

		Referral referral = getReferralFromMessage(referralMessage);
		return referralRepository.save(referral);
	}

	/**
	 * Saves a referral sent with JSON request
	 * Corresponding reading and patient is saved in their own tables
	 */
	@Override
	public Referral saveReferral(Authentication auth, @Nullable ReferralMessage referralMessage) throws Exception {
		// Create Referral object
		Referral referral = getReferralFromMessage(referralMessage);
		return referralRepository.save(referral);
	}

	/**
	 * Saves a referral sent with JSON request
	 * only saves referral
	 */
	@Override
	public Referral saveReferral(Authentication auth, Referral referral) throws Exception {
		assert auth.getPrincipal() instanceof UserDetailsImpl;
		var details = (UserDetailsImpl) auth.getPrincipal();
		String username = details.getUsername();
		if (username == null) {
			throw new AccessDeniedException("invalid user credentials");
		}
		validateReferral(referral);
		referral.setTimestamp(new Timestamp(new Date().getTime()));
		referral.setReferrerUserName(username);
		// Create Referral object
		return referralRepository.save(referral);
	}

	@Override
	public Referral resolveReferral(Authentication auth, int id) throws Exception {
		// Get user details
		assert auth.getPrincipal() instanceof UserDetailsImpl;
		var userDetails = (UserDetailsImpl) auth.getPrincipal();
		Integer userId = userDetails.getId();
		if (userId == null) {
			throw new AccessDeniedException("Invalid user credentials");
		}

		Optional<Referral> referralOptional = referralRepository.findById(id);
		if (referralOptional.isEmpty()) {
			throw new EntityNotFoundException("Referral not found");
		}

		// If referral is resolved already, don't do it again
		Referral referral = referralOptional.get();
		if (referral.getReviewerUserId() != null) {
			return referral;
		}

		referral.setClosed(new Timestamp(new Date().getTime()));
		referral.setReviewerUserId(userId);
		return referralRepository.save(referral);
	}

	@Override
	public Diagnosis addDiagnosis(Authentication auth, int referralId, Diagnosis diagnosis) throws Exception {
		Optional<Referral> referralCheck = referralRepository.findById(referralId);
		if (referralCheck.isEmpty()) {
			throw new EntityNotFoundException("Referral does not exist");
		}

		// save diagnosis
		validateDiagnosis(diagnosis);
		diagnosis.setResolved(false);
		diagnosis = diagnosisRepository.save(diagnosis);

		// update referral
		Referral referral = referralCheck.get();
		referral.setDiagnosisId(diagnosis.getId());
		referralRepository.save(referral);
		return diagnosis;
	}

	private void validateReferral(Referral referral) throws BadRequestException {
		assertNotNull(referral.getHealthCentreId(), "healthCentreId");
		assertNotNull(referral.getPatientId(), "patientId");
		assertNotNull(referral.getReadingId(), "readingId");
	}

	private void validateDiagnosis(@NotNull Diagnosis diagnosis) throws BadRequestException {
		assertNotNull(diagnosis.getDescription(), "description");
		assertNotNull(diagnosis.getPatientId(), "patientId");
	}

	private void validateReferralMessage(@NotNull ReferralMessage referral) throws BadRequestException {
		assertNotNull(referral, "referral");
		assertNotNull(referral.getReadingView(), "reading");
		assertNotNull(referral.getPatient(), "patient");
		assertNotNull(referral.getReferrerUserName(), "userName");
		assertNotNull(referral.getHealthCentrePhoneNumber(), "healthCentrePhoneNumber");
	}

	private void assertNotNull(@Nullable Object field, @NotNull String fieldName) throws BadRequestException {
		if (field == null) {
			throw BadRequestException.missingField(fieldName);
		}
	}
}
