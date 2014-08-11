package com.lendup.twiliocaller.scheduler;

import play.Logger;
import akka.actor.Actor;
import akka.actor.UntypedActor;

import com.lendup.twiliocaller.models.DelayInterface;
import com.lendup.utils.Twilio;

/**
 * {@link Actor} class that handles the call that gets scheduled by the user
 * 
 * @author harishs
 * 
 */
public class MyActor extends UntypedActor {

    @Override
    public void onReceive(Object obj) throws Exception {
	if (obj instanceof DelayInterface) {
	    DelayInterface cObj = (DelayInterface) obj;
	    boolean returnVal = Twilio.callTo(cObj);
	    if (!returnVal)
		Logger.error("Call to Twilio failed");
	}

    }

}
