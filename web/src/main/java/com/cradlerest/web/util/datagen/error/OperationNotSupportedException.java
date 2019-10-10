package com.cradlerest.web.util.datagen.error;

/**
 * Exception type for operations which are not supported.
 */
public class OperationNotSupportedException extends RuntimeException {

	public OperationNotSupportedException(String message) {
		super(message);
	}
}
