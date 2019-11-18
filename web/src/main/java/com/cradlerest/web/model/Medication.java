package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenRange;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class Medication {

    public static class IdType implements Serializable {
        private Integer patientId;

        private Integer medicationId;

        public IdType() {
        }

        public IdType(Integer patientId, Integer medicationId) {
            this.patientId = patientId;
            this.medicationId = medicationId;
        }

        public Integer getPatientId() {
            return patientId;
        }

        public void setPatientId(Integer patientId) {
            this.patientId = patientId;
        }

        public Integer getMedicationId() {
            return medicationId;
        }

        public void setMedicationId(Integer medicationId) {
            this.medicationId = medicationId;
        }

        @Override
        public int hashCode() {
            return (patientId << 1) ^ medicationId;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            // this is a composite primary key,
            // neither of these fields should be null
            assert patientId != null;
            assert medicationId != null;

            var validComp = obj != null
                    && obj.hashCode() == hashCode()
                    && obj instanceof IdType;
            if (!validComp) {
                return false;
            }

            var other = (IdType) obj;
            return patientId.equals(other.patientId) && medicationId.equals(other.medicationId);
        }
    }

    @Id
    @Column(name = "pid", nullable = false)
    @ForeignKey(Patient.class)
    private Integer patientId;

    @Id
    @Column(name = "med_id")
    private Integer medId;

    @Column(name = "medication")
    private String medication;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "usage_frequency")
    private String usageFrequency;

    public Medication(
            Integer patientId,
            Integer medId,
            String medication,
            String dosage,
            String usageFrequency
    ) {
        this.patientId = patientId;
        this.medId = medId;
        this.medication = medication;
        this.dosage = dosage;
        this.usageFrequency = usageFrequency;
    }

    public Medication(){}

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getMedId() {
        return medId;
    }

    public void setMedId(Integer medId) {
        this.medId = medId;
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
