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

	// TODO: Foreign keys
	// Need help with this one.
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "patient_id")
//	private Patient patient;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User vht;

//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "reading_id")
//	private Reading reading;

	private String patientId;
	private Integer vhtId;
	private Integer readingId;

	@Column(name = "timestamp")
	private String timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	// temporary
	// TODO: Health Centre entity
	@Column(name = "health_centre")
	private String healthCentre;

	// temporary
	@Column(name = "health_centre_number")
	private String healthCentreNumber;

	@Column(name = "comments")
	private String comments;

	public Referral() {}

	public Referral(String patientId, Integer vhtId, Integer readingId, String timestamp, String healthCentre, String healthCentreNumber) {
		this.patientId = patientId;
		this.vhtId = vhtId;
		this.readingId = readingId;
		this.timestamp = timestamp;
		this.healthCentre = healthCentre;
		this.healthCentreNumber = healthCentreNumber;
	}

	public Integer getId() {
		return id;
	}

	public String getPatientId() {
		return patientId;
	}

	public Integer getVhtId() {
		return vhtId;
	}

	public Integer getReadingId() {
		return readingId;
	}

	@JsonSerialize(using = DateSerializer.class)
	public String getTimestamp() {
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
