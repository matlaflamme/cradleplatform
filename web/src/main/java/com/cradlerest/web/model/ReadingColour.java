package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenOrdinal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enumeration of possible colour values for a reading.
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
