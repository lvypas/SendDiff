package com.softserveinc.senddiff.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.HashSet;
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
            FileWriter fw = new FileWriter(fileName);
            String query = "select * from "+ dbProperties.getProperty(DATABASE_TABLE);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                fw.append(rs.getString(1));
                fw.append(',');
                fw.append(rs.getString(2));
                fw.append(',');
                fw.append(rs.getString(3));
                fw.append(',');
                fw.append(rs.getString(4));
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            logger.info("CSV File is created successfully: ");
        } catch (Exception ex) {
            logger.error("File creation failed!: " + ex.getMessage());
        }
    }

    public String compareCsvFiles(String firstFile, String secondFile){
        String diff = "";
        // using FileUtils to read in the files.
        HashSet<String> f1 = null;
        HashSet<String> f2 = null;
        try {
            f1 = new HashSet<String>(FileUtils.readLines(new File(firstFile)));
            f2 = new HashSet<String>(FileUtils.readLines(new File(secondFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        f2.removeAll(f1); // f2 now contains only the lines which are not in f1
        diff = f2.toString();
        System.out.println(diff);
        return diff;
    }

}
