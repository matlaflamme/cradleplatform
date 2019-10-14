package com.cradlerest.web.controller;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.service.PatientManagerService;
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

	public PatientController(PatientManagerService patientManagerService) {
		this.patientManagerService = patientManagerService;
	}

	@GetMapping("/all")
	public List<Patient> all() {
		return patientManagerService.getAllPatients();
	}

	@GetMapping("/{id}")
	public Object profile(@PathVariable("id") String id) throws Exception {
		return patientManagerService.getFullPatientProfile(id);
	}

	@GetMapping("/{id}/info")
	public Patient info(@PathVariable("id") String id) throws Exception {
		return patientManagerService.getPatientWithId(id);
	}

	@GetMapping("/{id}/readings")
	public List<Reading> readings(@PathVariable("id") String id) {
		return patientManagerService.getReadingsForPatientWithId(id);
	}

	@PostMapping("")
	public Patient createPatient(@RequestBody Patient patient) throws Exception {
		return patientManagerService.savePatient(patient);
	}

	@PostMapping("/reading")
	public Reading createReading(@RequestBody Reading reading) throws Exception {
		return patientManagerService.saveReading(reading);
	}
}
