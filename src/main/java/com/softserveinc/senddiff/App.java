package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.DBService;

public class App {

    public static void main( String[] args )
    {
        /*PropertiesService propService = new PropertiesService();
        propService.readProperties();*/
        DBService dbService = new DBService();
        dbService.connectToDB();
    }
}
