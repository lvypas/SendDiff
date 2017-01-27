package com.softserveinc.senddiff.service;

import java.io.FileWriter;
import java.sql.*;

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
                fw.append(rs.getString(3));
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
