package com.cradlerest.web.util.datagen.mock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "a")
public class MockEntityA {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
}
