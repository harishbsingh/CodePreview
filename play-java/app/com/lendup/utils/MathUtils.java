package com.lendup.utils;

/**
 * Utility class for all Math operations
 * 
 * @author harishs
 * 
 */
public class MathUtils {

    /**
     * takes a number format {@link String} and returns a {@link Long} form of
     * the same
     * 
     * @param numberString
     *            {@link String}
     * @return {@link Long} form for the input String. The return value will be
     *         "null" if the input String is not a number
     */
    public static Long getLongFrom(String numberString) {

	try {
	    Long num = Long.parseLong(numberString);
	    return num;
	} catch (Exception e) {
	    return null;
	}

    }
}
