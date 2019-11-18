package com.cradlerest.web.service.config;

import com.cradlerest.web.service.AuthorizerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dependency injection configuration for {@code AuthorizerFactor}.
 */
@Configuration
public class AuthorizerFactoryConfig {

	@Bean
	AuthorizerFactory authorizerFactory() {
		return new AuthorizerFactory();
	}
}
