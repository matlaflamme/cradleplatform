package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.DataGenRelativeAmount;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.error.DeadlockException;
import com.cradlerest.web.util.datagen.error.DuplicateItemException;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.ForeignKeyRepositoryImpl;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;
import com.cradlerest.web.util.datagen.impl.UniformNoise;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.cradlerest.web.util.datagen.Algorithm.*;

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
			var entities = linearizeTypes(getAllEntityTypes());
			var sqlStatements = generateData(noise, entities)
					.map(Data::toSqlStatement)
					.fold((accum, x) -> accum + "\n\n" + x);
			System.out.println(sqlStatements);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
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
	static Vec<Class<?>> referencesOf(@NotNull Class<?> type) throws MissingAnnotationException {
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

		// identity map(e -> e) is required to make the return type Vec<Class<?>>
		// instead of Vec<? extends Class<?>> for some reason
		return references.map(e -> e);
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
	 * @throws DuplicateItemException If {@code types} contains duplicate entries.
	 */
	static Vec<Class<?>> linearizeTypes(@NotNull Vec<Class<?>> types)
			throws DeadlockException, MissingAnnotationException, DuplicateItemException {

		return linearize(types, GenerateDummyData::dependenciesOf);
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

	private static Vec<Data> generateData(@NotNull Noise noise, @NotNull Vec<Class<?>> entities) {
		var factory = new DataFactory(noise, new ForeignKeyRepositoryImpl());
		factory.registerCustomGenerator(new GibberishSentenceGenerator(noise));
		factory.registerCustomGenerator(new AutoIncrementGenerator());

		Vec<Data> dataVec = Vec.of();
		var amountMap = new HashMap<Class<?>, Integer>();
		for (var entityClass : entities) {
			var iter = factory.prepare(entityClass);
			var amount = generationAmount(entityClass, amountMap);
			amountMap.put(entityClass, amount);

			dataVec = dataVec.append(iter.take(amount).toVec());
		}

		return dataVec;
	}

	private static int generationAmount(@NotNull Class<?> type, Map<Class<?>, Integer> amountMap) {
		if (type.isAnnotationPresent(DataGenRelativeAmount.class)) {
			var annotation = type.getAnnotation(DataGenRelativeAmount.class);
			var baseType = annotation.base();
			var multiplier = annotation.multiplier();

			// failed to linearize properly if this assertion fails
			assert amountMap.containsKey(baseType);

			return (int) ((double) amountMap.get(baseType) * multiplier);
		}

		return type.isAnnotationPresent(DataGenAmount.class)
				? type.getAnnotation(DataGenAmount.class).value()
				: DEFAULT_AMOUNT;
	}

	private static Optional<Class<?>> amountGenerationDependency(@NotNull Class<?> type) {
		return type.isAnnotationPresent(DataGenRelativeAmount.class)
				? Optional.of(type.getAnnotation(DataGenRelativeAmount.class).base())
				: Optional.empty();
	}

	private static Vec<Class<?>> dependenciesOf(@NotNull Class<?> type) throws MissingAnnotationException {
		var foreignKeyDependencies = referencesOf(type);
		var amountDependency = amountGenerationDependency(type);
		if (amountDependency.isEmpty() || foreignKeyDependencies.contains(amountDependency.get())) {
			return foreignKeyDependencies;
		}

		return foreignKeyDependencies.append(amountDependency.get());
	}
}
