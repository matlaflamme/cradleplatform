package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Referral;
import com.cradlerest.web.model.view.ReferralView;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.NoSuchElementException;

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

	List<ReferralView> findAllByHealthCentre(String healthCentreName) throws NoSuchElementException, EntityNotFoundException;
	List<ReferralView> findAllByOrderByTimestampDesc();


}
