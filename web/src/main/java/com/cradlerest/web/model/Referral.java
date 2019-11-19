package com.cradlerest.web.model;

import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;
import com.cradlerest.web.util.datagen.impl.NameGenerator;
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

	@Column(name = "referred_by", nullable = false)
	@ForeignKey(User.class)
	private String referrerUserName;

	@Column(name = "referred_to", nullable = false)
	@ForeignKey(HealthCentre.class)
	private String healthCentrePhoneNumber;

	@Column(name = "patient", nullable = false)
	@ForeignKey(Patient.class)
	private String patientId;

	@Column(name = "timestamp", nullable = false)
	@DataGenDateRange(min = "2016-01-01", max = "2019-12-31")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	@Column(name = "closed")
	@DataGenDateRange(min = "2017-01-01", max = "2019-12-31")
	private Date closed;

	@Column(name = "closed_by")
	@DataGenRange(min = 3, max = 4)
	private Integer reviewerUserId;

	@OneToOne
	private Reading reading;

	public Referral() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) { this.id = id; }

	public String getReferrerUserName() {
		return referrerUserName;
	}

	public void setReferrerUserName(String referrerUserName) {
		this.referrerUserName = referrerUserName;
	}

	public String getHealthCentrePhoneNumber() {
		return healthCentrePhoneNumber;
	}

	public void setHealthCentrePhoneNumber(String healthCentrePhoneNumber) {
		this.healthCentrePhoneNumber = healthCentrePhoneNumber;
	}

//	public Integer getReadingId() {
//		return readingId;
//	}

//	public void setReadingId(Integer readingId) {
//		this.readingId = readingId;
//	}

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

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Integer getReviewerUserId() {
		return reviewerUserId;
	}

	public void setReviewerUserId(Integer userId) {
		this.reviewerUserId = userId;
	}

	public Reading getReading() {
		return reading;
	}

	public void setReading(Reading reading) {
		this.reading = reading;
	}
}
