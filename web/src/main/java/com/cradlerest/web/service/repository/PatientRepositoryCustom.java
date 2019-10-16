package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.PatientWithLatestReadingView;

import java.util.List;

/**
 * Extension component to {@code PatientRepository} allowing for custom
 * implementation of repository methods.
 */
public interface PatientRepositoryCustom {

	/**
	 * Returns a list of all patients in the database along with the latest
	 * reading for each patient. If a patient has no readings, then
	 * {@code null} is returned in place of the latest reading.
	 */
	List<PatientWithLatestReadingView> getAllPatientsAndLatestReadings();
}
