package com.cradlerest.web.util.datagen.error;

/**
 * Exception thrown in the even of a circular reference.
 */
public class DeadlockException extends Exception {

	public DeadlockException(String message) {
		super(message);
	}
}
