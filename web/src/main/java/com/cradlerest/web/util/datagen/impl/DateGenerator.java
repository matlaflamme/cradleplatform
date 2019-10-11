package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.DateParser;
import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generator for random {@code Date} objects.
 *
 * Accepts "min" and "max" date strings as parameters.
 */
public class DateGenerator implements Generator<Date> {

	private static final String MIN_KEY = "min";
	private static final String MAX_KEY = "max";

	private static final Date MIN_DATE = DateParser.parseDate("1970-01-01");
	private static final Date MAX_DATE = new Date();

	@NotNull
	private Noise noise;

	@NotNull
	private Map<String, Object> curriedParameters = new HashMap<>();

	public DateGenerator(@NotNull Noise noise) {
		this.noise = noise;
	}

	private DateGenerator(@NotNull Noise noise, @NotNull Map<String, Object> curriedParameters) {
		this.noise = noise;
		this.curriedParameters = curriedParameters;
	}

	@Override
	public Date generate() {
		var min = (Date) curriedParameters.getOrDefault(MIN_KEY, MIN_DATE);
		var max = (Date) curriedParameters.getOrDefault(MAX_KEY, MAX_DATE);

		// To get a random date between the minimum and maximum, subtract some
		// number of nanoseconds from the duration of the two dates then add
		// that duration back onto the minimum date.
		var duration = Duration.between(min.toInstant(), max.toInstant());
		var durationNanos = duration.toNanos();
		var randomDuration = duration.minusNanos(noise.generate(0, durationNanos));
		return Date.from(min.toInstant().plus(randomDuration));
	}

	@Override
	public Generator<Date> with(@NotNull String key, @NotNull Object value) throws IllegalArgumentException, OperationNotSupportedException {
		switch (key) {
			case MIN_KEY: // fallthrough
			case MAX_KEY:
				if (!(value instanceof String)) {
					throw new IllegalArgumentException("illegal value for key '" + key + "': " + value.toString());
				}
				break;
			default:
				throw new IllegalArgumentException("illegal key: " + key);
		}

		var str = (String) value;
		Date date = str.length() == "yyyy-MM-dd".length()
				? DateParser.parseDate(str)
				: DateParser.parseDateTime(str);

		var paramClone = new HashMap<>(curriedParameters);
		paramClone.put(key, date);
		return new DateGenerator(noise, paramClone);
	}
}
