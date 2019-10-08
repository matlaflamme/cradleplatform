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
}
