package com.cradlerest.web.service;

import com.cradlerest.web.controller.error.EntityNotFoundException;
import com.cradlerest.web.controller.error.NotImplementedException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.ReadingColour;
import com.cradlerest.web.model.Sex;
import com.cradlerest.web.model.builder.PatientBuilder;
import com.cradlerest.web.model.builder.ReadingBuilder;
import com.cradlerest.web.service.repository.PatientRepository;
import com.cradlerest.web.service.repository.ReadingRepository;
import com.cradlerest.web.service.utilities.HybridFileDecrypter;
import com.cradlerest.web.service.utilities.Zipper;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	@Override
	public List<Reading> getReadingsForPatientWithId(@NotNull String id) {
		return readingRepository.findAllByPatientId(id);
	}

	@Override
	public Patient constructPatient(Map<String, String> body) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public Reading constructReading(Map<String, String> body) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public Reading constructReadingFromEncrypted(MultipartFile file) throws Exception {
		// Unzip uploaded file
		Map<String, byte[]> encryptedFiles = Zipper.unZip(file.getInputStream());

		// Decrypt unzipped files
		ByteArrayInputStream decryptedZip = HybridFileDecrypter.hybridDecryptFile(encryptedFiles);

		// Unzip the decrypted data
		Map<String, byte[]> decryptedFiles = Zipper.unZip(decryptedZip);


		// TODO : format JSON so android matches Database
		for (Map.Entry<String, byte[]> readingFile : decryptedFiles.entrySet()) {

			JSONObject reading = new JSONObject(new String(readingFile.getValue()));

			// Parse Uploaded JSON values
			String id = reading.getString("patientId");
			String villageNumber = reading.getString("villageNumber");
			String initials = reading.getString("patientName");
			int ageYears = reading.getInt("ageYears");
			String gender = reading.getString("patientSex");
			List<Object> symptoms = reading.getJSONArray("symptoms").toList();
			String gestationalAge = reading.getString("gestationalAgeValue");

			int diastolic = reading.getInt("bpDiastolic");
			int systolic = reading.getInt("bpSystolic");
			int heartRate = reading.getInt("heartRateBPM");

			String dateCreated = reading.getString("dateTimeTaken");
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateCreated);
			Timestamp timestamp = Timestamp.valueOf(zonedDateTime.toLocalDateTime());


			Patient readingPatient;
			Optional<Patient> optionalPatient = patientRepository.findById(id);
			if (optionalPatient.isEmpty()) {
				// patient id is not found, create new patient?
				readingPatient = new PatientBuilder()
						.id(id)
						.villageNumber(villageNumber)
						.name(initials)
						.dateOfBirth(2000, 1, 1)
						.sex(Sex.UNKNOWN)
						.gestationalAgeMonths(0)
						.pregnant(!gestationalAge.equals("N/A") && !gestationalAge.equals("0"))
						.build();
				patientRepository.save(readingPatient);
			}
			else {
				readingPatient = optionalPatient.get();
			}

			// Create new reading
			Reading newReading = new ReadingBuilder()
					.pid(readingPatient.getId())
					.colour(ReadingColour.RED)
					.diastolic(diastolic)
					.systolic(systolic)
					.heartRate(heartRate)
					.timestamp(timestamp)
					.build();
			readingRepository.save(newReading);

		}

		return null;
	}
}
