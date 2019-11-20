package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.Medication;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@code Medication} entities.
 *
 * @see Medication
 */
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    @Query("SELECT r FROM Medication r WHERE r.patientId = ?1")
    List<Medication> findAllByPatientId(@NotNull String patientId);

    @Query("SELECT r.patientId FROM Reading r WHERE r.id = ?1")
    String findPatientIdOfReadingWithId(int readingId);
}
