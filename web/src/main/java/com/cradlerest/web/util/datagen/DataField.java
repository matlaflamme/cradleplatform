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
	private Vec<Annotation> annotations;

	public DataField(
			@NotNull Column column,
			@NotNull Class<?> type,
			@NotNull Vec<Annotation> annotations)
	{
		this.column = column;
		this.type = type;
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

	@NotNull
	public Vec<Annotation> getAnnotations() {
		return annotations;
	}
}
