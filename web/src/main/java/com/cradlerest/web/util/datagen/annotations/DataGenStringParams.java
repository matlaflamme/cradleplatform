package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines parameters to pass to a {@code StringGenerator} when generating a
 * value for the annotated field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataGenStringParams {
	/**
	 * The length of the string to generate.
	 */
	int length() default -1;

	/**
	 * The character set to use to generate the string.
	 */
	String charset();
}
