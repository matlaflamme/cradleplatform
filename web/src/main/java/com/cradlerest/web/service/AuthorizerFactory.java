package com.cradlerest.web.service;

import com.cradlerest.web.model.UserRole;
import com.cradlerest.web.service.mock.NullAuthorizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

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

		var roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		if (roles.contains(UserRole.HEALTH_WORKER.getRoleString())) {
			return new HealthWorkerAuthorizer(auth, patientManager, readingManager);
		} else if (roles.contains(UserRole.VHT.getRoleString())) {
			return new VHTAuthorizer(auth, patientManager, readingManager);
		} else {
			return new NullAuthorizer(auth);
		}
	}
}
