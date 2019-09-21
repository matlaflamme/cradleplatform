package com.cradlerest.web.service;

import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for services dealing with patient management.
 */
public interface PatientManagerService {

	Patient getPatientWithId(@NotNull String id) throws Exception;

	List<Reading> getReadingsForPatientWithId(@NotNull String id);
}
