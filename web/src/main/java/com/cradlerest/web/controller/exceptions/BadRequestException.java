package com.cradlerest.web.controller.exceptions;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

/**
 * Exception type for invalid or malformed API requests.
 *
 * Response Code: 400
 */
public class BadRequestException extends RestException {

	public static BadRequestException missingField(@NotNull String field) {
		return new BadRequestException(String.format("missing field: '%s'", field));
	}

	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(HttpStatus.BAD_REQUEST, message, cause);
	}
}
