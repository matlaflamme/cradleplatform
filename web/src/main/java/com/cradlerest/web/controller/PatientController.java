package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Medication;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.MedicationManager;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import com.cradlerest.web.service.config.MedicationManagerConfig;
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

	public PatientController(
			PatientManagerService patientManagerService,
			ReadingManager readingManager,
			MedicationManager medicationManager
	) {
		this.patientManagerService = patientManagerService;
		this.readingManager = readingManager;
		this.medicationManager = medicationManager;
	}

	@GetMapping("/all")
	public List<Patient> all() {
		return patientManagerService.getAllPatients();
	}

	@GetMapping("/all_with_latest_reading")
	public List<?> allSummary() {
		return patientManagerService.getAllPatientsWithLastReading();
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
	public List<ReadingView> readings(@PathVariable("id") String id) throws EntityNotFoundException {
		return readingManager.getAllReadingViewsForPatient(id);
	}


	@PostMapping("")
	public Patient createPatient(@RequestBody Patient patient) throws Exception {
		return patientManagerService.savePatient(patient);
	}

	// feature will stop working if a patient is ever given more than 2 billion medications ever this is unlikely since they would need to be given over 5000 medications per day for a 100 years
	@PostMapping("/{id}/addMedication")
	public Medication addMedication(@PathVariable("id") String id, @RequestBody Medication medication) throws Exception {
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
	public void addMedications(@PathVariable("id") String id, @RequestBody List<Medication> medications) throws Exception {
		List <Medication> patientMedications =  medicationManager.getAllMedicationsForPatient(id);

		for (Medication newMedication: medications) {
			if(patientMedications.size() == 0){
				newMedication.setMedId(0);
			}else {
				newMedication.setMedId(patientMedications.get(patientMedications.size() - 1).getMedId() + 1);
			}
			newMedication.setPatientId(id);
			 medicationManager.saveMedication(newMedication);
		}

	}

	@GetMapping("/{id}/getMedications")
	public List<Medication> getMedication(@PathVariable("id") String id) throws Exception {
		if(patientManagerService.getFullPatientProfile(id) == null){
			throw new EntityNotFoundException("Patient "+id+" does not exist");
		}
		return medicationManager.getAllMedicationsForPatient(id);
	}

	@DeleteMapping("/{id}/removeMedication/{medId}")
	public Medication removeMedication(@PathVariable("id") String id, @PathVariable String medId) throws Exception {
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
}
