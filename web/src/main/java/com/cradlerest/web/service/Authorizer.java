package com.cradlerest.web.service;

import org.jetbrains.annotations.NotNull;

/**
 * Authorizer is an interface which provides methods to determine whether a
 * user has access to various parts of the system.
 *
 * Authorizer instances are created via the {@code AuthorizerFactor} service.
 *
 * @see AuthorizerFactory
 */
public interface Authorizer {

	/**
	 * Returns {@code true} if the authorizer grants permission to query a list
	 * of available patients. Note that this does not imply <i>all</i> patients.
	 * @return {@code true} if permission is granted, otherwise {@code false}.
	 */
	boolean canListPatients();

	/**
	 * Returns {@code true} if the authorizer grants access to the patient with
	 * a given {@code id}.
	 * @param id The identifier of the patient to request access to.
	 * @return {@code true} if access is granted, otherwise {@code false}.
	 */
	boolean canAccessPatient(@NotNull String id);

	/**
	 * Returns {@code true} if the authorizer grants permission to create a new
	 * patient entity.
	 * @return {@code true} if permission is granted to create a new patient.
	 */
	boolean canCreatePatient();

	/**
	 * Returns {@code true} if the authorizer grants access to a particular
	 * reading.
	 * @param id The id of the reading to request access to.
	 * @return {@code true} if access is granted, otherwise {@code false}
	 */
	boolean canAccessReading(int id);
}
