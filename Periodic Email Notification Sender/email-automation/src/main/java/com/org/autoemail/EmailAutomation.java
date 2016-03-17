package com.org.autoemail;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.DumperOptions;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.org.MyAutomatorServiceConfiguration;
import com.org.config.EmailTemplateConfiguration;
import com.org.utils.EmailSender;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;

public class EmailAutomation {
    private final Logger LOGGER = LoggerFactory.getLogger(EmailAutomation.class);
    public MyAutomatorServiceConfiguration conf;

    public EmailAutomation(MyAutomatorServiceConfiguration conf) {
        this.conf = conf;
    }

    public void startSendingEmails() {
        try {
            String dirPath = conf.getEmailsender().getEmailDir();
            File baseFile = new File(dirPath);
            if (baseFile.exists() && baseFile.isDirectory()) {
                File[] directoryListing = baseFile.listFiles();

                if (directoryListing != null) {

                    for (File f : directoryListing) {
                        //Look for Client specific Directories
                        if (f.isDirectory()) {
                            String clientName = f.getName();
                            LOGGER.debug("Processing for client => " + clientName);
                            File[] clientEmailFiles = f.listFiles();
                            if (clientEmailFiles != null) {
                                File emailConfig = null;
                                File emailBody = null;
                                for (File ymlFile : clientEmailFiles) {
                                    //Look for client email files
                                    if (ymlFile.isFile() && ymlFile.getName().endsWith(".yml")) {
                                        emailConfig = ymlFile;
                                    }
                                    if (ymlFile.isFile() && ymlFile.getName().endsWith(".html")) {
                                        emailBody = ymlFile;
                                    }
                                }
                                loadAndSendEmail(emailConfig, emailBody, clientName);
                            }
                        }
                    }

                }
            }
        }catch (Exception e){
            LOGGER.error("Error in startSendingEmails => " + e );
        }
    }


    private void loadAndSendEmail(File emailConfig, File emailBody, String clientName) {
        try {
            boolean sendEmail = true;
            InputStream inputStream = new FileInputStream(emailConfig);

            Yaml ymlLoader = new Yaml();
            EmailTemplateConfiguration emailTemplateConfiguration = ymlLoader.loadAs(inputStream, EmailTemplateConfiguration.class);
            int periodInDays = emailTemplateConfiguration.getPeriodInDays();
            DateTime currentTime = DateTime.now(DateTimeZone.forID("America/Los_Angeles"));
            String lastTimeSent = emailTemplateConfiguration.getLastTimeSent();
            LOGGER.debug("Last email was sent on => " + lastTimeSent);
            LOGGER.debug("Current Time  => " + currentTime);

            if(lastTimeSent != null && !lastTimeSent.isEmpty()) {
                LOGGER.debug("Periodicity for the client is => " + periodInDays);
                DateTime lastTime = new DateTime(lastTimeSent, DateTimeZone.forID("America/Los_Angeles"));
                if(lastTime.plusDays(periodInDays).isAfterNow()){
                    sendEmail = false;
                    LOGGER.debug("Email will NOT be sent for " + clientName);
                }
            }

            if(sendEmail){
                LOGGER.debug("Email will be sent for " + clientName);
                DumperOptions options = new DumperOptions();
                options.setPrettyFlow(true);
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                BufferedReader br = new BufferedReader(new FileReader(emailBody));
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    String everything = sb.toString();
                    EmailSender.sendEmail(everything, emailTemplateConfiguration.getEmailSubject(),
                            conf.getEmailsender().getSmtpServer(),
                            conf.getEmailsender().getSmtpPort(),  conf.getEmailsender().getEmailUser().get(0),
                            conf.getEmailsender().getEmailPassword(),
                            conf.getEmailsender().getEmailUser(), emailTemplateConfiguration.getEmailTo(),
                            emailTemplateConfiguration.getEmailCc(), new LinkedList<String>(), emailTemplateConfiguration.getAttachments());
                } finally {
                    br.close();
                }

                Yaml y = new Yaml(options);
                emailTemplateConfiguration.setLastTimeSent(DateTime.now(DateTimeZone.forID("America/Los_Angeles")).toString());
                StringWriter writer = new StringWriter();

                FileWriter fw = new FileWriter(emailConfig.getAbsolutePath());
                y.dump(emailTemplateConfiguration, fw);
                fw.write(writer.toString());
                fw.close();

            }

        } catch (FileNotFoundException e) {
           LOGGER.error("Error in loadAndSendEmail => " + e );
        } catch (IOException e) {
            LOGGER.error("Error in loadAndSendEmail => " + e);
        } catch (Exception e) {
            LOGGER.error("Error in loadAndSendEmail => " + e);
        }
    }
}
