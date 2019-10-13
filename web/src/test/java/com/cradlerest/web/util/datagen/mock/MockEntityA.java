package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.annotations.Generator;
import com.cradlerest.web.util.datagen.annotations.Omit;
import com.cradlerest.web.util.datagen.impl.GibberishSentenceGenerator;

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

	@Column(name = "text")
	@Generator(GibberishSentenceGenerator.class)
	private String text;

	@Column(name = "hidden")
	@Omit
	private String hidden;
}
