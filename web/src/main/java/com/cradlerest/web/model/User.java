package com.cradlerest.web.model;

import com.cradlerest.web.constraints.user.Role;
import com.cradlerest.web.constraints.user.ValidPassword;
import com.cradlerest.web.constraints.user.ValidRole;
import com.cradlerest.web.constraints.user.ValidUsername;
import com.cradlerest.web.service.DateSerializer;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Class {@code User} is a database entity holding data for users of the web
 * system (admin, health workers, VHTs).
 *
 * NotEmpty: not null + length > 0
 */
@Entity
@Table(name = "user")
@Omit
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotEmpty(message = "username required")
	@ValidUsername
	@Column(name = "username", nullable = false)
	private String username;

	@NotEmpty(message = "Password required") // Bcrypt will encode an empty string so this might be redundant
	@ValidPassword
	@Column(name = "password", nullable = false)
	private String password;

	@NotEmpty(message = "Role required")
	@ValidRole // valid format "ROLE_XX,ROLE_XX,ROLE_XX", where XX = ADMIN,VHT,HEALTHWORKER
	@Column(name = "role", nullable = false)
	private String roles; // ADMIN,VHT,HEALTHWORKER

	@Column(name = "active", columnDefinition = "default boolean true", nullable = false)
	private boolean active;

	@CreationTimestamp
	@Column(name = "created")
	private Date created;

	@UpdateTimestamp
	@Column(name = "modified")
	private Date modified;

	@Column(name = "works_at")
	private Integer worksAtHealthCentreId;

	public User() {}

	public User(Integer id, String username, String password, String roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.active = true;
	}

	public User(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.roles = user.getRoles();
		this.active = user.getActive();
		this.created = user.getCreated();
		this.modified = user.getModified();
		this.worksAtHealthCentreId = user.getWorksAtHealthCentreId();
	}

	public User(String username, String password, String roles, Integer healthCentreId) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.worksAtHealthCentreId = healthCentreId;
		this.active = true;
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

	public boolean getActive() { return active; }

	public void swapActive() {
		// Admins cannot be disabled?
		if (!roles.equals(Role.ADMIN.getRole())) {
			this.active = !active;
		}
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getCreated() {
		return created;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getModified() {
		return modified;
	}

	@Nullable
	public Integer getWorksAtHealthCentreId() {
		return worksAtHealthCentreId;
	}

	public void setWorksAtHealthCentreId(@Nullable Integer worksAtHealthCentreId) {
		this.worksAtHealthCentreId = worksAtHealthCentreId;
	}
}
