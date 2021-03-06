package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Database repository for {@code Patient} entities.
 *
 * @see Patient
 */
public interface PatientRepository extends JpaRepository<Patient, String>, PatientRepositoryCustom {

	/**
	 * Returns all patients who have been referred to a given health center.
	 *
	 * If the health center id is invalid, an empty list is returned.
	 *
	 * Since it is possible that a patient was referred multiple times to the
	 * same health center, we use the {@code DISTINCT} quantifier.
	 * @param healthCentreId The id of the health center to get patients for.
	 * @return A list of patients.
	 */
	@Query("SELECT DISTINCT p " +
			"FROM Patient p JOIN Referral r ON r.patientId = p.id " +
			"WHERE r.healthCentreId = ?1")
	List<Patient> getAllReferredToHealthCenter(int healthCentreId);

	/**
	 * Returns all patients who have a reading which was created by a specified
	 * user. The user could be either a VHT or health worker, the server does
	 * not care what role the user has.
	 *
	 * If the user id is invalid, an empty list is returned.
	 * @param userId The id of the user to find patients for.
	 * @return A list of patients.
	 */
	@Query("SELECT DISTINCT p " +
			"FROM Patient  p JOIN Reading r ON r.patientId = p.id " +
			"WHERE r.createdBy = ?1")
	List<Patient> getAllWithReadingsBy(int userId);

	/**
	 * Returns all of the patients created by a given user.
	 * @param userId The id of the user to find patients for.
	 * @return A list of patients.
	 */
	List<Patient> findAllByCreatedBy(int userId);
}
