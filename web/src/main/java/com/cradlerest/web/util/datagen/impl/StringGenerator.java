package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating random strings of various character sets and
 * lengths. Also doubles as a {@code Generator<String>}.
 */
public class StringGenerator implements Generator<String> {

	public static final String LOWER_ALPHA_CHARSET = "abcdefghijklmnopqrstuvwxyz";
	public static final String UPPER_ALPHA_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String DECIMAL_CHARSET = "0123456789";
	public static final String GIBBERISH_CHARSET = LOWER_ALPHA_CHARSET + UPPER_ALPHA_CHARSET + "          ";

	private static final int DEFAULT_LENGTH = 16;
	private static final String DEFAULT_CHARSET = GIBBERISH_CHARSET;

	@NotNull
	private Noise noise;

	@NotNull
	private Map<String, Object> curriedParameters = new HashMap<>();

	public StringGenerator() {
		this.noise = new UniformNoise();
	}

	public StringGenerator(@NotNull Noise noise) {
		this.noise = noise;
	}

	private StringGenerator(@NotNull Noise noise, @NotNull Map<String, Object> parameters) {
		this.noise = noise;
		this.curriedParameters = parameters;
	}

	/**
	 * Generates a random string of a given length using only characters from
	 * a provided character set.
	 *
	 * Assumes that {@code charset} is non-empty and that {@code length} is
	 * strictly greater than 0.
	 *
	 * @param length The length of the string to generate.
	 * @param charset The set of characters to pull from.
	 * @return A random string.
	 */
	public String generate(int length, @NotNull String charset) {
		assert !charset.isEmpty();
		assert length > 0;

		var builder = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			var index = (int) noise.generate(0, charset.length());
			builder.append(charset.charAt(index));
		}
		return builder.toString();
	}

	@Override
	public String generate() {
		var length = (Integer) curriedParameters.getOrDefault("length", DEFAULT_LENGTH);
		var charset = (String) curriedParameters.getOrDefault("charset", DEFAULT_CHARSET);
		return generate(length, charset);
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) {
		// validate the key/value pair
		switch (key) {
			case "length":
				if (!(value instanceof Integer) || ((Integer) value) < 0) {
					throw new IllegalArgumentException("illegal value for key 'length': " + value.toString());
				}
				break;
			case "charset":
				if (!(value instanceof String) || ((String) value).isEmpty()) {
					throw new IllegalArgumentException("illegal value for key 'charset': " + value.toString());
				}
				break;
			default:
				throw new IllegalArgumentException("illegal key: " + key);
		}

		// curry and return
		var paramClone = new HashMap<>(curriedParameters);
		paramClone.put(key, value);
		return new StringGenerator(noise, paramClone);
	}
}
