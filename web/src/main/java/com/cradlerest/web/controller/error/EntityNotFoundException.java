package com.cradlerest.web.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Database exception type denoting a request for an entity which does not
 * exist.
 *
 * HTTP Response Status: 404
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity Not Found")
public class EntityNotFoundException extends DatabaseException {
}
