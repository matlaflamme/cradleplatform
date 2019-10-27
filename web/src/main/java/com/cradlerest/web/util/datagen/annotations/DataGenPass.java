package com.cradlerest.web.util.datagen.annotations;

import com.cradlerest.web.util.datagen.DataPass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a {@code DataPass} which will be run after on the data generated
 * for this type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataGenPass {

	Class<? extends DataPass> value();
}
