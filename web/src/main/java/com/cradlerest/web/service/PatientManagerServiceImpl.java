package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.AlreadyExistsException;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.Sex;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	/**
	 * Returns an entire patient profile by combining a patient entity with all
	 * of the readings associated with it.
	 *
	 * Return type is a local class which is serializable to a JSON object
	 * which contains all of the fields of {@code Patient} along with a
	 * {@code readings} field: which contain an array of {@code Reading} objects.
	 *
	 * @param id The identifier for the desired user.
	 * @return An aggregate object containing the full profile for the user.
	 * @throws EntityNotFoundException If no such user with the given {@param id}
	 * 	exists in the database.
	 */
	@Override
	public Object getFullPatientProfile(@NotNull String id) throws EntityNotFoundException {
		class AggregatePatientProfile {
			// properties declared public to avoid having to write getters/setters for local class

			@JsonUnwrapped
			@SuppressWarnings("WeakerAccess")
			public Patient patient;

			@SuppressWarnings("WeakerAccess")
			public List<Reading> readings;

			private AggregatePatientProfile(Patient patient, List<Reading> readings) {
				this.patient = patient;
				this.readings = readings;
			}
		}

		return new AggregatePatientProfile(getPatientWithId(id), getReadingsForPatientWithId(id));
	}

	/**
	 * Returns the {@code Patient} object with a given {@param id}.
	 * @param id Unique identifier for the requested patient.
	 * @return The patient with {@param id}.
	 * @throws EntityNotFoundException If no such patient exists or an error occurred.
	 */
	@Override
	public Patient getPatientWithId(@NotNull String id) throws EntityNotFoundException {
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		if (optionalPatient.isEmpty()) {
			// cast id to Object to use the Object constructor
			throw new EntityNotFoundException((Object) id);
		}
		return optionalPatient.get();
	}

	/**
	 * Returns the list of all patients in the database.
	 * @return All patients.
	 */
	@Override
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	/**
	 * Returns the list of readings associated with the patient with a given
	 * {@param id}.
	 * @param id Unique identifier for a patient.
	 * @return A list of readings, or, in the case of no such patient with the
	 * 	requested id, an empty list.
	 */
	@Override
	public List<Reading> getReadingsForPatientWithId(@NotNull String id) {
		return readingRepository.findAllByPatientId(id);
	}

	/**
	 * Creates a new, or updates an existing, patient in the system. If a patient
	 * with the same id as {@param patient} exists, then that patient's profile
	 * will be overwritten with the contents of {@param patient}. If no such
	 * patient already exists, then a new one is created.
	 *
	 * @implNote The returned patient is not guarantied to be the same object
	 * 	as {@param patient}.
	 *
	 * @param patient The patient to persist.
	 * @return The saved patient.
	 * @throws BadRequestException If an error occurred.
	 */
	@Override
	public Patient savePatient(@Nullable Patient patient) throws BadRequestException, AlreadyExistsException {
		if (patient == null) {
			throw new BadRequestException("request body is null");
		}

		Optional<Patient> checkPatient = patientRepository.findById(patient.getId());

		if (checkPatient.isPresent()) {
			Patient existingPatient = checkPatient.get();
			// If current patient is more recently updated than request patient, don't update current patient
			if (existingPatient.getLastUpdated().compareTo(patient.getLastUpdated()) > 0) {
				return existingPatient;
			}
		}
		validatePatient(patient);
		return patientRepository.save(patient);
	}

	/**
	 * Creates a new, or updates an existing, reading in the system. If a reading
	 * with the same id as {@param reading} exists, then that reading is
	 * overwritten with the contents of {@param reading}. If no such reading
	 * already exists, then a new one is created.
	 *
	 * @implNote The returned reading is not guarantied to be the same object
	 * 	as {@param reading}.
	 *
	 * @param reading The reading to persist.
	 * @return The saved reading.
	 * @throws BadRequestException If an error occurred.
	 */
	@Override
	public Reading saveReading(@Nullable Reading reading) throws BadRequestException {
		if (reading == null) {
			throw new BadRequestException("request body is null");
		}
		validateReading(reading);
		return readingRepository.save(reading);
	}

	private void assertNotNull(@Nullable Object field, @NotNull String fieldName) throws BadRequestException {
		if (field == null) {
			throw BadRequestException.missingField(fieldName);
		}
	}

	private void validatePatient(@NotNull Patient patient) throws BadRequestException {
		assertNotNull(patient.getId(), "id");
		assertNotNull(patient.getName(), "name");
		assertNotNull(patient.getVillageNumber(), "villageNumber");
		assertNotNull(patient.getBirthYear(), "birthYear");
		assertNotNull(patient.getSex(), "sex");
		assertNotNull(patient.getLastUpdated(), "lastUpdated");
		if (patient.getSex() != Sex.MALE) {
			assertNotNull(patient.isPregnant(), "pregnant");
		} else if (patient.isPregnant() == null) {
			// set patient's isPregnant field to false if they are a MALE
			// and don't have the field already set
			patient.setPregnant(false);
		}
		if (patient.isPregnant()) {
			// gestational age is only required for patients that are pregnant
			assertNotNull(patient.getGestationalAge(), "gestationalAge");
		}
	}

	private void validateReading(@NotNull Reading reading) throws BadRequestException {
		assertNotNull(reading.getPatientId(), "patientId");
		assertNotNull(reading.getSystolic(), "systolic");
		assertNotNull(reading.getDiastolic(), "diastolic");
		assertNotNull(reading.getHeartRate(), "heartRate");
		assertNotNull(reading.getColour(), "colour");
		assertNotNull(reading.getTimestamp(), "timestamp");
	}
}
