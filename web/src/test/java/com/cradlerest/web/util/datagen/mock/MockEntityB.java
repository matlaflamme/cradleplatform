package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "b")
public class MockEntityB {

	@Id
	@Column(name = "aid", nullable = false)
	@ForeignKey(MockEntityA.class)
	private Integer aid;

	@Id
	@Column(name = "cid", nullable = false)
	@ForeignKey(MockEntityC.class)
	private Integer cid;
}
