package com.cradlerest.web.controller;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.service.PatientManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for querying information about a health center.
 */
@RestController
@RequestMapping("/api/hc")
public class HealthCenterController {

	private PatientManagerService patientManagerService;

	public HealthCenterController(PatientManagerService patientManagerService) {
		this.patientManagerService = patientManagerService;
	}

	@GetMapping("/{id}/patients")
	public List<Patient> patients(@PathVariable("id") int id) {
		return patientManagerService.getPatientsReferredToHealthCenter(id);
	}
}
