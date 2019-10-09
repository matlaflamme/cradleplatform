package com.cradlerest.web.util.datagen;

import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.error.MissingAnnotationException;
import com.github.maumay.jflow.vec.Vec;
import org.reflections.Reflections;

/**
 * Application entry point for the dummy data generation tool.
 *
 * Accepts a single, optional command line argument which is used as the seed
 * for the random number generator allowing for the reproduction of data.
 */
public class GenerateDummyData {

	private static final String SEARCH_PACKAGE = "com.cradlerest.web";

	public static void main(String[] args) {
		try {
			var entities = getAllEntityTypes();
			for (var entity : entities) {
				System.out.println(entity.getName());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Returns the set of all database entity classes in the "com.cradlerest.web"
	 * package. These classes should have a one-to-one correspondence to the
	 * database schema allowing us to generate dummy data based on their structure.
	 *
	 * @return A set of entity classes.
	 */
	private static Vec<Class<?>> getAllEntityTypes() throws MissingAnnotationException {
		Reflections.log = null;
		var reflections = new Reflections(SEARCH_PACKAGE);
		var entities = Vec.copy(reflections.getTypesAnnotatedWith(javax.persistence.Entity.class))
				.filter(e -> !e.isAnnotationPresent(Omit.class));

		// ensure that classes have the @Table annotation
		for (var entity : entities) {
			if (!entity.isAnnotationPresent(javax.persistence.Table.class)) {
				throw MissingAnnotationException.type(entity, javax.persistence.Table.class);
			}
		}

		return entities;
	}
}
