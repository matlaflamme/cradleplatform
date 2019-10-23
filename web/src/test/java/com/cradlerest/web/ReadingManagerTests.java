package com.cradlerest.web;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Symptom;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.service.*;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.service.repository.SymptomReadingRelationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadingManagerTests {

	@TestConfiguration
	class TestServiceFactory {

		@Bean
		ReadingManager readingManager() {
			return new ReadingManagerImpl(readingRepository, symptomReadingRelationRepository, symptomManager);
		}
	}

	@MockBean
	ReadingRepository readingRepository;

	@MockBean
	SymptomReadingRelationRepository symptomReadingRelationRepository;

	@MockBean
	SymptomManager symptomManager;

	@Autowired
	@Qualifier("impl")
	ReadingManager readingManager;

	private final Symptom headache = new Symptom(0, "Headache");
	private final Symptom unwell = new Symptom(5, "Unwell");

	private final Reading mockReading = new ReadingBuilder()
			.id(1)
			.pid("001")
			.systolic(100)
			.diastolic(80)
			.heartRate(70)
			.colour(ReadingColour.GREEN)
			.pregnant(false)
			.timestamp("2019-10-10 16:32:40")
			.build();

	@Before
	public void mockSetup() {
		Mockito.when(readingRepository.findById(1))
				.thenReturn(Optional.of(mockReading));

		Mockito.when(symptomReadingRelationRepository.getSymptomsForReading(1))
				.thenReturn(Arrays.asList(headache, unwell));

		Mockito.when(readingRepository.findAllByPatientId("001"))
				.thenReturn(Collections.singletonList(mockReading));
	}

	@Test
	public void getReadingView_WhenValidReadingId_ReturnReadingView() throws EntityNotFoundException {
		var result = readingManager.getReadingView(1);

		assertThat(result.getSymptoms())
				.containsExactlyInAnyOrder(headache, unwell);
	}

	@Test(expected = EntityNotFoundException.class)
	public void getReadingView_WhenInvalidReadingId_ThrowException() throws EntityNotFoundException {
		readingManager.getReadingView(10);
	}

	@Test
	public void getAllReadingViewsForPatient_WhenPatientHasReadings_ReturnReadingViews() throws EntityNotFoundException {
		var result = readingManager.getAllReadingViewsForPatient("001");

		assertThat(result)
				.hasSize(1);
		assertThat(result.get(0).getSymptoms())
				.containsExactlyInAnyOrder(headache, unwell);
	}
}
