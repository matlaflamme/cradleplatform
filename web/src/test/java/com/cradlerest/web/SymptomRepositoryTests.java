package com.cradlerest.web;

import com.cradlerest.web.model.Symptom;
import com.cradlerest.web.service.repository.SymptomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SymptomRepositoryTests {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	SymptomRepository symptomRepository;

	@Test
	public void getSymptomViewById_WhenSymptomWithIdIsPresent_ReturnView() {
		var symptom = new Symptom(1, "Headache");
		entityManager.persist(symptom);
		entityManager.flush();

		var result = symptomRepository.getSymptomViewById(1);

		assertThat(result)
				.isNotNull();
		assertThat(result.getText())
				.isEqualTo("Headache");
	}

	@Test
	public void getSymptomViewById_SymptomSerializesToCorrectJSON() throws JsonProcessingException {
		var symptom = new Symptom(1, "Headache");
		entityManager.persist(symptom);
		entityManager.flush();

		var view = symptomRepository.getSymptomViewById(1);
		var objectMapper = new ObjectMapper();
		var json = objectMapper.writeValueAsString(view);

		assertThat(json)
				.isEqualTo("\"Headache\"");
	}
}
