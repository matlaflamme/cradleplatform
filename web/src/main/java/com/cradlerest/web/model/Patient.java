package com.cradlerest.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Database entity model for a patient.
 *
 * Holds data for a single patient.
 */
@Entity
@Table(name = "patient")
public class Patient {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "village")
	private Integer villageNumber;

	@Column(name = "initials")
	private String initials;

	@Column(name = "dob")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;

	@Column(name = "sex")
	@Enumerated(EnumType.ORDINAL)
	private Sex sex;

	@Column(name = "is_pregnant")
	private Boolean isPregnant;

	@Column(name = "gestational_age")
	private Integer gestationalAge;

	@Column(name = "medical_history")
	private String medicalHistory;

	@Column(name = "drug_history")
	private String drugHistory;

	@Column(name = "other_symptoms")
	private String otherSymptoms;

	public Patient() {}

	public Patient(
			String id,
			int villageNumber,
			String initials,
			Date dateOfBirth,
			Sex sex,
			boolean isPregnant,
			Integer gestationalAge,
			String medicalHistory,
			String drugHistory,
			String otherSymptoms
	) {
		this.id = id;
		this.villageNumber = villageNumber;
		this.initials = initials;
		this.dateOfBirth = dateOfBirth;
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

	public Integer getVillageNumber() {
		return villageNumber;
	}

	public void setVillageNumber(int villageNumber) {
		this.villageNumber = villageNumber;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
