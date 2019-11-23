package com.cradlerest.web.service.repository;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.HealthCentre;
import com.cradlerest.web.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface HealthCentreRepository extends JpaRepository<HealthCentre, Integer> {
	Optional<HealthCentre> findByName(String name);
	Optional<HealthCentre> findByPhoneNumber(@NotNull String phone_number) throws NoSuchElementException;
}
