package com.cradlerest.web.util;

import com.cradlerest.web.model.UserRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthenticationExt {

	/**
	 * Returns {@code true} if {@code auth} contains a given user role.
	 * @param auth The authentication to check.
	 * @param role The role to check for.
	 * @return Whether the authentication has a role.
	 */
	public static boolean hasRole(@Nullable Authentication auth, @NotNull UserRole role) {
		if (auth == null) {
			return false;
		}

		var roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		return roles.contains(role.getRoleString());
	}
}
