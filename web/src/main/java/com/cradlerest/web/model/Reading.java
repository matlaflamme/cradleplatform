package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;
import com.cradlerest.web.util.datagen.impl.ReadingColourComputationPass;
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
@DataGenRelativeAmount(base = Patient.class, multiplier = 6.0)
@DataGenPass(ReadingColourComputationPass.class)
public class Reading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	@Generator(AutoIncrementGenerator.class)
	private Integer id;

	@Column(name = "pid", nullable = false)
	@ForeignKey(Patient.class)
	private String patientId;

	@Column(name = "systolic", nullable = false)
	@DataGenRange(min = 100, max = 165)
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
	private ReadingColour colour; // Use *INTEGER* values {0..4}

	@Column(name = "timestamp", nullable = false)
	@DataGenDateRange(min = "2019-10-01", max = "2019-11-31")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	@Column(name = "other_symptoms")
	@Generator(GibberishSentenceGenerator.class)
	@DataGenNullChance(0.7)
	private String otherSymptoms;

	@Column(name = "notes")
	@Generator(GibberishSentenceGenerator.class)
	@DataGenNullChance(0.8)
	private String readingNotes;

	@Column(name = "created_by", nullable = false)
	@DataGenRange(min = 3, max = 4) // always generate as the id for the `vht` user
	private Integer createdBy;


	public Reading() {}

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

	public String getOtherSymptoms() {
		return otherSymptoms;
	}

	public void setOtherSymptoms(String otherSymptoms) {
		this.otherSymptoms = otherSymptoms;
	}

	public String getReadingNotes() {
		return readingNotes;
	}

	public void setReadingNotes(String readingNotes) {
		this.readingNotes = readingNotes;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reading reading = (Reading) o;
		return Objects.equals(id, reading.id) &&
				Objects.equals(patientId, reading.patientId) &&
				Objects.equals(systolic, reading.systolic) &&
				Objects.equals(diastolic, reading.diastolic) &&
				Objects.equals(heartRate, reading.heartRate) &&
				Objects.equals(isPregnant, reading.isPregnant) &&
				Objects.equals(gestationalAge, reading.gestationalAge) &&
				colour == reading.colour &&
				Objects.equals(timestamp, reading.timestamp) &&
				Objects.equals(otherSymptoms, reading.otherSymptoms) &&
				Objects.equals(readingNotes, reading.readingNotes) &&
				Objects.equals(createdBy, reading.createdBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
