package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

/**
 * Generates some usage frequency phrases.
 */
public class UsageFrequencyGenerator implements Generator<String> {

	private interface SubGenerator {
		String call();
	}

	private Noise noise;

	public UsageFrequencyGenerator(Noise noise) {
		this.noise = noise;
	}

	@Override
	public String generate() {
		return noise.pick(cases()).call();
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("operation not supported");
	}

	private SubGenerator[] cases() {
		return new SubGenerator[] {
				() -> "Morning and Evening",
				() -> "After Every Meal",
				() -> "Before Every Meal",
				() -> "Before Going To Bed",
				() -> "As Needed",
				() -> "Once Daily",
				() -> String.format("%d Times Per Day", noise.generate(2, 5)),
		};
	}
}
