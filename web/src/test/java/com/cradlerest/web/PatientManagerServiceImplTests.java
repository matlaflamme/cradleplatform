package com.cradlerest.web;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Sex;
import com.cradlerest.web.model.builder.PatientBuilder;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.PatientManagerServiceImpl;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientManagerServiceImplTests {

	@TestConfiguration
	class TestServiceFactory {

		@Bean
		PatientManagerService patientManagerService() {
			return new PatientManagerServiceImpl(patientRepository, readingRepository);
		}
	}

	@MockBean
	PatientRepository patientRepository;

	@MockBean
	ReadingRepository readingRepository;

	@Autowired
	PatientManagerService patientManagerService;

	@Before
	public void setup() {
		// setup responses for repository queries
		// 	https://www.baeldung.com/spring-boot-testing

		Patient patient = new PatientBuilder()
				.id("001")
				.name("Taki Tachibana")
				.villageNumber("1")
				.zoneNumber("1")
				.birthYear(1998)
				.sex(Sex.MALE)
				.build();

		Mockito.when(patientRepository.findById(patient.getId()))
				.thenReturn(Optional.of(patient));

		Mockito.when(patientRepository.findById("000"))
				.thenReturn(Optional.empty());
	}

	@Test
	public void whenValidId_ReturnPatient() throws Exception {
		String id = "001";
		Patient result = patientManagerService.getPatientWithId(id);
		assertThat(result.getId())
				.isEqualTo(id);

		// sanity check some of the other patient data as well
		assertThat(result.getName())
				.isEqualTo("Taki Tachibana");
		assertThat(result.getBirthYear())
				.isEqualTo(1998);
		assertThat(result.getZoneNumber())
				.isEqualTo("1");
		assertThat(result.isPregnant())
				.isEqualTo(false);
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenInvalidId_ThrowException() throws Exception {
		String id = "000";
		patientManagerService.getPatientWithId(id);
	}
}
