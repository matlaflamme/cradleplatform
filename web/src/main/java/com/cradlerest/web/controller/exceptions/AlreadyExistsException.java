package com.cradlerest.web.controller.exceptions;

import org.springframework.http.HttpStatus;

/*
 * Utility to handle duplicate entries (accounts/usernames in this case)
 */
public class AlreadyExistsException extends DatabaseException {
	public AlreadyExistsException(String username) {
		super(HttpStatus.CONFLICT, "entity already exists: " + username);

	}
}
