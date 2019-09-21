package com.cradlerest.web.controller;

import com.cradlerest.web.controller.error.DatabaseException;
import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.service.repository.PatientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

	private PatientRepository patientRepository;

	public PatientController(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@GetMapping("/{id}")
	public Patient patient(@PathVariable("id") String id) throws DatabaseException {
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		if (optionalPatient.isEmpty()) {
			// cast id to Object to use the Object constructor
			throw new EntityNotFoundException((Object) id);
		}
		return optionalPatient.get();
	}
}
