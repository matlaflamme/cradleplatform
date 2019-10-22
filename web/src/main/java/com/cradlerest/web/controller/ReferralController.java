package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.model.builder.ReferralBuilder;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReferralManagerService;
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
	private ReferralManagerService referralManagerService;

	public ReferralController(ReferralManagerService referralManagerService) {
		this.referralManagerService = referralManagerService;
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
		Referral savedReferral = referralManagerService.saveReferral(requestBody);

		return "Success:\n " +
				"Health centre referred: " + savedReferral.getHealthCentre();
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
		return referralManagerService.findAll();
	}

	@GetMapping("/{healthCentreName}/all")
	public @ResponseBody List<Referral> healthCentreReferrals(@PathVariable("healthCentreName") String healthCentreName) {
		return referralManagerService.findAllByHealthCentre(healthCentreName);
	}

}
