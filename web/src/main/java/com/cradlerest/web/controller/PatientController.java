package com.cradlerest.web.controller;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.service.PatientManagerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
	public Patient createPatient(@RequestBody Map<String, String> body) throws Exception {
		return patientManagerService.constructPatient(body);
	}

	@PostMapping("/encrypted_reading")
	public Reading createReadingFromEncrypted(@RequestParam("userDataFile") MultipartFile file) throws Exception {
		return patientManagerService.constructReadingFromEncrypted(file);
	}

	@PostMapping("/reading")
	public Reading createReading(@RequestBody Map<String, String> body) throws Exception {
		return patientManagerService.constructReading(body);
	}
}
