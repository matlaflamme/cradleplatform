package com.cradlerest.web.service.repository;

import com.cradlerest.web.util.datagen.annotations.Omit;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A dummy entity to trick JPA into giving us a repository.
 */
@Entity
@Omit
class MockEntity {

	@Id
	private Integer id;
}
