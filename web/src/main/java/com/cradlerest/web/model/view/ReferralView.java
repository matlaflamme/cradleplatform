package com.cradlerest.web.model.view;

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
	@JsonSerialize(using = DateSerializer.class)
	Date getTimestamp();
	@JsonSerialize(using = DateSerializer.class)
	Date getClosed();

	static ReferralView fromReferral(Referral referral, String hcName, String hcNumber, String pid) {
		return new ReferralView() {
			@Override
			public int getId() {
				return referral.getId();
			}

			@Override
			public String getPatientId() {
				return pid;
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
				return hcName;
			}

			@Override
			public String getHealthCentreNumber() {
				return hcNumber;
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
