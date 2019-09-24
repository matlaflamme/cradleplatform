package com.cradlerest.web.controller.error;

import org.springframework.http.HttpStatus;

/**
 * Database exception type denoting a request for an entity which does not
 * exist.
 *
 * HTTP Response Status: 404
 */
public class EntityNotFoundException extends DatabaseException {

	public EntityNotFoundException(Object id) {
		super(HttpStatus.NOT_FOUND, String.format("entity with id '%s' not found", id.toString()));
	}

	public EntityNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(HttpStatus.NOT_FOUND, message, cause);
	}
}
