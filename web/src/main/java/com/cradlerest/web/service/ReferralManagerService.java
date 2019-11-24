package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Referral;
import com.cradlerest.web.model.ReferralMessage;
import com.cradlerest.web.model.view.ReferralView;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Interface for referrals management
 */
public interface ReferralManagerService {

	/**
	 * Saves a referral, (also a reading)
	 *
	 * @param referral
	 * @return Referral object saved
	 */
	Referral saveReferral(ReferralMessage referral) throws Exception;
	Referral saveReferral(Map<String, String[]> mmsMessage) throws Exception;

	Referral resolveReferral(@Nullable Authentication auth, int referralId) throws Exception;

	List<ReferralView> findAllByHealthCentre(String healthCentreName) throws NoSuchElementException, EntityNotFoundException;
	List<ReferralView> findAllByOrderByTimestampDesc();


}
