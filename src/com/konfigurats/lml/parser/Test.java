package com.konfigurats.lml.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Test {
	public static final char QUOTATION_CHAR = '\'';
	public static final char DOUBLE_QUOTATION_CHAR = '"';

	public static void main(final String... a) {
		log('a' + "" + 'b' + "string" + 'c');

		log("${CONTENT}siemsy${CONTENT}".replace("${CONTENT}", "dupsko"));
		log("<@import import.lml/>${CONTENT}<label row=true>This should be second.</label>{$CONTENT}"
				.replace("${CONTENT}", "moja"));
		log(escapeQuotation("\"nosiema\"") + " " + escapeQuotation("'nosiema'") + " "
				+ escapeQuotation("'nosiema\""));
		log("dsaijdiasoej".split("EEE")[0]);

		final String siem = "aaaaaaaabbbbbbbbbb";
		final Queue<Character> buffer = new LinkedList<Character>();
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < siem.length(); i++) {
			buffer.add(siem.charAt(i));
			if (i == 7) {
				buffer.addAll(new ArrayList<Character>() {
					private static final long serialVersionUID = 1L;
					{
						add('d');
						add('e');
						add('f');
						add('g');
					}
				});
			}
			while (!buffer.isEmpty()) {
				sb.append(buffer.poll());
			}
		}
		log(sb);
	}

	private static String escapeQuotation(final String attribute) {
		if (isStartingAndEndingWith(attribute, QUOTATION_CHAR)
				|| isStartingAndEndingWith(attribute, DOUBLE_QUOTATION_CHAR)) {
			return attribute.substring(1, attribute.length() - 1);
		}
		return attribute;
	}

	private static boolean isStartingAndEndingWith(final String attribute, final char character) {
		return attribute.charAt(0) == character && attribute.charAt(attribute.length() - 1) == character;
	}

	static void log(final Object... os) {
		for (final Object o : os) {
			log(o);
		}
	}

	static void log(final Object o) {
		System.out.println(o);
	}
}
