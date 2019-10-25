package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.SymptomReadingRelation;
import com.cradlerest.web.model.view.SymptomView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Database repository for {@code SymptomReadingRelation} entities.
 *
 * @see SymptomReadingRelation
 */
public interface SymptomReadingRelationRepository extends JpaRepository<SymptomReadingRelation, Integer> {

	/**
	 * Returns the list of symptoms associated with a reading with a given id.
	 * If the reading does not have any symptoms, or if the no reading with the
	 * given {@code readingId} exists, returns an empty list.
	 * @param readingId The id of a reading to get symptoms for.
	 * @return A list of symptoms.
	 */
	@Query("SELECT s FROM SymptomReadingRelation sr JOIN Symptom s ON sr.symptomId = s.id WHERE sr.readingId = ?1")
	List<SymptomView> getSymptomsForReading(int readingId);
}
