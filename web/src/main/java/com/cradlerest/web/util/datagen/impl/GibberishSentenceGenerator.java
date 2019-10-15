package com.cradlerest.web.util.datagen.impl;

import com.cradlerest.web.util.datagen.Generator;
import com.cradlerest.web.util.datagen.Noise;
import com.github.maumay.jflow.iterator.Iter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Generates a random gibberish string in a form resembling an english sentence.
 *
 * Accepts parameters "min" and "max" to determine the number of words in the
 * sentence.
 */
public class GibberishSentenceGenerator implements Generator<String> {

	private static final int MIN_WORD_PHONETIC_COUNT = 1;
	private static final int MAX_WORD_PHONETIC_COUNT = 4;

	private static final int MIN_WORD_COUNT = 5;
	private static final int MAX_WORD_COUNT = 40;

	@NotNull
	private Noise noise;

	@NotNull
	private Map<String, Object> curriedParameters = new HashMap<>();

	public GibberishSentenceGenerator(@NotNull Noise noise) {
		this.noise = noise;
	}

	private GibberishSentenceGenerator(@NotNull Noise noise, @NotNull Map<String, Object> curriedParameters) {
		this.noise = noise;
		this.curriedParameters = curriedParameters;
	}

	@Override
	public String generate() {
		var minWordCount = (Integer) curriedParameters.getOrDefault("min", MIN_WORD_COUNT);
		var maxWordCount = (Integer) curriedParameters.getOrDefault("max", MAX_WORD_COUNT);
		var wordCount = (int) noise.generate(minWordCount, maxWordCount);
		var sentence = Iter.call(this::generateWord)
				.take(wordCount - 1)
				.fold(capitalize(generateWord()), (accum, word) -> accum + " " + word);
		return sentence + ".";
	}

	@Override
	public Generator<String> with(@NotNull String key, @NotNull Object value) throws IllegalArgumentException {
		// validate the key/value pair
		switch (key) {
			case "max": // fallthrough
			case "min":
				if (!(value instanceof Integer) || ((Integer) value) < 0) {
					throw new IllegalArgumentException("illegal value for key '" + key + "': " + value.toString());
				}
				break;
			default:
				throw new IllegalArgumentException("illegal key: " + key);
		}

		var paramClone = new HashMap<>(curriedParameters);
		paramClone.put(key, value);
		return new GibberishSentenceGenerator(noise, paramClone);
	}

	/**
	 * Generates a random gibberish work by combining a sequence of random
	 * phonetics.
	 * @return A gibberish word.
	 */
	private String generateWord() {
		var count = (int) noise.generate(MIN_WORD_PHONETIC_COUNT, MAX_WORD_PHONETIC_COUNT);
		return Iter.call(this::generatePhonetic)
				.take(count)
				.fold((accum, ph) -> accum + ph);
	}

	/**
	 * Generates a random phonetic.
	 *
	 * Gibberish phonetics are modeled after Japanese phonetics -- a consonant
	 * followed by a vowel.
	 */
	private String generatePhonetic() {
		// don't use a full consonant set, instead use something similar to the
		// Japanese consonant set.
		final var CONSONANT_CHARSET = "bdfghklmnprsty";
		final var VOWEL_CHARSET = "aeiou";
		final double ONLY_VOWEL_PERCENT = 0.2;

		if (noise.generateBool(ONLY_VOWEL_PERCENT)) {
			return Character.toString(noise.pick(VOWEL_CHARSET));
		} else {
			return Character.toString(noise.pick(CONSONANT_CHARSET)) + noise.pick(VOWEL_CHARSET);
		}
	}

	/**
	 * Capitalizes the first letter of a given non-empty word.
	 * @param word The word to capitalize.
	 * @return The capitalized version of the word.
	 */
	private String capitalize(@NotNull String word) {
		assert !word.isEmpty();

		var firstChar = word.charAt(0);
		return Character.toUpperCase(firstChar) + word.substring(1);
	}
}
