package com.cradlerest.web.controller.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception type for requests to forbidden resources.
 *
 * Response code: 403
 */
public class AccessDeniedException extends RestException {

	public AccessDeniedException(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}

	public AccessDeniedException(String message, Throwable cause) {
		super(HttpStatus.FORBIDDEN, message, cause);
	}
}
