package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@code User} entities.
 *
 * @see User
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
