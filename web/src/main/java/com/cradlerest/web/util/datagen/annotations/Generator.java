package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the generator class to use to generate values for this field. If
 * omitted from a field, the default generator for the field type will be used
 * if one is available.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Generator {
	Class<?> value();
}
