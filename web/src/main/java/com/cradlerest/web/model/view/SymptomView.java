package com.cradlerest.web.model.view;

import com.cradlerest.web.service.SymptomViewDeserializer;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * {@code SymptomView} is a simplified version of {@code Symptom} giving access
 * to only the symptoms's {@code text} field.
 *
 * It is serialized to JSON as a string via the {@code getText} method, not as
 * a regular object.
 */
@JsonDeserialize(using = SymptomViewDeserializer.class)
public interface SymptomView {

	@JsonValue
	String getText();
}
