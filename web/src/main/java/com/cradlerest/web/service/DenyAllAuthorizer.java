package com.cradlerest.web.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

/**
 * An authorizer implementation which denies all access.
 */
public class DenyAllAuthorizer extends Authorizer {

	DenyAllAuthorizer(Authentication auth) {
		super(auth);
	}

	@Override
	public boolean canListPatients() {
		return false;
	}

	@Override
	public boolean canAccessPatient(@NotNull String id) {
		return false;
	}

	@Override
	public boolean canCreatePatient() {
		return false;
	}

	@Override
	public boolean canAccessReading(int id) {
		return false;
	}
}
