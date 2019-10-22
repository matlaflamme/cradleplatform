package com.cradlerest.web.util.datagen.error;

/**
 * Exception type thrown when unable to resolve a foreign key relation between
 * two types.
 */
public class ForeignKeyException extends RuntimeException {

	public ForeignKeyException(String message) {
		super(message);
	}
}
