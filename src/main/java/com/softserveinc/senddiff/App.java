package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class App {
    private static final String LOOPON = "loopon";
    private static final String NOTIFY_EMAIL = "EMAIL";
    private static final String NOTIFY_API = "API";
    private static final String NOTIFY_EMAIL_API = "EMAIL_API";
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args) throws Exception
    {
        if (LOOPON.equalsIgnoreCase(args[0])) {
            Integer interval = Integer.parseInt(AppProperties.getProps().getProperty("senddiff.run.interval"));
            logger.info("Application started in loop mode with run interval:" + interval);
            while(true) {
                runService();
                logger.info("Application loop completed. Invoking notification.");
                Thread.sleep(interval);
            }
        } else {
            runService();
            logger.info("Application loop completed. Invoking notification.");
        }
    }

    private static void runService() {
        DBService dbService = new DBService();
        dbService.connectToDB();

        CompareService compareService = new CompareService();

        try {
            compareService.renameFile("test_new.csv", "test_old.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbService.tableToCsv("test_new.csv");
        dbService.closeConnection();

        Set<String> resutls = compareService.compareCsvFiles("test_new.csv", "test_old.csv");

        if (NOTIFY_EMAIL.equalsIgnoreCase(AppProperties.getProps().getProperty("senddiff.send.method"))) {
            new SendEmailService().sendEmail("Updates on db sync run on:" + new Date(), resutls.toString());
        };

        if (NOTIFY_API.equalsIgnoreCase(AppProperties.getProps().getProperty("senddiff.send.method"))) {
            new InvokeAPIService().invokeApi(resutls.toString());
        };

        if (NOTIFY_EMAIL_API.equalsIgnoreCase(AppProperties.getProps().getProperty("senddiff.send.method"))) {
            new SendEmailService().sendEmail("Updates on db sync run on:" + new Date(), resutls.toString());
            new InvokeAPIService().invokeApi(resutls.toString());
        };

    }
}
