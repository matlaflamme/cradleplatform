package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Noise;

import java.util.Random;

/**
 * Uniform {@code Noise} implementation using {@code java.util.Random}.
 */
public class UniformNoise implements Noise {

	private Random rng;

	public UniformNoise() {
		this.rng = new Random();
	}

	public UniformNoise(long seed) {
		this.rng = new Random(seed);
	}

	@Override
	public double generate() {
		return rng.nextDouble();
	}
}
