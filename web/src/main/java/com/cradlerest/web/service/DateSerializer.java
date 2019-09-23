package com.cradlerest.web.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom JSON serializer component for converting {@code Date} objects into JSON.
 *
 * Unlike with the default serializer, no timezone conversions are performed on
 * the {@code Date} value: it is simply serialized as is.
 */
@Component
public class DateSerializer extends StdSerializer<Date> {
	// ref: https://stackoverflow.com/a/44543313

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DateSerializer() {
		this(null);
	}

	public DateSerializer(Class<Date> t) {
		super(t);
	}

	@Override
	public void serialize(
			Date date,
			JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider
	) throws IOException {
		var dateString = formatter.format(date);
		jsonGenerator.writeString(dateString);
	}
}
