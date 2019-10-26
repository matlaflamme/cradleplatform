package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import org.jetbrains.annotations.NotNull;

public class AutoIncrementGenerator implements Generator<Integer> {

	private static final String START_VAL_KEY = "start";

	private int nextValue = 1;

	@Override
	public Integer generate() {
		var value = nextValue;
		nextValue += 1;
		return value;
	}

	@Override
	public Generator<Integer> with(@NotNull String key, @NotNull Object value) throws IllegalArgumentException {
		if (key.equals(START_VAL_KEY) && value instanceof Integer) {
			this.nextValue = (Integer) value;
			return this;
		} else {
			throw new IllegalArgumentException(String.format("invalid argument pair: %s: %s", key, value.toString()));
		}
	}
}
