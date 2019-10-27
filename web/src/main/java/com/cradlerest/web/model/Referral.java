package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;
import com.cradlerest.web.util.datagen.impl.NameGenerator;
import com.cradlerest.web.util.datagen.impl.PhoneNumberGenerator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * Defines a referral entity in database
 *
 * A referral is a special reading
 */
@Entity
@Table(name = "referral")
@DataGenRelativeAmount(base = Patient.class, multiplier = 0.6)
public class Referral {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Generator(AutoIncrementGenerator.class)
	private Integer id;

	@Column(name = "pid", nullable = false)
	@ForeignKey(Patient.class)
	private String patientId;

	// TODO: Update to foreign key once VHT models are created
	@Column(name = "vid", nullable = false)
	@DataGenRange(min = 3, max = 4)
	private Integer vhtId;

	@Column(name = "reading_id", nullable = false)
	@ForeignKey(Reading.class)
	private Integer readingId;

	// temporary
	// TODO: Health Centre entity
	@Column(name = "health_centre", nullable = false)
	// TODO: Update to foreign key
	@Generator(NameGenerator.class)
	private String healthCentre;

	// temporary
	@Column(name = "health_centre_number", nullable = false)
	@Generator(PhoneNumberGenerator.class)
	private String healthCentreNumber;

	@Column(name = "comments", nullable = false)
	@Generator(GibberishSentenceGenerator.class)
	private String comments;

	@Column(name = "timestamp", nullable = false)
	@DataGenDateRange(min = "2016-01-01", max = "2019-12-31")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	@Column(name = "closed")
	@DataGenDateRange(min = "2017-01-01", max = "2019-12-31")
	private Date closed;

	// TODO: User instaed of String
	@Column(name = "accepter")
	@Generator(NameGenerator.class)
	@DataGenNullChance(0.5)
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

	@JsonSerialize(using = DateSerializer.class)
	public Date getClosed() {
		return this.closed;
	}
	
    @JsonDeserialize(using = DateDeserializer.class)
	public void setClosed(Date timestamp) {
		this.closed = timestamp;
	}

	public String getAccepter() {
		return this.accepter;
	}

	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}
}
