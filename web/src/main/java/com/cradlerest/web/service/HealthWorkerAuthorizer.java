package com.cradlerest.web.service;

import com.cradlerest.web.service.repository.ReferralRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

/**
 * Authorizer implementation for health worker users.
 */
public class HealthWorkerAuthorizer extends Authorizer {

	private ReferralRepository referralRepository;

	public HealthWorkerAuthorizer(Authentication auth, ReferralRepository referralRepository) {
		super(auth);
		this.referralRepository = referralRepository;
	}

	@Override
	public boolean canListPatients() {
		return true;
	}

	@Override
	public boolean canAccessPatient(@NotNull String id) {
		return false;
	}

	@Override
	public boolean canCreatePatient() {
		return true;
	}

	@Override
	public boolean canAccessReading(int id) {
		return false;
	}
}
