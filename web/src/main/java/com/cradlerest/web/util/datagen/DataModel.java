package com.cradlerest.web.util.datagen;

import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

/**
 * Model class containing the data layout of a {@code @Entity} class.
 */
class DataModel {

	public static final Object NULL_VALUE = new Object() {
		@Override
		public String toString() {
			return "null";
		}
	};

	@NotNull
	private String table;

	@NotNull
	private Class<?> tableType;

	@NotNull
	private Vec<DataField> fields;

	DataModel(@NotNull String table, @NotNull Class<?> tableType, @NotNull Vec<DataField> fields) {
		this.table = table;
		this.tableType = tableType;
		this.fields = fields;
	}

	@NotNull
	String getTable() {
		return table;
	}

	@NotNull
	Class<?> getTableType() {
		return tableType;
	}

	@NotNull
	Vec<DataField> getFields() {
		return fields;
	}
}
