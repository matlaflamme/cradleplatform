package com.cradlerest.web.service.repository;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SuppressWarnings("unused")
public class RawDatabaseAccessRepositoryCustomImpl implements RawDatabaseAccessRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	@Transactional
	public void update(@NotNull String statement) {
		entityManager.createNativeQuery(statement).executeUpdate();
	}
}
