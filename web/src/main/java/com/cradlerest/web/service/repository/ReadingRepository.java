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
public interface ReadingRepository extends JpaRepository<Reading, Integer> {

	@Query("SELECT r FROM Reading r JOIN FETCH r.patient p WHERE p.id = ?1 ORDER BY r.date DESC")
	List<Reading> findAllByPatientId(@NotNull String patientId);
}
