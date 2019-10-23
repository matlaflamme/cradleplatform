package com.cradlerest.web.service.config;

import com.cradlerest.web.service.ReadingManager;
import com.cradlerest.web.service.ReadingManagerImpl;
import com.cradlerest.web.service.SymptomManager;
import com.cradlerest.web.service.mock.MockReadingManager;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.service.repository.SymptomReadingRelationRepository;
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

	@Bean
	ReadingManager impl(
			ReadingRepository readingRepository,
			SymptomReadingRelationRepository symptomReadingRelationRepository,
			SymptomManager symptomManager
	) {
		return new ReadingManagerImpl(readingRepository, symptomReadingRelationRepository, symptomManager);
	}
}
