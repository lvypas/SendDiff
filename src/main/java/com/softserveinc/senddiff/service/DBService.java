package com.softserveinc.senddiff.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;

/**
 * Created by lvypas on 26.01.2017.
 */
public class DBService {

    PropertiesService propertiesService;
    Connection connection;

    public DBService() {
        propertiesService = new PropertiesService();
        propertiesService.readProperties();
    }

    public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public Connection connectToDB() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found");
            e.printStackTrace();
        }

        System.out.println("MySQL JDBC Driver Registered!");

        try {
            connection =  DriverManager.getConnection(
                    "jdbc:mysql://" + propertiesService.databaseUri + ":3306/" + propertiesService.database,
                    propertiesService.dbuser,
                    propertiesService.dbpassword);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

        if (connection == null) {
            System.out.println("Failed to make connection!");
        }
        return connection;
    }

    public void closeConnection(){
        if(connection !=null){
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void tableToCsv(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            String query = "select * from "+ propertiesService.table;
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
            System.out.println("CSV File is created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
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
