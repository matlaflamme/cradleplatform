package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * Defines a referral entity in database
 *
 * A referral is a special reading
 */
@Entity
@Table(name = "referral")
public class Referral {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "patient")
	private Patient patient;

	@Column(name = "vht")
	private User vht;

	@Column(name = "reading")
	private Reading reading;

	@Column(name = "timestamp")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	// temporary
	// TODO: Health Centre entity
	@Column(name = "health_centre")
	private String healthCentre;

	// temporary
	@Column(name = "health_centre_number")
	private String healthCentreNumber;

	@Column(name = "comments")
	private String comments;

	Referral() {}

	Referral(Integer id, Patient patient, User vht, Reading reading, Date timestamp, String healthCentre, String healthCentreNumber) {
		this.id = id;
		this.patient = patient;
		this.vht = vht;
		this.reading = reading;
		this.timestamp = timestamp;
		this.healthCentre = healthCentre;
		this.healthCentreNumber = healthCentreNumber;
	}

	public Integer getId() {
		return id;
	}

	public Patient getPatient() {
		return patient;
	}

	public User vht() {
		return vht;
	}

	public Reading getReading() {
		return reading;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getTimestamp() {
		return timestamp;
	}

	public String getHealthCentre() {
		return healthCentre;
	}

	public void setHealthCentre(String healthCentre) {
		this.healthCentre = healthCentre;
	}

	public String getHealthCentreNumber() {
		return healthCentreNumber;
	}

	public void setHealthCentreNumber(String healthCentreNumber) {
		this.healthCentreNumber = healthCentreNumber;
	}

	public String getComments() {
		return comments;
	}

}
