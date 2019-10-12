package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;

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

	@Column(name = "name")
	private String name;

	@Column(name = "village")
	private String villageNumber;

	@Column(name = "zone")
	private String zoneNumber;

	@Column(name = "birth_year")
	private Integer birthYear;

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

	@Column(name = "last_updated")
	private Date lastUpdated; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	public Patient() {}

	public Patient(
			String id,
			String villageNumber,
			String zoneNumber,
			String name,
			Integer birthYear,
			Sex sex,
			boolean isPregnant,
			Integer gestationalAge,
			String medicalHistory,
			String drugHistory,
			String otherSymptoms,
			@NotNull Date lastUpdated
	) {
		this.id = id;
		this.name = name;
		this.villageNumber = villageNumber;
		this.zoneNumber = zoneNumber;
		this.birthYear = birthYear;
		this.sex = sex;
		this.isPregnant = isPregnant;
		this.gestationalAge = gestationalAge;
		this.medicalHistory = medicalHistory;
		this.drugHistory = drugHistory;
		this.otherSymptoms = otherSymptoms;
		this.lastUpdated = lastUpdated;
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

	public String getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(String zoneNumber) {
		this.zoneNumber = zoneNumber;
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

	@JsonSerialize(using = DateSerializer.class)
	public Date getLastUpdated() { return lastUpdated; }

	@JsonDeserialize(using = DateDeserializer.class)
	public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
