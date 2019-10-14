package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.util.DateParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;


/**
 * Simplifies the process of constructing {@code Reading} objects.
 *
 *
 * <h2>Example</h2>
 * <code>
 *	 Reading newReading = new ReadingBuilder()
 *		 .pid(readingPatient)
 *		 .colour(ReadingColour.RED)
 *		 .diastolic(diastolic)
 *		 .systolic(systolic)
 *		 .heartRate(heartRate)
 *		 .timestamp(timestamp)
 *		 .build();
 * </code>
 */
public class ReadingBuilder {

	private Reading reading;

	public ReadingBuilder() {
		this.reading = new Reading();
	}

	public Reading build() throws InstantiationError {
		validate();
		return reading;
	}

	public ReadingBuilder id(int id) {
		reading.setId(id);
		return this;
	}

	public ReadingBuilder pid(@NotNull String pid) {
		reading.setPatientId(pid);
		return this;
	}

	public ReadingBuilder colour(@NotNull ReadingColour colour) {
		reading.setColour(colour);
		return this;
	}

	public ReadingBuilder diastolic(@NotNull Integer diastolic) {
		reading.setDiastolic(diastolic);
		return this;
	}

	public ReadingBuilder heartRate(@NotNull Integer heartRate) {
		reading.setHeartRate(heartRate);
		return this;
	}


	public ReadingBuilder systolic(@NotNull Integer systolic) {
		reading.setSystolic(systolic);
		return this;
	}

	public ReadingBuilder gestationalAgeMonths(@Nullable Integer months) {
		if (months == null) {
			reading.setGestationalAge(null);
			return this;
		}
		final int DAYS_PER_MONTH = 30;
		reading.setGestationalAge(months * DAYS_PER_MONTH);
		return this;
	}

	public ReadingBuilder gestationalAgeWeeks(@Nullable Integer weeks) {
		if (weeks == null) {
			reading.setGestationalAge(null);
			return this;
		}
		final int DAYS_PER_WEEK = 7;
		reading.setGestationalAge(weeks * DAYS_PER_WEEK);
		return this;
	}

	public ReadingBuilder gestationalAgeDays(@Nullable Integer days) {
		reading.setGestationalAge(days);
		return this;
	}


	public ReadingBuilder pregnant(boolean isPregnant) {
		reading.setPregnant(isPregnant);
		return this;
	}

	public ReadingBuilder timestamp(@NotNull Date timestamp) {
		reading.setTimestamp(timestamp);
		return this;
	}

	public ReadingBuilder timestamp(@NotNull String timestampText) {
		reading.setTimestamp(DateParser.parseDateTime(timestampText));
		return this;
	}

	private void assertNotNull(Object object, String fieldName) throws InstantiationError {
		if (object == null) {
			throw new InstantiationError(String.format("field '%s' is null", fieldName));
		}
	}

	private void validate() throws InstantiationError {
		assertNotNull(reading.getPatientId(), "pid");
		assertNotNull(reading.getColour(), "colour");
		assertNotNull(reading.getHeartRate(), "heartRate");
		assertNotNull(reading.getSystolic(), "systolic");
		assertNotNull(reading.getDiastolic(), "diastolic");
		assertNotNull(reading.isPregnant(), "isPregnant");
		assertNotNull(reading.getTimestamp(), "timestamp");
	}
}
