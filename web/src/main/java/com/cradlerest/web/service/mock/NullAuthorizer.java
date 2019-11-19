package com.cradlerest.web.service.mock;

import com.cradlerest.web.service.Authorizer;
import org.jetbrains.annotations.NotNull;

/**
 * A null object for the {@code Authorizer} interface.
 */
public class NullAuthorizer implements Authorizer {

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
