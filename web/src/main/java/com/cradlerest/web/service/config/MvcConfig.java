package com.cradlerest.web.service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Initializes url paths to resources (html pages)
 *
 * WebMvcConfigurer:
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.html
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/patientSummary").setViewName("patientSummary");
		registry.addViewController("/createNewReading").setViewName("createNewReading");
		registry.addViewController("/createAccount").setViewName("createAccount");
		registry.addViewController("/manageUserAccounts").setViewName("manageUserAccounts");
		registry.addViewController("/referrals").setViewName("referralMain");
		registry.addViewController("/adminLogs").setViewName("adminLogs");
		registry.addViewController("/help").setViewName("help");
		registry.addViewController("/createNewPatient").setViewName("createNewPatient");
		registry.addViewController("/accountSetting").setViewName("accountSetting");
	}
}
