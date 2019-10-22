package com.cradlerest.web.model;

import javax.persistence.*;
import java.util.Set;

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
public class HealthCentre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "zone")
	private Integer zone;

	@Column(name = "email")
	private String email;

	// Phone number for the health centre
	@Column(name = "health_centre_number")
	private String healthCentreNumber;

	// Person in charge at this health centre
	@Column(name = "manager_phone_number")
	private String managerPhoneNumber;

//	@Column(name = "patients")
//	private Set<String> patients;

//	@Column(name = "location")
//	private Location location;


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
