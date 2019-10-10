package com.cradlerest.web.util.datagen;

import org.jetbrains.annotations.NotNull;

/**
 * Generic value generator interface.
 *
 * Generators may be parameterized via the {@code with} method which curries a
 * new generator instance using some parameter.
 *
 * For example, passing a min and max value to an integer generator can be done
 * like so:
 *
 * <code>
 *     var intGenerator = new IntegerGenerator();
 *     Integer value = intGenerator
 *                      .with("min", 3)
 *                      .with("max", 10)
 *                      .generate();
 * </code>
 *
 * @param <T> Generated object type.
 */
public interface Generator<T> {

	/**
	 * Generates a new random object of type {@code T}.
	 * @return A new object.
	 */
	T generate();

	/**
	 * Curries a new generator by passing a parameter to this generator.
	 * @param key Parameter key (e.g., "min", "max", etc.).
	 * @param value Parameter value.
	 * @return A new generator which should respect parameterized values.
	 */
	Generator<T> with(@NotNull String key, @NotNull Object value);
}
