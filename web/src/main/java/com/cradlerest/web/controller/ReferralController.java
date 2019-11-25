package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.AccessDeniedException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.*;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.ReferralManagerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
	 *   "u": "vht",			- user name of referrer
	 *   "h": "555555555",      - health centre phone number
	 *   "i": "001"				- patient id
	 *   "p": {					- patient
	 * 	     "sex":0,
	 *       "birthYear":1995,
	 * 	     "name":"sdd"
	 *   }
	 *   "r": {					- reading
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
	 */
	@PostMapping(path = "/send/sms", consumes = "application/x-www-form-urlencoded")
	public Referral saveReferralSMS(WebRequest request) throws Exception {
		Map<String, String[]> parameters = request.getParameterMap();
		return referralManagerService.saveReferral(parameters);
	}

	@PostMapping("/send")
	public Referral saveReferral(Authentication auth, @RequestBody ReferralMessage referral) throws Exception {
		return referralManagerService.saveReferral(auth, referral);
	}

	@PostMapping("/new")
	public Referral saveReferral(Authentication auth, @RequestBody Referral referral) throws Exception {
		return referralManagerService.saveReferral(auth, referral);
	}

	@PostMapping("/{id}/resolve")
	public Referral resolveReferral(Authentication auth, @PathVariable("id") int referralId) throws Exception {
		assert auth != null;
		return referralManagerService.resolveReferral(auth, referralId);
	}

	@PostMapping("{id}/diagnosis")
	public Diagnosis addDiagnosis(Authentication auth, @PathVariable("id") int referralId, @RequestBody Diagnosis diagnosis) throws Exception {
		assert auth != null;
		return referralManagerService.addDiagnosis(auth, referralId, diagnosis);
	}

	/**
	 * Returns all referrals sorted by timestamp in descending order
	 *
	 * @return
	 */
	@GetMapping("/all")
	public @ResponseBody
	List<ReferralView> all(Authentication auth) {
		return referralManagerService.allReferrals(auth);
	}

	@GetMapping("/{healthCentreName}/all")
	public @ResponseBody
	List<ReferralView> healthCentreReferrals(@PathVariable("healthCentreName") String healthCentreName) throws EntityNotFoundException {
		return referralManagerService.findAllByHealthCentre(healthCentreName);
	}



}