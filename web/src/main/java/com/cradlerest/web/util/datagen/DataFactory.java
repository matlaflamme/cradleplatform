package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.error.ForeignKeyException;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
import com.cradlerest.web.util.datagen.error.NoDefinedGeneratorException;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import com.cradlerest.web.util.datagen.impl.*;
import com.github.maumay.jflow.iterator.Iter;
import com.github.maumay.jflow.iterator.RichIterator;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for constructing random {@code Data} objects from given
 * {@code @Entity types}.
 *
 * Instead of producing the types directly, the factory constructs a closure
 * which is used as the range for an infinite iterator which is passed back in
 * place of a concrete list of {@code Data} objects. This gives the caller more
 * freedom of how it wants to work with the data. For example, the caller could
 * directly persist the data to disk, using a lazy iterator we save memory by
 * not having to store a huge chunk for {@code Data} objects in memory.
 */
class DataFactory {

	@NotNull
	private final Noise noise;

	@NotNull
	private final Map<Class<?>, Generator<?>> generators = new HashMap<>();

	@NotNull
	private final Map<Class<?>, Generator<?>> customGenerators = new HashMap<>();

	@NotNull
	private final ForeignKeyRepository foreignKeyRepository = new ForeignKeyRepository();

	DataFactory(@NotNull Noise noise) {
		this.noise = noise;

		// register default generators
		registerGenerator(Integer.class, new IntegerGenerator(noise));
		registerGenerator(String.class, new StringGenerator(noise));
		registerGenerator(Boolean.class, new BooleanGenerator(noise));
		registerGenerator(Date.class, new DateGenerator(noise));
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
	 * Registers a custom generator with this factory instance. Custom
	 * generators are generators which a field can request by using the
	 * {@code @Generator} annotation.
	 *
	 * @param generator Instance of the generator to register.
	 */
	void registerCustomGenerator(@NotNull Generator<?> generator) {
		customGenerators.put(generator.getClass(), generator);
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

		var value = field.isForeignKeyField()
				? generateForeignKeyValue(field)
				: generateValueViaGenerator(field);

		// can't store literal `null` in a tuple so use DataModel's NULL_VALUE
		// constant as a work around
		if (value == null) {
			value = DataModel.NULL_VALUE;
		}

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
	 * @throws IllegalArgumentException If an invalid argument was passed to
	 * 	the generator. This happens when a field is annotated with a
	 * 	{@code DataGen} annotation which that field's type does not support.
	 * @throws OperationNotSupportedException If attempting to pass parameters
	 * 	to a generator which does not support parameter passing.
	 */
	private Object generateValueViaGenerator(@NotNull DataField field)
			throws IllegalArgumentException, OperationNotSupportedException {
		final var fieldType = field.getType();

		var generator = field.requiresCustomGenerator()
				? customGenerators.get(field.requestedGeneratorType())
				: generators.get(fieldType);
		if (generator == null) {
			if (fieldType.isEnum()) {
				generator = new EnumGenerator<Enum<?>>(noise).with("type", fieldType);
				// store the generator so we don't need to re-create it later
				generators.put(fieldType, generator);
			} else {
				throw NoDefinedGeneratorException.forType(fieldType);
			}
		}

		// if field is nullable, wrap generator in a NullableGenerator
		if (!field.isIdField() && field.isNullable()) {
			generator = new NullableGenerator<>(noise, generator);
		}

		// curry parameters to the generator
		for (var annotation : field.getAnnotations().filter(DataFactory::isCurryAnnotation)) {
			var values = annotationValues(annotation);
			for (var tup : values) {
				generator = generator.with(tup._1, tup._2);
			}
		}

		return generator.generate();
	}

	/**
	 * Returns {@code true} if a given {@code annotation} contains values which
	 * should be passed to the generator when constructing a value.
	 * @param annotation The annotation to check.
	 * @return {@code true} if the values in the annotation should be passed to
	 * 	a generator.
	 */
	private static boolean isCurryAnnotation(@NotNull Annotation annotation) {
		return annotation instanceof DataGenRange ||
		       annotation instanceof DataGenStringParams ||
		       annotation instanceof DataGenNullChance ||
		       annotation instanceof DataGenDateRange;
	}

	/**
	 * Returns a list of name/value pairs for all fields in a given
	 * {@code annotation}.
	 * @param annotation The annotation to get values from.
	 * @return A list of values in this annotation.
	 */
	private static Vec<Tup<String, Object>> annotationValues(@NotNull Annotation annotation) {
		return Vec.copy(Arrays.asList(annotation.annotationType().getDeclaredMethods()))
				.map(method -> {
					try {
						String key;
						if (method.isAnnotationPresent(MetaDataGenAnnotationName.class)) {
							key = method.getAnnotation(MetaDataGenAnnotationName.class).value();
						} else {
							key = method.getName();
						}
						return Tup.of(key, method.invoke(annotation));
					} catch (InvocationTargetException | IllegalAccessException e) {
						// since all values in an annotation should be public
						// it is a programming error if we ever get here
						assert false;
						throw new RuntimeException(e);
					}
				});
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
