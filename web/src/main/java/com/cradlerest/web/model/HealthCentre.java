package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.DataGenRange;
import com.cradlerest.web.util.datagen.annotations.Generator;
import com.cradlerest.web.util.datagen.impl.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

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
	private String phoneNumber;

	@Column(name = "name", nullable = false)
	@Generator(NameGenerator.class)
	private String name;

	@Column(name = "location", nullable = false)
	@Generator(NameGenerator.class)
	private String location;

	@Email(message = "invalid email")
	@Column(name = "email")
	@Generator(EmailGenerator.class)
	private String email;

	// Person in charge at this health centre
	@Column(name = "alternate_phone_number")
	@Generator(PhoneNumberGenerator.class)
	private String alternatePhoneNumber;

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

	public String getAlternatePhoneNumber() {
		return alternatePhoneNumber;
	}

	public void setAlternatePhoneNumber(String managerPhoneNumber) {
		this.alternatePhoneNumber = managerPhoneNumber;
	}

}
