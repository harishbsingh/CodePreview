package com.lendup.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import play.Logger;

import com.lendup.constants.TwilioConstants;
import com.lendup.twiliocaller.models.Caller;
import com.lendup.twiliocaller.models.DelayInterface;
import com.lendup.twiliocaller.models.DirectCaller;
import com.lendup.twiliocaller.models.FizzBuzz;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.verbs.Gather;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

public class Twilio {
    public static org.slf4j.Logger logger = LoggerFactory.getLogger(Twilio.class);

    /* Instantiate a new Twilio Rest Client */
    private final static TwilioRestClient client = new TwilioRestClient(TwilioConstants.SSID, TwilioConstants.AUTH_KEY);

    /**
     * 
     * Method that starts conversation with the caller
     * 
     * @return {@link TwiMLResponse} sent over the call
     */
    public static TwiMLResponse getCallStarter(Caller caller) {
	Logger.info("Preparing Greeting message and url.");
	TwiMLResponse twiml = new TwiMLResponse();
	Say say = new Say(TwilioConstants.GREETING);
	say.setVoice(TwilioConstants.FEMALE_USER);

	Gather gather = new Gather();
	gather.setAction("/handle-key/" + caller.getDays() + "/" + caller.getHours() + "/" + caller.getMinutes() + "/" + caller.getSeconds() + "/");
	gather.setMethod("GET");
	gather.setFinishOnKey("#");
	Say sayInGather = new Say("Please Press a Number.");
	try {
	    gather.append(sayInGather);
	    twiml.append(say);
	    twiml.append(gather);
	} catch (TwiMLException e) {
	    Logger.error("Error occured during the Fizz-Buzz generator call: " + e);
	}

	return twiml;
    }

    public static boolean callTo(DelayInterface caller) {

	Logger.info("Starting a Call..");
	if (caller instanceof Caller) {
	    Caller c = (Caller) caller;
	    return callTo(c);
	} else if (caller instanceof DirectCaller) {
	    DirectCaller dc = (DirectCaller) caller;
	    return callTo(dc);
	}

	return false;

    }

    private static boolean callTo(DirectCaller caller) {
	String callFrom = caller.callFrom;
	String callTo = caller.callTo;

	try {

	    // Get the account and call factory class
	    Account acct = client.getAccount();
	    CallFactory callFactory = acct.getCallFactory();

	    // build map of post parameters
	    Map<String, String> params = new HashMap<String, String>();
	    String url = "http://e4fa2e.ngrok.com/v1/twilio/directCall/" + caller.fizzBuzzInput;
	    Logger.info("Calling API " + url);
	    params.put("From", callFrom);
	    params.put("To", callTo);
	    params.put("Url", url);
	    params.put("Method", "GET");

	    // Make a phone call ( This makes a POST request to the Calls
	    // resource)
	    callFactory.create(params);
	} catch (TwilioRestException e) {
	    Logger.error(e.getErrorMessage());
	    return false;
	}
	Logger.info("Calling Directly..");
	return true;
    }

    private static boolean callTo(Caller caller) {
	String callFrom = caller.callFrom;
	String callTo = caller.callTo;

	try {

	    // Get the account and call factory class
	    Account acct = client.getAccount();
	    CallFactory callFactory = acct.getCallFactory();

	    // build map of post parameters
	    Map<String, String> params = new HashMap<String, String>();
	    String url = "http://e4fa2e.ngrok.com/v1/twilio/delay/" + caller.getDays() + "/" + caller.getHours() + "/" + caller.getMinutes() + "/" + caller.getSeconds();
	    Logger.info("Calling API " + url);
	    params.put("From", callFrom);
	    params.put("To", callTo);
	    params.put("Url", url);
	    params.put("Method", "GET");

	    // Make a phone call ( This makes a POST request to the Calls
	    // resource)
	    callFactory.create(params);
	} catch (TwilioRestException e) {
	    Logger.error(e.getErrorMessage());
	    return false;
	}
	Logger.info("Calling...");
	return true;
    }

    public static boolean isValidPhoneNumber(String number) {
	if (number == null || number.length() == 0) {
	    return false;
	} else if (!number.matches("^(((\\d-?)?\\d{3}-?)?(\\d{3}-?\\d{4}))$")) {
	    return false;
	}
	return true;
    }

    public static Long getLongFromString(String numberString) {
	String longFrom = removeNonNumericCharacters(numberString);
	try {
	    if (isValidLong(longFrom))
		return Long.parseLong(longFrom);
	} catch (Exception e) {
	    Logger.error(e.toString());
	}
	return null;

    }

    public static boolean isValidLong(String numberString) {
	try {
	    long num = Long.parseLong(numberString);
	    if (num <= 0 || num > Long.MAX_VALUE) {
		return false;
	    }

	    return true;
	} catch (NumberFormatException e) {
	    Logger.error("Not a valid String: " + numberString + " : " + e);
	}

	return false;
    }

    public static String removeNonNumericCharacters(String str) {
	if (str == null)
	    return null;
	return str.replaceAll("[^\\d.]", "");
    }

    public static TwiMLResponse getFizzBizzStarter() {
	return getCallStarter(null);
    }

    /**
     * 
     * Method that starts conversation with the caller
     * 
     * @return {@link TwiMLResponse} sent over the call
     */
    public static TwiMLResponse getCallStarterFizzBUzz(String number) {
	Logger.info("starting direct fizz buzz call...");
	TwiMLResponse twiml = new TwiMLResponse();

	try {
	    FizzBuzz fb = new FizzBuzz(number);
	    String result = fb.getFizzBizzString();

	    Say say = new Say(TwilioConstants.GREETING);
	    say.setVoice(TwilioConstants.FEMALE_USER);
	    Gather gather = new Gather();
	    Say sayInGather = new Say(result);

	    gather.append(sayInGather);
	    twiml.append(say);
	    twiml.append(gather);
	} catch (TwiMLException e) {
	    Logger.error("Error occured during the direct Fizz-Buzz generator call: " + e);
	}

	return twiml;
    }

    /**
     * This method takes in a String and converts it into a TwiMl Response which
     * can be sent to the caller
     * 
     * @param fizzBuzz
     *            {@link String} that needs to sent as Response
     * @return {@link TwiMLResponse} converted to XML-string
     * @throws TwiMLException
     */
    public static String getTwiMlResponseFromString(String str) throws TwiMLException {
	TwiMLResponse twiml = new TwiMLResponse();
	try {
	    Say say = new Say(str);
	    say.setVoice("man");
	    twiml.append(say);

	} catch (TwiMLException e) {
	    Logger.error("Exception Occured while generating TwmiMl from string :" + str + " : " + e);
	    throw new TwiMLException("Exception Occured while generating TwmiMl from string :" + str + " : " + e);
	}
	return twiml.toXML();
    }
}
