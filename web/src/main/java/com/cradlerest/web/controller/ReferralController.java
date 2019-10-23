package com.cradlerest.web.controller;

import com.cradlerest.web.model.*;
import com.cradlerest.web.service.ReferralManagerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
	private Logger logger = LoggerFactory.getLogger(ReferralController.class);
	private ReferralManagerService referralManagerService;

	public ReferralController(ReferralManagerService referralManagerService) {
		this.referralManagerService = referralManagerService;
	}

	/**
	 * Handles Twilio post request (VHT has no internet)
	 * TODO: Request validator. Right now, anyone can post to this url
	 * @see com.twilio.security.RequestValidator
	 * It's kinda bugged? Enabling it renders the endpoint inaccessible (always 403-forbidden)
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
	public Referral saveReferral(@RequestBody String request, HttpServletResponse response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// TODO: Handle exceptions, validate etc..
		JsonNode requestBody = mapper.readTree(request);
		Referral savedReferral = referralManagerService.saveReferral(requestBody);

		return savedReferral;
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
