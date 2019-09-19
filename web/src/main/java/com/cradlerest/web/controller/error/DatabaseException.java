package com.cradlerest.web.controller.error;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * Base class for all database related exception types.
 */
public class DatabaseException extends RestException {

	public DatabaseException(@NotNull HttpStatus status, String message) {
		super(status, message);
	}

	public DatabaseException(@NotNull HttpStatus status, String message, Throwable cause) {
		super(status, message, cause);
	}
}
