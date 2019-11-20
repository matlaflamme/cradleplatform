package com.cradlerest.web.service;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Medication;
import com.cradlerest.web.service.repository.MedicationRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MedicationManagerImpl implements MedicationManager {

    private MedicationRepository medicationRepository;

    public MedicationManagerImpl(MedicationRepository medicationRepository){
        this.medicationRepository = medicationRepository;
    }

    @Override
    public List<Medication> getAllMedicationsForPatient(@NotNull String patientId) throws EntityNotFoundException {
        return medicationRepository.findAllByPatientId(patientId);
    }

    @Override
    public Medication saveMedication(@NotNull Medication medication) throws EntityNotFoundException {
        // use copyFields to extract the reading and persist it
        var persistedMedication = medicationRepository.save(medication);
        return persistedMedication;
    }
}
