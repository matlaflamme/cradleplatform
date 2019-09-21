package com.cradlerest.web.service;

import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Class {@code PatientManagerService} implements the logic for managing
 * patients.
 */
public class PatientManagerServiceImpl implements PatientManagerService {

	private PatientRepository patientRepository;
	private ReadingRepository readingRepository;

	public PatientManagerServiceImpl(PatientRepository patientRepository, ReadingRepository readingRepository) {
		this.patientRepository = patientRepository;
		this.readingRepository = readingRepository;
	}

	@Override
	public Patient getPatientWithId(@NotNull String id) throws EntityNotFoundException {
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		if (optionalPatient.isEmpty()) {
			// cast id to Object to use the Object constructor
			throw new EntityNotFoundException((Object) id);
		}
		return optionalPatient.get();
	}

	@Override
	public List<Reading> getReadingsForPatientWithId(@NotNull String id) {
		return readingRepository.findAllByPatientId(id);
	}
}
