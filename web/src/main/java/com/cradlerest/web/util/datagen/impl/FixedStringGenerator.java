package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import org.jetbrains.annotations.NotNull;

/**
 * Always generates a fixed string.
 */
public class FixedStringGenerator implements Generator<String> {

	private String generatorString = null;

	@Override
	public String generate() {
		assert generatorString != null;
		return generatorString;
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws IllegalArgumentException {
		if (key.equals("value") && value instanceof String) {
			generatorString = (String) value;
			return this;
		} else {
			throw new IllegalArgumentException("invalid key/value");
		}
	}
}
