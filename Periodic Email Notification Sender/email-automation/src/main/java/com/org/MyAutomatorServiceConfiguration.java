package com.org;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

public class MyAutomatorServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private EmailSenderConfiguration emailsender;

    @Valid
    @NotNull
    @JsonProperty
    private ThreadPoolConfiguration threadPool;

    public static class EmailSenderConfiguration{
        private String emailDir;
        private List<String> emailUser = new LinkedList<String>();
        private String emailPassword;
        private String smtpServer;
        private int smtpPort;

        public String getEmailDir() {
            return emailDir;
        }

        public void setEmailDir(String emailDir) {
            this.emailDir = emailDir;
        }

        public List<String> getEmailUser() {
            return emailUser;
        }

        public void setEmailUser(List<String> emailUser) {
            this.emailUser = emailUser;
        }

        public String getEmailPassword() {
            return emailPassword;
        }

        public void setEmailPassword(String emailPassword) {
            this.emailPassword = emailPassword;
        }

        public String getSmtpServer() {
            return smtpServer;
        }

        public void setSmtpServer(String smtpServer) {
            this.smtpServer = smtpServer;
        }

        public int getSmtpPort() {
            return smtpPort;
        }

        public void setSmtpPort(int smtpPort) {
            this.smtpPort = smtpPort;
        }
    }

    public static class ThreadPoolConfiguration {

        @JsonProperty
        private int corePoolSize;

        @JsonProperty
        private int maxPoolSize;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }
    }

    //getters and setters
    public EmailSenderConfiguration getEmailsender() {
        return emailsender;
    }

    public void setEmailsender(EmailSenderConfiguration emailsender) {
        this.emailsender = emailsender;
    }

    public ThreadPoolConfiguration getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolConfiguration threadPool) {
        this.threadPool = threadPool;
    }
}
