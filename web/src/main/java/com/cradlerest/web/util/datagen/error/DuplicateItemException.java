package com.cradlerest.web.util.datagen.error;

/**
 * Exception thrown in the event of a duplicate item passed to a method which
 * does not except such lists.
 */
public class DuplicateItemException extends RuntimeException {

	public DuplicateItemException(String message) {
		super(message);
	}
}
