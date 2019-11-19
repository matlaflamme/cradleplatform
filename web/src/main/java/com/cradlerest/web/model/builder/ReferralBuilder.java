package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Referral;
import com.cradlerest.web.util.DateParser;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;

/**
 * Simplifies the process of constructing {@code Referral} objects.
 *
 * E.g.
 * Referral currentReferral = new ReferralBuilder()
 * 		.pid(currentPatient.getId())
 * 		.referredBy(currentVHT.get().getId())
 * 		.referredTo(3)
 * 		.readingId(currentReading.getId())
 * 		.comments(comments)
 * 		.timestamp(referralTimestamp)
 * 		.build();
 */
public class ReferralBuilder {

	private Referral referral;

	public ReferralBuilder() {
		this.referral = new Referral();
	}

	public Referral build() throws InstantiationError {
		validate();
		return referral;
	}

	public ReferralBuilder id(int id) {
		referral.setId(id);
		return this;
	}

	public ReferralBuilder referredByUsername(@NotNull String username) {
		referral.setReferrerByUserName(username);
		return this;
	}

	public ReferralBuilder referredToHealthCentrePhoneNumber(@NotNull String healthCenterPhoneNumber) {
		referral.setHealthCentrePhoneNumber(healthCenterPhoneNumber);
		return this;
	}

	public ReferralBuilder readingId(@NotNull Integer readingId) {
		referral.setReadingId(readingId);
		return this;
	}


	public ReferralBuilder timestamp(@NotNull Date timestamp) {
		referral.setTimestamp(timestamp);
		return this;
	}

	public ReferralBuilder timestamp(@NotNull String timestampText) {
		referral.setTimestamp(DateParser.parseDateTime(timestampText));
		return this;
	}

	private void validate() throws InstantiationError {
		assertFieldNotNull(referral.getReferrerByUserName(), "referrerUserName");
		assertFieldNotNull(referral.getHealthCentrePhoneNumber(), "healthCentrePhoneNumber");
		assertFieldNotNull(referral.getReadingId(), "readingId");
		assertFieldNotNull(referral.getPatientId(), "patientId");
		assertFieldNotNull(referral.getTimestamp(), "timestamp");
	}
}

