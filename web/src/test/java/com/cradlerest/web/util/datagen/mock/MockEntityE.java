package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MockEntityE {
	@Column(name = "x")
	@ForeignKey(MockEntityD.class)
	private Integer x;
}