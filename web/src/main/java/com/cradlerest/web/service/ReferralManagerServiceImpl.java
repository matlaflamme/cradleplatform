package com.cradlerest.web.service;

import com.cradlerest.web.controller.ReferralController;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;

import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.repository.*;
import com.cradlerest.web.util.BitmapEncoder;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.sql.Ref;
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
	private ReferralRepository referralRepository; // Saving referrals
	private HealthCentreRepository healthCentreRepository;
	private ReadingManager readingManager;
	private PatientManagerService patientManagerService;

	public ReferralManagerServiceImpl(ReferralRepository referralRepository,
									  HealthCentreRepository healthCentreRepository,
									  ReadingManager readingManager,
									  PatientManagerService patientManagerService) {
		this.referralRepository = referralRepository;
		this.healthCentreRepository = healthCentreRepository;
		this.readingManager = readingManager;
		this.patientManagerService = patientManagerService;
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



	private InputStream retrieveMediaFromTwilio(Map<String, String[]> parameters) throws Exception {
		String numMediaStr = parameters.get("NumMedia")[0];
		int numMedia = Integer.parseInt(numMediaStr);

		InputStream source;
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
		Patient patient = patientManagerService.savePatient(referralMessage.getPatient());
		Reading reading = readingManager.saveReadingView(referralMessage.getReadingView());

		// Create Referral object
		return new ReferralBuilder()
				.referredByUsername(referralMessage.getReferrerUserName())
				.referredToHealthCentrePhoneNumber(referralMessage.getHealthCentrePhoneNumber())
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
		String referralString = new String(BitmapEncoder.decodeFromBitmap(referralBytes));

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
	public Referral saveReferral(@Nullable ReferralMessage referralMessage) throws Exception {
		// Create Referral object
		Referral referral = getReferralFromMessage(referralMessage);
		return referralRepository.save(referral);
	}

	private void validateReferralMessage(@NotNull ReferralMessage referral) throws BadRequestException {
		assertNotNull(referral, "referral");
		assertNotNull(referral.getReadingView(), "reading");
		assertNotNull(referral.getPatient(), "patient");
		assertNotNull(referral.getReferrerUserName(), "userName");
		assertNotNull(referral.getHealthCentrePhoneNumber(), "healthCentrePhoneNumber");
		assertNotNull(referral.getTimestamp(),"timestamp");
	}

	private void assertNotNull(@Nullable Object field, @NotNull String fieldName) throws BadRequestException {
		if (field == null) {
			throw BadRequestException.missingField(fieldName);
		}
	}
}
