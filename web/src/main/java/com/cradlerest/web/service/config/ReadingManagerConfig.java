package com.cradlerest.web.service.config;

import com.cradlerest.web.service.ReadingManager;
import com.cradlerest.web.service.mock.MockReadingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dependency inject configuration for the {@code ReadingManager} service.
 */
@Configuration
public class ReadingManagerConfig {

	@Bean
	ReadingManager mock() {
		return new MockReadingManager();
	}
}
