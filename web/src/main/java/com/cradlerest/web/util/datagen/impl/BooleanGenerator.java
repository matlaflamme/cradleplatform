package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

/**
 * Generator for {@code Boolean} values.
 *
 * Does not support parameterization.
 */
public class BooleanGenerator implements Generator<Boolean> {

	private Noise noise;

	public BooleanGenerator(Noise noise) {
		this.noise = noise;
	}

	@Override
	public Boolean generate() {
		return noise.generate() >= 0.5;
	}

	@Override
	public Generator<Boolean> with(@NotNull String key, @NotNull Object value) throws OperationNotSupportedException {
		throw new OperationNotSupportedException(
				"BooleanGenerator does not support parameterization via the with method");
	}
}
