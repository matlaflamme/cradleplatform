package com.cradlerest.web.model;


import com.cradlerest.web.model.view.ReadingView;

import javax.persistence.*;
import java.util.Date;

/**
 * Defines a referral message that is received from the client
 *
 * A referral is a special reading
 */

public class ReferralMessage {

	private String referrerUserName;

	private String healthCentrePhoneNumber;

	private String timestamp; // USE FORMAT: YYYY-MM-DD HH:MM:SS

	private Patient patient;

	private ReadingView readingView;

	public ReferralMessage() {}

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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public ReadingView getReadingView() {
		return readingView;
	}

	public void setReadingView(ReadingView readingView) {
		this.readingView = readingView;
	}
}
