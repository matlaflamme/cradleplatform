package com.cradlerest.web.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model view class containing a pairing of a patient with its latest reading.
 */
public class PatientWithLatestReadingView {

	@NotNull
	private Patient patient;

	@Nullable
	private Reading reading;

	public PatientWithLatestReadingView(@NotNull Patient patient, @Nullable Reading reading) {
		this.patient = patient;
		this.reading = reading;
	}

	@NotNull
	public Patient getPatient() {
		return patient;
	}

	@Nullable
	public Reading getReading() {
		return reading;
	}
}
