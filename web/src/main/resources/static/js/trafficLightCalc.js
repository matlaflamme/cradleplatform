//Based off cradlemobile/app/src/main/java/com/cradletrial/cradlevhtapp/model/ReadingAnalysis.java

class trafficLightCalc {
    //private variables
    #RED_SYSTOLIC = 160;
    #RED_DIASTOLIC = 110;
    #YELLOW_SYSTOLIC = 140;
    #YELLOW_DIASTOLIC = 90;
    #SHOCK_HIGH = 1.7;
    #SHOCK_MEDIUM = 0.9;

    shockIndex;
    currSystolic;
    currDiastolic;
    currHeartRate;

    constructor(systolic, diastolic, heartRate) {
        this.currSystolic = systolic;
        this.currDiastolic = diastolic;
        this.currHeartRate = heartRate;
        this.shockIndex = this.getShockIndex(systolic, heartRate);
    }

    getShockIndex(systolic, heartRate) {
        return heartRate/systolic;
    }

    getColour() {
        let isBpVeryHigh = (this.currSystolic >= this.#RED_SYSTOLIC) || (this.currDiastolic >= this.#RED_DIASTOLIC);
        let isBpHigh = (this.currSystolic >= this.#YELLOW_SYSTOLIC) || (this.currDiastolic >= this.#YELLOW_DIASTOLIC);
        let isSevereShock = (this.shockIndex >= this.#SHOCK_HIGH);
        let isShock = (this.shockIndex >= this.#SHOCK_MEDIUM);

        //return analysis based on priority
        if (isSevereShock) {
            return "RED_DOWN";
        } else if (isBpVeryHigh) {
            return "RED_UP";
        } else if (isShock) {
            return "YELLOW_DOWN";
        } else if (isBpHigh) {
            return "YELLOW_UP";
        } else {
            return "GREEN";
        }
    }
}