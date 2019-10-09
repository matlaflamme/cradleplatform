package com.cradlerest.web.controller.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception type denoting that this API or service method has not yet been
 * implemented.
 *
 * Response Code: 500
 */
public class NotImplementedException extends RestException {

	public NotImplementedException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR, "not yet implemented");
	}
}
