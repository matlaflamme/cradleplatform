package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.cradlerest.web.util.datagen.error.OperationNotSupportedException;
import org.jetbrains.annotations.NotNull;

/**
 * A random generator for emails.
 */
public class EmailGenerator implements Generator<String> {

	private GibberishSentenceGenerator gibberishSentenceGenerator;

	private static final String SUFFIX = "@real.email.com";

	public EmailGenerator(@NotNull Noise noise) {
		this.gibberishSentenceGenerator = new GibberishSentenceGenerator(noise);
	}

	@Override
	public String generate() {
		var username = gibberishSentenceGenerator
				.with("min", 1)
				.with("max", 4)
				.generate()
				.replace(".", "")
				.toLowerCase()
				.replace(' ', '-');
		return username + SUFFIX;
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("EmailGenerator does not support parameters");
	}
}
