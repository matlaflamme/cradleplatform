package com.cradlerest.web.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository giving us the ability to send native update queries directly to
 * the database without having to go through JPA.
 *
 * This repository is only meant for the purpose of dynamically rebuilding the
 * database with dummy data. IT SHOULD NEVER BE USED FOR ANYTHING ELSE.
 */
public interface RawDatabaseAccessRepository
		extends JpaRepository<MockEntity, Integer>, RawDatabaseAccessRepositoryCustom {
}
