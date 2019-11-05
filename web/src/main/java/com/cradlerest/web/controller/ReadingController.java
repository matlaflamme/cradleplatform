package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for saving and retrieving {@code ReadingView} objects.
 */
@RestController
@RequestMapping("/api/reading")
public class ReadingController {

    private static final String SYSTOLIC = "systolic";
    private static final String DIASTOLIC = "diastolic";
    private static final String HEART_RATE = "heartRate";

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

	// Proper way of calling this https://localhost:9000/api/reading/trafficlight?systolic=SYSTOLIC_INPUT_VALUE&diastolic=DIASTOLIC_INPUT_VALUE&heartRate=HEART_RATE_INPUT_VALUE
	@GetMapping ("trafficlight")
	public Integer getLight(@RequestParam Map<String,String> params) throws BadRequestException {

		Integer systolic;
		Integer diastolic;
		Integer heartRate;
		try{
			systolic = Integer.parseInt(params.get(SYSTOLIC));
			diastolic = Integer.parseInt(params.get(DIASTOLIC));
			heartRate = Integer.parseInt(params.get(HEART_RATE));
			return ReadingColour.computeColour(systolic, diastolic, heartRate).ordinal();
		}catch (Exception e) {
			throw new BadRequestException("Missing or invalid argument", e);
		}

	}
}
