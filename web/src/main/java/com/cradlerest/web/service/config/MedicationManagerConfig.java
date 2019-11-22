package com.cradlerest.web.service.config;

import com.cradlerest.web.service.*;
import com.cradlerest.web.service.repository.MedicationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MedicationManagerConfig {


    @Bean
    @Primary
    MedicationManager medicationManagerImpl(
            MedicationRepository medicationRepository
    ) {
        return new MedicationManagerImpl(medicationRepository);
    }
}
