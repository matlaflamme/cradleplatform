package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.AccessDeniedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

/**
 * Authorizer is an interface which provides methods to determine whether a
 * user has access to various parts of the system.
 *
 * Authorizer instances are created via the {@code AuthorizerFactor} service.
 *
 * @see AuthorizerFactory
 */
public abstract class Authorizer {

	private Authentication auth;

	protected Authorizer(Authentication auth) {
		this.auth = auth;
	}

	protected Authentication getAuth() {
		return auth;
	}

	/**
	 * Returns {@code true} if the authorizer grants permission to query a list
	 * of available patients. Note that this does not imply <i>all</i> patients.
	 * @return {@code true} if permission is granted, otherwise {@code false}.
	 */
	public abstract boolean canListPatients();

	/**
	 * Returns {@code true} if the authorizer grants access to the patient with
	 * a given {@code id}.
	 * @param id The identifier of the patient to request access to.
	 * @return {@code true} if access is granted, otherwise {@code false}.
	 */
	public abstract boolean canAccessPatient(@NotNull String id);

	/**
	 * Returns {@code true} if the authorizer grants permission to create a new
	 * patient entity.
	 * @return {@code true} if permission is granted to create a new patient.
	 */
	public abstract boolean canCreatePatient();

	/**
	 * Returns {@code true} if the authorizer grants access to a particular
	 * reading.
	 * @param id The id of the reading to request access to.
	 * @return {@code true} if access is granted, otherwise {@code false}
	 */
	public abstract boolean canAccessReading(int id);

	/**
	 * Method reference interface for an {@code Authorizer} method which
	 * takes no parameters.
	 */
	public interface AccessRequest {
		boolean call(Authorizer a);
	}

	/**
	 * Method reference interface for an {@code Authorizer} method which
	 * takes a single parameter.
	 */
	public interface SingletonAccessRequest<T> {
		boolean call(Authorizer a, T t);
	}

	/**
	 * Invokes the given access request and throws an exception if it returns
	 * {@code false}.
	 *
	 * Usage example:
	 * <code>
	 *     authorizerFactory.construct(auth)
	 *         .check(Authorizer::canListPatients);
	 * </code>
	 *
	 * @param r An access request method. Should be passed as a method reference
	 *          to one of {@code Authorizer}'s methods.
	 * @throws AccessDeniedException If the access request was denied.
	 */
	public final void check(AccessRequest r) throws AccessDeniedException {
		if (!r.call(this)) {
			throw new AccessDeniedException("Permission denied");
		}
	}

	/**
	 * Invokes the given access request and throws an exception if it returns
	 * {@code false}.
	 *
	 * Usage example:
	 * <code>
	 *     authorizerFactory.construct(auth)
	 *         .check(Authorizer::canAccessPatient, somePatientId);
	 * </code>
	 *
	 * @param r An access request method. Should be passed as a method reference
	 *          to one of {@code Authorizer}'s methods.
	 * @throws AccessDeniedException If the access request was denied.
	 */
	public final <T> void check(SingletonAccessRequest<T> r, T t) throws AccessDeniedException {
		if (!r.call(this, t)) {
			throw new AccessDeniedException("Permission denied: entity " + t.toString());
		}
	}
}
