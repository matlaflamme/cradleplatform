package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.builder.SymptomReadingRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Database repository for {@code SymptomReadingRelation} entities.
 *
 * @see SymptomReadingRelation
 */
public interface SymptomReadingRelationRepository
		extends JpaRepository<SymptomReadingRelation, Integer> {
}
