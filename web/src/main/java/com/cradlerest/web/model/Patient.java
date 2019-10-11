package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.DataGenNullChance;
import com.cradlerest.web.util.datagen.annotations.DataGenRange;
import com.cradlerest.web.util.datagen.annotations.DataGenStringParams;
import com.cradlerest.web.util.datagen.impl.StringGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

/**
 * Database entity model for a patient.
 *
 * Holds data for a single patient.
 */
@Entity
@Table(name = "patient")
@DataGenAmount(50)
public class Patient {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	@DataGenStringParams(length = 14, charset = StringGenerator.DECIMAL_CHARSET)
	private String id;

	@Column(name = "name", nullable = false)
	@DataGenStringParams(length = 2, charset = StringGenerator.UPPER_ALPHA_CHARSET)
	private String name;

	@Column(name = "village", nullable = false)
	@DataGenStringParams(length = 3, charset = StringGenerator.DECIMAL_CHARSET)
	private String villageNumber;

	@Column(name = "birth_year", nullable = false)
	@DataGenRange(min = 1950, max = 2010)
	private Integer birthYear;

	@Column(name = "sex", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Sex sex;

	@Column(name = "is_pregnant", nullable = false)
	private Boolean isPregnant;

	@Column(name = "gestational_age")
	@DataGenRange(min = 0, max = 270)
	private Integer gestationalAge;

	@Column(name = "medical_history")
	@DataGenStringParams(length = 32, charset = StringGenerator.GIBBERISH_CHARSET)
	@DataGenNullChance(0.5)
	private String medicalHistory;

	@Column(name = "drug_history")
	@DataGenStringParams(length = 32, charset = StringGenerator.GIBBERISH_CHARSET)
	@DataGenNullChance(0.5)
	private String drugHistory;

	@Column(name = "other_symptoms")
	@DataGenStringParams(length = 32, charset = StringGenerator.GIBBERISH_CHARSET)
	@DataGenNullChance(0.7)
	private String otherSymptoms;

	public Patient() {}

	public Patient(
			String id,
			String villageNumber,
			String name,
			Integer birthYear,
			Sex sex,
			boolean isPregnant,
			Integer gestationalAge,
			String medicalHistory,
			String drugHistory,
			String otherSymptoms
	) {
		this.id = id;
		this.name = name;
		this.villageNumber = villageNumber;
		this.birthYear = birthYear;
		this.sex = sex;
		this.isPregnant = isPregnant;
		this.gestationalAge = gestationalAge;
		this.medicalHistory = medicalHistory;
		this.drugHistory = drugHistory;
		this.otherSymptoms = otherSymptoms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVillageNumber() {
		return villageNumber;
	}

	public void setVillageNumber(String villageNumber) {
		this.villageNumber = villageNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	public Integer getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Boolean isPregnant() {
		return isPregnant;
	}

	public void setPregnant(boolean pregnant) {
		isPregnant = pregnant;
	}

	public Integer getGestationalAge() {
		return gestationalAge;
	}

	public void setGestationalAge(Integer gestationalAge) {
		this.gestationalAge = gestationalAge;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public String getDrugHistory() {
		return drugHistory;
	}

	public void setDrugHistory(String drugHistory) {
		this.drugHistory = drugHistory;
	}

	public String getOtherSymptoms() {
		return otherSymptoms;
	}

	public void setOtherSymptoms(String otherSymptoms) {
		this.otherSymptoms = otherSymptoms;
	}
}
