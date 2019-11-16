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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Accessing twilio logs. Ideally this is not done by our API but this is just to make SMS testing simpler.
 *
 * Returns ALL logs from the beginning of time
 */
@RestController
@RequestMapping("/api/twilio")
public class TwilioController {
	@Value("${twilio.account_sid}")
	private String account_sid;

	@Value("${twilio.auth_token}")
	private String auth_token;


	/**
	 * Fetches SMS logs from Twilio account (security details above)
	 *
	 * For testing purposes as having the auth_token available is not secure.
	 *
	 * @return
	 */
	@GetMapping("/logs")
	public @ResponseBody List<Message> getLogs() {
		Twilio.init(account_sid, auth_token);
		ResourceSet<Message> messages = Message.reader().read();

		// Converting ResourceSet to List
		List<Message> messagesList = new ArrayList<>();

		Iterator<Message> messagesIterator = messages.iterator();
		while (messagesIterator.hasNext()) {
			messagesList.add(messagesIterator.next());
		}
		return messagesList;
	}

	// Alerts are errors
	// TODO: Currently outputs 2008 API format instead of 2010 API. Not sure why...
	@GetMapping("/alerts")
	public @ResponseBody List<Alert> getAlerts() {
		Twilio.init(account_sid, auth_token);
		ResourceSet<Alert> alerts = Alert.reader().read();

		// Converting ResourceSet to list
		List<Alert> alertsList = new ArrayList<>();

		Iterator<Alert> alertsIterator = alerts.iterator();
		while (alertsIterator.hasNext()) {
			alertsList.add(alertsIterator.next());
		}
		return alertsList;
	}
}
