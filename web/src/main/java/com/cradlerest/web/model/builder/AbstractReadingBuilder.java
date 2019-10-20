package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.util.DateParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Abstract builder for classes which extend the {@code Reading} class.
 * Implements builder methods for all of {@code Reading}'s fields which will
 * be common among subclasses.
 *
 * @param <T> The subclass of {@code Reading} which the builder constructs.
 * @param <Self> The builder's type. Used as the return value for all builder
 *               methods so that the specialized methods of the implementing
 *               builders can still be called after calling one of the general
 *               methods.
 */
abstract class AbstractReadingBuilder<T extends Reading, Self>  extends AbstractBuilder<T> {

	/**
	 * Implementers of this class should override this method to return
	 * {@code this}. We cannot implement this in the abstract class as we
	 * need to return an instance of the concrete builder and not one of
	 * the abstract superclass.
	 * @return The implementer's {@code this}.
	 */
	protected abstract Self self();

	public Self id(int id) {
		value.setId(id);
		return self();
	}

	public Self pid(@NotNull String pid) {
		value.setPatientId(pid);
		return self();
	}

	public Self colour(@NotNull ReadingColour colour) {
		value.setColour(colour);
		return self();
	}

	public Self diastolic(@NotNull Integer diastolic) {
		value.setDiastolic(diastolic);
		return self();
	}

	public Self heartRate(@NotNull Integer heartRate) {
		value.setHeartRate(heartRate);
		return self();
	}

	public Self systolic(@NotNull Integer systolic) {
		value.setSystolic(systolic);
		return self();
	}

	public Self gestationalAgeMonths(@Nullable Integer months) {
		if (months == null) {
			value.setGestationalAge(null);
			return self();
		}
		final int DAYS_PER_MONTH = 30;
		value.setGestationalAge(months * DAYS_PER_MONTH);
		return self();
	}

	public Self gestationalAgeWeeks(@Nullable Integer weeks) {
		if (weeks == null) {
			value.setGestationalAge(null);
			return self();
		}
		final int DAYS_PER_WEEK = 7;
		value.setGestationalAge(weeks * DAYS_PER_WEEK);
		return self();
	}

	public Self gestationalAgeDays(@Nullable Integer days) {
		value.setGestationalAge(days);
		return self();
	}

	public Self pregnant(boolean isPregnant) {
		value.setPregnant(isPregnant);
		return self();
	}

	public Self timestamp(@NotNull Date timestamp) {
		value.setTimestamp(timestamp);
		return self();
	}

	public Self timestamp(@NotNull String timestampText) {
		value.setTimestamp(DateParser.parseDateTime(timestampText));
		return self();
	}
}
