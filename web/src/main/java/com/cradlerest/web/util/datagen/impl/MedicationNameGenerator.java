package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

/**
 * Generates some random medication names.
 */
public class MedicationNameGenerator implements Generator<String> {

	private static final String[] MEDICATION_LIST = new String[] {
			"Ibuprofen",
			"Acetaminophen",
			"Simvastatin",
			"Lisinopril",
			"Metformin",
			"Lipitor",
			"Amlodipine",
			"Hydrochlorothiazide"
	};

	private Noise noise;

	public MedicationNameGenerator(Noise noise) {
		this.noise = noise;
	}

	@Override
	public String generate() {
		return noise.pick(MEDICATION_LIST);
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("operation not supported");
	}
}
