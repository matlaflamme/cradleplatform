package com.cradlerest.web.service.repository;

import org.jetbrains.annotations.NotNull;

import javax.transaction.Transactional;

/**
 * Extension component of {@code RawDatabaseAccessRepository},
 */
public interface RawDatabaseAccessRepositoryCustom {

	/**
	 * Executes a native SQL statement on the underlying database without going
	 * though any JPA validation.
	 * @param statement The statement to execute.
	 */
	@Transactional
	void update(@NotNull String statement);
}
