package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenAmount;
import com.cradlerest.web.util.datagen.annotations.DataGenRange;
import com.cradlerest.web.util.datagen.annotations.Generator;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.EmailGenerator;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;
import com.cradlerest.web.util.datagen.impl.PhoneNumberGenerator;

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

	@Column(name = "name", nullable = false)
	@Generator(GibberishSentenceGenerator.class)
	@DataGenRange(min = 1, max = 2)
	private String name;

	@Column(name = "zone", nullable = false)
	@DataGenRange(min = 1, max = 16)
	private Integer zone;

	@Email(message = "invalid email")
	@Column(name = "email", nullable = false)
	@Generator(EmailGenerator.class)
	private String email;

	// Phone number for the health centre
	@Column(name = "health_centre_number", nullable = false)
	@Generator(PhoneNumberGenerator.class)
	private String healthCentreNumber;

	// Person in charge at this health centre
	@Column(name = "manager_phone_number", nullable = false)
	@Generator(PhoneNumberGenerator.class)
	private String managerPhoneNumber;

	HealthCentre() {}

	HealthCentre(String name, Integer zone, String email, String healthCentreNumber, String managerPhoneNumber) {
		this.name = name;
		this.zone = zone;
		this.email = email;
		this.healthCentreNumber = healthCentreNumber;
		this.managerPhoneNumber = managerPhoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getZone() {
		return zone;
	}

	public void setZone(Integer zone) {
		this.zone = zone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHealthCentreNumber() {
		return healthCentreNumber;
	}

	public void setHealthCentreNumber(String triagePhoneNumber) {
		this.healthCentreNumber = triagePhoneNumber;
	}

	public String getManagerPhoneNumber() {
		return managerPhoneNumber;
	}

	public void setManagerPhoneNumber(String managerPhoneNumber) {
		this.managerPhoneNumber = managerPhoneNumber;
	}

}
