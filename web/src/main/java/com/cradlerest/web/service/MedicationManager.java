package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Medication;
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
     *
     * @param patientId is the id of the patient to find the medication for
     * @param medId is the id of the Medication to find for the patient
     * @return is the singular medication of the keycvalue patientId, medId
     * @throws EntityNotFoundException If unable to find a patient with the
     *  given identifier
     */
    Medication getMedication(@NotNull String patientId, int medId) throws EntityNotFoundException;

    /**
     * Deconstructs and persists a given medication.
     * @param medication A Medication.
     * @return The saved medication.
     */
    Medication saveMedication(@NotNull Medication medication);

    /**
     *
     * @param pid is the medication object's PatientId to delete
     * @param medId is the medication object's MedicationId to delete
     * @throws EntityNotFoundException if it could not find the medication
     * @return
     */
    Medication remove(String pid, int medId) throws EntityNotFoundException;
}
