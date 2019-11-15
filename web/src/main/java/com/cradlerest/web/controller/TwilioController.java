package com.cradlerest.web.controller;

import com.twilio.Twilio;

import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.monitor.v1.Alert;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.time.LocalDate;

/**
 *
 * Accessing twilio logs. Ideally this is not done by our API but this is just to make SMS testing simpler.
 *
 * Currently returns the most recent twilio log (SMS)
 */
@RestController
@RequestMapping("/api/twilio")
public class TwilioController {
	@Value("${twilio.account_sid}")
	private String account_sid;

	@Value("${twilio.auth_token}")
	private String auth_token;

	// Anytime SMS is sent to/from Twilio a log is created
	@GetMapping("/logs")
	public @ResponseBody String getLogs() {
		Twilio.init(account_sid, auth_token);
		ResourceSet<Message> messages = Message.reader().read();
		for (Message record : messages) {
			return record.toString();
		}
		return "x";
	}

	// Alerts are errors
	@GetMapping("/alerts")
	public @ResponseBody String getAlerts() {
		Twilio.init(account_sid, auth_token);

		ResourceSet<Alert> alerts = Alert.reader().read();

		for (Alert alert : alerts) {
			return alert.toString();
		}
		return "x";
	}
}
