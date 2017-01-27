package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.DBService;

public class App {

    public static void main( String[] args )
    {
        DBService dbService = new DBService();
        dbService.connectToDB();
        dbService.tableToCsv("test.csv");
        dbService.closeConnection();
    }
}
