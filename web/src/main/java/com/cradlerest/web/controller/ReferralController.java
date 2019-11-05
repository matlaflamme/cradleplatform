package com.cradlerest.web.controller;

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
import java.io.PrintWriter;
import java.io.StringWriter;
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
	 * @see com.twilio.security.RequestValidator
	 * It's kinda bugged? Enabling it renders the endpoint inaccessible (always 403-forbidden)
	 *
	 * Example of a valid request "Body":
	 * {
	 *     "patientName": "HY",
	 *     "patientId": "001",
	 *     "patientAge": 15,
	 *     "gestationAge": 60,
	 *     "systolic": 25,
	 *     "diastolic": 20,
	 *     "heartRate": 30,
	 *     "readingColour": 1,
	 *     "timestamp": "2019-10-23T02:25:43.453",
	 *     "healthCentre": "Twilio",
	 * 	   "VHT": "vht",
	 *     "comments": ""
	 * }
	 *
	 *  For each referral the VHT has made, they can see:
	 *  Health centre referred to,
	 *  TODO: Distance from health centre,
	 *  TODO: Mode of transport to reach health centre
	 *  TODO: Repository exception handling
	 *
	 * @param request Twilio post response body: https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 * @throws Exception
	 * @return A SMS from Twilio number to whoever sent the text.
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
					"Health centre referred: " + savedReferral.getReferredToHealthCenterId();
		} catch (Exception exception) {
			// temporay error response
			// should try to give VHT more detail on the error
			return "There was an error processing your referral";
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
					"Health centre referred: " + savedReferral.getReferredToHealthCenterId();
		} catch (Exception exception) {
			// temporay error response
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return stackTraceString(exception);
		}
	}

	/**
	 * Returns all referrals sorted by timestamp in descending order
	 * @return
	 */
	@GetMapping("/all")
	public @ResponseBody List<ReferralView> allReferralsSortByTimestamp() {
		return referralManagerService.findAllByOrderByTimestampDesc();
	}

	@GetMapping("/{healthCentreName}/all")
	public @ResponseBody List<ReferralView> healthCentreReferrals(@PathVariable("healthCentreName") String healthCentreName) throws EntityNotFoundException {
		return referralManagerService.findAllByHealthCentre(healthCentreName);
	}

	// Returns stack trace for exception as string
	private String stackTraceString(Exception exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		return stringWriter.toString();
	}

}
