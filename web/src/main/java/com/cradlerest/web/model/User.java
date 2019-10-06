package com.cradlerest.web.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Class {@code User} is a database entity holding data for users of the web
 * system (admin, health workers, VHTs).
 *
 * NotEmpty: not null + length > 0
 */
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotEmpty(message = "Username required")
	@Column(name = "username")
	private String username;

	@NotEmpty(message = "Password required") // Bcrypt will encode an empty string so this might be redundant
	@Column(name = "password")
	private String password;

	@NotEmpty(message = "Role required")
	@Column(name = "role")
	private String roles; // ADMIN,VHT,HEALTHWORKER

	public User() {}

	public User(Integer id, String username, String password, String roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public User(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.roles = user.getRoles();
	}

	public User(String username, String password, String roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String roles) { this.roles = roles; } ;

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRoles() { return roles; }

}
