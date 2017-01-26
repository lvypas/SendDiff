package com.softserveinc.senddiff.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lvypas on 26.01.2017.
 */
public class PropertiesService {

    private static final String DATABASE = "database";
    private static final String DBUSER = "dbuser";
    private static final String DBPASSWORD = "dbpassword";
    private static final String TABLE = "table";
    private static final String EMAIL = "email";
    private static final String API_ADDRESS = "api";
    private static final String FILENAME = "config.properties";

    private String database;
    private String dbuser;
    private String dbpassword;
    private String table;
    private String email;
    private String apiAddress;

    public String getDatabase() {
        return database;
    }

    private void setDatabase(String database) {
        this.database = database;
    }

    public String getDbuser() {
        return dbuser;
    }

    private void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    private void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public String getTable() {
        return table;
    }

    private void setTable(String table) {
        this.table = table;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    private void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    public void readProperties() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(FILENAME);

            prop.load(input);

            setDatabase(prop.getProperty(DATABASE));
            setDbuser(prop.getProperty(DBUSER));
            setDbpassword(prop.getProperty(DBPASSWORD));
            setTable(prop.getProperty(TABLE));
            setEmail(prop.getProperty(EMAIL));
            setApiAddress(prop.getProperty(API_ADDRESS));

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
