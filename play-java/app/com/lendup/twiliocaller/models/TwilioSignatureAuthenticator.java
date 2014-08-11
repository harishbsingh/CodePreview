package com.lendup.twiliocaller.models;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security.Authenticator;

import com.lendup.constants.TwilioConstants;

/**
 * {@link Authenticator} class that intercepts the incoming call and does the
 * authentication
 * 
 * @author harishs
 * 
 */
public class TwilioSignatureAuthenticator extends Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
	String token = getTokenFromHeader(ctx);
	if (token != null) {
	    return token;
	}
	return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
	return Results.redirect(com.lendup.controllers.routes.TwilioApplication.forbiddenUrl());
    }

    /**
     * Authentication is done on the basis of presence of Twilio Signature. We
     * can add proper session management here to handle this better. The
     * application does not have a sign in page which hels us to authenticate if
     * the user is valid and/or if the user has already logged in
     * 
     * @param ctx
     * @return
     */
    private String getTokenFromHeader(Http.Context ctx) {
	String[] authTokenHeaderValues = ctx.request().headers().get(TwilioConstants.TWILIO_SIGNATURE_KEY);
	if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
	    return authTokenHeaderValues[0];
	}
	return null;
    }

}
