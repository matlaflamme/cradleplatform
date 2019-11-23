package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.Authorizer;
import com.cradlerest.web.service.AuthorizerFactory;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for saving and retrieving {@code ReadingView} objects.
 */
@RestController
@RequestMapping("/api/reading")
public class ReadingController {
    private ReadingManager readingManager;
    private AuthorizerFactory authorizerFactory;

	public ReadingController(ReadingManager readingManager, AuthorizerFactory authorizerFactory) {
		this.readingManager = readingManager;
		this.authorizerFactory = authorizerFactory;
	}

	/**
	 * Uploads and persists a new reading view in the database.
	 * @param readingView The reading view to save.
	 */
	@PostMapping("save")
	public Reading save(Authentication auth, @RequestBody ReadingView readingView) throws Exception {
		if (readingView.getPatientId() == null) {
			throw BadRequestException.missingField("patientId");
		}

		authorizerFactory.construct(auth)
				.check(Authorizer::canAccessPatient, readingView.getPatientId());
		try {
			return readingManager.saveReadingView(auth, readingView);
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
	public ReadingView get(Authentication auth, @PathVariable("id") Integer readingId) throws Exception {
		authorizerFactory.construct(auth)
				.check(Authorizer::canAccessReading, readingId);
		return readingManager.getReadingView(readingId);
	}

	// Proper way of calling this https://localhost:9000/api/reading/trafficlight?systolic=SYSTOLIC_INPUT_VALUE&diastolic=DIASTOLIC_INPUT_VALUE&heartRate=HEART_RATE_INPUT_VALUE
	@GetMapping ("trafficlight")
	public Integer getLight(@RequestParam int systolic, @RequestParam int diastolic, @RequestParam int heartRate) throws BadRequestException {

		try{
			return ReadingColour.computeColour(systolic, diastolic, heartRate).ordinal();
		}catch (Exception e) {
			throw new BadRequestException("Missing or invalid argument", e);
		}

	}
}
