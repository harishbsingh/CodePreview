package com.lendup.twiliocaller.models;

import play.Logger;

import com.lendup.utils.Twilio;
import com.twilio.sdk.verbs.TwiMLException;

/**
 * Class responsible for computing the FizzBuzz series for the number entered by
 * User
 * 
 * @author harishs
 * 
 */
public class FizzBuzz {

    private final String number;

    public FizzBuzz(String number) {
	this.number = number;
    }

    /**
     * Finds the FizzBuzz and returns it.
     * 
     * @return {@link String} FizzBuzz series upto the number entered by user
     * @throws TwiMLException
     */
    public String getFizzBuzzTwiMlResponse() throws TwiMLException {
	String result = getFizzBizzString();
	if (result == null)
	    return result;

	return Twilio.getTwiMlResponseFromString(result);
    }

    /**
     * Finds the FizzBuzz and returns it.
     * 
     * @return {@link String} FizzBuzz series upto the number entered by user
     */
    public String getFizzBizzString() {
	String fbString;
	try {
	    Long num = Twilio.getLongFromString(number);
	    if (num == null) {
		fbString = null;
	    } else {
		fbString = computeFizzBuzzString(num);
	    }

	} catch (Exception e) {
	    Logger.error("Error occured while finding FizzBuzz: " + e);
	    fbString = null;
	}
	return fbString;
    }

    /**
     * 
     * @param num
     *            {@link Long} number whose fizzBuzz series is to be computed
     * @return FizzBuzz {@link String} for that number
     * 
     */
    private String computeFizzBuzzString(long num) {
	String result = "";
	for (int i = 1; i <= num; i++) {
	    String str;
	    if (isFIzzBuzz(i)) {
		str = "FIzzBuzz";
	    } else if (isFizz(i)) {
		str = "Fizz";
	    } else if (isBuzz(i)) {
		str = "Buzz";
	    } else {
		str = Integer.toString(i);
	    }
	    result = result + " " + str;
	}

	Logger.info("FizzBuzz for this request: " + result);
	return result;
    }

    /**
     * checks if the number is divisble by both 3 and 5
     * 
     * @param num
     * @return true if the number is divisible by both 3 and 5
     */
    private boolean isFIzzBuzz(int num) {
	if (num % 5 == 0 && num % 3 == 0)
	    return true;
	return false;
    }

    /**
     * checks if the number is divisble by 3
     * 
     * @param num
     * @return true if the number is divisible by 3
     */
    private boolean isFizz(int num) {
	if (num % 3 == 0)
	    return true;
	return false;
    }

    /**
     * checks if the number is divisble by 5
     * 
     * @param num
     * @return true if the number is divisible by 5
     */
    private boolean isBuzz(int num) {
	if (num % 5 == 0)
	    return true;
	return false;
    }
}
