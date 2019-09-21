package com.cradlerest.web.service.config;

import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.PatientManagerServiceImpl;
import com.cradlerest.web.service.repository.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientManagerServiceConfig {

	@Bean
	public PatientManagerService patientManagerService(PatientRepository repository) {
		return new PatientManagerServiceImpl(repository);
	}
}
