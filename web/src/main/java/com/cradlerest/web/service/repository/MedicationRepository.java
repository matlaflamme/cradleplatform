package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.Medication;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
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

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medication med WHERE med.pid = ?1 AND med.med_id = ?2", nativeQuery = true)
//    @Query("SELECT r FROM Medication r WHERE r.patientId = ?1")
    void deleteMedicationByPatientId(@NotNull String pid, int medId);
}
