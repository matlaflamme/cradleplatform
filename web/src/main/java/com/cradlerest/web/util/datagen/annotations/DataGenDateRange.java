package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Data generation annotation bounding a date field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataGenDateRange {
	/**
	 * Inclusive lower data bound.
	 *
	 * Either in format "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss".
	 */
	String min();

	/**
	 * Exclusive upper data bound.
	 *
	 * Either in format "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss".
	 */
	String max();
}
