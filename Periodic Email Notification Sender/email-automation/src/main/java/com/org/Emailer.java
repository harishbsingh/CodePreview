package com.org;

import com.org.autoemail.EmailAutomation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class Emailer {

    private final Logger LOGGER = LoggerFactory.getLogger(Emailer.class);
    private final MyAutomatorServiceConfiguration conf;
    AutomatorThreadPool automatorThreadPool;

    public Emailer(MyAutomatorServiceConfiguration conf, AutomatorThreadPool automatorThreadPool){
        this.conf = conf;
        this.automatorThreadPool = automatorThreadPool;
        LOGGER.debug("Starting alert audit logger...");
        this.automatorThreadPool.scheduleAtFixedRate(new EmailSenderThread(), 0L, 1, TimeUnit.HOURS);
    }


    class EmailSenderThread implements Runnable {
        private final Logger LOGGER = LoggerFactory.getLogger(EmailSenderThread.class);
        private boolean shutdown = false;
        ;
        @Override
        public void run() {
                try {
                    LOGGER.debug(" Starting new email sending cycle...");
                    EmailAutomation emailAutomation = new EmailAutomation(conf);
                    emailAutomation.startSendingEmails();
                    LOGGER.debug(" Email sending cycle has come to an end");
                } catch (Exception e) {
                   LOGGER.error("Exception in the Email Sender => " +  e);
                }
            LOGGER.info("Email Sender thread exit");
        }

        public boolean isShutdown(){
            return shutdown;
        }

        public void setShutdown(boolean shutdown){
            this.shutdown = shutdown;
        }
    }

}