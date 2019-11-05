package com.cradlerest.web.controller;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.service.PatientManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for querying information about a VHT.
 */
@RestController
@RequestMapping("/api/vht")
public class VHTController {

	private PatientManagerService patientManagerService;

	public VHTController(PatientManagerService patientManagerService) {
		this.patientManagerService = patientManagerService;
	}

	@GetMapping("/{id}/patients")
	public List<Patient> patients(@PathVariable("id") int id) {
		return patientManagerService.getPatientsWithReadingsCreatedBy(id);
	}
}
