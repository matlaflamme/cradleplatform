package com.cradlerest.web.util;

import com.cradlerest.web.util.datagen.error.DeadlockException;
import com.cradlerest.web.util.datagen.error.DuplicateItemException;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.function.Function;

public class Algorithm {

	/**
	 * Orders a vector of elements in such a way that all elements appear after
	 * all of their dependencies.
	 *
	 * For example, given three elements, A, B, C where A depends on both B and
	 * C and C depends only on B, the only possible ordering is: [B, C, A].
	 *
	 * @param vec The vector of elements to linearize.
	 * @param dependencies A function which computes the dependencies for any
	 *                     given element.
	 * @param <T> The elements' type.
	 * @return A new vector of ordered elements.
	 * @throws DeadlockException If a circular dependency chain is found
	 * 	between the elements in {@code vec}.
	 * @throws DuplicateItemException If {@code vec} contains duplicate items.
	 */
	public static <T> Vec<T> linearize(@NotNull Vec<T> vec, @NotNull Function<T, Vec<T>> dependencies)
			throws DeadlockException, DuplicateItemException {

		if (vec.isEmpty()) {
			return vec;
		}
		assertNoDuplicates(vec);

		var partitioned = vec.partition(elem -> dependencies.apply(elem).isEmpty());
		var ordered = partitioned._1;
		var unordered = partitioned._2;

		if (ordered.isEmpty()) {
			throw new DeadlockException("cycle detected");
		}

		while (!unordered.isEmpty()) {
			final var finalOrdered = ordered;
			partitioned = unordered.partition(elem -> dependencies.apply(elem).all(finalOrdered::contains));

			// if we can't order any classes this round, we have a circular
			// reference an it is impossible to linearize them
			if (partitioned._1.isEmpty()) {
				throw new DeadlockException("cycle detected");
			}

			ordered = ordered.append(partitioned._1);
			unordered = partitioned._2;
		}

		return ordered;
	}

	/**
	 * Throws a {@code DuplicateItemException} in the event that a given
	 * iterable collection.
	 * @param iter The iterable collection to check.
	 * @param <T> The item type. Duplicates are determined via calls to this
	 *           type's {@code equals} method.
	 * @throws DuplicateItemException If a duplicate item is found.
	 */
	private static <T> void assertNoDuplicates(@NotNull Iterable<T> iter) throws DuplicateItemException {
		final var items = new HashSet<T>();
		for (var item : iter) {
			if (!items.add(item)) {
				throw new DuplicateItemException(item.toString());
			}
		}
	}

	/**
	 * Removes any duplicates from {@code iter} returning a new {@code Vec}
	 * containing no duplicates with the items in the same order as they
	 * appear in {@code iter}.
	 * @param iter A collection of items which may contain duplicates.
	 * @param <T> The item type.
	 * @return A vector without duplicates.
	 */
	public static <T> Vec<T> removeDuplicates(@NotNull Iterable<T> iter) {
		final var items = new HashSet<T>();
		Vec<T> result = Vec.of();
		for (var item : iter) {
			if (!items.contains(item)) {
				result = result.append(item);
				items.add(item);
			}
		}
		return result;
	}

}
