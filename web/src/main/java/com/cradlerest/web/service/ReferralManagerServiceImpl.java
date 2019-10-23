package com.cradlerest.web.service;

import com.cradlerest.web.controller.ReferralController;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.service.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

	public ReferralManagerServiceImpl(PatientManagerService patientManagerService, UserRepository userRepository, ReferralRepository referralRepository, HealthCentreRepository healthCentreRepository) {
		this.patientManagerService = patientManagerService;
		this.userRepository = userRepository;
		this.referralRepository = referralRepository;
		this.healthCentreRepository = healthCentreRepository;
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

		String patientId = requestBody.get("patientId").textValue();
		logger.info("patientId: " + patientId);
		// String patientName = requestBody.get("patientName").textValue();
		// int patientAge = requestBody.get("patientAge").intValue();
		int systolic = requestBody.get("systolic").intValue();
		logger.info("systolic: " + systolic);
		int diastolic = requestBody.get("diastolic").intValue();
		logger.info("diastolic: " + diastolic);
		int heartRate = requestBody.get("heartRate").intValue();
		logger.info("heartRate: " + heartRate);
		int readingColourKey = requestBody.get("readingColour").intValue();
		logger.info("readingColourKey: " + readingColourKey);

		// "2019-10-19T23:20:11" => "2019-10-19 23:20:11",
		String timestamp = requestBody.get("timestamp").textValue().replace("T", " ");

		logger.info("timestamp: " + timestamp);
		String healthCentreName = requestBody.get("healthCentre").textValue();
		logger.info("healthCentreName: " + healthCentreName);
		String comments = requestBody.get("comments").textValue();

		Patient currentPatient = null;
		try {
			logger.info("start");
			currentPatient = patientManagerService.getPatientWithId(patientId);
			logger.info("end");
		} catch (EntityNotFoundException exception) {
			exception.printStackTrace();
			// TODO: No patient found, create new patient
			// We can either send another text message requesting more information
			// OR
			// request all necessary information from initial referral
		}

		Optional<User> currentVHT = null;
		try {
			currentVHT = userRepository.findByUsername(requestBody.get("VHT").textValue());
		} catch (UsernameNotFoundException exception) {
			exception.printStackTrace();
			// TODO: VHT not found, create new VHT?
		}

		Optional<HealthCentre> currentHealthCentre = null;

		try {
			currentHealthCentre = healthCentreRepository.findByName(healthCentreName);
		} catch (EntityNotFoundException exception) {
			exception.printStackTrace();
		}

		Reading currentReading = new ReadingBuilder()// I need this. I shouldn't need this.
				.pid(currentPatient.getId())
				.colour(ReadingColour.valueOf(readingColourKey))
				.diastolic(diastolic)
				.systolic(systolic)
				.heartRate(heartRate)
				.timestamp(timestamp)
				.build();

		patientManagerService.saveReading(currentReading);

		Referral currentReferral = new ReferralBuilder()
				.pid(currentPatient.getId())
				.vid(currentVHT.get().getId())
				.readingId(currentReading.getId())
				.healthCentre(currentHealthCentre.get().getName())
				.healthCentreNumber(currentHealthCentre.get().getHealthCentreNumber())
				.comments(comments)
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
	public List<Referral> findAllByHealthCentre(String healthCentreName) {
		return referralRepository.findAllByHealthCentre(healthCentreName);
	}
}
