package com.lendup.twiliocaller.scheduler;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.lendup.twiliocaller.models.DelayInterface;

/**
 * Scheduler class that handles scheduling of Twilio call after the Duration
 * specified by the Twilio user
 * 
 * @author harishs
 * 
 */
public class SchedulerUtil {

    private static final ActorSystem system = ActorSystem.create("helloakka");
    private static final ActorRef actor = system.actorOf(Props.create(MyActor.class));

    /**
     * schedules a twilio call. The call is made after the delay time
     * 
     * @param caller
     *            implementation of {@link DelayInterface} which will provide
     *            the Total delay
     */
    public static void scheduleTwilioCall(DelayInterface caller) {
	system.scheduler().scheduleOnce(Duration.create(caller.getTotalDelayInSeconds(), TimeUnit.SECONDS), actor, caller, system.dispatcher(), null);
    }

}
