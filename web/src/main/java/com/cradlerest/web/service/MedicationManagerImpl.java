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
        checkPatientValid(patientId);
        return medicationRepository.findAllByPatientId(patientId);
    }


    @Override
    public Medication getMedication(@NotNull String patientId, int medId) throws EntityNotFoundException {
        checkPatientValid(patientId);
        return medicationRepository.findAllByPatientId(patientId).get(medId);
    }

    @Override
    public Medication saveMedication(@NotNull Medication medication){
        return medicationRepository.save(medication);
    }

    @Override
    public Medication remove(String pid, int medId) throws EntityNotFoundException {
        List<Medication> allMedsPatientIsOn = getAllMedicationsForPatient(pid);
        medicationRepository.deleteMedicationByPatientId(pid, medId);

        return allMedsPatientIsOn.get(medId);
    }



    private void checkPatientValid(@NotNull String patientId) throws EntityNotFoundException {
        if(medicationRepository.GetPatient(patientId).size() == 0){
            throw new EntityNotFoundException("Patient "+patientId+" does not exist");
        }
    }
}
