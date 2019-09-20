package com.cradlerest.web.controller.error;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for all custom exceptions thrown by controllers.
 *
 * Allows for custom handling of how exceptions are rendered to the web client.
 */
public class RestException extends Exception {

	@NotNull
	private HttpStatus status;

	RestException(@NotNull HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	RestException(@NotNull HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	@NotNull
	public HttpStatus getStatus() {
		return status;
	}
}
