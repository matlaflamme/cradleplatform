package com.cradlerest.web.util;

import com.cradlerest.web.model.builder.InstantiationError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A collection of static validation methods.
 */
public class Validation {

	/**
	 * Throws an instantiation exception if a given field is {@code null}.
	 * @param object The field value to test.
	 * @param fieldName The name of the field, for error handling purposes.
	 * @throws InstantiationError If {@code object} is null.
	 */
	public static void assertFieldNotNull(@Nullable Object object, @NotNull String fieldName)
			throws InstantiationError {

		if (object == null) {
			throw new InstantiationError(String.format("field '%s' is null", fieldName));
		}
	}
}
