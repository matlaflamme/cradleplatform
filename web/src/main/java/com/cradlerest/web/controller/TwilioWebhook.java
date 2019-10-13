package com.cradlerest.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class TwilioWebhook {

	@PostMapping("/sms")
	public void respondToSmS(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request
		System.out.println("sms");
		return "textmsg received";
	}
}
