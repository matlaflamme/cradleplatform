package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

/**
 * A generator for names.
 */
public class NameGenerator implements Generator<String> {

	private GibberishSentenceGenerator gibberishSentenceGenerator;

	public NameGenerator(@NotNull Noise noise) {
		this.gibberishSentenceGenerator = new GibberishSentenceGenerator(noise);
	}

	@Override
	public String generate() {
		return gibberishSentenceGenerator
				.with("min", 1)
				.with("max", 2)
				.generate()
				.replace(".", "");
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("NameGenerator does not support parameters");
	}
}
