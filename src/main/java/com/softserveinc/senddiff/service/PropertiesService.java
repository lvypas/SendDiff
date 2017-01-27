package com.softserveinc.senddiff.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lvypas on 26.01.2017.
 */
public class PropertiesService {

    private static final String DATABASE_URI = "database_uri";
    private static final String DATABASE = "database";
    private static final String DBUSER = "dbuser";
    private static final String DBPASSWORD = "dbpassword";
    private static final String TABLE = "table";
    private static final String EMAIL = "email";
    private static final String API_ADDRESS = "api";
    private static final String FILENAME = "config.properties";

    public String databaseUri;
    public String database;
    public String dbuser;
    public String dbpassword;
    public String table;
    public String email;
    public String apiAddress;

    public void readProperties() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(FILENAME);

            prop.load(input);

            databaseUri = prop.getProperty(DATABASE_URI);
            database = prop.getProperty(DATABASE);
            dbuser = prop.getProperty(DBUSER);
            dbpassword = prop.getProperty(DBPASSWORD);
            table = prop.getProperty(TABLE);
            email = prop.getProperty(EMAIL);
            apiAddress = prop.getProperty(API_ADDRESS);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
