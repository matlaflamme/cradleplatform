package com.cradlerest.web.service;

import com.cradlerest.web.model.view.SymptomView;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Custom JSON deserializer for {@code SymptomView}.
 */
public class SymptomViewDeserializer extends StdDeserializer<SymptomView> {

	public SymptomViewDeserializer() {
		this(null);
	}

	public SymptomViewDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public SymptomView deserialize(
			JsonParser jsonParser,
			DeserializationContext deserializationContext
	) throws IOException {
		var text = jsonParser.getText();
		return () -> text;
	}
}
