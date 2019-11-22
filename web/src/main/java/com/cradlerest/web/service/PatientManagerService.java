package com.cradlerest.web.service;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.PatientWithLatestReadingView;
import com.cradlerest.web.model.Reading;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;

import java.util.List;

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

	/**
	 * Returns the {@code Patient} object with a given {@param id}.
	 * @param id Unique identifier for the requested patient.
	 * @return The patient with {@param id}.
	 * @throws Exception If no such patient exists or an error occurred.
	 */
	Patient getPatientWithId(@NotNull String id) throws Exception;

	/**
	 * Returns the list of all patients in the the database paired with their
	 * latest reading. If a patient has no readings, then {@code null} is
	 * returned in place of one.
	 * @return A list of patient/reading pairs.
	 */
	List<PatientWithLatestReadingView> getAllPatientsWithLastReading();

	/**
	 * Returns the list of all patients in the database.
	 * @return All patients.
	 */
	List<Patient> getAllPatients();

	/**
	 * Returns the list of readings associated with the patient with a given
	 * {@param id}.
	 * @param id Unique identifier for a patient.
	 * @return A list of readings, or, in the case of no such patient with the
	 * 	requested id, an empty list.
	 */
	List<Reading> getReadingsForPatientWithId(@NotNull String id);

	/**
	 * Returns all of the patients which have been referred to a specific
	 * health center.
	 * @param healthCenterId The id of the health center to query patients for.
	 * @return A list of patients.
	 */
	List<PatientWithLatestReadingView> getPatientsReferredToHealthCenter(int healthCenterId);

	/**
	 * Returns all of the patients who have a reading created by a specific
	 * user.
	 * @param userId The id of the user to get patients for.
	 * @return A list of patients.
	 */
	List<PatientWithLatestReadingView> getPatientsWithReadingsCreatedBy(int userId);

	/**
	 * Returns all of the patients created by the user with a given id.
	 * @param userId The id of the user to get patients for.
	 * @return A list of patients.
	 */
	List<Patient> getPatientsCreatedBy(int userId);

	/**
	 * Takes a patient and pairs it with its latest reading.
	 * @param patient The patient to pair with.
	 * @return The patient along with its latest reading.
	 */
	PatientWithLatestReadingView pairWithLatestReading(@NotNull Patient patient);

	/**
	 * Creates a new, or updates an existing, patient in the system. If a patient
	 * with the same id as {@param patient} exists, then that patient's profile
	 * will be overwritten with the contents of {@param patient}. If no such
	 * patient already exists, then a new one is created.
	 *
	 * @implNote The returned patient is not guarantied to be the same object
	 * 	as {@param patient}.
	 *
	 * @param patient The patient to persist.
	 * @return The saved patient.
	 * @throws Exception If an error occurred.
	 */
	Patient savePatient(@Nullable Authentication auth, @Nullable Patient patient) throws Exception;

	@Deprecated
	default Patient savePatient(@Nullable Patient patient) throws Exception {
		return savePatient(null, patient);
	}

	/**
	 * Creates a new, or updates an existing, reading in the system. If a reading
	 * with the same id as {@param reading} exists, then that reading is
	 * overwritten with the contents of {@param reading}. If no such reading
	 * already exists, then a new one is created.
	 *
	 * @implNote The returned reading is not guarantied to be the same object
	 * 	as {@param reading}.
	 *
	 * @param reading The reading to persist.
	 * @return The saved reading.
	 * @throws Exception If an error occurred.
	 */
	Reading saveReading(@Nullable Reading reading) throws Exception;

}
