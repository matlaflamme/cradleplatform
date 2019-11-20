package com.cradlerest.web.model;

public class Stat {
    private int numberOfReadings;
    private int numberOfReds;
    private int numberOfGreens;
    private int numberOfYellows;
    private int numberOfPatientsSeen;
    private int numberOfVHTs;
    private int numberOfReferrals;


    public Stat(
            // reading info
            int numberOfReadings,
            int numberOfReds,
            int numberOfGreens,
            int numberOfYellows,
            int numberOfPatientsSeen,
            int numberOfVHTs,
            // might need referral details
            int numberOfReferrals
    ) {
        this.numberOfReadings = numberOfReadings;
        this.numberOfReds = numberOfReds;
        this.numberOfGreens = numberOfGreens;
        this.numberOfYellows = numberOfYellows;
        this.numberOfPatientsSeen = numberOfPatientsSeen;
        this.numberOfVHTs = numberOfVHTs;
        this.numberOfReferrals = numberOfReferrals;
    }

    public Stat() {
        this.numberOfReadings = 0;
        this.numberOfReds = 0;
        this.numberOfGreens = 0;
        this.numberOfYellows = 0;
        this.numberOfPatientsSeen = 0;
        this.numberOfVHTs = 0;
        this.numberOfReferrals = 0;
    }


    public int getNumberOfReadings() {
        return numberOfReadings;
    }

    public void setNumberOfReadings(int numberOfReadings) {
        this.numberOfReadings = numberOfReadings;
    }

    public int getNumberOfReds() {
        return numberOfReds;
    }

    public void setNumberOfReds(int numberOfReds) {
        this.numberOfReds = numberOfReds;
    }

    public int getNumberOfGreens() {
        return numberOfGreens;
    }

    public void setNumberOfGreens(int numberOfGreens) {
        this.numberOfGreens = numberOfGreens;
    }

    public int getNumberOfYellows() {
        return numberOfYellows;
    }

    public void setNumberOfYellows(int numberOfYellows) {
        this.numberOfYellows = numberOfYellows;
    }

    public int getNumberOfPatientsSeen() {
        return numberOfPatientsSeen;
    }

    public void setNumberOfPatientsSeen(int numberOfPatientsSeen) {
        this.numberOfPatientsSeen = numberOfPatientsSeen;
    }

    public int getNumberOfVHTs() {
        return numberOfVHTs;
    }

    public void setNumberOfVHTs(int numberOfVHTs) {
        this.numberOfVHTs = numberOfVHTs;
    }

    public int getNumberOfReferrals() {
        return numberOfReferrals;
    }

    public void setNumberOfReferrals(int numberOfReferrals) {
        this.numberOfReferrals = numberOfReferrals;
    }
}
