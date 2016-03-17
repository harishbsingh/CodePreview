package com.org.config;

import java.util.LinkedList;
import java.util.List;

public class EmailTemplateConfiguration {

    private List<String> emailTo = new LinkedList<String>();
    private List<String> emailCc = new LinkedList<String>();
    private int periodInDays;
    private String lastTimeSent;
    private String emailSubject;
    private List<String> attachments = new LinkedList<String>();

    public List<String> getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(List<String> emailTo) {
        this.emailTo = emailTo;
    }

    public List<String> getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(List<String> emailCc) {
        this.emailCc = emailCc;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public int getPeriodInDays() {
        return periodInDays;
    }

    public void setPeriodInDays(int periodInDays) {
        this.periodInDays = periodInDays;
    }

    public String getLastTimeSent() {
        return lastTimeSent;
    }

    public void setLastTimeSent(String lastTimeSent) {
        this.lastTimeSent = lastTimeSent;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

}
