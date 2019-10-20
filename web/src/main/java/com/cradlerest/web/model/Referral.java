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
	@Column(name = "healthCentre")
	private String healthCentreId;

	// temporary
	@Column(name = "healthCentreNumber")
	private String healthCentreNumber;

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

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getVht() {
		return vht;
	}

	public void setVht(String vht) {
		this.vht = vht;
	}

	public String getReadingId() {
		return readingId;
	}

	public void setReadingId(String readingId) {
		this.readingId = readingId;
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
