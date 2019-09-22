package com.cradlerest.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enumeration of possible colour values for a reading.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum ReadingColour {
	GREEN, YELLOW, RED
}