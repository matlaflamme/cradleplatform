package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Symptom;
import com.cradlerest.web.service.repository.SymptomRepository;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The service responsible for interfacing with the {@code SymptomRepository}.
 *
 * Since symptoms are statically defined in the database and will rarely change
 * during runtime, this service includes an internal cache to allow for querying
 * of symptoms without having to access the database.
 */
public class SymptomManagerImpl implements SymptomManager {

	private final SymptomRepository symptomRepository;

	private Map<String, Symptom> symptomCache = null;

	public SymptomManagerImpl(SymptomRepository symptomRepository) {
		this.symptomRepository = symptomRepository;
	}

	/**
	 * Searches for, and returns, the symptom with a given text attribute.
	 *
	 * {@code text} is first normalized before searching for the associated
	 * symptom allowing for case-insensitive searching.
	 *
	 * For example, the following is valid:
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
	public Symptom getSymptom(@NotNull String text) throws EntityNotFoundException {
		lazyInitializeSymptomCache();
		var symptom = symptomCache.get(normalize(text));
		if (symptom == null) {
			var msg = String.format("symptom does not exist: '%s'", text);
			throw new EntityNotFoundException(msg);
		}
		return symptom;
	}

	@Override
	public void relateReadingWithSymptom(@NotNull Integer readingId, @NotNull Integer symptomId) {
		// TODO: implement me
	}

	/**
	 * Initialize's the service's symptom cache if it has not yet been initialized.
	 */
	private void lazyInitializeSymptomCache() {
		if (symptomCache == null) {
			initializeCache();
		}
	}

	/**
	 * Initialize's the service's symptom cache by pulling data from the database.
	 */
	private void initializeCache() {
		symptomCache = new HashMap<>();
		var symptoms = symptomRepository.findAll();
		for (var symptom : symptoms) {
			assert symptom.getText() != null;
			symptomCache.put(normalize(symptom.getText()), symptom);
		}
	}

	/**
	 * Normalizes a string.
	 *
	 * In order to preserve MySQL's case-insensitive string comparison we need
	 * to normalize search strings when looking for symptoms in the cache.
	 *
	 * @param str The string to normalize.
	 * @return A normalized version of {@code str}.
	 */
	private static String normalize(@NotNull String str) {
		return str.toLowerCase();
	}
}
