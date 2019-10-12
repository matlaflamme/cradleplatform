package com.cradlerest.web.util.datagen;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for pseudo random generators.
 */
public interface Noise {

	/**
	 * Generates a pseudo random number normalized to the range [0, 1).
	 * @return A pseudo random number.
	 */
	double generate();

	/**
	 * Returns a pseudo random integer within the range [{@code min}, {@code max}).
	 * @param min Inclusive lower bound for the result.
	 * @param max Exclusive upper bound for the result.
	 * @return A pseudo random number.
	 */
	default long generate(long min, long max) {
		assert max > min;

		var val = generate();
		var span = max - min;
		return Math.round(Math.floor((val * span) + min));
	}

	/**
	 * Returns a random element from a non-empty list.
	 * @param list The list to pick from.
	 * @param <T> List element type.
	 * @return An element from the list.
	 */
	default <T> T pick(@NotNull List<T> list) {
		assert !list.isEmpty();

		var index = (int) generate(0, list.size());
		return list.get(index);
	}

	/**
	 * Returns a random element from a non-empty array.
	 * @param array The array to pick from.
	 * @param <T> List element type.
	 * @return An element from the list.
	 */
	default <T> T pick(@NotNull T[] array) {
		assert array.length != 0;

		var index = (int) generate(0, array.length);
		return array[index];
	}

	/**
	 * Returns a random element from a non-empty string.
	 * @param string The string to pick from.
	 * @return An element from the list.
	 */
	default char pick(@NotNull String string) {
		assert !string.isEmpty();

		var index = (int) generate(0, string.length());
		return string.charAt(index);
	}

	/**
	 * Generates a random boolean with a given chance of being {@code true}.
	 * @param chance The chance that the boolean will be be {@code true} as
	 *               a percent ranging from 0 to 1.
	 * @return A random boolean.
	 */
	default boolean	generateBool(double chance) {
		assert chance >= 0 && chance <= 1;

		return generate() < chance;
	}
}
