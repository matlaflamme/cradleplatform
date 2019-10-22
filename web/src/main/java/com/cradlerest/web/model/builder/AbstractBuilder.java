package com.cradlerest.web.model.builder;

/**
 * Base class for all builders.
 *
 * Contains the object which is to be built along with a {@code build} method
 * to finalize the building process. It is common for subclasses to override
 * the {@code build} method to perform validation on the constructed object
 * before returning it.
 *
 * @param <T> The type of object to build.
 */
abstract class AbstractBuilder<T> {

	protected T value;

	public T build() {
		return value;
	}
}
