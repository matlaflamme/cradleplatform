package com.cradlerest.web.model;

/**
 * Enumeration of user roles.
 */
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	HEALTH_WORKER("ROLE_HEALTHWORKER"),
	VHT("ROLE_VHT")
	;

	private String roleString;

	UserRole(String roleString) {
		this.roleString = roleString;
	}

	public String getRoleString() {
		return roleString;
	}
}
