package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.Generator;
import com.cradlerest.web.util.datagen.impl.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Defines a health centre
 *
 * 	For each referral the VHT has made, they can see:
 * 	Health centre referred to,
 * 	Distance from health centre,
 * 	Mode of transport to reach health centre
 */
@Entity
@Table(name = "health_centre")
@DataGenAmount(6)
public class HealthCentre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@Generator(AutoIncrementGenerator.class)
	private Integer id;

	@Column(name = "phone_number", nullable = false, unique = true)
	@Generator(PhoneNumberGenerator.class)
	@NotNull(message = "Missing health centre number")
	private String phoneNumber;

	@Column(name = "name", nullable = false)
	@Generator(NameGenerator.class)
	@NotNull(message = "Missing name")
	private String name;

	@Column(name = "location", nullable = false)
	@Generator(NameGenerator.class)
	@NotNull(message = "Missing location")
	private String location;

	@Email(message = "invalid email")
	@Column(name = "email")
	@Generator(EmailGenerator.class)
	@NotNull(message = "Missing email")
	private String email;

	// Person in charge at this health centre
	@Column(name = "manager_phone_number")
	@Generator(PhoneNumberGenerator.class)
	@NotNull(message = "Missing manager phone number")
	private String managerPhoneNumber;

	HealthCentre() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getManagerPhoneNumber() {
		return managerPhoneNumber;
	}

	public void setManagerPhoneNumber(String managerPhoneNumber) {
		this.managerPhoneNumber = managerPhoneNumber;
	}

}
