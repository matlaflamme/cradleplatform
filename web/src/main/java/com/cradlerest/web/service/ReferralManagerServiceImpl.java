package com.cradlerest.web.service;

import com.cradlerest.web.controller.ReferralController;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.builder.ReadingViewBuilder;
import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cradlerest.web.util.CopyFields.copyFields;

/**
 * Implements ReferralManagerService
 *
 * Still a lot to be done...
 * @see ReferralController
 */
@Service
public class ReferralManagerServiceImpl implements ReferralManagerService {

	private Logger logger = LoggerFactory.getLogger(ReferralManagerServiceImpl.class);
	private UserRepository userRepository; // VHT INFO
	private ReferralRepository referralRepository; // Saving referrals
	private HealthCentreRepository healthCentreRepository;
	private PatientManagerService patientManagerService; // Patients, Readings
	private ReadingManager readingManager;
	private ReadingRepository readingRepository;

	public ReferralManagerServiceImpl(PatientManagerService patientManagerService,
									  UserRepository userRepository,
									  ReferralRepository referralRepository,
									  HealthCentreRepository healthCentreRepository,
									  ReadingManager readingManager,
									  ReadingRepository readingRepository
	) {
		this.patientManagerService = patientManagerService;
		this.userRepository = userRepository;
		this.referralRepository = referralRepository;
		this.healthCentreRepository = healthCentreRepository;
		this.readingManager = readingManager;
		this.readingRepository = readingRepository;
	}

	/**
	 * Saves a referral
	 * Corresponding reading is saved to Reading table
	 *
	 * @param requestBody IS IMMUTABLE
	 * @return
	 * @throws Exception
	 */
	@Override
	public Referral saveReferral(JsonNode requestBody) throws Exception {
		System.out.println(requestBody.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		String patientId = requestBody.get("patientId").textValue();
		// String patientName = requestBody.get("patientName").textValue();
		// int patientAge = requestBody.get("patientAge").intValue();
		int systolic = requestBody.get("systolic").intValue();
		int diastolic = requestBody.get("diastolic").intValue();
		int heartRate = requestBody.get("heartRate").intValue();
		int readingColourKey = requestBody.get("readingColour").intValue();

		//https://stackoverflow.com/questions/39237835/jackson-jsonnode-to-typed-collection
		String symptoms = requestBody.get("symptoms").textValue();
		// "2019-10-19T23:20:11" => "2019-10-19 23:20:11",
		String readingTimestamp = requestBody.get("readingTimestamp").textValue();
		String referralTimestamp = requestBody.get("referralTimestamp").textValue();
		String healthCentreName = requestBody.get("healthCentre").textValue();
		String comments = requestBody.get("comments").textValue();
		logger.info("Referral data \n" +
				"patientId: " + patientId + "\n" +
				"systolic: " + systolic + "\n" +
				"diastolic: " + diastolic + "\n" +
				"heartRate: " + heartRate + "\n" +
				"readingColourKey: " + readingColourKey + "\n" +
				"symptoms: " + symptoms + " \n" +
				"readingTimestamp: " + readingTimestamp + "\n" +
				"referralTimestamp: " + referralTimestamp + "\n" +
				"healthCentreName: " + healthCentreName + "\n" +
				"comments: " + comments);

		// TODO: No patient found, create new patient
		// We can either send another text message requesting more information
		// OR
		// request all necessary information from initial referral
		Patient currentPatient = patientManagerService.getPatientWithId(patientId);

		Optional<User> currentVHT = userRepository.findByUsername(requestBody.get("VHT").textValue());
		Optional<HealthCentre> currentHealthCentre = healthCentreRepository.findByPhoneNumber(healthCentreName);
		if (currentVHT.isEmpty()) {
			throw new EntityNotFoundException("VHT username not found: " + requestBody.get("VHT").textValue());
		}

		if (currentHealthCentre.isEmpty()) {
			throw new EntityNotFoundException("Health centre not found: " + healthCentreName);
		}


		String[] symptomsArr = symptoms.replace("[", "").replace("]", "").split(",");

		String[] symptomsArrNoTrailingWhiteSpace = Arrays.stream(symptomsArr).map(String::trim).toArray(String[]::new);

		ReadingView readingView = new ReadingViewBuilder()
				.pid(currentPatient.getId())
				.pregnant(false) // TODO: Info not sent through referral at the moment
				.colour(ReadingColour.valueOf(readingColourKey))
				.diastolic(diastolic)
				.systolic(systolic)
				.heartRate(heartRate)
				.timestamp(readingTimestamp)
				.symptoms(symptomsArrNoTrailingWhiteSpace) // TODO: Symptoms are not saved! BUG
				.build();

		Reading currentReading = readingManager.saveReadingView(readingView);

		Referral currentReferral = new ReferralBuilder()
				.referredByUsername(currentVHT.get().getUsername())
				.referredToHealthCentrePhoneNumber(currentHealthCentre.get().getPhoneNumber())
//				.readingId(currentReading.getId())
				.timestamp(referralTimestamp)
				.build();

		return referralRepository.save(currentReferral);
	}

	/**
	 * @param healthCentreName
	 * @return All referrals from a health centre
	 */
	@Override
	public List<ReferralView> findAllByHealthCentre(String healthCentreName) throws EntityNotFoundException {
		Optional<HealthCentre> healthCentre = healthCentreRepository.findByName(healthCentreName);
		if (healthCentre.isEmpty()) {
			throw new EntityNotFoundException("No health centre with name: " + healthCentreName);
		}
		return Vec.copy(referralRepository.findAllByHealthCentrePhoneNumber(healthCentre.get().getPhoneNumber()))
				.map(this::computeReferralView)
				.toList();
	}

	@Override
	public List<ReferralView> findAllByOrderByTimestampDesc() {
		return Vec.copy(referralRepository.findAllByOrderByTimestampDesc())
				.map(this::computeReferralView)
				.toList();
	}

	private ReferralView computeReferralView(@NotNull Referral r) {
		var optHc = healthCentreRepository.findByPhoneNumber(r.getHealthCentrePhoneNumber());
		if (optHc.isEmpty()) {
			throw new RuntimeException("unable to find health center: constraint violation");
		}
		var hc = optHc.get();

		return ReferralView.fromReferral(r, hc.getName(), hc.getPhoneNumber(), r.getPatientId());
	}

	@Override
	public Referral saveReferral(@Nullable Referral referral) throws BadRequestException, EntityNotFoundException {
		validateReferral(referral);

		ReadingView readingView = new ReadingView();
		copyFields(referral.getReading(), readingView);
		Reading reading = readingManager.saveReadingView(readingView);

		referral.setTimestamp(reading.getTimestamp());
		referral.setPatientId(reading.getPatientId());
		referral.setReading(reading);

		return referralRepository.save(referral);
	}

	private void validateReferral(@NotNull Referral referral) throws BadRequestException {
		assertNotNull(referral, "referral");
		assertNotNull(referral.getReading(), "reading");
		assertNotNull(referral.getReferrerUserName(), "referral");
		assertNotNull(referral.getHealthCentrePhoneNumber(), "reading");
	}

	private void assertNotNull(@Nullable Object field, @NotNull String fieldName) throws BadRequestException {
		if (field == null) {
			throw BadRequestException.missingField(fieldName);
		}
	}
}
