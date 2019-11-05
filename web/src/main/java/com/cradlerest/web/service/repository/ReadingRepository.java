package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.Reading;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Database repository for {@code Reading} entities.
 *
 * @see Reading
 */
public interface ReadingRepository extends JpaRepository<Reading, Integer>, PatientRepositoryCustom {

	@Query("SELECT r FROM Reading r WHERE r.patientId = ?1 ORDER BY r.timestamp DESC")
	List<Reading> findAllByPatientId(@NotNull String patientId);

	@Query("SELECT r.patientId FROM Reading r WHERE r.id = ?1")
	String findPatientIdOfReadingWithId(int readingId);

	List<Reading> findAllByCreatedBy(int createdBy);
}
