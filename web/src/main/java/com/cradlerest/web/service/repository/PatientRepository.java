package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Database repository for {@code Patient} entities.
 *
 * @see Patient
 */
public interface PatientRepository extends JpaRepository<Patient, String>, PatientRepositoryCustom {
}
