package com.cradlerest.web.util.datagen;

import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

/**
 * Model class containing the data layout of a {@code @Entity} class.
 */
public class DataModel {

	public static final Object NULL_VALUE = new Object() {
		@Override
		public String toString() {
			return "null";
		}
	};

	@NotNull
	private String table;

	@NotNull
	private Vec<DataField> fields;

	public DataModel(@NotNull String table, @NotNull Vec<DataField> fields) {
		this.table = table;
		this.fields = fields;
	}

	@NotNull
	public String getTable() {
		return table;
	}

	@NotNull
	public Vec<DataField> getFields() {
		return fields;
	}
}
