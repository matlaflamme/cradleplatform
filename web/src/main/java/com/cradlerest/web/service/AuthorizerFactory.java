package com.cradlerest.web.service;

import com.cradlerest.web.model.UserRole;
import com.cradlerest.web.service.mock.NullAuthorizer;
import org.springframework.security.core.Authentication;

import static com.cradlerest.web.util.AuthenticationExt.hasRole;

public class AuthorizerFactory {

	private PatientManagerService patientManager;
	private ReadingManager readingManager;

	public AuthorizerFactory(PatientManagerService patientManager, ReadingManager readingManager) {
		this.patientManager = patientManager;
		this.readingManager = readingManager;
	}

	/**
	 * Constructs an {@code Authorizer} instance for a given user authentication.
	 * @param auth The user authentication to wrap in an {@code Authorizer}.
	 * @return A new {@code Authorizer}
	 */
	public Authorizer construct(Authentication auth) {
		if (auth == null) {
			return new DenyAllAuthorizer(null);
		}

		if (hasRole(auth, UserRole.HEALTH_WORKER)) {
			return new HealthWorkerAuthorizer(auth, patientManager, readingManager);
		} else if (hasRole(auth, UserRole.VHT)) {
			return new VHTAuthorizer(auth, patientManager, readingManager);
		} else {
			return new NullAuthorizer(auth);
		}
	}
}
