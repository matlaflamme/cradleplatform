package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Reading;

/**
 * Simplifies the process of constructing {@code Reading} objects.
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
 *
 * @see AbstractReadingBuilder
 * @see Reading
 */
public class ReadingBuilder extends AbstractReadingBuilder<Reading, ReadingBuilder> {

	public ReadingBuilder() {
		this.value = new Reading();
	}

	@Override
	public Reading build() throws InstantiationError {
		validate();
		return value;
	}

	@Override
	protected ReadingBuilder self() {
		return this;
	}

	private void assertNotNull(Object object, String fieldName) throws InstantiationError {
		if (object == null) {
			throw new InstantiationError(String.format("field '%s' is null", fieldName));
		}
	}

	private void validate() throws InstantiationError {
		assertNotNull(value.getPatientId(), "pid");
		assertNotNull(value.getColour(), "colour");
		assertNotNull(value.getHeartRate(), "heartRate");
		assertNotNull(value.getSystolic(), "systolic");
		assertNotNull(value.getDiastolic(), "diastolic");
		assertNotNull(value.isPregnant(), "isPregnant");
		assertNotNull(value.getTimestamp(), "timestamp");
	}
}
