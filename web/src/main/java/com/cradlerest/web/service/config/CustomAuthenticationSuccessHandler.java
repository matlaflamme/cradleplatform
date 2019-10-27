package com.cradlerest.web.service.config;

import com.cradlerest.web.constraints.user.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
/**
 * Defines what happens when a user has been authenticated (logged in)
 *
 * Redirects user to path based on role
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String roles = auth.getAuthorities().toString();
		if (roles.contains(Role.ADMIN.getRole())) {
			httpServletResponse.sendRedirect("/admin");
		} else if (roles.contains(Role.HEALTHWORKER.getRole())) {
			httpServletResponse.sendRedirect("/healthworker");
		} else if (roles.contains(Role.VHT.getRole())) {
			httpServletResponse.sendRedirect("/vht");
		}
	}
}
