package com.cradlerest.web.util;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Convenience date parser using implicit exceptions for cleaner test code.
 */
public class DateParser {

	/**
	 * Parses a date string in the form "yyyy-MM-dd".
	 *
	 * Example:
	 * <code>
	 *     var date = DateParser.parseDate("2019-12-25");
	 * </code>
	 * @param text Text to parse, must be in the form "yyyy-MM-dd".
	 * @return The date described by {@param text} as a {code Date} object.
	 * @throws RuntimeException If {@param text} is an invalid date string.
	 */
	public static Date parseDate(@NotNull String text) {
		return parse(text, "yyyy-MM-dd");
	}

	/**
	 * Parses a date-time string in the form "yyyy-MM-dd HH:mm:ss".
	 *
	 * Example:
	 * <code>
	 *     var date = DateParser.parseDate("2019-12-25 23:59:59");
	 * </code>
	 * @param text Text to parse, must be in the form "yyyy-MM-dd HH:mm:ss".
	 * @return The date described by {@param text} as a {code Date} object.
	 * @throws RuntimeException If {@param text} is an invalid date-time string.
	 */
	public static Date parseDateTime(@NotNull String text) {
		return parse(text, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Parses a date string in a given {@param format}.
	 *
	 * Example:
	 * <code>
	 *     var date = DateParser.parseDate("2019-12-15", "yyyy-MM-dd");
	 * </code>
	 * @param text Text to parse.
	 * @return The date described by {@param text} as a {code Date} object.
	 * @throws RuntimeException If {@param text} is an invalid date string.
	 */
	public static Date parse(@NotNull String text, @NotNull String format) {
		try {
			return new SimpleDateFormat(format).parse(text);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}
}
