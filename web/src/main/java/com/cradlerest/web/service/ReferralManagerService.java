package com.cradlerest.web.service;

import com.cradlerest.web.model.Referral;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Interface for referrals management
 */
public interface ReferralManagerService {

	/**
	 * Saves a referral, (also a reading)
	 *
	 * @param requestBody
	 * @return Referral object saved
	 */
	Referral saveReferral(JsonNode requestBody) throws Exception;

	List<Referral> findAll();
	List<Referral> findAllByHealthCentre(String healthCentreName);


}
