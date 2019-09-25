package com.cradlerest.web.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Class {@code User} is a database entity holding data for users of the web
 * system (health workers, VHTs, etc.).
 *
 * TODO: this class has been constructed for testing purposes,
 * 	please revisit its implementation at a later date
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_id")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role;

	public User() {}

	public User(Integer id, String username, String password, Role role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.role = user.getRole();
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
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

	public void setRole(Role role) { this.role = role; } ;

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Role getRole() { return role; }

}
