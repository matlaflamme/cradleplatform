package com.cradlerest.web.service.config;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Handles failed HTTP authentication
 *
 * HTTP Basic Authentication
 * https://springbootdev.com/2017/08/29/spring-security-http-basic-authentication-example/
 *
 * BasicAuthenticationEntryPoint documentation:
 * https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/web/authentication/www/BasicAuthenticationEntryPoint.html#commence-javax.servlet.http.HttpServletRequest-javax.servlet.http.HttpServletResponse-org.springframework.security.core.AuthenticationException-
 *
 * What is a realm:
 * https://stackoverflow.com/questions/16186834/whats-the-meaning-of-realm-in-spring-security
 */
@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException, ServletException {
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Respond with Status Code 401 (HTTP Authentication error)
		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status 401 - " + authEx.getMessage());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName("Admin-Realm");
		super.afterPropertiesSet();
	}

}