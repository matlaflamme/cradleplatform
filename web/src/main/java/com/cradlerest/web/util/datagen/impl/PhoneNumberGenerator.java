package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

/**
 * A random generator for phone numbers.
 */
public class PhoneNumberGenerator implements Generator<String> {

	private static final String AREA_CODE = "+1 (604) ";

	private StringGenerator stringGenerator;

	public PhoneNumberGenerator(@NotNull Noise noise) {
		this.stringGenerator = new StringGenerator(noise);
	}

	@Override
	public String generate() {
		var firstPart = stringGenerator.generate(3, StringGenerator.DECIMAL_CHARSET);
		var lastPart = stringGenerator.generate(4, StringGenerator.DECIMAL_CHARSET);
		return AREA_CODE + firstPart + " - " + lastPart;
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("PhoneNumberGenerator does not accept parameters");
	}
}
