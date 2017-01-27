package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.CompareService;
import com.softserveinc.senddiff.service.DBService;

import java.io.IOException;

public class App {

    public static void main( String[] args )
    {
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
