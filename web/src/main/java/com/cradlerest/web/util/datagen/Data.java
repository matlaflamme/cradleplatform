package com.cradlerest.web.util.datagen;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * General data container.
 */
public class Data {

	@NotNull
	private String table;

	@NotNull
	private Map<String, Object> columnValueMap;

	public Data(@NotNull String table,  @NotNull Map<String, Object> columnValueMap) {
		this.table = table;
		this.columnValueMap = columnValueMap;
	}

	@NotNull
	public String getTable() {
		return table;
	}

	@NotNull
	public Map<String, Object> getColumnValueMap() {
		return columnValueMap;
	}
}
