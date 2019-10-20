package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Reading;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;

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

	private void validate() throws InstantiationError {
		assertFieldNotNull(value.getPatientId(), "pid");
		assertFieldNotNull(value.getColour(), "colour");
		assertFieldNotNull(value.getHeartRate(), "heartRate");
		assertFieldNotNull(value.getSystolic(), "systolic");
		assertFieldNotNull(value.getDiastolic(), "diastolic");
		assertFieldNotNull(value.isPregnant(), "isPregnant");
		assertFieldNotNull(value.getTimestamp(), "timestamp");
	}
}
