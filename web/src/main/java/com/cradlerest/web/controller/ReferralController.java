package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.ReferralManagerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
	 *
	 * @param request Twilio post response body: https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 * @return A SMS from Twilio number to whoever sent the text.
	 * @throws Exception
	 * @see com.twilio.security.RequestValidator
	 * It's kinda bugged? Enabling it renders the endpoint inaccessible (always 403-forbidden)
	 * <p>
	 * Example of a valid request "Body":
	 * <p>
	 * <p>
	 *  {
	 *   "referrerUserName": "",
	 *   "healthCentrePhoneNumber": ""
	 *   "reading": {
	 *      "patientId": "001",
	 *      "systolic": 25,
	 *      "diastolic": 20,
	 *      "heartRate": 30,
	 *      "colour":0,
	 *      "pregnant":true,
	 *      "gestationalAge":90,
	 *      "symptoms":["Headache"],
	 *      "timestamp":"2019-10-24 09:32:10"
	 *    }
	 *  }
	 * <p>
	 * For each referral the VHT has made, they can see:
	 * Health centre referred to,
	 * TODO: Distance from health centre,
	 * TODO: Mode of transport to reach health centre
	 * TODO: Repository exception handling
	 */
	@PostMapping(path = "/send/sms", consumes = "application/x-www-form-urlencoded")
	public String saveReferralSMS(WebRequest request, HttpServletResponse response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// TODO: Handle exceptions, validate etc..
		JsonNode requestBody = mapper.readTree(request.getParameter("Body"));
		Referral savedReferral = null;
		try {
			savedReferral = referralManagerService.saveReferral(requestBody);
			return "Success:\n " +
					"Health centre referred: " + savedReferral.getHealthCentrePhoneNumber();
		} catch (Exception exception) {
			return "There was an error processing your referral: " + exception.getMessage();
		}
	}

	/**
	 * Handle referral sent through HTTP request
	 *
	 * @param httpEntity https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html
	 * @return Response body
	 * @throws Exception
	 */
	@PostMapping("/send")
	public String saveReferral(HttpEntity<String> httpEntity, HttpServletResponse response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		// TODO: Handle exceptions, validate etc..
		JsonNode requestBody = mapper.readTree(httpEntity.getBody());
		Referral savedReferral = null;
		try {
			savedReferral = referralManagerService.saveReferral(requestBody);
			return "Success:\n " +
					"Health centre referred: " + savedReferral.getHealthCentrePhoneNumber();
		} catch (Exception exception) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return exception.getMessage();
		}
	}

	/**
	 * Returns all referrals sorted by timestamp in descending order
	 *
	 * @return
	 */
	@GetMapping("/all")
	public @ResponseBody
	List<ReferralView> allReferralsSortByTimestamp() {
		return referralManagerService.findAllByOrderByTimestampDesc();
	}

	@GetMapping("/{healthCentreName}/all")
	public @ResponseBody
	List<ReferralView> healthCentreReferrals(@PathVariable("healthCentreName") String healthCentreName) throws EntityNotFoundException {
		return referralManagerService.findAllByHealthCentre(healthCentreName);
	}


	@PostMapping("/new")
	public Referral saveReferral(@RequestBody Referral referral) throws Exception {
		return referralManagerService.saveReferral(referral);

	}
}