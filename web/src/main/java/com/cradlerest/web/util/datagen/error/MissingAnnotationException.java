package com.cradlerest.web.util.datagen.error;

import org.jetbrains.annotations.NotNull;

/**
 * Exception type thrown when a target is missing a required annotation.
 */
public class MissingAnnotationException extends Exception {

	public static MissingAnnotationException type(@NotNull Class<?> type, @NotNull Class<?> annotation) {
		return new MissingAnnotationException("class", type.getName(), annotation);
	}

	public static MissingAnnotationException method(@NotNull String methodName, @NotNull Class<?> annotation) {
		return new MissingAnnotationException("method", methodName, annotation);
	}

	public static MissingAnnotationException field(@NotNull String fieldName, @NotNull Class<?> annotation) {
		return new MissingAnnotationException("field", fieldName, annotation);
	}

	private MissingAnnotationException(@NotNull String targetType, @NotNull String target, @NotNull Class<?> annotation) {
		super(String.format("%s %s missing expected annotation %s", targetType, target, annotation.getName()));
	}
}
