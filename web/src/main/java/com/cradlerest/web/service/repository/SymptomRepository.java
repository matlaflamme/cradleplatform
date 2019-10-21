package com.cradlerest.web.service.repository;


import com.cradlerest.web.model.Symptom;
import com.cradlerest.web.model.view.SymptomView;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Database repository for {@code Symptom} entities.
 *
 * @see Symptom
 * @see SymptomView
 */
public interface SymptomRepository extends JpaRepository<Symptom, Integer> {

	/**
	 * Returns a {@code SymptomView} (i.e., only the {@code text} field of a
	 * symptom) for the symptom with a given {@code id}.
	 * @param id The primary key of the symptom to get.
	 * @return A {@code SymptomView}.
	 */
	SymptomView getSymptomViewById(@NotNull Integer id);
}
