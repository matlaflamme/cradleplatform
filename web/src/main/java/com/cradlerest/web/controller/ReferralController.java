package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.ReferralManagerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
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
	private static final String TWILIO_SID = "";
	private static final String TWILIO_PWD = "";


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
	 *   "referrerUserName": "vht",
	 *   "healthCentrePhoneNumber": "555555555",
	 *   "timestamp":"2019-10-24 09:32:10",
	 *   "patient": {
	 *       "id":"001",
	 * 	     "sex":0,
	 * 	     "zoneNumber":0,
	 * 	     "villageNumber":8,
	 *       "birthYear":1995,
	 * 	     "name":"sdd"
	 *   }
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
	public Referral saveReferralSMS(WebRequest request) throws Exception {
		Map<String, String[]> parameters = request.getParameterMap();
		return referralManagerService.saveReferral(parameters);
	}

	@PostMapping("/send")
	public Referral saveReferral(@RequestBody ReferralMessage referral) throws Exception {
		return referralManagerService.saveReferral(referral);

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



}