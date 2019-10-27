package com.cradlerest.web.service.config;

import com.cradlerest.web.service.InternalDataGenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dependency injection configuration for {@code InternalDataGenService}.
 */
@Configuration
public class InternalDataGenServiceConfig {

	@Bean
	InternalDataGenService dataGenService() {
		return new InternalDataGenService();
	}
}
