package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Data generation annotation specifying the chance that this field has of
 * being {@code null}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataGenNullChance {

	/**
	 * The chance that this field has of being {@code null}. Must be between
	 * the inclusive range [0, 1].
	 */
	@MetaDataGenAnnotationName("nullChance")
	double value();
}
