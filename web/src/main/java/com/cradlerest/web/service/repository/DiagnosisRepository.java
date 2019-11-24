package com.cradlerest.web.service.repository;

import com.cradlerest.web.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {
}
