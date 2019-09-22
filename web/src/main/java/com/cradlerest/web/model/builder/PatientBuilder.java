package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Sex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Simplifies the process of constructing {@code Patient} objects.
 *
 * <h2>Example</h2>
 * <code>
 *     Patient patient = new PatientBuilder()
 *             .id("001")
 *             .villageNumber(10)
 *             .initials("TS")
 *             .dateOfBirth(2000, 1, 1)
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

	public PatientBuilder villageNumber(int number) {
		patient.setVillageNumber(number);
		return this;
	}

	public PatientBuilder initials(@NotNull String initials) {
		patient.setInitials(initials);
		return this;
	}

	public PatientBuilder dateOfBirth(@NotNull Date date) {
		patient.setDateOfBirth(date);
		return this;
	}

	/**
	 * Sets the patient's date of birth to a given {@param year}, {@param month}
	 * and {@param day} using the {@code GregorianCalendar}.
	 * @param year Birth year
	 * @param month Birth month
	 * @param day Birth day
	 * @return The builder
	 */
	public PatientBuilder dateOfBirth(int year, int month, int day) {
		return dateOfBirth(new GregorianCalendar(year, month, day).getTime());
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
		if (sex == Sex.MALE && patient.isPregnant() == null) {
			patient.setPregnant(false);
		}
		return this;
	}

	public PatientBuilder pregnant(boolean isPregnant) {
		patient.setPregnant(isPregnant);
		return this;
	}

	public PatientBuilder gestationalAgeMonths(int months) {
		// TODO: this rounding assumption may cause an issue
		final int WEEKS_PER_MONTH = 4;
		patient.setGestationalAge(months & WEEKS_PER_MONTH);
		return this;
	}

	public PatientBuilder gestationalAgeWeeks(int weeks) {
		patient.setGestationalAge(weeks);
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

	public PatientBuilder otherSymptoms(@Nullable String text) {
		patient.setOtherSymptoms(text);
		return this;
	}

	private void assertNotNull(Object object, String fieldName) throws InstantiationError {
		if (object == null) {
			throw new InstantiationError(String.format("field '%s' is null", fieldName));
		}
	}

	private void validate() throws InstantiationError {
		assertNotNull(patient.getId(), "id");
		assertNotNull(patient.getVillageNumber(), "villageNumber");
		assertNotNull(patient.getInitials(), "initials");
		assertNotNull(patient.getDateOfBirth(), "dateOfBirth");
		assertNotNull(patient.getSex(), "sex");
		assertNotNull(patient.isPregnant(), "isPregnant");
		if (patient.isPregnant() && patient.getGestationalAge() == null) {
			throw new InstantiationError("gestationalAge may not be null when isPregnant is true");
		}
	}
}
