package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.AppProperties;
import com.softserveinc.senddiff.service.CompareService;
import com.softserveinc.senddiff.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {
    private static final String LOOPON = "loopon";
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

        compareService.compareCsvFiles("test_new.csv", "test_old.csv");
        dbService.closeConnection();
    }
}
