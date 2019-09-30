package com.cradlerest.web.service;

import com.cradlerest.web.controller.error.BadRequestException;
import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Sex;
import com.cradlerest.web.model.builder.PatientBuilder;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.util.HybridFileDecrypter;
import com.cradlerest.web.util.Zipper;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
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
	public Patient savePatient(@Nullable Patient patient) throws BadRequestException {
		if (patient == null) {
			throw new BadRequestException("request body is null");
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

	@Override
	public Reading constructReadingFromEncrypted(MultipartFile file) throws IOException, GeneralSecurityException {
		JSONObject reading = decryptUpload(file);

		// TODO : format JSON so android matches Database
		String id = reading.getString("patientId");
		String villageNumber = reading.getString("villageNumber");
		String patientName = reading.getString("patientName");
		String gender = reading.getString("patientSex");
		String symptoms = reading.getJSONArray("symptoms").toString();
		String readingColour = reading.getString("readingColour");

		boolean pregnant = reading.getBoolean("pregnant");

		int ageYears = reading.getInt("ageYears");
		int diastolic = reading.getInt("bpDiastolic");
		int systolic = reading.getInt("bpSystolic");
		int heartRate = reading.getInt("heartRateBPM");
		int gestationalAge = reading.getInt("gestationalAgeInWeeks");

		String dateCreated = reading.getString("dateTimeTaken");
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateCreated);
		Timestamp timestamp = Timestamp.valueOf(zonedDateTime.toLocalDateTime());

		// Create or update patient
		Patient readingPatient = new PatientBuilder()
				.id(id)
				.villageNumber(villageNumber)
				.name(patientName)
				.dateOfBirth(zonedDateTime.getYear() - ageYears, 1, 1)
				.sex(Sex.valueOf(gender))
				.pregnant(pregnant)
				.gestationalAgeWeeks(gestationalAge)
				.otherSymptoms(symptoms)
				.build();
		patientRepository.save(readingPatient);

		// Create new reading
		Reading newReading = new ReadingBuilder()
				.pid(readingPatient.getId())
				.colour(ReadingColour.valueOf(readingColour))
				.diastolic(diastolic)
				.systolic(systolic)
				.heartRate(heartRate)
				.timestamp(timestamp)
				.build();
		readingRepository.save(newReading);

		return newReading;
	}

	private JSONObject decryptUpload(MultipartFile file) throws IOException, GeneralSecurityException {
		// Unzip uploaded file
		Map<String, byte[]> encryptedFiles = Zipper.unZip(file.getInputStream());

		// Decrypt unzipped files
		ByteArrayInputStream decryptedZip = HybridFileDecrypter.hybridDecryptFile(encryptedFiles);

		JSONObject uploadedJSON = new JSONObject(new String(decryptedZip.readAllBytes()));
		return uploadedJSON;
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
		assertNotNull(patient.getDateOfBirth(), "dateOfBirth");
		assertNotNull(patient.getSex(), "sex");
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
