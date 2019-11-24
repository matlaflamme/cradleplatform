package com.cradlerest.web.model;

import com.cradlerest.web.service.DateDeserializer;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.*;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.FixedStringGenerator;
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
	@Generator(FixedStringGenerator.class)
	@DataGenFixedString("vht")
	private String referrerUserName;

	@Column(name = "referred_to", nullable = false)
	@ForeignKey(HealthCentre.class)
	private Integer healthCentreId;

	@Column(name = "patient", nullable = false)
	@ForeignKey(Patient.class)
	private String patientId;

	@Column(name = "timestamp", nullable = false)
	@DataGenDateRange(min = "2019-10-01", max = "2019-11-31")
	private Date timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	@Column(name = "closed")
	@DataGenDateRange(min = "2019-10-01", max = "2019-11-31")
	private Date closed;

	@Column(name = "closed_by")
	@DataGenRange(min = 3, max = 4)
	private Integer reviewerUserId;

	@Column(name = "reading_id", nullable = false)
	@ForeignKey(Reading.class)
	private Integer readingId;

	@Column(name = "diagnosis")
	@ForeignKey(Diagnosis.class)
	private Integer diagnosisId;

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

	public Integer getHealthCentreId() {
		return healthCentreId;
	}

	public void setHealthCentreId(Integer healthCentreId) {
		this.healthCentreId = healthCentreId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
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

	public Integer getReadingId() {
		return readingId;
	}

	public void setReadingId(Integer readingId) {
		this.readingId = readingId;
	}
}
