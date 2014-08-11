package com.lendup.constants;

import play.Play;

/**
 * class to store all the constants related to Twilio calls
 * 
 * @author harishs
 * 
 */
public class TwilioConstants {

    public static String GREETING = "Hello User";
    public static String MALE_USER = "male";
    public static String FEMALE_USER = "female";
    public static String TWILIO_SIGNATURE_KEY = "X-Twilio-Signature";
    public static String SSID = Play.application().configuration().getString("user.ssid");
    public static String AUTH_KEY = Play.application().configuration().getString("user.token");;

    public static final long SECONDS_MULTIPLIER = 1;
    public static final long MINUTES_MULTIPLIER = 60 * SECONDS_MULTIPLIER;
    public static final long HOURS_MULTIPLIER = 60 * MINUTES_MULTIPLIER;
    public static final long DAYS_MULTIPLIER = 24 * HOURS_MULTIPLIER;

    /*
     * Twilio Request Parameters
     */
    public static String CALLER = "Caller";
    public static String CALL_TO = "Called";
    public static String DIGITS_ENTERED = "Digits";
    public static String CALL_DIRECTION = "Direction";

}
