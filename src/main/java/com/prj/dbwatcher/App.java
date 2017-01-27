package com.prj.dbwatcher;


import com.prj.dbwatcher.service.*;
import com.prj.dbwatcher.model.ComparedObject;
import com.prj.dbwatcher.service.notification.impl.InvokeAPIService;
import com.prj.dbwatcher.service.notification.impl.SendEmailService;
import com.prj.dbwatcher.utils.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class App {
    private static final String LOOPON = "loopOn";
    private static final String NOTIFY_EMAIL = "EMAIL";
    private static final String NOTIFY_API = "API";
    private static final String NOTIFY_EMAIL_API = "EMAIL_API";

    private static final String DUMP_OLD_FILE_NAME = "previous_table_snapshot.csv";
    private static final String DUMP_NEW_FILE_NAME = "current_table_snapshot.csv";

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args) throws Exception {
        if (args.length > 0 && LOOPON.equalsIgnoreCase(args[0])) {
            Integer interval = Integer.parseInt(AppProperties.getProps().getProperty("dbwatcher.runInterval"));
            logger.info("Application started in loop mode with run interval:" + interval);
            while(true) {
                runService();
                logger.info("Run loop completed. Invoking notification.");
                Thread.sleep(interval);
            }
        } else {
            runService();
            logger.info("Run loop completed.");
        }
    }

    private static void runService() throws Exception {
        DBService dbService = new DBService();
        dbService.connectToDB();

        CompareService compareService = new CompareService();

        //Important sequence
        compareService.createDumpFile(DUMP_NEW_FILE_NAME);
        compareService.createDumpFile(DUMP_OLD_FILE_NAME);
        dbService.dumpTableToCsv(DUMP_NEW_FILE_NAME);
        List<ComparedObject> resutls = compareService.compareCsvFiles(DUMP_NEW_FILE_NAME, DUMP_OLD_FILE_NAME);
        compareService.backUpProcessedDump(DUMP_NEW_FILE_NAME, DUMP_OLD_FILE_NAME);
        // finished

        String sendMethod = AppProperties.getProps().getProperty("dbwatcher.sendMethod");
        if (NOTIFY_EMAIL.equalsIgnoreCase(sendMethod)) {
            new SendEmailService().notify(resutls);
        };

        if (NOTIFY_API.equalsIgnoreCase(sendMethod)) {
            new InvokeAPIService().notify(resutls);
        };

        if (NOTIFY_EMAIL_API.equalsIgnoreCase(sendMethod)) {
            new SendEmailService().notify(resutls);
            new InvokeAPIService().notify(resutls);
        };
        dbService.disconnectFromDB();
    }
}
