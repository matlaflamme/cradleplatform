package com.cradlerest.web.service;

import com.cradlerest.web.model.Patient;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for services dealing with patient management.
 */
public interface PatientManagerService {

	Patient getPatientWithId(@NotNull String id) throws Exception;
}
