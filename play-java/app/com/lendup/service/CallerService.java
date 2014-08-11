package com.lendup.service;

import java.util.List;

import play.Logger;
import play.Logger.ALogger;

import com.lendup.persistence.CallerDbPeristence;
import com.lendup.persistence.CallerDbPersistenceImpl;
import com.lendup.twiliocaller.models.Caller;
import com.lendup.twiliocaller.models.DelayInterface;
import com.lendup.twiliocaller.models.DirectCaller;
import com.lendup.twiliocaller.models.FizzBuzz;
import com.lendup.twiliocaller.scheduler.SchedulerUtil;
import com.lendup.utils.Twilio;
import com.twilio.sdk.verbs.TwiMLResponse;

/**
 * Service layer for this app
 * 
 * @author harishs
 * 
 */
public class CallerService {
    public static ALogger logger = Logger.of(CallerService.class);

    private final CallerDbPeristence dbInteraction;

    /**
     * default constructor
     */
    public CallerService() {
	dbInteraction = new CallerDbPersistenceImpl();

    }

    public CallerService(CallerDbPeristence dbInteraction) {
	this.dbInteraction = dbInteraction;
    }

    /**
     * api to initiate a Twilio call and start conversation with the caller
     * 
     * @param callFrom
     *            {@link String} caller number
     * @param callTo
     *            {@link String} number to which the call is made
     * @param days
     *            {@link Long} delay in number of days
     * @param hours
     *            {@link Long} delay in number of hours
     * @param minutes
     *            {@link Long} delay in number of minutes
     * @param seconds
     *            {@link Long} delay in seconds
     * @param digits
     *            {@link String} Digits entered by the user in call
     * 
     * @return {@link TwiMLResponse} which will be heard by our caller
     */
    public TwiMLResponse getCallStarter(String callFrom, String callTo, Long days, Long hours, Long minutes, Long seconds, String digits) {
	logger.debug("In call starter: CALL FROM: " + callFrom + " and CAll TO: " + callTo);
	Caller caller = new Caller(callFrom, callTo, days, hours, minutes, seconds, digits);
	return Twilio.getCallStarter(caller);
    }

    /**
     * Method that generates FIzzBuzz depending upon the number entered by the
     * user on call. An entry is made into the database when fizzbuzz is
     * successfully generated
     * 
     * @param callFrom
     *            {@link String} caller number
     * @param callTo
     *            {@link String} number to which the call is made
     * @param days
     *            {@link Long} delay in number of days
     * @param hours
     *            {@link Long} delay in number of hours
     * @param minutes
     *            {@link Long} delay in number of minutes
     * @param seconds
     *            {@link Long} delay in seconds
     * @param digits
     *            {@link String} Digits entered by the user in call
     * @return {@link String} which is {@link TwiMLResponse} FizzBuzz for the
     *         digits entered by the user
     */
    public String getFizzBuzz(String callFrom, String callTo, Long days, Long hours, Long minutes, Long seconds, String digits, String direction) {
	Caller caller = new Caller(callFrom, callTo, days, hours, minutes, seconds, digits);
	FizzBuzz fb = new FizzBuzz(digits);
	String result;
	try {
	    result = fb.getFizzBuzzTwiMlResponse();
	    if (result != null && !direction.equalsIgnoreCase("inbound")) {
		saveToDb(caller);
	    }
	} catch (Exception e) {
	    Logger.error("Error occured: " + e);
	    return e.getMessage();
	}

	return result;
    }

    /**
     * service that handles scheduling of a Twilio call
     * 
     * @param caller
     *            implementation of {@link DelayInterface} that will help in
     *            getting the duration for scheduling
     */
    public void scheduleCall(DelayInterface caller) {
	try {
	    Logger.info("Scheduling...");
	    SchedulerUtil.scheduleTwilioCall(caller);
	} catch (Exception e) {
	    Logger.error("Error in schedule Call: " + e);
	}
	Logger.info("Scheduled");
    }

    /**
     * Repeats the call that was scheduled by the user in the past. Time time,
     * once the call is connected, Fizz buzz is read directly for the number
     * that was entered by the user in the past call
     * 
     * @param callId
     *            {@link Long} Past call id used to get {@link Caller} entry
     *            from db
     */
    public void callDirectlyForTwiMl(long callId) {
	Caller caller = dbInteraction.findById(callId);
	String callFrom = caller.callFrom;
	String callTo = caller.callTo;
	String fizzBuzzInput = caller.digits;
	DirectCaller dc = new DirectCaller(callFrom, callTo, fizzBuzzInput);
	scheduleCall(dc);
    }

    /**
     * persist the {@link Caller} information in db
     * 
     * @param caller
     */
    public void saveToDb(Caller caller) {
	dbInteraction.saveInDb(caller);
    }

    /**
     * Get a list of all the calls that were scheduled successfuly by the user.
     * Queries the db to get the list
     * 
     * @return {@link List} of {@link Caller} calls made in the past
     */
    public List<Caller> getHistoryCalls() {
	Logger.info("Get History...");
	return dbInteraction.getList();
    }

}
