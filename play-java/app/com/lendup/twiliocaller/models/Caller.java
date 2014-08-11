package com.lendup.twiliocaller.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import play.Logger;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.lendup.constants.TwilioConstants;
import com.lendup.utils.Twilio;

/**
 * Model for recording the calls scheduled. Uses {@link Ebean}
 * 
 * @author harishs
 * 
 */
@Entity(name = "Scheduled Call History")
public class Caller extends Model implements DelayInterface {

    @Version
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Required
    public String callFrom;

    @Required
    public String callTo;

    public Long days;

    public Long hours;

    public Long minutes;

    public Long seconds;

    @Column(nullable = false)
    public String digits;

    public static Finder<Long, Caller> find = new Finder<Long, Caller>(Long.class, Caller.class);

    /**
     * constructor
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
     */
    public Caller(String caller, String callTo, Long days, Long hours, Long minutes, Long seconds, String digits) {
	super();
	callFrom = caller;
	this.callTo = callTo;
	this.days = days == null ? 0L : days;
	this.hours = hours == null ? 0L : hours;
	this.minutes = minutes == null ? 0 : minutes;
	this.seconds = seconds == null ? 0 : seconds;
	this.digits = digits == null ? "" : digits;
    }

    /**
     * Internally called by the play framework to valid the {@link Form} data
     * 
     * @return null if no errors, otherwise returns a {@link List} of
     *         {@link ValidationError}
     */
    public List<ValidationError> validate() {
	List<ValidationError> errors = new ArrayList<ValidationError>();
	if (!Twilio.isValidPhoneNumber(callFrom)) {
	    errors.add(new ValidationError("callFrom", "Enter a valid callFrom Number"));
	}

	if (!Twilio.isValidPhoneNumber(callTo)) {
	    errors.add(new ValidationError("callTo", "Enter Call To number"));
	}

	String errorMsg = "cannot be negative";
	if (days != null && days < 0)
	    errors.add(new ValidationError("days", "Days" + errorMsg));

	if (hours != null && hours < 0)
	    errors.add(new ValidationError("hours", "Hours" + errorMsg));

	if (minutes != null && minutes < 0)
	    errors.add(new ValidationError("minutes", "Minutes" + errorMsg));

	if (seconds != null && seconds < 0)
	    errors.add(new ValidationError("seconds", "Seconds" + errorMsg));

	Logger.info("Call To: " + callTo);
	Logger.info("Call From: " + callFrom);
	Logger.info("Days: " + days);
	Logger.info("Minutes: " + minutes);
	Logger.info("Hours: " + hours);
	Logger.info("Seconds: " + seconds);
	return errors.isEmpty() ? null : errors;
    }

    /**
     * @return {@link Long} total delay in seconds
     */
    @Override
    public long getTotalDelayInSeconds() {
	long delay = 0;
	if (days != null)
	    delay += days * TwilioConstants.DAYS_MULTIPLIER;
	if (hours != null)
	    delay += hours * TwilioConstants.HOURS_MULTIPLIER + minutes;
	if (minutes != null)
	    delay += minutes * TwilioConstants.MINUTES_MULTIPLIER;
	if (seconds != null)
	    delay += seconds;
	return delay;
    }

    /**
     * 
     * @return delay in days
     */
    public Long getDays() {
	return days == null ? 0 : days;
    }

    /**
     * setter for delay in days
     * 
     * @param days
     */
    public void setDays(Long days) {
	this.days = days;
    }

    /**
     * 
     * @return delay in hours
     */
    public Long getHours() {
	return hours == null ? 0 : hours;
    }

    /**
     * setter for delay in hours
     * 
     * @param hours
     */
    public void setHours(Long hours) {
	this.hours = hours;
    }

    /**
     * 
     * @return delay in minutes
     */
    public Long getMinutes() {
	return minutes == null ? 0 : minutes;
    }

    /**
     * setter for delay in minutes
     * 
     * @param minutes
     */
    public void setMinutes(Long minutes) {
	this.minutes = minutes;
    }

    /**
     * 
     * @return delay in seconds
     */
    public Long getSeconds() {
	return seconds == null ? 0 : seconds;
    }

    /**
     * setter for delay in seconds
     * 
     * @param seconds
     */
    public void setSeconds(Long seconds) {
	this.seconds = seconds;
    }

    /**
     * @return the callFrom
     */
    public String getCallFrom() {
	return callFrom;
    }

    /**
     * @param callFrom
     *            the callFrom to set
     */
    public void setCallFrom(String callFrom) {
	this.callFrom = callFrom;
    }

    /**
     * @return the callTo
     */
    public String getCallTo() {
	return callTo;
    }

    /**
     * @param callTo
     *            the callTo to set
     */
    public void setCallTo(String callTo) {
	this.callTo = callTo;
    }

    /**
     * @return the digits
     */
    public String getDigits() {
	return digits;
    }

    /**
     * @param digits
     *            the digits to set
     */
    public void setDigits(String digits) {
	this.digits = digits;
    }

}
