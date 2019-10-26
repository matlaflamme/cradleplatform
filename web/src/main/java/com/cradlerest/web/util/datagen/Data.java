package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.DataGenOrdinal;
import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.BinaryOperator;

/**
 * General data container.
 */
public class Data {

	@NotNull
	private String table;

	@NotNull
	private Map<String, Object> columnValueMap;

	Data(@NotNull String table,  @NotNull Map<String, Object> columnValueMap) {
		this.table = table;
		this.columnValueMap = columnValueMap;
	}

	public @NotNull String getTable() {
		return table;
	}

	@NotNull
	Map<String, Object> getColumnValueMap() {
		return columnValueMap;
	}

	public @Nullable Object getValueForColumn(@NotNull String column) {
		return columnValueMap.get(column);
	}

	/**
	 * Converts this data instance into a SQL INSERT statement.
	 *
	 * Assumes that all field types are convertible to an SQL value.
	 * @return An SQL insert statement.
	 */
	public String toSqlStatement() {
		final var values = Vec.copy(columnValueMap.entrySet())
				.map(entry -> Tup.of(entry.getKey(), entry.getValue()));

		final BinaryOperator<String> concatWithCommas = (accum, a) -> accum + ", " + a;

		final var columnList = values
				.map(tup -> tup._1)
				.fold(concatWithCommas);
		final var valueList = values
				.map(tup -> tup._2)
				.map(Data::serializeToSqlValue)
				.fold(concatWithCommas);
		return String.format("INSERT INTO %s (%s)\n  VALUES (%s);", table, columnList, valueList);
	}

	private static String serializeToSqlValue(@NotNull Object value) {
		if (value instanceof String) {
			return "'" + value.toString() + "'";
		} else if (value instanceof Date) {
			// TODO: we might not always want to format the date in this way
			var formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "'" + formatter.format((Date) value) + "'";
		} else if (value instanceof Enum<?>) {
			if (value.getClass().isAnnotationPresent(DataGenOrdinal.class)) {
				return Integer.toString(((Enum) value).ordinal());
			} else {
				return value.toString();
			}
		} else {
			return value.toString();
		}
	}
}
