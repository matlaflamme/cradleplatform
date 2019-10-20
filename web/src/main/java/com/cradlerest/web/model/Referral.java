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

	@ManyToOne() // a patient can have many referrals
	@JoinColumn(name = "pid")
	private String patientId; // one referra

	@ManyToOne()
	@JoinColumn(name = "vid")
	private String vht;

	@OneToOne()
	@JoinColumn(name = "readingId")
	private String readingId;

	@Column(name = "timestamp")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	// temporary
	// TODO: Health Centre entity
	@Column(name = "health_centre")
	private String healthCentreId;

	// temporary
	@Column(name = "healthCentreNumber")
	private String healthCentreNumber;

	@Column(name = "comments")
	private String comments;

	Referral() {}

	Referral(Integer id, String patientId, String vhtId, Date timestamp) {
		this.id = id;
		this.patientId = patientId;
		this.vht = vhtId;
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public String getPatientId() {
		return patientId;
	}

	public String getVht() {
		return vht;
	}

	public String getReadingId() {
		return readingId;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getTimestamp() {
		return timestamp;
	}

	public String getHealthCentreId() {
		return healthCentreId;
	}

	public void setHealthCentreId(String healthCentreId) {
		this.healthCentreId = healthCentreId;
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
