package com.cradlerest.web.model;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class PatientMedicationsTest {

    @Test
    public void getMedicationAsList() {
        Patient patient = CreateNewTestPatient();
        patient.addMedication("Tylenol\t Asprin");
        ArrayList<String> arrayList = patient.getMedicationAsList();
        Assert.assertEquals(arrayList.size(), 1);
    }

    @NotNull
    private Patient CreateNewTestPatient() {
        return new Patient(
                    "1",
                    "1",
                    "1",
                    "1",
                    1,
                    Sex.FEMALE,
                    null,
                    "",
                    "",
                    Date.from(Instant.now()),
                    ""
                    );
    }

    @Test
    public void getMedication() {
        Patient patient = CreateNewTestPatient();
        patient.addMedication("Tylenol\t Asprin");
        patient.addMedication("amlodipine");
        patient.addMedication("diltiazem");
        Assert.assertTrue(patient.getMedication().equals("Tylenol     Asprin\tamlodipine\tdiltiazem"));
    }

    @Test
    public void setMedication() {
        Patient patient = CreateNewTestPatient();
        patient.addMedication("Tylenol\t Asprin");
        patient.addMedication("amlodipine");
        patient.addMedication("diltiazem");
        patient.setMedication("nisoldipine");
        Assert.assertTrue(patient.getMedication().equals("nisoldipine"));
    }

    @Test
    public void addMedication() {
        Patient patient = CreateNewTestPatient();
        ArrayList<String> list = new ArrayList<>();
        list.add("Tylenol\t Asprin");
        list.add("amlodipine");
        list.add("diltiazem");
        list.add("nisoldipine");
        patient.addMedication(list);
        Assert.assertEquals(patient.getMedication(), "Tylenol     Asprin\tamlodipine\tdiltiazem\tnisoldipine");

    }

    @Test
    public void removeMedication() {
        Patient patient = CreateNewTestPatient();
        ArrayList<String> list = new ArrayList<>();
        list.add("Tylenol\t Asprin");
        list.add("amlodipine");
        list.add("diltiazem");
        list.add("nisoldipine");
        patient.addMedication(list);
        patient.removeMedication("amlodipine");
        Assert.assertEquals(patient.getMedication(), "Tylenol     Asprin\tdiltiazem\tnisoldipine");
    }
}