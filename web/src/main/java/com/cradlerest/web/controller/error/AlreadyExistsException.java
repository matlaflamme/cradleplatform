package com.cradlerest.web.controller.error;

import org.hibernate.dialect.Database;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends DatabaseException {
	public AlreadyExistsException(String username) {
		super(HttpStatus.CONFLICT, "entity already exists: " + username);

	}
}
