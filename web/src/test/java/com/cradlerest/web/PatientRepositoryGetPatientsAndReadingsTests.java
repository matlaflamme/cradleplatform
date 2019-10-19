package com.cradlerest.web;

import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Sex;
import com.cradlerest.web.model.builder.PatientBuilder;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.util.DateParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PatientRepositoryGetPatientsAndReadingsTests {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	PatientRepository patientRepository;

	@Autowired
	ReadingRepository readingRepository;

	@Test
	public void whenNoPatients_ReturnEmptyList() {
		var result = patientRepository.getAllPatientsAndLatestReadings();
		assertThat(result)
				.isEmpty();
	}

	@Test
	public void whenPatientHasNoReadings_ReturnNullForReading() {
		// given
		var patient = new PatientBuilder()
				.id("001")
				.name("Hikari Tachibana")
				.villageNumber("1")
				.birthYear(2002)
				.sex(Sex.FEMALE)
				.medicalHistory("x")
				.drugHistory("y")
				.otherSymptoms("z")
				.zoneNumber("1")
				// use Timestamp instead of Date because assertj's isEqualTo doesn't
				// think they're equal even when .equal does
				.lastUpdated(new Timestamp(new Date().getTime()))
				.build();
		entityManager.persist(patient);
		entityManager.flush();

		// when
		var result = patientRepository.getAllPatientsAndLatestReadings();

		// then
		assertThat(result)
				.hasSize(1);
		assertThat(result.get(0).getReading())
				.isNull();
		assertThat(result.get(0).getPatient())
				.isEqualTo(patient);
	}

	@Test
	public void whenPatientHasReadings_PairPatientWithLatestReading() {
		// given
		var patient = new PatientBuilder()
				.id("001")
				.name("Hikari Tachibana")
				.villageNumber("1")
				.birthYear(2002)
				.sex(Sex.FEMALE)
				.medicalHistory(null)
				.drugHistory(null)
				.otherSymptoms(null)
				// use Timestamp instead of Date because assertj's isEqualTo doesn't
				// think they're equal even when .equal does
				.lastUpdated(new Timestamp(new Date().getTime()))
				.zoneNumber("1")
				.build();

		var reading1 = new ReadingBuilder()
				.id(0)
				.pid(patient.getId())
				.systolic(110)
				.diastolic(80)
				.heartRate(85)
				.pregnant(false)
				.gestationalAgeDays(0)
				.colour(ReadingColour.GREEN)
				.timestamp(new Timestamp(DateParser.parseDateTime("2019-10-01 22:00:00").getTime()))
				.build();

		var reading2 = new ReadingBuilder()
				.id(1)
				.pid(patient.getId())
				.systolic(100)
				.diastolic(75)
				.heartRate(70)
				.pregnant(false)
				.gestationalAgeDays(0)
				.colour(ReadingColour.GREEN)
				.timestamp(new Timestamp(DateParser.parseDateTime("2019-10-01 23:00:00").getTime()))
				.build();

		entityManager.persist(patient);
		// use `merge` to persist detached entities, ref: https://stackoverflow.com/a/42432971
		entityManager.merge(reading1);
		entityManager.merge(reading2);
		entityManager.flush();

		// when
		var result = patientRepository.getAllPatientsAndLatestReadings();

		// then
		assertThat(result)
				.hasSize(1);
		assertThat(result.get(0).getPatient())
				.isEqualTo(patient);
		assertThat(result.get(0).getReading())
				.isEqualTo(reading2);
	}
}
