package com.cradlerest.web.service.config;

import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.PatientManagerServiceImpl;
import com.cradlerest.web.service.ReferralManagerService;
import com.cradlerest.web.service.repository.*;
import org.springframework.context.annotation.Bean;

//public class ReferralManagerServiceConfig {
//	private UserRepository userRepository; // VHT INFO
//	private ReferralRepository referralRepository; // Saving referrals
//	private HealthCentreRepository healthCentreRepository;
//	private PatientManagerService patientManagerService; // Patients, Readings
//	@Bean
//	public ReferralManagerService referralManagerService(
//			PatientRepository patientRepository,
//			ReadingRepository readingRepository
//	) {
//		return new PatientManagerServiceImpl(patientRepository, readingRepository);
//	}
//}
