package com.cradlerest.web.service.config;

import com.cradlerest.web.service.AuthorizerFactory;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dependency injection configuration for {@code AuthorizerFactor}.
 */
@Configuration
public class AuthorizerFactoryConfig {

	@Bean
	AuthorizerFactory authorizerFactory(PatientManagerService patientManagerService, ReadingManager readingManager) {
		return new AuthorizerFactory(patientManagerService, readingManager);
	}
}
