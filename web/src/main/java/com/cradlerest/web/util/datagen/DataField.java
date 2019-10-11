package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import java.lang.annotation.Annotation;

/**
 * Model class containing information about a single field in a {@code DataModel}.
 */
class DataField {

	@NotNull
	private Class<?> tableType;

	@NotNull
	private Column column;

	@NotNull
	private Class<?> type;

	@NotNull
	private Vec<Annotation> annotations;

	DataField(
			@NotNull Class<?> tableType,
			@NotNull Column column,
			@NotNull Class<?> type,
			@NotNull Vec<Annotation> annotations)
	{
		this.tableType = tableType;
		this.column = column;
		this.type = type;
		this.annotations = annotations;
	}

	@NotNull
	Class<?> getTableType() {
		return tableType;
	}

	@NotNull
	Column getColumn() {
		return column;
	}

	@NotNull
	Class<?> getType() {
		return type;
	}

	@NotNull
	Vec<Annotation> getAnnotations() {
		return annotations;
	}

	boolean isIdField() {
		return annotations.any(a -> a instanceof javax.persistence.Id);
	}

	boolean isForeignKeyField() {
		return annotations.any(a -> a instanceof ForeignKey);
	}
}
