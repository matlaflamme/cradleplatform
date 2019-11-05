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
	 * @param healthCenterId The id of the health center to get patients for.
	 * @return A list of patients.
	 */
	@Query("SELECT DISTINCT p " +
			"FROM Patient p JOIN Reading r ON r.patientId = p.id JOIN Referral r2 ON r2.readingId = r.id " +
			"WHERE r2.referredToHealthCenterId = ?1")
	List<Patient> getAllReferredToHealthCenter(int healthCenterId);
}
