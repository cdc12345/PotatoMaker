/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.cdc.potatomaker.util;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

	private static final Pattern namePartsSplitter = Pattern.compile(
			"(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])|(_)|(?=\\d)");
	private static final Pattern underscoreReducer = Pattern.compile("(?<=\\d)_(?=\\d)");
	private static final Pattern nonescapedCommaSplitter = Pattern.compile("(?<!\\\\),");
	private static final String englishMatch = "[a-zA-Z_:\s]+";

	public static String abbreviateString(String input, int maxLength) {
		return abbreviateString(input, maxLength, true);
	}

	public static String abbreviateString(String input, int maxLength, boolean sumUp) {
		if (input.length() <= maxLength)
			return input;
		else if (sumUp)
			return input.substring(0, maxLength - 3) + "...";
		else
			return input.substring(0, maxLength);
	}

	public static String abbreviateStringInverse(String input, int maxLength) {
		if (input.length() <= maxLength)
			return input;

		return "..." + input.substring(input.length() - maxLength);
	}

	public static boolean isUppercaseLetter(char c) {
		return (c >= 'A' && c <= 'Z');
	}

	public static boolean isLowercaseLetter(char c) {
		return (c >= 'a' && c <= 'z');
	}

	public static String uppercaseFirstLetter(String name) {
		if (name.length() <= 1)
			return name.toUpperCase(Locale.ENGLISH);
		return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
	}

	public static String lowercaseFirstLetter(String name) {
		if (name.length() <= 1)
			return name.toLowerCase(Locale.ENGLISH);
		return name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
	}

	public static String camelToSnake(String original) {
		return underscoreReducer.matcher(String.join("_", namePartsSplitter.split(original))).replaceAll("");
	}

	public static String snakeToCamel(String original) {
		return Arrays.stream(original.split("_")).map(StringUtils::uppercaseFirstLetter).collect(Collectors.joining());
	}

	public static String machineToReadableName(@NotNull String input) {
		String merged = String.join(" ", namePartsSplitter.split(input));
		return WordUtils.capitalize(org.apache.commons.lang3.StringUtils.normalizeSpace(merged));
	}

	public static List<String> splitCommaSeparatedStringListWithEscapes(String specialInfoString) {
		List<String> retval = new ArrayList<>();
		if (!specialInfoString.equals("")) {
			String[] info = nonescapedCommaSplitter.split(specialInfoString);
			for (String infoelement : info) {
				String data = infoelement.trim().replace("\\,", ",");
				if (!data.trim().equals(""))
					retval.add(data);
			}
		}
		return retval;
	}

	public static int countRegexMatches(String where, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(where);

		int count = 0;
		while (matcher.find())
			count++;

		return count;
	}

	/**
	 * 检查是否为英文,如果不是则返回否
	 * @param text
	 * @return 英文和null返回是,否则返回false
	 */
	public static boolean isEnglish(String text){
		if (text == null) return true;
		return text.matches(englishMatch);
	}

	public static String getUnicodeCode(String text){
		return Arrays.stream(text.split("\\+")).map(a->{
			if (a.matches("\".+\"")){
				return '"'+ StringEscapeUtils.escapeJava(a.substring(1,a.length()-1))+'"';
			} else if (a.matches("\".+")){
				return '"'+StringEscapeUtils.escapeJava(a.substring(1));
			} else if (a.matches(".+\"")){
				return  StringEscapeUtils.escapeJava(a.substring(0,a.length()-1))+'"';
			}
			return a;
		}).collect(Collectors.joining("+"));
	}

}
