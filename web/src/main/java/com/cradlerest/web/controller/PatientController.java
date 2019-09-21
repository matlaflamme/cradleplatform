package com.cradlerest.web.controller;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.repository.PatientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller responsible for managing requests dealing with patients.
 *
 * Implements the following endpoints:
 *
 * - {@code GET:/api/patient/{id}}: Returns information about the patient with
 * 	a given {@code id}.
 *
 * @see Patient
 * @see PatientRepository
 */
@RestController
@RequestMapping("/api/patient")
public class PatientController {

	private PatientManagerService patientManagerService;

	public PatientController(PatientManagerService patientManagerService) {
		this.patientManagerService = patientManagerService;
	}

	@GetMapping("/{id}")
	public Patient patient(@PathVariable("id") String id) throws Exception {
		return patientManagerService.getPatientWithId(id);
	}

	@GetMapping("/{id}/readings")
	public List<Reading> readings(@PathVariable("id") String id) {
		return patientManagerService.getReadingsForPatientWithId(id);
	}
}
