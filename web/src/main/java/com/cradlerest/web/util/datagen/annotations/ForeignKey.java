package com.cradlerest.web.util.datagen.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code ForeignKey} annotation expresses an SQL foreign key constraint
 * between the annotated field and some other field in a different class.
 *
 * The data generation system uses theses annotations as a guide when
 * constructing its data generation pipeline: data for foreign tables must be
 * generated before the ones that reference them.
 *
 * Example - Implicit Foreign Key:
 * <code>
 *     // references the &amp;Id field in the User class
 *     &amp;ForeignKey(User.class)
 *     private Integer uid;
 * </code>
 *
 * Example - Explicit Foreign Key:
 * <code>
 *     &amp;ForeignKey(value = Patient.class, field = "id")
 *     private String pid;
 * </code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {

	/**
	 * The class that this foreign key references. This class must also be an
	 * {@code Entity} class.
	 */
	Class<?> value();

	/**
	 * The field that this foreign key references. If left empty, the system
	 * will attempt to infer the foreign key by looking for the foreign class's
	 * field with the {@code Id} annotation.
	 *
	 * The system is unable to infer the foreign key if the foreign class has
	 * a composite primary key.
	 */
	String field() default "";
}
