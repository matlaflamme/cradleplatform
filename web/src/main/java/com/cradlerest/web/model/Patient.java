package com.cradlerest.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Database entity model for a patient.
 *
 * Holds data for a single patient.
 */
@Entity
@Table(name = "patient")
public class Patient {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "dob")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;

	public Patient() {}

	public Patient(String id, String name, Date dateOfBirth) {
		this.id = id;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
