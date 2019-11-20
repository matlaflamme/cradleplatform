package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenOrdinal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enumeration of possible colour values for a reading.
 *
 * GREEN (Patient is likely healthy. Continue normal care),
 * YELLOW_UP (Raised BP. Monitor for preeclampsia. Transfer to health centre WITHIN 24hr),
 * YELLOW_DOWN (Common but assess for infection, bleeding, anaemia, and dehydration),
 * RED_UP (Urgent action needed. Transfer to health centre within 4h. Monitor baby),
 * RED_DOWN (Urgent action needed. Get help and assess mother. Immediately transfer to health centre within 1h.);
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
@DataGenOrdinal
public enum ReadingColour {
	GREEN,
	YELLOW_DOWN,
	YELLOW_UP,
	RED_DOWN,
	RED_UP;

	public static ReadingColour valueOf(int ordinal) {
		return ReadingColour.values()[ordinal];
	}

	/**
	 * Global Constants
	 */
	private static final int RED_SYSTOLIC = 160;
	private static final int RED_DIASTOLIC = 110;
	private static final int YELLOW_SYSTOLIC = 140;
	private static final int YELLOW_DIASTOLIC = 90;
	private static final double SHOCK_HIGH = 1.7;
	private static final double SHOCK_MEDIUM = 0.9;


	public static ReadingColour computeColour(int systolic, int diastolic, int heartRate) {

		double shockIndex = getShockIndex(systolic,heartRate);

		boolean isBpVeryHigh = (systolic >= RED_SYSTOLIC) || (diastolic >= RED_DIASTOLIC);
		boolean isBpHigh = (systolic >= YELLOW_SYSTOLIC) || (diastolic >= YELLOW_DIASTOLIC);
		boolean isSevereShock = (shockIndex >= SHOCK_HIGH);
		boolean isShock = (shockIndex >= SHOCK_MEDIUM);
		boolean noPulseOrError = (shockIndex == 0);

		// Return analysis based on priority:
		ReadingColour analysis;
		if(noPulseOrError) {
			analysis = RED_DOWN;
		}else if (isSevereShock) {
			analysis = RED_DOWN;
		} else if (isBpVeryHigh) {
			analysis = RED_UP;
		} else if (isShock) {
			analysis = YELLOW_DOWN;
		} else if (isBpHigh) {
			analysis = YELLOW_UP;
		} else {
			analysis = GREEN;
		}
		return analysis;
	}

	public boolean isRed(){
		return this == RED_DOWN || this == RED_UP;
	}

	public boolean isYellow(){
		return this == YELLOW_DOWN || this == YELLOW_UP;
	}

	public boolean isGreen(){
		return this == GREEN;
	}

	private static double getShockIndex(int systolic, int heartRate) {
		// Div-zero guard:
		if (systolic == 0) {
			return 0;
		}
		return (double) heartRate / (double) systolic;
	}

}
