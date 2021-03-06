package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.AccessDeniedException;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Medication;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.Authorizer;
import com.cradlerest.web.service.AuthorizerFactory;
import com.cradlerest.web.service.MedicationManager;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import com.github.maumay.jflow.vec.Vec;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
	private MedicationManager medicationManager;
	private ReadingManager readingManager;
	private AuthorizerFactory authorizerFactory;

	public PatientController(
			PatientManagerService patientManagerService,
			ReadingManager readingManager,
			MedicationManager medicationManager,
			AuthorizerFactory authorizerFactory
	) {
		this.patientManagerService = patientManagerService;
		this.readingManager = readingManager;
		this.medicationManager = medicationManager;
		this.authorizerFactory = authorizerFactory;
	}

	@GetMapping("/all")
	public List<Patient> all(Authentication auth) throws Exception {
		authorizerFactory.construct(auth)
				.check(Authorizer::canListPatients);
		return patientManagerService.getAllPatientsUsingAuth(auth);
	}

	@GetMapping("/all_with_latest_reading")
	public List<?> allSummary(Authentication auth) throws Exception {
		authorizerFactory.construct(auth)
				.check(Authorizer::canListPatients);
		return patientManagerService.getAllPatientsWithLastReading(auth);
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
		return patientManagerService.savePatient(auth, patient);
	}

	// feature will stop working if a patient is ever given more than 2 billion medications ever this is unlikely since they would need to be given over 5000 medications per day for a 100 years
	@PostMapping("/{id}/addMedication")
	public Medication addMedication(Authentication auth, @PathVariable("id") String id, @RequestBody Medication medication) throws Exception {
		requestAccessToPatient(auth, id);
		List <Medication> patientMedications =  medicationManager.getAllMedicationsForPatient(id);
		if(patientMedications.size() == 0){
			medication.setMedId(0);
		}else {
			medication.setMedId(patientMedications.get(patientMedications.size() - 1).getMedId() + 1);
		}
		medication.setPatientId(id);
		return medicationManager.saveMedication(medication);
	}


	// feature will stop working if a patient is ever given more than 2 billion medications ever this is unlikely since they would need to be given over 5000 medications per day for a 100 years
	@PostMapping("/{id}/addMedications")
	public void addMedications(Authentication auth, @PathVariable("id") String id, @RequestBody List<Medication> medications) throws Exception {

		requestAccessToPatient(auth, id);
		List <Medication> patientMedications =  medicationManager.getAllMedicationsForPatient(id);

		int i = 0;

		// If there are already existing medications, start our incrementer at
		// 1 + max(existing medication ids).
		if (!patientMedications.isEmpty()) {
			var max = Vec.copy(patientMedications)
					.map(Medication::getMedId)
					.max(Integer::compareTo);
			i = max + 1;
		}

		for (Medication newMedication: medications) {
			newMedication.setMedId(i);
			i++;
			newMedication.setPatientId(id);
			medicationManager.saveMedication(newMedication);
		}
	}

	@GetMapping("/{id}/getMedications")
	public List<Medication> getMedication(Authentication auth, @PathVariable("id") String id) throws EntityNotFoundException, AccessDeniedException {
		requestAccessToPatient(auth, id);
		return medicationManager.getAllMedicationsForPatient(id);
	}

	@DeleteMapping("/{id}/removeMedication/{medId}")
	public Medication removeMedication(Authentication auth, @PathVariable("id") String id, @PathVariable String medId) throws EntityNotFoundException, BadRequestException, AccessDeniedException {
		requestAccessToPatient(auth, id);
		int medIdAsInt = 0;
		try {
			medIdAsInt = Integer.parseInt(medId);
		} catch (Exception e){
			throw new BadRequestException("The Id: '"+medId+"' is not a valid integer");
		}
		try{
			return medicationManager.remove(id,medIdAsInt);
		} catch (Exception e){
			throw new EntityNotFoundException("Could not find requested Medication");
		}
	}

	@Deprecated
	@PostMapping("/reading")
	public Reading createReading(@RequestBody Reading reading) throws Exception {
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
