package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service responsible for managing {@code Reading} entities.
 *
 * Because the external <i>view</i> of a {@code Reading} differs from how they
 * are handled by the database. This service acts as the translation layer
 * between the external {@code ReadingView} (what is transferred via JSON to
 * the front-end clients) and the internal {@code Reading} model which matches
 * the database description of a reading entity.
 *
 * The service provides no guaranties on the implementation of the reading view
 * class, only that it is serializable to JSON in such a way that conforms to
 * the documentation specification.
 *
 * In brief, the reading view is an aggregation of many different database
 * entities ({@code Reading}, {@code Symptom}, {@code Medication, etc.).
 */
public interface ReadingManager {

	/**
	 * Returns a reading view for the reading with a given identifier.
	 * @param readingId The id of the reading view to construct.
	 * @return A new reading view for the specified reading.
	 * @throws EntityNotFoundException If unable to find a reading with the
	 * 	given identifier.
	 */
	ReadingView getReadingView(@NotNull Integer readingId) throws EntityNotFoundException;

	/**
	 * Returns a list of all reading views for a given patient.
	 * @param patientId The id of the patient to construct reading views for.
	 * @return A list of reading views.
	 * @throws EntityNotFoundException If unable to find a patient with the
	 * 	given identifier.
	 */
	List<ReadingView> getAllReadingViewsForPatient(@NotNull String patientId) throws EntityNotFoundException;

	/**
	 * Returns the list of all readings created by a specific user.
	 * @param userId The id of the user to get readings for.
	 * @return A list of reading views.
	 */
	List<ReadingView> getAllCreatedBy(int userId);

	/**
	 * Deconstructs and persists a given reading view.
	 * @param readingView A reading view.
	 * @return The saved reading.
	 */
	Reading saveReadingView(@NotNull ReadingView readingView) throws EntityNotFoundException;
}
