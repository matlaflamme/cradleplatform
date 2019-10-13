package com.cradlerest.web.controller;

import com.twilio.example.TwiMLResponseExample;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Handles HTTP requests from Twilio
 *
 * Twilio src
 * https://github.com/twilio
 *
 * {
 *     "sid": "SM722f2af6f0c048e2b2b336222c236f19",
 *     "date_created": "Sun, 13 Oct 2019 06:25:20 +0000",
 *     "date_updated": "Sun, 13 Oct 2019 06:25:20 +0000",
 *     "date_sent": null,
 *     "account_sid": "AC6cf5e5d23357bc99a1d39777f20647d8",
 *     "to": "+17781234567",
 *     "from": "+12053465536",
 *     "messaging_service_sid": null,
 *     "body": "Sent from your Twilio trial account - hello",
 *     "status": "queued",
 *     "num_segments": "1",
 *     "num_media": "0",
 *     "direction": "outbound-api",
 *     "api_version": "2010-04-01",
 *     "price": null,
 *     "price_unit": "USD",
 *     "error_code": null,
 *     "error_message": null,
 *     "uri": "/2010-04-01/Accounts/AC6cf5e5d23357bc99a1d39777f20647d8/Messages/SM722f2af6f0c048e2b2b336222c236f19.json",
 *     "subresource_uris": {
 *         "media": "/2010-04-01/Accounts/AC6cf5e5d23357bc99a1d39777f20647d8/Messages/SM722f2af6f0c048e2b2b336222c236f19/Media.json"
 *     }
 * }
 */
@RestController
@RequestMapping("/api/twilio")
public class TwilioWebhook {

	/**
	 * https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 *
	 * @param request Twilio post response body: https://www.twilio.com/docs/sms/twiml#twilios-request-to-your-application
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/uploadsms")
	public String respondToSmS(@RequestParam Map<String, String> request, HttpServletResponse response) throws IOException {
		System.out.println("received: " + request.toString());
		Message message = new Message.Builder("Message Received").build();
		MessagingResponse responseMessage = new MessagingResponse.Builder().message(message).build();

		return responseMessage.toXml();
	}
}
