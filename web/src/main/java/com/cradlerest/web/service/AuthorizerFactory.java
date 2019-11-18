package com.cradlerest.web.service;

import com.cradlerest.web.service.mock.NullAuthorizer;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

public class AuthorizerFactory {

	/**
	 * Constructs an {@code Authorizer} instance for a given user authentication.
	 * @param authentication The user authentication to wrap in an {@code Authorizer}.
	 * @return A new {@code Authorizer}
	 */
	public Authorizer construct(@NotNull Authentication authentication) {
		// TODO: implement me
		return new NullAuthorizer();
	}
}
