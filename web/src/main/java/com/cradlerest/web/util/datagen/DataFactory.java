package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.error.NoDefinedGeneratorException;
import com.cradlerest.web.util.datagen.impl.BooleanGenerator;
import com.cradlerest.web.util.datagen.impl.EnumGenerator;
import com.cradlerest.web.util.datagen.impl.IntegerGenerator;
import com.cradlerest.web.util.datagen.impl.StringGenerator;
import com.github.maumay.jflow.utils.Tup;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DataFactory {

	@NotNull
	private Noise noise;

	@NotNull
	private Map<Class<?>, Generator<?>> generatorMap = new HashMap<>();

	public <T> void registerGenerator(Class<T> type, @NotNull Generator<T> generator) {
		generatorMap.put(type, generator);
	}

	public DataFactory(@NotNull Noise noise) {
		this.noise = noise;

		// register default generators
		registerGenerator(Integer.class, new IntegerGenerator(noise));
		registerGenerator(String.class, new StringGenerator(noise));
		registerGenerator(Boolean.class, new BooleanGenerator(noise));
	}

	public Data generate(@NotNull DataModel model) {
		var table = model.getTable();
		var data = model.getFields().map(this::generateDataForField);
		return new Data(table, data.toMap(tup -> tup._1, tup -> tup._2));
	}

	private Tup<String, Object> generateDataForField(@NotNull DataField field) {
		final var columnName = field.getColumn().name();
		final var fieldType = field.getType();

		var generator = generatorMap.get(fieldType);
		if (generator == null) {
			if (fieldType.isEnum()) {
				generator = new EnumGenerator<Enum<?>>(noise).with("type", fieldType);
				// store the generator so we don't need to re-create it later
				generatorMap.put(fieldType, generator);
			} else {
				throw NoDefinedGeneratorException.forType(fieldType);
			}
		}

		var value = generator.generate();
		return new Tup<>(columnName, value);
	}
}
