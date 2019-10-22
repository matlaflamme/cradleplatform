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
}
