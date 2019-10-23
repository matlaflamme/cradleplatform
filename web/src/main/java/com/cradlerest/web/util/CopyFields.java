package com.cradlerest.web.util;

import com.github.maumay.jflow.utils.Tup;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CopyFields {

	/**
	 * Copies the values of all common fields from {@code src} to {@code dst}.
	 * @param src The source object.
	 * @param dst The destination object.
	 */
	public static void copyFields(@NotNull Object src, @NotNull Object dst) {
		var srcFields = fieldsOf(src.getClass());
		var dstFieldTupSet = fieldsOf(dst.getClass())
				.map(field -> Tup.of(field.getName(), field.getType()))
				.toSet();

		var commonFields = srcFields
				.filter(field -> dstFieldTupSet.contains(Tup.of(field.getName(), field.getType())));

		var dstFields = fieldsOf(dst.getClass());
		for (var field : commonFields) {
			try {
				var value = field.get(src);
				var dstField = dstFields
						.filter(f -> f.getName().equals(field.getName()))
						.head();
				dstField.set(dst, value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Returns a list of declared fields in the given type and it's direct
	 * superclass if it has one.
	 * @param type Class to get fields for.
	 * @return A list of fields.
	 */
	private static Vec<Field> fieldsOf(@NotNull Class<?> type) {
		var fields = Vec.copy(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null) {
			fields = fields.append(Arrays.asList(type.getSuperclass().getDeclaredFields()));
		}
		fields.forEach(field -> field.setAccessible(true));
		return fields;
	}
}
