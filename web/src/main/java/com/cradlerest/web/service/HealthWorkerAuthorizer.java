package com.cradlerest.web.service;

import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

/**
 * Authorizer implementation for health worker users.
 */
public class HealthWorkerAuthorizer extends Authorizer {

	private PatientManagerService patientManager;

	public HealthWorkerAuthorizer(Authentication auth, PatientManagerService patientManager) {
		super(auth);
		this.patientManager = patientManager;
	}

	@Override
	public boolean canListPatients() {
		return true;
	}

	/**
	 * Determines whether a health worker user can access a patient with a given
	 * identifier. This is true iff the patient has been referred to the health
	 * centre that the worker works at.
	 * @param id The identifier of the patient to request access to.
	 * @return {@code true} or {@code false} whether a health worker can access
	 * 	a patient with a given id;
	 */
	@Override
	public boolean canAccessPatient(@NotNull String id) {
		var details = getUserDetails();
		var healthCentreId = details.getWorksAtHealthCentreId();

		// If this health worker is not part of a health centre, then it cannot
		// access any patients.
		if (healthCentreId == null) {
			return false;
		}

		var patients = Vec.copy(patientManager.getPatientsReferredToHealthCenter(healthCentreId));
		return patients.any(p -> p.getPatient().getId().equals(id));
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
