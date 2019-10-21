package com.cradlerest.web.model;

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
public enum ReadingColour {
	GREEN(0),
	YELLOW_DOWN(1),
	YELLOW_UP(2),
	RED_DOWN(3),
	RED_UP(4);

	private final int key;

	ReadingColour(int key) {
		this.key = key;
	}

	public int getKey() {
		return this.key;
	}

	public static ReadingColour fromKey(int key) {
		for(ReadingColour type : ReadingColour.values()) {
			if(type.getKey() == key) {
				return type;
			}
		}
		return null;
	}
}
