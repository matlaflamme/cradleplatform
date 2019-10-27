package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.DataGenDateRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;

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

	@Column(name = "pid")
	private String patientId;

	@Column(name = "vid")
	private Integer vhtId;

	@Column(name = "reading_id")
	private Integer readingId;

	// temporary
	// TODO: Health Centre entity
	@Column(name = "health_centre")
	private String healthCentre;

	// temporary
	@Column(name = "health_centre_number")
	private String healthCentreNumber;

	@Column(name = "comments")
	private String comments;

	@Column(name = "timestamp", nullable = false)
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	@Column(name = "closed", columnDefinition = "boolean default false")
	private boolean closed;

	// TODO: User instaed of String
	@Column(name = "accepter", columnDefinition = "boolean default null")
	private String accepter;

	public Referral() {}

	public Referral(String patientId, Integer vhtId, Integer readingId, String healthCentre, String healthCentreNumber, String comments) {
		this.patientId = patientId;
		this.vhtId = vhtId;
		this.readingId = readingId;
		this.healthCentre = healthCentre;
		this.healthCentreNumber = healthCentreNumber;
		this.comments = comments;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) { this.id = id; }

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Integer getVhtId() {
		return vhtId;
	}

	public void setVhtId(Integer vhtId) {
		this.vhtId = vhtId;
	}

	public Integer getReadingId() {
		return readingId;
	}

	public void setReadingId(Integer readingId) {
		this.readingId = readingId;
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

	public void setComments(String comments) {
		this.comments = comments;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getTimestamp() {
		return timestamp;
	}

	@JsonDeserialize(using = DateDeserializer.class)
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean getClosed() {
		return this.closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getAccepter() {
		return this.accepter;
	}

	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}
}
