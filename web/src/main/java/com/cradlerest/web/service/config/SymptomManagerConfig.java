package com.cradlerest.web.service.config;

import com.cradlerest.web.service.SymptomManager;
import com.cradlerest.web.service.SymptomManagerImpl;
import com.cradlerest.web.service.repository.SymptomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Dependency injection configuration for {@code SymptomManager} services.
 */
@Configuration
public class SymptomManagerConfig {

	/**
	 * Dependency injection factory method for {@code SymptomManager} services.
	 *
	 * It is important that the scope of this service is "singleton" as
	 * {@code SymptomManagerImpl} assumes that it is instantiated as a singleton.
	 *
	 * @param symptomRepository A {@code SymptomRepository}.
	 * @return A {@code SymptomManager}.
	 */
	@Bean
	@Scope("singleton")
	SymptomManager symptomManager(SymptomRepository symptomRepository) {
		return new SymptomManagerImpl(symptomRepository);
	}
}
