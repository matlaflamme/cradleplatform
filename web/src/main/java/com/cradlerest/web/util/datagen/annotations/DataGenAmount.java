package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: it may be nice to eventually implement a relative system
//		 e.g., create 5 instances of type A for each instance of type B

/**
 * Determines the number of instances of the annotated type to create when
 * generating data.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataGenAmount {
	int value();
}
