package com.cradlerest.web.model;

public class Stats {
    private int numberOfReadings;
    private int numberOfReds;
    private int numberOfGreens;
    private int numberOfYellows;
    private int numberOfPatientsSeen;
    private int numberOfVHTs;
    private int numberOfReadingsTaken;


    public Stats(
            int numberOfReadings,
            int numberOfReds,
            int numberOfGreens,
            int numberOfYellows,
            int numberOfPatientsSeen,
            int numberOfVHTs,
            int numberOfReadingsTaken
    ) {
        this.numberOfReadings = numberOfReadings;
        this.numberOfReds = numberOfReds;
        this.numberOfGreens = numberOfGreens;
        this.numberOfYellows = numberOfYellows;
        this.numberOfPatientsSeen = numberOfPatientsSeen;
        this.numberOfVHTs = numberOfVHTs;
        this.numberOfReadingsTaken = numberOfReadingsTaken;
    }

    public Stats() {
        this.numberOfReadings = 0;
        this.numberOfReds = 0;
        this.numberOfGreens = 0;
        this.numberOfYellows = 0;
        this.numberOfPatientsSeen = 0;
        this.numberOfVHTs = 0;
        this.numberOfReadingsTaken = 0;
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

    public int getNumberOfReadingsTaken() {
        return numberOfReadingsTaken;
    }

    public void setNumberOfReadingsTaken(int numberOfReadingsTaken) {
        this.numberOfReadingsTaken = numberOfReadingsTaken;
    }
}
