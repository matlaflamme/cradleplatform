package com.cradlerest.web.service.config;

import com.twilio.security.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ensures twilio requests are twilio requests
 * Currenlt applied to /api/twilio/*
 * @see WebSecurityConfig
 *
 * THIS CLASS WAS COPY AND PASTED FROM:
 * https://www.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */

public class TwilioRequestValidatorFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TwilioRequestValidatorFilter.class);
	private RequestValidator requestValidator;
	private String myAuthToken = "2190957f91e23511702ae9b0eb866726";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("twilio filter");
		LOGGER.info("Initiating Twilio filter");
		requestValidator = new RequestValidator(myAuthToken);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean isValidRequest = false;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			// Concatenates the request URL with the query string
			String pathAndQueryUrl = getRequestUrlAndQueryString(httpRequest);
			// Extracts only the POST parameters and converts the parameters Map type
			Map<String, String> postParams = extractPostParams(httpRequest);
			String signatureHeader = httpRequest.getHeader("X-Twilio-Signature");

			isValidRequest = requestValidator.validate(
					pathAndQueryUrl,
					postParams,
					signatureHeader);
		}

		if(isValidRequest) {
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	@Override
	public void destroy() {
		// Nothing to do
	}

	private Map<String, String> extractPostParams(HttpServletRequest request) {
		String queryString = request.getQueryString();
		Map<String, String[]> requestParams = request.getParameterMap();
		List<String> queryStringKeys = getQueryStringKeys(queryString);

		return requestParams.entrySet().stream()
				.filter(e -> !queryStringKeys.contains(e.getKey()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
	}

	private List<String> getQueryStringKeys(String queryString) {
		if(queryString == null || queryString.length() == 0) {
			return Collections.emptyList();
		} else {
			return Arrays.stream(queryString.split("&"))
					.map(pair -> pair.split("=")[0])
					.collect(Collectors.toList());
		}
	}

	private String getRequestUrlAndQueryString(HttpServletRequest request) {
		String queryString = request.getQueryString();
		String requestUrl = request.getRequestURL().toString();
		if(queryString != null && queryString != "") {
			return requestUrl + "?" + queryString;
		}
		return requestUrl;
	}
}
