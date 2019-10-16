package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Database entity model for a reading.
 */
@Entity
@Table(name = "reading")
@DataGenAmount(200)
public class Reading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	@Omit
	private Integer id;

	@Column(name = "pid", nullable = false)
	@ForeignKey(Patient.class)
	private String patientId;

	@Column(name = "systolic", nullable = false)
	@DataGenRange(min = 80, max = 150)
	private Integer systolic;

	@Column(name = "diastolic", nullable = false)
	@DataGenRange(min = 60, max = 100)
	private Integer diastolic;

	@Column(name = "heart_rate", nullable = false)
	@DataGenRange(min = 40, max = 110)
	private Integer heartRate;

	@Column(name = "is_pregnant", nullable = false)
	private Boolean isPregnant;

	@Column(name = "gestational_age")
	@DataGenRange(min = 0, max = 270)
	private Integer gestationalAge;

	@Column(name = "colour", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private ReadingColour colour; // Use *INTEGER* values {0..3}

	@Column(name = "timestamp", nullable = false)
	@DataGenDateRange(min = "2016-01-01", max = "2019-12-31")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	public Reading() {}

	public Reading(
			@NotNull String patientId,
			int systolic,
			int diastolic,
			int heartRate,
			boolean isPregnant,
			Integer gestationalAge,
			@NotNull ReadingColour colour,
			@NotNull Date timestamp
	) {
		this.patientId = patientId;
		this.systolic = systolic;
		this.diastolic = diastolic;
		this.heartRate = heartRate;
		this.isPregnant = isPregnant;
		this.gestationalAge = gestationalAge;
		this.colour = colour;
		this.timestamp = timestamp;
	}

	public Reading(
			int id,
			@NotNull String patientId,
			int systolic,
			int diastolic,
			int heartRate,
			boolean isPregnant,
			Integer gestationalAge,
			@NotNull ReadingColour colour,
			@NotNull Date timestamp
	) {
		this.id = id;
		this.patientId = patientId;
		this.systolic = systolic;
		this.diastolic = diastolic;
		this.heartRate = heartRate;
		this.isPregnant = isPregnant;
		this.gestationalAge = gestationalAge;
		this.colour = colour;
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(@NotNull String patientId) {
		this.patientId = patientId;
	}

	public Integer getSystolic() {
		return systolic;
	}

	public void setSystolic(int systolic) {
		this.systolic = systolic;
	}

	public Integer getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(int diastolic) {
		this.diastolic = diastolic;
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

	public Integer getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public ReadingColour getColour() {
		return colour;
	}

	public void setColour(ReadingColour colour) {
		this.colour = colour;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getTimestamp() {
		return timestamp;
	}

	@JsonDeserialize(using = DateDeserializer.class)
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reading reading = (Reading) o;
		return id.equals(reading.id) &&
				patientId.equals(reading.patientId) &&
				systolic.equals(reading.systolic) &&
				diastolic.equals(reading.diastolic) &&
				heartRate.equals(reading.heartRate) &&
				isPregnant.equals(reading.isPregnant) &&
				Objects.equals(gestationalAge, reading.gestationalAge) &&
				colour == reading.colour &&
				timestamp.equals(reading.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
