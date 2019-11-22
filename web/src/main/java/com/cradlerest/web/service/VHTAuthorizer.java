package com.cradlerest.web.service;

import com.cradlerest.web.model.PatientWithLatestReadingView;
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

	/**
	 * A VHT can access any patient that it has readings created for or that it
	 * created itself.
	 * @param id The identifier of the patient to request access to.
	 * @return Whether the VHT can access a given patient or not.
	 */
	@Override
	public boolean canAccessPatient(@NotNull String id) {
		var details = getUserDetails();
		var a = Vec.copy(patientManager.getPatientsWithReadingsCreatedBy(details.getId()))
				.map(PatientWithLatestReadingView::getPatient)
				.any(p -> p.getId().equals(id));
		var b = Vec.copy(patientManager.getPatientsCreatedBy(details.getId()))
				.map(PatientWithLatestReadingView::getPatient)
				.any(p -> p.getId().equals(id));
		return a || b;
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
