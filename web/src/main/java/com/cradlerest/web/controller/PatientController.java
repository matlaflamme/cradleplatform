package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.AccessDeniedException;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.Authorizer;
import com.cradlerest.web.service.AuthorizerFactory;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing requests dealing with patients.
 *
 * Implements the following endpoints:
 *
 * - {@code GET:/api/patient/{id}}: Returns the full patient profile for
 * 	patient with a given {@code id}.
 *
 * - {@code GET:/api/patient/{id}/info}: Returns only the patient information
 * 	for a patient with a given {@code id}.
 *
 * - {@code GET:/api/patient/{id}/readings}: Returns the readings associated
 * 	with the patient with a given {@code id}.
 *
 * - {@code POST:/api/patient}: Creates a new patient
 *
 *
 * @see Patient
 * @see PatientManagerService
 */
@RestController
@RequestMapping("/api/patient")
public class PatientController {

	private PatientManagerService patientManagerService;
	private ReadingManager readingManager;
	private AuthorizerFactory authorizerFactory;

	public PatientController(
			PatientManagerService patientManagerService,
			ReadingManager readingManager,
			AuthorizerFactory authorizerFactory
	) {
		this.patientManagerService = patientManagerService;
		this.readingManager = readingManager;
		this.authorizerFactory = authorizerFactory;
	}

	@GetMapping("/all")
	public List<Patient> all(Authentication auth) throws Exception {
		authorizerFactory.construct(auth)
				.check(Authorizer::canListPatients);
		return patientManagerService.getAllPatients();
	}

	@GetMapping("/all_with_latest_reading")
	public List<?> allSummary(Authentication auth) throws Exception {
		authorizerFactory.construct(auth)
				.check(Authorizer::canListPatients);
		return patientManagerService.getAllPatientsWithLastReading();
	}

	@GetMapping("/{id}")
	public Object profile(Authentication auth, @PathVariable("id") String id) throws Exception {
		requestAccessToPatient(auth, id);
		return patientManagerService.getFullPatientProfile(id);
	}

	@GetMapping("/{id}/info")
	public Patient info(Authentication auth, @PathVariable("id") String id) throws Exception {
		requestAccessToPatient(auth, id);
		return patientManagerService.getPatientWithId(id);
	}

	@GetMapping("/{id}/readings")
	public List<ReadingView> readings(Authentication auth, @PathVariable("id") String id) throws Exception {
		requestAccessToPatient(auth, id);
		return readingManager.getAllReadingViewsForPatient(id);
	}

	@PostMapping("")
	public Patient createPatient(Authentication auth, @RequestBody Patient patient) throws Exception {
		authorizerFactory.construct(auth)
				.check(Authorizer::canCreatePatient);
		return patientManagerService.savePatient(patient);
	}

	@Deprecated
	@PostMapping("/reading")
	public Reading createReading(Authentication auth, @RequestBody Reading reading) throws Exception {
		if (reading.getPatientId() == null) {
			throw BadRequestException.missingField("patientId");
		}

		requestAccessToPatient(auth, reading.getPatientId());
		return patientManagerService.saveReading(reading);
	}

	/**
	 * Attempts to request access to the patient with a given {@code id}. Throws
	 * an exception if unable to gain access.
	 * @param auth A user's authentication credentials.
	 * @param id The id of the patient to request access to.
	 * @throws AccessDeniedException If unable to acquire access to the patient.
	 */
	private void requestAccessToPatient(Authentication auth, @NotNull String id) throws AccessDeniedException {
		authorizerFactory.construct(auth)
				.check(Authorizer::canAccessPatient, id);
	}
}
