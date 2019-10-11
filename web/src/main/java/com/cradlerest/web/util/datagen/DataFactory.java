package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.error.ForeignKeyException;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
import com.cradlerest.web.util.datagen.error.NoDefinedGeneratorException;
import com.cradlerest.web.util.datagen.impl.BooleanGenerator;
import com.cradlerest.web.util.datagen.impl.EnumGenerator;
import com.cradlerest.web.util.datagen.impl.IntegerGenerator;
import com.cradlerest.web.util.datagen.impl.StringGenerator;
import com.github.maumay.jflow.iterator.Iter;
import com.github.maumay.jflow.iterator.RichIterator;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class DataFactory {

	@NotNull
	private final Noise noise;

	@NotNull
	private final Map<Class<?>, Generator<?>> generators = new HashMap<>();

	@NotNull
	private final ForeignKeyRepository foreignKeyRepository = new ForeignKeyRepository();

	DataFactory(@NotNull Noise noise) {
		this.noise = noise;

		// register default generators
		registerGenerator(Integer.class, new IntegerGenerator(noise));
		registerGenerator(String.class, new StringGenerator(noise));
		registerGenerator(Boolean.class, new BooleanGenerator(noise));
	}

	/**
	 * Registers a generator instance for objects of type {@code T} with this
	 * factory. Fields who's types have registered generators need not use the
	 * {@code @Generator} annotation to specify the generator type to use for
	 * the field.
	 *
	 * Only one generator per generation type may be registered at a given time.
	 * Registering a generator for a type which already exists, overwrites the
	 * existing generator replacing it with a new one. For example, one could
	 * overwrite the default {@code IntegerGenerator} by passing in a new
	 * generator which produces new {@code Integer} objects.
	 *
	 * @param type The type that the generator generates.
	 * @param generator An instance of the generator to register.
	 * @param <T> The type that the generator generates.
	 */
	<T> void registerGenerator(Class<T> type, @NotNull Generator<T> generator) {
		generators.put(type, generator);
	}

	/**
	 * Constructs an infinite, lazy iterator which procedurally creates random
	 * data values for a given {@code @Entity} type.
	 *
	 * Example Usage:
	 * <code>
	 *     var factory = new DataFactor(new UniformNoise());
	 *     // objects is a vector of 50 random data objects for the `User` class
	 *     var objects = factory.prepare(User.class)
	 *                          .take(50)
	 *                          .toVec();
	 * </code>
	 *
	 * @param type Class to create data objects for.
	 * @return An infinite data iterator.
	 */
	RichIterator<Data> prepare(@NotNull Class<?> type) {
		return Iter.call(() -> {
			var model = generateModel(type);
			var data = model.getFields().map(this::generateDataForField);
			return new Data(model.getTable(), data.toMap(tup -> tup._1, tup -> tup._2));
		});
	}

	/**
	 * Generates a data value for a given {@code DataField} returning said
	 * value along with the column name associated with the value as a pair.
	 *
	 * @param field The field to generate data for.
	 * @return A column, data value pair.
	 */
	private Tup<String, Object> generateDataForField(@NotNull DataField field) {
		final var columnName = field.getColumn().name();

		final var value = field.isForeignKeyField()
				? generateForeignKeyValue(field)
				: generateValueViaGenerator(field);

		if (field.isIdField()) {
			foreignKeyRepository.put(field.getTableType(), columnName, value);
		}

		return new Tup<>(columnName, value);
	}

	/**
	 * Picks a random value for a given foreign key {@code field}.
	 * @param field The field to generate a value for.
	 * @return The generated value.
	 * @throws ForeignKeyException If unable to generate a foreign key value.
	 */
	private Object generateForeignKeyValue(@NotNull DataField field) throws ForeignKeyException {
		assert field.isForeignKeyField();

		final var foreignKeyAnnotation = (ForeignKey) field.getAnnotations()
				.filter(a -> a instanceof ForeignKey)
				.head();
		final var type = foreignKeyAnnotation.value();
		final var column = foreignKeyAnnotation.column();

		Vec<Object> candidateValues;
		if (column.isEmpty()) {
			candidateValues = foreignKeyRepository.get(type);
		} else {
			candidateValues = foreignKeyRepository.get(type, column);
		}

		return noise.pick(candidateValues.toList());
	}

	/**
	 * Generates a value for a given {@code field} using one of the registered
	 * generators.
	 * @param field The field to generate a value for.
	 * @return The generated value.
	 */
	private Object generateValueViaGenerator(@NotNull DataField field) {
		final var fieldType = field.getType();

		var generator = generators.get(fieldType);
		if (generator == null) {
			if (fieldType.isEnum()) {
				generator = new EnumGenerator<Enum<?>>(noise).with("type", fieldType);
				// store the generator so we don't need to re-create it later
				generators.put(fieldType, generator);
			} else {
				throw NoDefinedGeneratorException.forType(fieldType);
			}
		}

		return generator.generate();
	}

	/**
	 * Generates a {@code DataModel} for a given {@code @Entity} instance by
	 * querying the values of the object's fields.
	 *
	 * Fields annotated with {@code @Omit} are ignored from this computation.
	 *
	 * @return A data model containing the values from the instance.
	 * @throws MissingAnnotationException If a field without the {@code @Omit}
	 * 	annotation does not contain a {@code @Column} annotation.
	 */
	private static DataModel generateModel(@NotNull Class<?> type) throws MissingAnnotationException {
		assert type.isAnnotationPresent(javax.persistence.Entity.class);
		assert type.isAnnotationPresent(javax.persistence.Table.class);
		assert !type.isAnnotationPresent(Omit.class);

		final var tableType = type;
		final var tableName = type.getAnnotation(javax.persistence.Table.class).name();

		// find all fields to convert
		final var fields = Vec.copy(Arrays.asList(type.getDeclaredFields()))
				.filter(field -> !field.isAnnotationPresent(Omit.class));

		// ensure needed annotations are present
		for (var field : fields) {
			if (!field.isAnnotationPresent(javax.persistence.Column.class)) {
				throw MissingAnnotationException.field(field.getName(), javax.persistence.Column.class);
			}
		}

		// convert fields + values into DataField objects
		final var modelFields = fields.iter()
				.map(field -> {
					var column = field.getAnnotation(javax.persistence.Column.class);
					var annotations = Vec.copy(Arrays.asList(field.getAnnotations()));
					return new DataField(tableType, column, field.getType(), annotations);
				});

		return new DataModel(tableName, tableType, modelFields.toVec());
	}
}
