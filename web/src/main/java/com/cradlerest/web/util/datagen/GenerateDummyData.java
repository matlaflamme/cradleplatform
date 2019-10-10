package com.cradlerest.web.util.datagen;

import com.cradlerest.web.model.Sex;
import com.cradlerest.web.model.builder.PatientBuilder;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.error.DeadlockException;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
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

	private static final String SEARCH_PACKAGE = "com.cradlerest.web";

	public static void main(String[] args) {
		try {
			var entities = linearize(getAllEntityTypes());
			for (var entity : entities) {
				System.out.println(entity.getName());
			}

			var reading = new PatientBuilder()
					.id("001")
					.birthYear(1998)
					.name("hello")
					.sex(Sex.MALE)
					.pregnant(false)
					.villageNumber("1")
					.build();
			var model = generateModel(reading);
			System.out.printf("Table: %s\n", model.getTable());
			for (var fields : model.getFields()) {
				System.out.printf(
						"  %s: %s\n",
						fields.getColumn().name(),
						fields.getValue() == null ? "null" : fields.getValue().toString()
				);
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

	/**
	 * Generates a {@code DataModel} for a given {@code @Entity} instance by
	 * querying the values of the object's fields.
	 *
	 * Fields annotated with {@code @Omit} are ignored from this computation.
	 *
	 * @param instance The instance of the object to convert.
	 * @return A data model containing the values from the instance.
	 */
	private static DataModel generateModel(@NotNull final Object instance) throws MissingAnnotationException {
		var type = instance.getClass();
		assert type.isAnnotationPresent(javax.persistence.Entity.class);
		assert type.isAnnotationPresent(javax.persistence.Table.class);
		assert !type.isAnnotationPresent(Omit.class);

		var tableName = type.getAnnotation(javax.persistence.Table.class).name();

		// find all fields to convert
		var fields = Vec.copy(Arrays.asList(type.getDeclaredFields()))
				.filter(field -> !field.isAnnotationPresent(Omit.class));

		// ensure needed annotations are present
		for (var field : fields) {
			if (!field.isAnnotationPresent(javax.persistence.Column.class)) {
				throw MissingAnnotationException.field(field.getName(), javax.persistence.Column.class);
			}
		}

		// retrieve values for said fields
		var values = fields.map(field -> {
			try {
				field.setAccessible(true);
				var value = field.get(instance);
				return value == null ? DataModel.NULL_VALUE : value;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});

		// convert fields + values into DataField objects
		var modelFields = fields.iter().zip(values)
				.map(tup -> {
					var field = tup._1;
					var value = tup._2;
					var column = field.getAnnotation(javax.persistence.Column.class);
					var annotations = Vec.copy(Arrays.asList(value.getClass().getAnnotations()));
					return new DataField(column, value.getClass(), value, annotations);
				});

		return new DataModel(tableName, modelFields.toVec());
	}
}
