package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Generator;
import com.cradlerest.web.util.datagen.impl.*;

import javax.persistence.*;

/**
 * Defines a diagnosis
 *
 * 	For each referral, a diagnosis can be made
 */
@Entity
@Table(name = "diagnosis")
@DataGenAmount(3)
public class Diagnosis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Generator(AutoIncrementGenerator.class)
	private Integer id;

	@Column(name = "patient", nullable = false)
	@ForeignKey(Patient.class)
	private String patientId;

	@Column(name = "referral", nullable = false)
	@ForeignKey(Referral.class)
	private String referralId;

	@Column(name = "description", nullable = false)
	@Generator(GibberishSentenceGenerator.class)
	private String description;

	@Column(name = "resolved", nullable = false)
	private Boolean resolved;


	Diagnosis() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}

	public Boolean getResolved() {
		return resolved;
	}
}
