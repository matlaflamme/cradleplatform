package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MockEntityD {
	@Column(name = "x")
	@ForeignKey(MockEntityE.class)
	private Integer x;
}
