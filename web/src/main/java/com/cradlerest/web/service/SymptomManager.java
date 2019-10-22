package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Symptom;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for services dealing with symptoms.
 */
public interface SymptomManager {

	/**
	 * Searches for, and returns, the symptom with a given text attribute.
	 *
	 * {@code text} is case-insensitive. For example:
	 * <code>
	 *     var s1 = getSymptom("headache");
	 *     var s2 = getSymptom("HEADACHE");
	 *     assert s1 == s2;
	 * </code>
	 *
	 * @param text Symptom text to find the associated symptom for.
	 * @return The symptom that has a given text field.
	 * @throws EntityNotFoundException If no such symptom exists.
	 */
	Symptom getSymptom(@NotNull String text) throws EntityNotFoundException;

	/**
	 * Creates a link between a reading and a symptom in the database.
	 *
	 * Both {@code readingId} and {@code symptomId}
	 *
	 * @param readingId The id of the reading to link.
	 * @param symptomId The id of the symptom to link to.
	 * @throws EntityNotFoundException If unable to find the given reading or
	 * 	symptom in the database.
	 */
	void relateReadingWithSymptom(@NotNull Integer readingId, @NotNull Integer symptomId)
			throws EntityNotFoundException;

	/**
	 * Convenience overload allowing for the creation of a link between a given
	 * reading and the symptom with a given, case-insensitive, text description.
	 * @param readingId The id of the reading to link.
	 * @param symptomText The text of the symptom to link to.
	 * @throws EntityNotFoundException If unable to find the given reading or
	 * 	symptom in the database.
	 */
	default void relateReadingWithSymptom(@NotNull Integer readingId, @NotNull String symptomText)
			throws EntityNotFoundException {

		var symptom = getSymptom(symptomText);
		relateReadingWithSymptom(readingId, symptom.getId());
	}
}
