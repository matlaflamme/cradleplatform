package com.cradlerest.web;

import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Symptom;
import com.cradlerest.web.model.SymptomReadingRelation;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.service.repository.SymptomReadingRelationRepository;
import com.cradlerest.web.util.DateParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SymptomReadingRelationRepositoryTests {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	SymptomReadingRelationRepository repository;

	private final Reading testReading = new ReadingBuilder()
				.id(100)
				.pid("001")
				.systolic(100)
				.diastolic(75)
				.heartRate(70)
				.pregnant(false)
				.gestationalAgeDays(0)
				.colour(ReadingColour.GREEN)
				.timestamp(new Timestamp(DateParser.parseDateTime("2019-10-01 23:00:00").getTime()))
				.build();

	@Test
	public void getSymptomsForReading_WhenAReadingHasSymptoms_ReturnThoseSymptoms() {
		var headache = new Symptom(0, "Headache");
		var unwell = new Symptom(5, "Unwell");

		entityManager.merge(headache);
		entityManager.merge(unwell);
		entityManager.merge(testReading);
		entityManager.persist(new SymptomReadingRelation(0, 100));
		entityManager.persist(new SymptomReadingRelation(5, 100));

		var result = repository.getSymptomsForReading(testReading.getId());

		assertThat(result)
				.containsExactlyInAnyOrder(headache, unwell);
	}

	@Test
	public void getSymptomsForReading_WhenAReadingHasNoSymptoms_ReturnAnEmptyList() {
		entityManager.merge(testReading);

		var result = repository.getSymptomsForReading(testReading.getId());

		assertThat(result)
				.isEmpty();
	}

	@Test
	public void getSymptomsForReading_WhenReadingIdIsInvalid_ReturnEmptyList() {
		var result = repository.getSymptomsForReading(1001);
		assertThat(result)
				.isEmpty();
	}
}
