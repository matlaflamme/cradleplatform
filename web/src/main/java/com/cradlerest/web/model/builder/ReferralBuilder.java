package com.cradlerest.web.model.builder;

import com.cradlerest.web.model.Referral;
import com.cradlerest.web.util.DateParser;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static com.cradlerest.web.util.Validation.assertFieldNotNull;

/**
 * Simplifies the process of constructing {@code Referral} objects.
 *
 *
 * <h2>Example</h2>
 * <code>
 *	 Reading newReading = new ReadingBuilder()
 * </code>
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

	public ReferralBuilder pid(@NotNull String pid) {
		referral.setPatientId(pid);
		return this;
	}

	public ReferralBuilder vid(@NotNull Integer vid) {
		referral.setVhtId(vid);
		return this;
	}

	public ReferralBuilder readingId(@NotNull Integer readingId) {
		referral.setReadingId(readingId);
		return this;
	}

	public ReferralBuilder healthCentre(@NotNull String healthCentreName) {
		referral.setHealthCentre(healthCentreName);
		return this;
	}

	public ReferralBuilder healthCentreNumber(@NotNull String healthCentreNumber) {
		referral.setHealthCentreNumber(healthCentreNumber);
		return this;
	}

	public ReferralBuilder comments(String comments) {
		referral.setComments(comments);
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
		assertFieldNotNull(referral.getPatientId(), "pid");
		assertFieldNotNull(referral.getVhtId(), "vid");
		assertFieldNotNull(referral.getReadingId(), "readingId");
		assertFieldNotNull(referral.getHealthCentre(), "healthCentre");
		assertFieldNotNull(referral.getHealthCentreNumber(), "healthCentreNumber");
		assertFieldNotNull(referral.getTimestamp(), "referralTimestamp");
	}
}

