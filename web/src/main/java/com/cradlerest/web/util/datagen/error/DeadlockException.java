package com.cradlerest.web.util.datagen.error;

/**
 * Exception thrown in the even of a circular reference.
 */
public class DeadlockException extends RuntimeException {

	public DeadlockException(String message) {
		super(message);
	}
}
