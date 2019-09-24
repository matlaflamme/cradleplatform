package com.cradlerest.web.model;

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
	private String userId;

	@Column(name = "password")
	private String password;

	public User() {}

	public User(Integer id, String userId, String password) {
		this.id = id;
		this.userId = userId;
		this.password = password;
	}

	public User(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}
}
