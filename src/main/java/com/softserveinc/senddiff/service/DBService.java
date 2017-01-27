package com.softserveinc.senddiff.service;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by lvypas on 26.01.2017.
 */
public class DBService {

    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private static final String DATABASE_HOST = "db.database.host";
    private static final String DATABASE_PORT = "db.database.port";
    private static final String DATABASE_SCHEME = "db.database.scheme";
    private static final String DATABASE_USER = "db.database.user";
    private static final String DATABASE_PASSWORD = "db.database.password";
    private static final String DATABASE_TABLE = "db.database.table";
    private static final String FILENAME = "config.properties";
    public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public Properties dbProperties;
    public Connection connection;

    public void getPropersties() {
        dbProperties = new Properties();
        try {
            dbProperties.load(new FileInputStream(FILENAME));
        } catch (IOException e) {
            throw new RuntimeException("Properties file not found");
        }
    }

    public void connectToDB() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            logger.error("MySQL JDBC Driver not found: " + cnfe.getMessage());
        }
        getPropersties();
        try {
            connection =  DriverManager.getConnection(
                    "jdbc:mysql://" + dbProperties.getProperty(DATABASE_HOST) +":"+
                            dbProperties.getProperty(DATABASE_PORT) + "/" + dbProperties.getProperty(DATABASE_SCHEME),
                    dbProperties.getProperty(DATABASE_USER), dbProperties.getProperty(DATABASE_PASSWORD));
        } catch (SQLException se) {
            logger.error("DB connection Failed!: " + se.getMessage());
        }
    }

    public void closeConnection(){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.error("Connection close Failed!: " + ex.getMessage());
            }
        }

    }

    public void tableToCsv(String fileName) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter((fileName)));
            String query = "select * from "+ dbProperties.getProperty(DATABASE_TABLE);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            writer.writeAll(rs, true);
            writer.close();
            logger.info("CSV File is created successfully: " + fileName);
        } catch (Exception ex) {
            logger.error("File creation failed!: " + ex.getMessage());
        }
    }
}
