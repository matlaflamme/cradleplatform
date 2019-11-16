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


}
