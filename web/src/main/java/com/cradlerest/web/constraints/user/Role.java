package com.cradlerest.web.constraints.user;

/**
 * Defines an enum for Role
 *
 * current active roles:
 * VHT, ADMIN, HEALTHWORKER
 *
 * Used in
 * @see RoleValidator
 */
public enum Role {
	ADMIN("ROLE_ADMIN"),
	VHT("ROLE_VHT"),
	HEALTHWORKER("ROLE_HEALTHWORKER");

	private final String role;

	Role(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
