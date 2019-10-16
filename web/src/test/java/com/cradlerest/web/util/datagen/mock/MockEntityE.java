package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Omit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MockEntityE {

	@Id
	@Omit
	private Integer id;

	@Column(name = "x")
	@ForeignKey(MockEntityD.class)
	private Integer x;
}