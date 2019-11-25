package com.cradlerest.web.model.view;

import com.cradlerest.web.model.Diagnosis;
import com.cradlerest.web.model.HealthCentre;
import com.cradlerest.web.model.Referral;
import com.cradlerest.web.service.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * The external interface for a referral.
 *
 * TODO: eventually we should update this as it is only meant as a temporary
 * 		 solution to maintain backwards comparability with the frontend
 */
public interface ReferralView {
	int getId();
	String getPatientId();
	String getReferrerUserName();
	int getReadingId();
	String getHealthCentre();
	String getHealthCentreNumber();
	Diagnosis getDiagnosis();
	@JsonSerialize(using = DateSerializer.class)
	Date getTimestamp();
	@JsonSerialize(using = DateSerializer.class)
	Date getClosed();

	static ReferralView fromReferral(Referral referral, HealthCentre hc, Diagnosis diagnosis) {
		return new ReferralView() {
			@Override
			public int getId() {
				return referral.getId();
			}

			@Override
			public String getPatientId() {
				return referral.getPatientId();
			}

			@Override
			public Diagnosis getDiagnosis() {
				return diagnosis;
			}

			@Override
			public String getReferrerUserName() {
				return referral.getReferrerUserName();
			}

			@Override
			public int getReadingId() {
				return referral.getId();
			}

			@Override
			public String getHealthCentre() {
				return hc.getName();
			}

			@Override
			public String getHealthCentreNumber() {
				return hc.getPhoneNumber();
			}

			@Override
			public Date getTimestamp() {
				return referral.getTimestamp();
			}

			@Override
			public Date getClosed() {
				return referral.getClosed();
			}
		};
	}
}
