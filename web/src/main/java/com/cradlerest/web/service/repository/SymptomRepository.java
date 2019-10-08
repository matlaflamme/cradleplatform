package com.cradlerest.web.service.repository;


import com.cradlerest.web.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Database repository for {@code Symptom} entities.
 *
 * @see Symptom
 */
public interface SymptomRepository extends JpaRepository<Symptom, Integer> {
}
