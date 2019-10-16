package com.cradlerest.web.util.datagen.error;


import org.jetbrains.annotations.NotNull;

/**
 * Exception type thrown when unable to find a generator for a specific field.
 */
public class NoDefinedGeneratorException extends RuntimeException {

	public static NoDefinedGeneratorException forType(@NotNull Class<?> type) {
		return new NoDefinedGeneratorException(String.format("no registered generator for type %s", type.toString()));
	}

	private NoDefinedGeneratorException(String message) {
		super(message);
	}
}
