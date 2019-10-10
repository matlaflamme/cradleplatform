package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Generic generator for enumeration types. Randomly picks an enum value from
 * the enumeration.
 *
 * Due to limitations with Java's generics system, the class of the enumerated
 * type must be supplied as a parameter to the generator using the "type" key.
 *
 * Usage Example:
 * <code>
 *     var noise = new UniformNoise();
 *     var generator = new EnumGenerator&gt;Colour%lt;(noise);
 *     var colour = generator.with("type", Colour.class).generate();
 * </code>
 *
 * @param <T> The enum type to generate values for.
 */
public class EnumGenerator<T extends Enum> implements Generator<T> {

	private Noise noise;
	private Class<T> type = null;

	public EnumGenerator(Noise noise) {
		this.noise = noise;
	}

	@Override
	public T generate() {
		if (type == null) {
			throw new IllegalStateException("value for 'type' parameter must be supplied");
		}
		return noise.pick(Arrays.asList(type.getEnumConstants()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Generator<T> with(@NotNull String key, @NotNull Object value) {
		if (!"type".equals(key)) {
			throw new IllegalArgumentException("illegal key: " + key);
		}
		if (!(value instanceof Class<?>)) {
			throw new IllegalArgumentException("illegal value for key: " + key);
		}

		// we suppress this warning here because we cannot be sure that `value`
		// is of type `Class<T>` only that it is of type `Class<?>`
		type = (Class<T>) value;
		return this;
	}
}
