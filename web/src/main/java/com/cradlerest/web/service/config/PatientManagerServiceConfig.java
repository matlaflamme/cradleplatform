package com.cradlerest.web.service.config;

import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.PatientManagerServiceImpl;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientManagerServiceConfig {

	@Bean
	public PatientManagerService patientManagerService(
			PatientRepository patientRepository,
			ReadingRepository readingRepository
	) {
		return new PatientManagerServiceImpl(patientRepository, readingRepository);
	}
}
