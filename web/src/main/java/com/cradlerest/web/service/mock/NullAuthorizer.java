package com.cradlerest.web.service.mock;

import com.cradlerest.web.service.Authorizer;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

/**
 * A null object for the {@code Authorizer} interface.
 */
public class NullAuthorizer extends Authorizer {

	public NullAuthorizer(Authentication auth) {
		super(auth);
	}

	@Override
	public boolean canListPatients() {
		return true;
	}

	@Override
	public boolean canAccessPatient(@NotNull String id) {
		return true;
	}

	@Override
	public boolean canCreatePatient() {
		return true;
	}

	@Override
	public boolean canAccessReading(int id) {
		return true;
	}
}
