package com.cradlerest.web;

import com.cradlerest.web.model.view.ReadingView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadingViewTests {

	@Test
	public void deserializeJson() throws Exception {
		var objectMapper = new ObjectMapper();
		var json = "{\n" +
		            "  \"patientId\":\"001\",\n" +
		            "  \"systolic\":110,\n" +
		            "  \"diastolic\":80,\n" +
		            "  \"heartRate\":70,\n" +
		            "  \"colour\":0,\n" +
		            "  \"pregnant\":true,\n" +
		            "  \"gestationalAge\":90,\n" +
		            "  \"symptoms\":[\"Headache\"],\n" +
		            "  \"timestamp\":\"2019-10-24 09:32:10\"\n" +
		            "}";
		var view = objectMapper.readValue(json, ReadingView.class);

		assertThat(view)
				.isNotNull();
		assertThat(view.getPatientId())
				.isEqualTo("001");
		assertThat(view.getSymptoms())
				.isNotNull()
				.hasSize(1);
		assertThat(view.getSymptoms().get(0).getText())
				.isEqualTo("Headache");

	}
}
