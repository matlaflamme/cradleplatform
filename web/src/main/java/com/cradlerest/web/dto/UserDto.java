package com.cradlerest.web.dto;

import com.cradlerest.web.constraints.user.ValidRole;
import com.cradlerest.web.model.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * User Data transfer object
 *
 * DTO: ALlows API to send/receive data without touching database
 */
public class UserDto {

	@NotEmpty
	@Size(min = 6, message = "Minimum username length is 6")
	private String username;

	@NotEmpty
	@Size(min = 8, message = "Minimum password length is 8")
	private String password;

	@NotEmpty
	@ValidRole
	private String roles;

	public UserDto() {

	}

	public UserDto(String username, String password, String roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String roles) { this.roles = roles; } ;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRoles() { return roles; }

}
