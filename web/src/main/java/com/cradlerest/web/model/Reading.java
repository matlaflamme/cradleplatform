package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * Database entity model for a reading.
 */
@Entity
@Table(name = "reading")
public class Reading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	@JsonIgnore
	private Patient patient;

	@Column(name = "systolic")
	private int systolic;

	@Column(name = "diastolic")
	private int diastolic;

	@Column(name = "heart_rate")
	private int heartRate;

	@Column(name = "colour")
	@Enumerated(EnumType.ORDINAL)
	private ReadingColour colour;

	@Column(name = "timestamp")
	private Date timestamp;

	public Reading() {}

	public Reading(
			Patient patient,
			int systolic,
			int diastolic,
			int heartRate,
			ReadingColour colour,
			Date timestamp
	) {
		this.patient = patient;
		this.systolic = systolic;
		this.diastolic = diastolic;
		this.heartRate = heartRate;
		this.colour = colour;
		this.timestamp = timestamp;
	}

	public Reading(
			int id,
			Patient patient,
			int systolic,
			int diastolic,
			int heartRate,
			ReadingColour colour,
			Date timestamp
	) {
		this.id = id;
		this.patient = patient;
		this.systolic = systolic;
		this.diastolic = diastolic;
		this.heartRate = heartRate;
		this.colour = colour;
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getSystolic() {
		return systolic;
	}

	public void setSystolic(int systolic) {
		this.systolic = systolic;
	}

	public int getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(int diastolic) {
		this.diastolic = diastolic;
	}

	public int getHeartRate() {
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
}