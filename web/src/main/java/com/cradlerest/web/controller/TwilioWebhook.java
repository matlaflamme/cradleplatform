package com.cradlerest.web.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Used to test Twilio webhook / handle SMS referral
 * currently using: /api/twilio/uploadsms
 *
 */
@RestController
@RequestMapping("/api/twilio")
public class TwilioWebhook {

	private UserRepository userRepository;
	private ReferralRepository referralRepository;
	private PatientManagerService patientManagerService;

	public TwilioWebhook(PatientManagerService patientManagerService) {
		this.patientManagerService = patientManagerService;
	}

	/**
	 * Returns a string as an SMS reply (this replies to the sender with a text)
	 * https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 *
	 * Example body:
	 * {
	 * 		"patientName":"JS",
	 * 		"patientId":"123",
	 * 		"patientAge":52,
	 * 		"gestationAge":180,
	 * 		"bloodPressure":0,
	 * 		"heartRate":30,
	 * 		"analysis":"In severe shock",
	 * 		"timestamp":
	 * 		"2019-10-19T23:20:11",
	 * 		"healthCentre":"Twilio",
	 * 		"VHT":"Jackson",
	 * 		"comments:":""
	 * }
	 *
	 * @param request Twilio post response body: https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 * @param response
	 * @throws IOException
	 * @return A SMS from Twilio number to whoever sent the text.
	 */
	@PostMapping(path = "/uploadsms", consumes = "application/x-www-form-urlencoded")
	public String respondToSmS(WebRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		// Parses request body (body of text send by VHT)
		// TODO: Handle exceptions, validate etc..
		JsonNode requestBody = mapper.readTree(request.getParameter("Body"));

		// TODO: timestamp matches Reading entity timestamp
		// "2019-10-19T23:20:11" => "2019-10-19 23:20:11",
		System.out.println(requestBody);
		return "Success\n: " + requestBody.toString();
	}
}
