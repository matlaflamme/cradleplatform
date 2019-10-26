package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;
import com.cradlerest.web.util.datagen.impl.StringGenerator;
import org.jetbrains.annotations.NotNull;
import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


/**
 * Database entity model for a patient.
 *
 * Holds data for a single patient.
 */
@Entity
@Table(name = "patient")
@DataGenAmount(2)
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

    @Column(name = "zone", nullable = false)
    @DataGenStringParams(length = 3, charset = StringGenerator.DECIMAL_CHARSET)
    private String zoneNumber;

	@Column(name = "birth_year", nullable = false)
	@DataGenRange(min = 1950, max = 2010)
	private Integer birthYear;

	@Column(name = "sex", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Sex sex;

	@Column(name = "medical_history")
	@Generator(GibberishSentenceGenerator.class)
	@DataGenNullChance(0.5)
	private String medicalHistory;

	@Column(name = "drug_history")
	@Generator(GibberishSentenceGenerator.class)
	@DataGenNullChance(0.5)
	private String drugHistory;

	@Column(name = "last_updated", nullable = false)
	@DataGenDateRange(min = "2018-01-01", max = "2019-12-31")
	private Date lastUpdated; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	public Patient() {
	}

	public Patient(
			String id,
			String villageNumber,
			String zoneNumber,
			String name,
			Integer birthYear,
			Sex sex,
			String medicalHistory,
			String drugHistory,
			@NotNull Date lastUpdated
	) {
		this.id = id;
		this.name = name;
		this.villageNumber = villageNumber;
		this.zoneNumber = zoneNumber;
		this.birthYear = birthYear;
		this.sex = sex;
		this.medicalHistory = medicalHistory;
		this.drugHistory = drugHistory;
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

	@JsonSerialize(using = DateSerializer.class)
	public Date getLastUpdated() {
		return lastUpdated;
	}

	@JsonDeserialize(using = DateDeserializer.class)
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Patient patient = (Patient) o;
		return id.equals(patient.id) &&
				name.equals(patient.name) &&
				villageNumber.equals(patient.villageNumber) &&
				birthYear.equals(patient.birthYear) &&
				sex == patient.sex &&
				Objects.equals(medicalHistory, patient.medicalHistory) &&
				Objects.equals(drugHistory, patient.drugHistory) &&
				lastUpdated.equals(patient.lastUpdated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
