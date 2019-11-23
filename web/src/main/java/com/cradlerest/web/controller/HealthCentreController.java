package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.HealthCentre;
import com.cradlerest.web.service.repository.HealthCentreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller for querying information about a health center.
 */
@RestController
@RequestMapping("/api/hc")
public class HealthCentreController {

	private HealthCentreRepository healthCentreRepository;

	public HealthCentreController(HealthCentreRepository healthCentreRepository) {
		this.healthCentreRepository = healthCentreRepository;
	}

	@GetMapping("/all")
	public List<HealthCentre> findAllHealthCentres() {
		return healthCentreRepository.findAll();
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public HealthCentre createHealthCentre(@Valid @RequestBody HealthCentre healthCentre) {
		return healthCentreRepository.save(healthCentre);
	}

	@PostMapping("/{id}")
	public HealthCentre updateHealthCentre(@PathVariable Integer id, @Valid @RequestBody HealthCentre healthCentre) throws EntityNotFoundException {
		Optional<HealthCentre> healthCentreToUpdate = healthCentreRepository.findById(id);
		if (healthCentreToUpdate.isEmpty()) {
			throw new EntityNotFoundException("No health centre with id: " + id);
		}
		healthCentre.setId(id);
		return healthCentreRepository.save(healthCentre);
	}
}
