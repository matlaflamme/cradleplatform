package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.error.DeadlockException;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
import com.cradlerest.web.util.datagen.impl.UniformNoise;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Arrays;

/**
 * Application entry point for the dummy data generation tool.
 *
 * Accepts a single, optional command line argument which is used as the seed
 * for the random number generator allowing for the reproduction of data.
 */
public class GenerateDummyData {

	// TODO: Add support for UNIQUE constraint

	private static final String SEARCH_PACKAGE = "com.cradlerest.web";

	private static final int DEFAULT_AMOUNT = 20;

	public static void main(String[] args) {
		var noise = args.length == 1
				? new UniformNoise(args[0].hashCode())
				: new UniformNoise();
		try {
			var entities = linearize(getAllEntityTypes());
			var factory = new DataFactory(noise);
			for (var entityClass : entities) {
				var iter = factory.prepare(entityClass);
				var amount = entityClass.isAnnotationPresent(DataGenAmount.class)
						? entityClass.getAnnotation(DataGenAmount.class).value()
						: DEFAULT_AMOUNT;
				var sqlStatements = iter
						.take(amount)
						.map(Data::toSqlStatement)
						.fold((accum, x) -> accum + "\n\n" + x);
				System.out.print(sqlStatements + "\n\n\n");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Returns the set of all database entity classes in the "com.cradlerest.web"
	 * package. These classes should have a one-to-one correspondence to the
	 * database schema allowing us to generate dummy data based on their structure.
	 *
	 * @return A set of entity classes.
	 */
	private static Vec<Class<?>> getAllEntityTypes() throws MissingAnnotationException {
		Reflections.log = null;
		var reflections = new Reflections(SEARCH_PACKAGE);
		var entities = Vec.copy(reflections.getTypesAnnotatedWith(javax.persistence.Entity.class))
				.filter(e -> !e.isAnnotationPresent(Omit.class));

		// ensure that classes have the @Table annotation
		for (var entity : entities) {
			if (!entity.isAnnotationPresent(javax.persistence.Table.class)) {
				throw MissingAnnotationException.type(entity, javax.persistence.Table.class);
			}
		}

		return entities;
	}

	/**
	 * Returns the list of classes that {@code type} references via {@code ForeignKey}
	 * annotations. The result is not ordered in any particular way.
	 *
	 * @param type The class to find foreign references for.
	 * @return A set of classes that {@code type} references.
	 * @throws MissingAnnotationException If the a field of {@code type} references
	 * 	a non-entity class via a foreign key.
	 */
	private static Vec<? extends Class<?>> referencesOf(@NotNull Class<?> type) throws MissingAnnotationException {
		var references = Vec.copy(Arrays.asList(type.getDeclaredFields()))
				.filter(field -> field.isAnnotationPresent(ForeignKey.class))
				.map(field -> field.getAnnotation(ForeignKey.class))
				.map(ForeignKey::value)
				.filter(dep -> !dep.isAnnotationPresent(Omit.class));

		// assert that each reference is also an @Entity
		for (var reference : references) {
			if (!reference.isAnnotationPresent(javax.persistence.Entity.class)) {
				throw MissingAnnotationException.type(reference, javax.persistence.Entity.class);
			}
		}

		return references;
	}

	/**
	 * Orders a collection of classes in such a way that any given class appears
	 * after all of the classes it references as foreign keys.
	 *
	 * For example, given three classes, A, B, C where A references both B and C
	 * via foreign keys and C references B via a foreign key, the only possible
	 * ordering is: [B, C, A].
	 *
	 * Throws a {@code DeadlockException} if a circular reference is found. For
	 * example, if B also referenced A via a foreign key in the above example.
	 *
	 * @param types The collection of types to linearize.
	 * @return A new collection consisting of the same items as {@code types} but
	 * 	in a linearized order.
	 * @throws DeadlockException If a circular reference is found.
	 * @throws MissingAnnotationException Propagates from {@code referencesOf}.
	 */
	private static Vec<Class<?>> linearize(@NotNull Vec<Class<?>> types)
			throws DeadlockException, MissingAnnotationException {
		var partitioned = types.partition(type -> referencesOf(type).isEmpty());
		var ordered = partitioned._1;
		var unordered = partitioned._2;

		if (ordered.isEmpty()) {
			throw new DeadlockException("unable to find a class with no foreign keys");
		}

		while (!unordered.isEmpty()) {
			final var finalOrdered = ordered;
			partitioned = unordered.partition(type -> referencesOf(type).all(finalOrdered::contains));

			// if we can't order any classes this round, we have a circular
			// reference an it is impossible to linearize them
			if (partitioned._1.isEmpty()) {
				throw new DeadlockException("circular reference detected");
			}

			ordered = ordered.append(partitioned._1);
			unordered = partitioned._2;
		}

		return ordered;
	}
}
