package com.cradlerest.web.util.datagen.mock;

import com.cradlerest.web.util.datagen.Noise;

public class MockNoise implements Noise {

	private double value = 0;

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public double generate() {
		return value;
	}
}
