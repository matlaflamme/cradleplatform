package com.cradlerest.web.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom JSON deserializer component for parsing datetime strings.
 *
 * Unlike with the default deserializer, no timezone conversions are performed
 * on the {@code Date} object.
 */
@Component
public class DateDeserializer extends StdDeserializer<Date> {
	// ref: https://stackoverflow.com/a/44543313

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DateDeserializer() {
		this(null);
	}

	public DateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(
			JsonParser jsonParser,
			DeserializationContext deserializationContext
	) throws IOException {
		var date = jsonParser.getText();
		try {
			return formatter.parse(date);
		} catch (Exception ex) {
			throw new JsonParseException(jsonParser, "invalid date format", ex);
		}
	}
}
