package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps around an existing generator instance to generate objects with a
 * chance of being {@code null}.
 * @param <T> Type of object to generate.
 */
public class NullableGenerator<T> implements Generator<T> {

	private static final double DEFAULT_NULL_CHANCE = 0.2;
	private static final String NULL_CHANCE_KEY = "nullChance";

	@NotNull
	private Noise noise;

	@NotNull
	private Generator<T> baseGenerator;

	@NotNull
	private Map<String, Object> curriedParameters = new HashMap<>();

	public NullableGenerator(@NotNull Noise noise, @NotNull Generator<T> baseGenerator) {
		this.noise = noise;
		this.baseGenerator = baseGenerator;
	}

	private NullableGenerator(
			@NotNull Noise noise,
			@NotNull Generator<T> baseGenerator,
			@NotNull Map<String, Object> curriedParameters
	) {
		this.noise = noise;
		this.baseGenerator = baseGenerator;
		this.curriedParameters = curriedParameters;
	}

	@Override
	@Nullable
	public T generate() {
		if (noise.generate() < (Double) curriedParameters.getOrDefault(NULL_CHANCE_KEY, DEFAULT_NULL_CHANCE)) {
			return null;
		} else {
			return baseGenerator.generate();
		}
	}

	@Override
	public Generator<T> with(@NotNull String key, @NotNull Object value) throws IllegalArgumentException {
		if (key.equals(NULL_CHANCE_KEY)) {
			var val = (Double) value;
			if (val < 0 || val > 1) {
				throw new IllegalArgumentException("invalid value for key 'nullChance': " + value.toString());
			}
			var paramCopy = new HashMap<>(curriedParameters);
			paramCopy.put(key, value);
			return new NullableGenerator<>(noise, baseGenerator, paramCopy);
		} else {
			baseGenerator = baseGenerator.with(key, value);
			return this;
		}
	}
}
