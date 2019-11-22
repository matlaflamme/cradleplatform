package com.cradlerest.web.service;

import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

/**
 * Authorizer implementation for VHT users.
 */
public class VHTAuthorizer extends Authorizer {

	private PatientManagerService patientManager;
	private ReadingManager readingManager;

	VHTAuthorizer(
			Authentication auth,
			PatientManagerService patientManager,
			ReadingManager readingManager
	) {
		super(auth);
		this.patientManager = patientManager;
		this.readingManager = readingManager;
	}

	@Override
	public boolean canListPatients() {
		return true;
	}

	@Override
	public boolean canAccessPatient(@NotNull String id) {
		var details = getUserDetails();
		var patients = Vec.copy(patientManager.getPatientsWithReadingsCreatedBy(details.getId()));
		return patients.any(p -> p.getPatient().getId().equals(id));
	}

	@Override
	public boolean canCreatePatient() {
		return true;
	}

	@Override
	public boolean canAccessReading(int id) {
		try {
			var reading = readingManager.getReadingView(id);
			var patientId = reading.getPatientId();
			return canAccessPatient(patientId);
		} catch (Exception e) {
			return false;
		}
	}
}
