package com.lendup.twiliocaller.models;

/**
 * This class is used only when the goal is to call the user and return the
 * FizzBuzz string that corresponds to one of the digits the user had entered in
 * the past calls
 * 
 * @author harishs
 * 
 */
public class DirectCaller implements DelayInterface {

    public String fizzBuzzInput;
    public String callFrom;
    public String callTo;

    public DirectCaller(String callFrom, String callTo, String fizzBuzz) {
	this.callFrom = callFrom;
	this.callTo = callTo;
	fizzBuzzInput = fizzBuzz;
    }

    /**
     * Since the call is directly made without any delay, it returns 0
     */
    @Override
    public long getTotalDelayInSeconds() {
	return 0L;
    }
}
