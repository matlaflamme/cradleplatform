package com.cradlerest.web.service;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Interface for services dealing with patient management.
 */
public interface PatientManagerService {

	/**
	 * Returns an entire patient profile by combining a patient entity with all
	 * of the readings associated with it.
	 *
	 * As the return type is not an entity in and of itself, the definition of
	 * the return type is left up to the implementer. Only the invariant that
	 * the returned object must be serializable to JSON must hold.
	 *
	 * @param id The identifier for the desired user.
	 * @return An aggregate object containing the full profile for the user.
	 * @throws Exception If an error occurred.
	 */
	Object getFullPatientProfile(@NotNull String id) throws Exception;

	Patient getPatientWithId(@NotNull String id) throws Exception;

	List<Patient> getAllPatients();

	List<Reading> getReadingsForPatientWithId(@NotNull String id);

	Patient constructPatient(Map<String, String> body) throws Exception;

	Reading constructReading(Map<String, String> body) throws Exception;
}
