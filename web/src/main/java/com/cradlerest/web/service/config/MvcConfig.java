package com.cradlerest.web.service.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/vht").setViewName("vht");
		registry.addViewController("/healthworker").setViewName("healthworker");
		registry.addViewController("/admin").setViewName("admin");
		registry.addViewController("/patientSummary").setViewName("patientSummary");
		registry.addViewController("/createNewReading").setViewName("createNewReading");
		registry.addViewController("/createAccount").setViewName("createAccount");
		registry.addViewController("/manageUserAccounts").setViewName("manageUserAccounts");
		registry.addViewController("/healthworker/referrals").setViewName("referralMain");
	}

}
