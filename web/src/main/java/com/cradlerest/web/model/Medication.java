package com.cradlerest.web.model;

public class Medication {
    private int id;
    private String medication;
    private String dosage;
    private String usageFrequency;

    public Medication(int id, String medication, String dosage, String usageFrequency) {
        this.id = id;
        this.medication = medication;
        this.dosage = dosage;
        this.usageFrequency = usageFrequency;
    }

    public Medication(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getUsageFrequency() {
        return usageFrequency;
    }

    public void setUsageFrequency(String usageFrequency) {
        this.usageFrequency = usageFrequency;
    }
}
