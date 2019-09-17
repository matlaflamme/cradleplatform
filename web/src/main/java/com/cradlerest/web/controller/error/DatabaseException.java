package com.cradlerest.web.controller.error;

/**
 * Base class for all database related exception types.
 */
public abstract class DatabaseException extends Exception {

	/**
	 * Disables stack trace generation for this exception type. Doing this
	 * removes the wall of text that gets printed by {@code curl} in the
	 * event of an exception when testing API calls.
	 *
	 * @return this
	 */
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
