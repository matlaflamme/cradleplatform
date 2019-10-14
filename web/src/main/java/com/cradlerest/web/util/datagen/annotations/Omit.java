package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Omit} annotation tells the data generation system to 'omit' the
 * annotated class/field from data generation.
 *
 * When present on an {@code @Entity} class, the system will not generate any
 * data for that table.
 *
 * When present on a field, the system will not include that field in any data
 * generated for the corresponding table. This may be useful, for example, when
 * generating data with {@code AUTO_INCREMENT} primary keys.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Omit {
}
