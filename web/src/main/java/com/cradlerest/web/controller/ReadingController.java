package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for saving and retrieving {@code ReadingView} objects.
 */
@RestController
@RequestMapping("/api/reading")
public class ReadingController {

	private ReadingManager readingManager;

	public ReadingController(ReadingManager readingManager) {
		this.readingManager = readingManager;
	}

	/**
	 * Uploads and persists a new reading view in the database.
	 * @param readingView The reading view to save.
	 */
	@PostMapping("save")
	public void save(@RequestBody ReadingView readingView) throws BadRequestException {
		try {
			readingManager.saveReadingView(readingView);
		} catch (InstantiationError | EntityNotFoundException e) {
			throw new BadRequestException("invalid request body", e);
		}
	}

	/**
	 * Gets a reading view for a reading with a given {@code id}.
	 * @param readingId The id of the reading to retrieve.
	 * @return A reading view for the reading.
	 * @throws EntityNotFoundException If unable to find a reading with the
	 * 	given identifier.
	 */
	@GetMapping("{id}")
	public ReadingView get(@PathVariable("id") Integer readingId) throws EntityNotFoundException {
		return readingManager.getReadingView(readingId);
	}
}
