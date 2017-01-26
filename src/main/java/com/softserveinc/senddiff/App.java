package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.PropertiesService;

public class App {

    public static void main( String[] args )
    {
        PropertiesService propService = new PropertiesService();
        propService.readProperties();
    }
}
