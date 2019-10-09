package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Generator class for {@code Integer} objects.
 *
 * Accepts the following parameters via curry:
 *  - "min": Integer
 *  - "max": Integer
 */
public class IntegerGenerator implements Generator<Integer> {

	private static final int DEFAULT_MIN = 0;
	private static final int DEFAULT_MAX = Short.MAX_VALUE;

	private Noise noise;
	private Map<String, Object> curriedParameters = new HashMap<>();

	public IntegerGenerator(Noise noise) {
		this.noise = noise;
	}

	private IntegerGenerator(Noise noise, Map<String, Object> curriedParameters) {
		this.noise = noise;
		this.curriedParameters = curriedParameters;
	}

	@Override
	public Integer generate() {
		var min = (Integer) curriedParameters.getOrDefault("min", DEFAULT_MIN);
		var max = (Integer) curriedParameters.getOrDefault("max", DEFAULT_MAX);
		return (int) noise.generate(min, max);
	}

	@Override
	public Generator<Integer> with(@NotNull String key, @NotNull Object value) {
		var paramClone = new HashMap<>(curriedParameters);
		paramClone.put(key, value);
		return new IntegerGenerator(noise, paramClone);
	}
}
