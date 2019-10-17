package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Sex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;


/**
 * Simplifies the process of constructing {@code Patient} objects.
 *
 * <h2>Example</h2>
 * <code>
 *     Patient patient = new PatientBuilder()
 *             .id("001")
 *             .villageNumber(10)
 *             .name("Taki")
 *             .birthYear(2000)
 *             .sex(Sex.Female)
 *             .pregnant(false)
 *             .build();
 * </code>
 */
public class PatientBuilder {

	private Patient patient;

	public PatientBuilder() {
		this.patient = new Patient();
	}

	public Patient build() throws InstantiationError {
		validate();
		return patient;
	}

	public PatientBuilder id(@NotNull String id) {
		patient.setId(id);
		return this;
	}


	public PatientBuilder name(@NotNull String name) {
		patient.setName(name);
		return this;
	}

	public PatientBuilder villageNumber(@NotNull String number) {
		patient.setVillageNumber(number);
		return this;
	}

	public PatientBuilder zoneNumber(@NotNull String number) {
		patient.setZoneNumber(number);
		return this;
	}


	public PatientBuilder birthYear(int year) {
		patient.setBirthYear(year);
		return this;
	}

	/**
	 * Sets the sex of the patient. If the {@code isPregnant} field has not
	 * been set, setting sex to {@code Sex.Male} also sets {@code isPregnant}
	 * to {@code false}. This can be overridden by explicitly setting the
	 * {@code isPregnant} value.
	 * @param sex The sex
	 * @return The builder
	 */
	public PatientBuilder sex(@NotNull Sex sex) {
		patient.setSex(sex);
		return this;
	}

	public PatientBuilder lastUpdated(Date lastUpdated) {
		patient.setLastUpdated(lastUpdated);
		return this;
	}

	public PatientBuilder medicalHistory(@Nullable String text) {
		patient.setMedicalHistory(text);
		return this;
	}

	public PatientBuilder drugHistory(@Nullable String text) {
		patient.setDrugHistory(text);
		return this;
	}

	private void assertNotNull(Object object, String fieldName) throws InstantiationError {
		if (object == null) {
			throw new InstantiationError(String.format("field '%s' is null", fieldName));
		}
	}

	private void validate() throws InstantiationError {
		assertNotNull(patient.getId(), "id");
		assertNotNull(patient.getName(), "name");
		assertNotNull(patient.getVillageNumber(), "villageNumber");
		assertNotNull(patient.getBirthYear(), "birthYear");
		assertNotNull(patient.getSex(), "sex");
		assertNotNull(patient.getLastUpdated(), "lastUpdated" );
	}
}
