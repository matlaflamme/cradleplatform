//Based off cradlemobile/app/src/main/java/com/cradletrial/cradlevhtapp/model/ReadingAnalysis.java

var TrafficLightCalc = function trafficLightCalc() {

    const colourEnum = {"GREEN":0, "YELLOW_DOWN":1, "YELLOW_UP":2, "RED_DOWN":3, "RED_UP":4};

    this.RED_SYSTOLIC = 160;
    this.RED_DIASTOLIC = 110;
    this.YELLOW_SYSTOLIC = 140;
    this.YELLOW_DIASTOLIC = 90;
    this.SHOCK_HIGH = 1.7;
    this.SHOCK_MEDIUM = 0.9;

    this.shockIndex;
    this.currSystolic;
    this.currDiastolic;
    this.currHeartRate;

    this.init = function(systolic, diastolic, heartRate) {
        this.currSystolic = systolic;
        this.currDiastolic = diastolic;
        this.currHeartRate = heartRate;
        this.shockIndex = this.getShockIndex(systolic, heartRate);
    };

    this.getShockIndex = function(systolic, heartRate) {
        return heartRate/systolic;
    };

    this.getColour = function(systolic, diastolic, heartRate) {
        this.init(systolic, diastolic, heartRate);

        let isBpVeryHigh = (this.currSystolic >= this.RED_SYSTOLIC) || (this.currDiastolic >= this.RED_DIASTOLIC);
        let isBpHigh = (this.currSystolic >= this.YELLOW_SYSTOLIC) || (this.currDiastolic >= this.YELLOW_DIASTOLIC);
        let isSevereShock = (this.shockIndex >= this.SHOCK_HIGH);
        let isShock = (this.shockIndex >= this.SHOCK_MEDIUM);

        //return analysis based on priority
        if (isSevereShock) {
            return colourEnum.RED_DOWN;
        } else if (isBpVeryHigh) {
            return colourEnum.RED_UP;
        } else if (isShock) {
            return colourEnum.YELLOW_DOWN;
        } else if (isBpHigh) {
            return colourEnum.YELLOW_UP;
        } else {
            return colourEnum.GREEN;
        }
    }
};

export { TrafficLightCalc };