package com.cradlerest.web.constraints.user;

public enum Role {
	ADMIN("ROLE_ADMIN"),
	VHT("ROLE_VHT"),
	HEALTHWORKER("ROLE_HEALTHWORKER");

	private final String role;

	Role(String role) {
		this.role = role;
	}

	String getRole() {
		return role;
	}
}
