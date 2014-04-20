package com.thefind.InverseDocumentFrequency;
/**
 * Adding all String related operations here
 * 
 * @author harishs
 * 
 */
public class StringUtils {

	/**
	 * Checks if the String contains any Numbers or Signs
	 * 
	 * @param str
	 *            input String
	 * @return true if the String does not contain numbers, otherwise false
	 */
	public static boolean containsOnlyLetter(String str) {
		char[] chars = str.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}

		return true;
	}
}
