package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.repository.HealthCentreRepository;
import com.cradlerest.web.service.repository.ReferralRepository;
import com.cradlerest.web.service.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.example.TwiMLResponseExample;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Message;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handles referrals
 *
 * Twilio currently using: /api/twilio/uploadsms
 *
 */
@RestController
@RequestMapping("/api/referral/")
public class ReferralController {
	private Logger logger = LoggerFactory.getLogger(ReferralController.class);
	private UserRepository userRepository; // VHT INFO
	private ReferralRepository referralRepository; // Saving referrals
	private HealthCentreRepository healthCentreRepository;
	private PatientManagerService patientManagerService; // Patients, Readings

	public ReferralController(PatientManagerService patientManagerService, UserRepository userRepository, ReferralRepository referralRepository, HealthCentreRepository healthCentreRepository) {
		this.patientManagerService = patientManagerService;
		this.userRepository = userRepository;
		this.referralRepository = referralRepository;
		this.healthCentreRepository = healthCentreRepository;
	}

	/**
	 * Handles Twilio post request (VHT has no internet)
	 * TODO: Request validator. Right now, anyone can post to this url
	 * Example request "Body":
	 * {
	 * 		"patientName":"JS",
	 * 		"patientId":"123",
	 * 		"patientAge":52,
	 * 		"gestationAge":180,
	 * 		"systolic":0,
	 * 		"diastolic":0,
	 * 		"heartRate":30,
	 * 		"readingColour":"In severe shock",
	 * 		"timestamp":"2019-10-19T23:20:11",
	 * 		"healthCentre":"Twilio",
	 * 		"VHT":"Jackson",
	 * 		"comments":"\"HHahHhaha\""
	 * }
	 *
	 *  For each referral the VHT has made, they can see:
	 *  Health centre referred to,
	 *  TODO: Distance from health centre,
	 *  TODO: Mode of transport to reach health centre
	 *  TODO: Repository exception handling
	 *
	 * @param request Twilio post response body: https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 * @param response
	 * @throws IOException
	 * @return A SMS from Twilio number to whoever sent the text.
	 */
	@PostMapping(path = "/send/sms", consumes = "application/x-www-form-urlencoded")
	public String saveReferralSMS(WebRequest request, HttpServletResponse response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		// **JsonNodes are immutable**
		// TODO: Handle exceptions, validate etc..
		JsonNode requestBody = mapper.readTree(request.getParameter("Body"));
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
			currentPatient = patientManagerService.getPatientWithId(patientId);
		} catch (EntityNotFoundException exception) {
			exception.printStackTrace();
			// TODO: No patient found, create new patient
			// We can either send another text message requesting more information
			// OR
			// request all necessary information from initial referral
		}
		System.out.println("patientId::" + currentPatient.getId());
		Optional<User> currentVHT = null;
		try {
			currentVHT = userRepository.findByUsername(requestBody.get("VHT").textValue());
		} catch (UsernameNotFoundException exception) {
			exception.printStackTrace();
			// TODO: VHT not found, create new VHT?
		}

		// TODO: Initializing DB with health centres, validating health centre name, handling exception
		//Optional<HealthCentre> currentHealthCentre = healthCentreRepository.findByName(healthCentreName);

		Reading currentReading = new ReadingBuilder()
				.pid(currentPatient.getId())
				.colour(ReadingColour.fromKey(readingColourKey))
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
				.healthCentre(healthCentreName)
				.healthCentreNumber("+2052052055")
				.comments(comments)
				.build();


		referralRepository.save(currentReferral);

		return "Success:\n " +
				"Health centre referred: " + healthCentreName;
	}

	/**
	 * Handle referral sent through HTTP request
	 *
	 * @param request
	 * @param response
	 * @return Response body
	 * @throws IOException
	 */
	@PostMapping("/send")
	public String saveReferral(WebRequest request, HttpServletResponse response) throws IOException {
		// TODO
		return "Success";
	}

	@GetMapping("/all")
	public @ResponseBody List<Referral> allReferrals() {
		return referralRepository.findAll();
	}

	@GetMapping("/{healthCentreName}/all")
	public @ResponseBody List<Referral> healthCentreReferrals(@PathVariable("healthCentreName") String healthCentreName) {
		return referralRepository.findAllByHealthCentre(healthCentreName);
	}

}
