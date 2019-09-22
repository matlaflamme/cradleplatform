package com.cradlerest.web;

import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Sex;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

		Date dateOfBirth = new GregorianCalendar(1998, Calendar.NOVEMBER, 13).getTime();
		Patient taki = new Patient(
				"001",
				1,
				"AB",
				dateOfBirth,
				Sex.MALE,
				false,
				null,
				null,
				null,
				null
		);

		Mockito.when(patientRepository.findById(taki.getId()))
				.thenReturn(Optional.of(taki));

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
		assertThat(result.getInitials())
				.isEqualTo("AB");
		assertThat(result.getDateOfBirth())
				.isEqualTo(new GregorianCalendar(1998, Calendar.NOVEMBER, 13).getTime());
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenInvalidId_ThrowException() throws Exception {
		String id = "000";
		patientManagerService.getPatientWithId(id);
	}
}
