package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for {@code User} entities.
 *
 * @see User
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    // Optional list (may or may not be null / empty)
    Optional<User> findByUsername(String name);
    void deleteByUsername(String name);
}
