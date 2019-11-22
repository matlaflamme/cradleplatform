package com.cradlerest.web.model;


import com.cradlerest.web.model.view.ReadingView;


/**
 * Defines a referral message that is received from the client either through POST or SMS
 *
 * Members are minified to reduce character length of JSON
 *
 * Members are made public to avoid the need of additionally obscure Setters and Getters
 */

public class ReferralMessage {

	/**
	 * referrerUserName
	 */
	public String u;

	/**
	 * healthCentrePhoneNumber
	*/
	public String h;

	/**
	 * patientID
	 */
	public String i;

	/**
	 * patient
	 */
	public Patient p;

	/**
	 * reading
	 */
	public ReadingView r;

	/**
	 * timestamp - ( can be null )
	 */
	public String t;

	public ReferralMessage() {}

	public String getReferrerUserName() {
		return u;
	}

	public void setReferrerUserName(String referrerUserName) {
		this.u = referrerUserName;
	}

	public String getHealthCentrePhoneNumber() {
		return h;
	}

	public void setHealthCentrePhoneNumber(String healthCentrePhoneNumber) {
		this.h = healthCentrePhoneNumber;
	}

	public Patient getPatient() {
		return p;
	}

	public void setPatient(Patient patient) {
		this.p = patient;
	}

	public ReadingView getReadingView() {
		return r;
	}

	public void setReadingView(ReadingView readingView) {
		this.r = readingView;
	}

	public String getPatientId() {
		return i;
	}

	public void setPatientId(String patientId) {
		this.i = patientId;
	}

	public void setTimestamp(String timestamp) {
		this.t = timestamp;
	}

	public String getTimestamp() {
		return t;
	}
}
