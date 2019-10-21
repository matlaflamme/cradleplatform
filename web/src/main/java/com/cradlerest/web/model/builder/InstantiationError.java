package com.cradlerest.web.model.builder;

/**
 * Error type thrown from {@code build} methods of various {@code Builder}
 * objects. Means that an attempt was made to instantiate an incomplete or
 * invalid object.
 */
public class InstantiationError extends Error {

	public InstantiationError(String message) {
		super(message);
	}
}
