package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.service.PatientManagerService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Handles referrals
 *
 * Twilio currently using: /api/twilio/uploadsms
 *
 */
@RestController
@RequestMapping("/api/referral/")
public class ReferralController {

	private UserRepository userRepository;
	private ReferralRepository referralRepository;
	private PatientManagerService patientManagerService;

	public ReferralController(PatientManagerService patientManagerService) {
		this.patientManagerService = patientManagerService;
	}

	/**
	 * Handles Twilio post request (VHT has no internet)
	 *
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
	 * 		"comments:":""
	 * }
	 *
	 *  For each referral the VHT has made, they can see:
	 *  Health centre referred to,
	 *  TODO: Distance from health centre,
	 *  TODO: Mode of transport to reach health centre
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

		// "2019-10-19T23:20:11" => "2019-10-19 23:20:11"
		String patientId = requestBody.get("patientId").textValue();
		String timestamp = requestBody.get("timestamp").textValue().replace("T", "");
		int systolic = requestBody.get("systolic").intValue();
		int diastolic = requestBody.get("diastolic").intValue();
		int heartRate = requestBody.get("heartRate").intValue();
		int readingColourKey = requestBody.get("readingColour").intValue();

		Patient currentPatient;
		try {
			currentPatient = patientManagerService.getPatientWithId(patientId);
		} catch (EntityNotFoundException exception) {
			// TODO: No patient found, create new patient
		}
		Reading currentReading = patientManagerService.saveReading(new Reading(patientId, systolic, diastolic, heartRate, ReadingColour.fromKey(readingColourKey), timestamp));

		// TODO: timestamp matches Reading entity timestamp
		// "2019-10-19T23:20:11" => "2019-10-19 23:20:11",
		System.out.println(requestBody);
		return "Success\n: " +
				"Health centre referred: " + requestBody.get("healthCentre");
	}

	/**
	 * Handle referral sent through HTTP request
	 *
	 * @param request
	 * @param response
	 * @return Response body
	 * @throws IOException
	 */
	@PostMapping(path = "/send")
	public String saveReferral(WebRequest request, HttpServletResponse response) throws IOException {
		// TODO
		return "Success";
	}
}
