package com.cradlerest.web.model;

import com.cradlerest.web.util.datagen.annotations.DataGenOrdinal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enumerated sex for {@code Patient} model.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
@DataGenOrdinal
public enum Sex {
	MALE, FEMALE, UNKNOWN;

	public static Sex valueOf(int ordinal) {
		return Sex.values()[ordinal];
	}
}
