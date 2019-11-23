package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenFixedString;
import com.cradlerest.web.util.datagen.annotations.DataGenRelativeAmount;
import com.cradlerest.web.util.datagen.annotations.ForeignKey;
import com.cradlerest.web.util.datagen.annotations.Generator;
import com.cradlerest.web.util.datagen.impl.AutoIncrementGenerator;
import com.cradlerest.web.util.datagen.impl.FixedStringGenerator;
import com.cradlerest.web.util.datagen.impl.MedicationNameGenerator;
import com.cradlerest.web.util.datagen.impl.UsageFrequencyGenerator;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@IdClass(Medication.IdType.class)
@Table(name = "medication")
@DataGenRelativeAmount(base = Patient.class, multiplier = 0.7)
public class Medication {

    public static class IdType implements Serializable {
        private String patientId;
        private Integer medId;

        public IdType() {
        }

        public IdType(String patientId, Integer medId) {
            this.patientId = patientId;
            this.medId = medId;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public Integer getMedId() {
            return medId;
        }

        public void setMedId(Integer medId) {
            this.medId = medId;
        }

        @Override
        public int hashCode() {
            return patientId.hashCode() * medId * 7;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            // this is a composite primary key,
            // neither of these fields should be null
            assert patientId != null;
            assert medId != null;

            var validComp = obj != null
                    && obj.hashCode() == hashCode()
                    && obj instanceof IdType;
            if (!validComp) {
                return false;
            }

            var other = (IdType) obj;
            return patientId.equals(other.patientId) && medId.equals(other.medId);
        }
    }

    @Id
    @Column(name = "pid", nullable = false)
    @ForeignKey(Patient.class)
    private String patientId;

    @Id
    @Column(name = "med_id", nullable = false)
    @Generator(AutoIncrementGenerator.class)
    private Integer medId;

    @Column(name = "medication", nullable = false)
    @Generator(MedicationNameGenerator.class)
    private String medication;

    @Column(name = "dosage", nullable = false)
    @Generator(FixedStringGenerator.class)
    @DataGenFixedString("200 mg")
    private String dosage;

    @Column(name = "usage_frequency", nullable = false)
    @Generator(UsageFrequencyGenerator.class)
    private String usageFrequency;

    public Medication(
            String patientId,
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
    public Medication(
            String medication,
            String dosage,
            String usageFrequency
    ) {
        this.medication = medication;
        this.dosage = dosage;
        this.usageFrequency = usageFrequency;
        this.patientId = "";
        this.setMedId(-1);
    }

    public Medication(){}

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
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
