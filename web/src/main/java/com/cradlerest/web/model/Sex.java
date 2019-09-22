package com.cradlerest.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enumerated sex for {@code Patient} model.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum Sex {
	MALE, FEMALE, UNKNOWN
}
