package com.cradlerest.web.constraints.user;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Defines an annotation for a valid username
 */
@Documented
@Constraint(validatedBy = UsernameValidator.class) // referenced validator class
@Target({TYPE, ANNOTATION_TYPE, ElementType.FIELD}) // where (and what) this annotation can be used on
@Retention(RUNTIME)
public @interface ValidUsername {

	String message() default "Invalid Username Format"; // error message if bad request

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
