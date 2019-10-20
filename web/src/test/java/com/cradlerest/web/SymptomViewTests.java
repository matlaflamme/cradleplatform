package com.cradlerest.web;

import com.cradlerest.web.model.view.SymptomView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SymptomViewTests {

	@Test
	public void deserializeJson() throws Exception {
		var objectMapper = new ObjectMapper();
		var json = "\"Headache\"";
		var view = objectMapper.readValue(json, SymptomView.class);

		assertThat(view)
				.isNotNull();
		assertThat(view.getText())
				.isEqualTo("Headache");
	}
}
