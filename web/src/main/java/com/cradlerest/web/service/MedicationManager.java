package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Medication;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.view.ReadingView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service responsible for managing {@code Medication} entities.
 *
 *
 */
public interface MedicationManager {

    /**
     * Returns a list of all Medications for a given patient.
     * @param patientId The id of the patient to construct medications for.
     * @return A list of medications
     * @throws EntityNotFoundException If unable to find a patient with the
     * 	given identifier.
     */
    List<Medication> getAllMedicationsForPatient(@NotNull String patientId) throws EntityNotFoundException;

    /**
     * Deconstructs and persists a given medication.
     * @param medication A Medication.
     * @return The saved medication.
     */
    Medication saveMedication(@NotNull Medication medication) throws EntityNotFoundException;
}
