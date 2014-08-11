package com.lendup.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import com.lendup.constants.TwilioConstants;
import com.lendup.service.CallerService;
import com.lendup.twiliocaller.models.Caller;
import com.lendup.twiliocaller.models.TwilioSignatureAuthenticator;
import com.lendup.utils.Twilio;
import com.twilio.sdk.verbs.TwiMLResponse;

/**
 * The Controller Class
 * 
 * @author harishs
 * 
 */
public class TwilioApplication extends Controller {

    public static Logger logger = LoggerFactory.getLogger(TwilioApplication.class);

    /**
     * The root url of the application Hits this Api. This method is responsible
     * for talking to a caller, in a Twilio call. The following action happens
     * when the call hits this api:<br>
     * 1. A voice prompt indicates for the caller to enter a number.<br>
     * 2. The correct output of the FizzBuzz game (starting at 1 and ending with
     * the entered number) is read back to the caller. <br>
     * 
     * Additionally, Authentication is made using the
     * {@link TwilioSignatureAuthenticator} which checks if there is a Twilio
     * signature present in the Request Header
     * 
     * @return {@link Result} {@link TwiMLResponse} xml
     */
    @Authenticated(TwilioSignatureAuthenticator.class)
    public static Result twilioVoiceCall() {
	return twilioVoiceCallWithDelay(null, null, null, null);
    }

    /**
     * This method is responsible for talking to a caller, in a Twilio call,
     * when there is a Delay specified by the user. The following action happens
     * when the call hits this api:<br>
     * 1. A voice prompt indicates for the caller to enter a number.<br>
     * 2. The correct output of the FizzBuzz game (starting at 1 and ending with
     * the entered number) is read back to the caller. <br>
     * 
     * Additionally, Authentication is made using the
     * {@link TwilioSignatureAuthenticator} which checks if there is a Twilio
     * signature present in the Request Header
     * 
     * @param days
     *            {@link Long} delay in days
     * @param hours
     *            {@link Long} delay in hours
     * @param minutes
     *            {@link Long} delay in minutes
     * @param seconds
     *            {@link Long} delay in seconds
     * 
     * @return {@link Result} {@link TwiMLResponse} xml
     */
    @Authenticated(TwilioSignatureAuthenticator.class)
    public static Result twilioVoiceCallWithDelay(Long days, Long hours, Long minutes, Long seconds) {
	logger.info("Call reached to Twilio Voice URL");
	DynamicForm requestData = Form.form().bindFromRequest();
	String digits = requestData.get(TwilioConstants.DIGITS_ENTERED);
	logger.debug("Digit entered by the user: " + digits);

	String caller = requestData.get(TwilioConstants.CALLER);
	logger.debug("Caller: " + caller);

	String callTo = requestData.get(TwilioConstants.CALL_TO);
	logger.debug("Call TO: " + callTo);

	CallerService service = new CallerService();
	TwiMLResponse twiMl = service.getCallStarter(caller, callTo, days, hours, minutes, seconds, digits);

	ctx().response().setContentType("application/xml");
	return ok(twiMl.toXML());

    }

    /**
     * This api actually calls to the algorithm for generating the TwiMl
     * response <br>
     * The correct output of the FizzBuzz game (starting at 1 and ending with
     * the entered number) is read back to the caller. <br>
     * 
     * Additionally, Authentication is made using the
     * {@link TwilioSignatureAuthenticator} which checks if there is a Twilio
     * signature present in the Request Header
     * 
     * @return {@link Result} {@link TwiMLResponse}
     */
    @Authenticated(TwilioSignatureAuthenticator.class)
    public static Result playFizzBuzz(Long days, Long hours, Long minutes, Long seconds) {

	logger.debug("Finding find FizzBuzz...");
	DynamicForm requestData = Form.form().bindFromRequest();
	String digits = requestData.get(TwilioConstants.DIGITS_ENTERED);
	logger.debug("Digit entered by the user: " + digits);

	String callFrom = requestData.get(TwilioConstants.CALLER);
	logger.debug("Call From: " + callFrom);

	String callTo = requestData.get(TwilioConstants.CALL_TO);
	logger.debug("Call TO: " + callTo);

	String direction = requestData.get(TwilioConstants.CALL_DIRECTION);
	logger.debug("Call Direction: " + direction);

	CallerService service = new CallerService();
	String fizzBuzz = service.getFizzBuzz(callFrom, callTo, days, hours, minutes, seconds, digits, direction);

	logger.debug("FizBuzz generated for number " + digits + " is: " + fizzBuzz);

	return ok(fizzBuzz);

    }

    /**
     * 
     * @return returns a Response with error status 403. This happens when
     *         authentication fails
     */
    public static Result forbiddenUrl() {
	return forbidden("Fobidden");
    }

    /**
     * Renders the {@link Form} which asks the user to enter the caller number
     * (callFrom) and calling number (callTo).
     * 
     * @return
     */
    public static Result callFromBrowser() {
	return ok(views.html.caller.render(Form.form(Caller.class)));
    }

    /**
     * Makes a phone call, from the web-browser, to the specified number
     * 
     * @return {@link Result}
     */
    public static Result callScheduler() throws Exception {

	logger.info("Calling from Browser");
	Form<Caller> formData = Form.form(Caller.class).bindFromRequest();
	if (formData.hasErrors()) {
	    return badRequest(views.html.caller.render(formData));
	}
	Caller caller = (Caller) formData.get();
	try {
	    CallerService service = new CallerService();
	    service.scheduleCall(caller);
	} catch (Exception e) {
	    return internalServerError();
	}
	return ok(views.html.BackButton.render());
    }

    /**
     * Home page for the application
     */
    public static Result GO_HOME = redirect(com.lendup.controllers.routes.TwilioApplication.callFromBrowser());

    /**
     * api to get call history
     * 
     * 
     * @return {@link Result} which renders CallHistory.scala.html to show the
     *         list of all calls made
     */
    public static Result getCallRecords() {
	logger.debug("Getting history...");
	CallerService service = new CallerService();

	List<Caller> list = service.getHistoryCalls(); // Caller.getCallRecords();
	return ok(views.html.callHistory.render(list));
    }

    /**
     * This api is used when the user asks for fizzBuzz of a call that he made
     * in the past.
     * 
     * @param id
     *            {@link Long} id for the record stored in the database
     * @return
     */
    public static Result getFizzBuzzDirectly(Long id) {
	logger.debug("Get Direct Fizz Buzz  ");

	CallerService service = new CallerService();
	service.callDirectlyForTwiMl(id);
	/*
	 * DirectCaller dc = new DirectCaller(); dc.callDirectly(id);
	 */
	return ok(views.html.BackButton.render());
    }

    /**
     * This api returns a fizzbuzz {@link TwiMLResponse}, when the user asks for
     * fizzBuzz of a call that he made in the past.
     * 
     * @param number
     *            is the digigts entered by the user in the past record, the
     *            {@link TwiMLResponse} of which he is querying for
     * 
     * @return {@link Result} {@link TwiMLResponse}
     */
    @Authenticated(TwilioSignatureAuthenticator.class)
    public static Result getTwimlDirectly(String number) {
	play.Logger.debug("Get Direct Fizz Buzz  ");
	TwiMLResponse twiMl = Twilio.getCallStarterFizzBUzz(number);
	ctx().response().setContentType("application/xml");
	return ok(twiMl.toXML());
    }

}
