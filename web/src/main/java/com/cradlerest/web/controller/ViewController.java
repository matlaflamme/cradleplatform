package com.cradlerest.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Controller
public class ViewController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String HomePage(Authentication authentication) {
		if (authentication == null) {
			return "login";
		}

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

		if (roles == null || roles.isEmpty()) {
			return "login";
		}

		if (roles.contains("ROLE_ADMIN")) {
			return "admin";
		}
		else if (roles.contains("ROLE_HEALTHWORKER")) {
			return "healthworker";
		}
		else if (roles.contains("ROLE_VHT")) {
			return "vht";
		}

		return "login";
	}
}
