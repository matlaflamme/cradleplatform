package com.cradlerest.web.service;

import com.cradlerest.web.controller.ReferralController;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.model.builder.ReadingViewBuilder;
import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

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

	public ReferralManagerServiceImpl(PatientManagerService patientManagerService,
									  UserRepository userRepository,
									  ReferralRepository referralRepository,
									  HealthCentreRepository healthCentreRepository,
									  ReadingManager readingManager) {
		this.patientManagerService = patientManagerService;
		this.userRepository = userRepository;
		this.referralRepository = referralRepository;
		this.healthCentreRepository = healthCentreRepository;
		this.readingManager = readingManager;
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

		Patient currentPatient = null;
		try {
			currentPatient = patientManagerService.getPatientWithId(patientId);
		} catch (EntityNotFoundException exception) {
			exception.printStackTrace();
			// TODO: No patient found, create new patient
			// We can either send another text message requesting more information
			// OR
			// request all necessary information from initial referral
		}

		Optional<User> currentVHT = userRepository.findByUsername(requestBody.get("VHT").textValue());
		Optional<HealthCentre> currentHealthCentre = healthCentreRepository.findByName(healthCentreName);
		if (currentVHT.isEmpty()) {
			throw new EntityNotFoundException("Not found: " + requestBody.get("VHT").textValue());
		}

		if (currentHealthCentre.isEmpty()) {
			throw new EntityNotFoundException("Not found: " + healthCentreName);
		}


		String[] symptomsArr = symptoms.replace("[", "").replace("]", "").split(",");
		// src https://stackoverflow.com/questions/9864568/how-to-trim-white-space-from-all-elements-in-array
		String[] symptomsArrNoTrailingWhiteSpace = Arrays.stream(symptomsArr).map(String::trim).toArray(String[]::new);

		// commented because it is returning null
		ReadingView readingView = new ReadingViewBuilder()
				.pid(currentPatient.getId())
				.pregnant(false)
				.colour(ReadingColour.valueOf(readingColourKey))
				.diastolic(diastolic)
				.systolic(systolic)
				.heartRate(heartRate)
				.timestamp(readingTimestamp)
				.symptoms(symptomsArrNoTrailingWhiteSpace)
				.build();

		Reading currentReading = readingManager.saveReadingView(readingView);

		Referral currentReferral = new ReferralBuilder()
				.pid(currentPatient.getId())
				.vid(currentVHT.get().getId())
				.readingId(currentReading.getId())
				.healthCentre(currentHealthCentre.get().getName())
				.healthCentreNumber(currentHealthCentre.get().getHealthCentreNumber())
				.comments(comments)
				.timestamp(referralTimestamp)
				.build();

		return referralRepository.save(currentReferral);
	}

	/**
	 * @return All referrals
	 */
	public List<Referral> findAll() { return referralRepository.findAll(); }

	/**
	 *
	 * @param healthCentreName
	 * @return All referrals from a health centre
	 */
	public List<Referral> findAllByHealthCentre(String healthCentreName) throws EntityNotFoundException{
		Optional<HealthCentre> healthCentre = healthCentreRepository.findByName(healthCentreName);
		if (healthCentre.isEmpty()) {
			throw new EntityNotFoundException("No health centre with name: " + healthCentreName);
		}
		return referralRepository.findAllByHealthCentre(healthCentreName);
	}

	public List<Referral> findAllByOrderByTimestampDesc() { return referralRepository.findAllByOrderByTimestampDesc(); }
}
