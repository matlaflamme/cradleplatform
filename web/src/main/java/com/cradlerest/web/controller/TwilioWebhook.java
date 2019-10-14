package com.cradlerest.web.controller;

import com.twilio.example.TwiMLResponseExample;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Used to test Twilio webhook
 * currently using: /api/twilio/uploadsms
 *
 */
@RestController
@RequestMapping("/api/twilio")
public class TwilioWebhook {

	/**
	 * Returns a string as an SMS reply (this replies to the sender with a text)
	 * https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 *
	 * @param request Twilio post response body: https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 * @param response
	 * @throws IOException
	 */
	@PostMapping(path = "/uploadsms", consumes = "application/x-www-form-urlencoded")
	public String respondToSmS(WebRequest request, HttpServletResponse response) throws IOException {
		//System.out.println("received: " + request.toString());
		return "You: " + request.getParameter("Body");
	}
}
