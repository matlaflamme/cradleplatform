package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes the minimum (inclusive) and maximum (exclusive) values which should
 * be generated for a given field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataGenRange {

	/**
	 * Inclusive lower bound.
	 */
	int min();

	/**
	 * Exclusive upper bound.
	 */
	int max();
}
