package com.cradlerest.web.controller;

import com.cradlerest.web.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Controller
public class ViewController {

	@RequestMapping(value = "/healthcentres", method = RequestMethod.GET)
	public String HealthCentres(Authentication authentication) {
		return "healthcentres";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String HomePage(Authentication authentication) {
		if (authentication == null) {
			return "login";
		}

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

		if (roles == null || roles.isEmpty()) {
			return "login";
		}

		if (roles.contains(UserRole.ADMIN.getRoleString())) {
			return "admin";
		}
		else if (roles.contains(UserRole.HEALTH_WORKER.getRoleString())) {
			return "healthworker";
		}
		else if (roles.contains(UserRole.VHT.getRoleString())) {
			return "vht";
		}

		return "login";
	}
}
