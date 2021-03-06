package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Repository for {@code User} entities.
 *
 * @see User
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    // Optional return (may or may not be null / empty)
    Optional<User> findByUsername(String name);
    void deleteByUsername(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET worksAtHealthCentreId = ?2 WHERE u.username = ?1")
    void updateWorksAtByUsername(String name, @Nullable Integer healthCentreId);
}
