package com.cradlerest.web.controller;

import com.cradlerest.web.model.PatientWithLatestReadingView;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for querying information about a VHT.
 */
@RestController
@RequestMapping("/api/vht")
public class VHTController {

	private PatientManagerService patientManagerService;
	private ReadingManager readingManager;

	public VHTController(PatientManagerService patientManagerService, ReadingManager readingManager) {
		this.patientManagerService = patientManagerService;
		this.readingManager = readingManager;
	}

	@GetMapping("/{id}/patients")
	public List<PatientWithLatestReadingView> patients(@PathVariable("id") int id) {
		return patientManagerService.getPatientsWithReadingsCreatedBy(id);
	}

	@GetMapping("/{id}/readings")
	public List<ReadingView> readings(@PathVariable("id") int id) {
		return readingManager.getAllCreatedBy(id);
	}
}
