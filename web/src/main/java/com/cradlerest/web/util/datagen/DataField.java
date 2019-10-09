package com.cradlerest.web.util.datagen;

import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import java.lang.annotation.Annotation;

/**
 * Model class containing information about a single field in a {@code DataModel}.
 */
public class DataField {

	@NotNull
	private Column column;

	@NotNull
	private Class<?> type;

	@NotNull
	private Object value;

	@NotNull
	private Vec<Annotation> annotations;

	public DataField(
			@NotNull Column column,
			@NotNull Class<?> type,
			@NotNull Object value,
			@NotNull Vec<Annotation> annotations)
	{
		this.column = column;
		this.type = type;
		this.value = value;
		this.annotations = annotations;
	}

	@NotNull
	public Column getColumn() {
		return column;
	}

	@NotNull
	public Class<?> getType() {
		return type;
	}

	@Nullable
	public Object getValue() {
		if (value == DataModel.NULL_VALUE) {
			return null;
		}

		return value;
	}

	@NotNull
	public Vec<Annotation> getAnnotations() {
		return annotations;
	}
}
