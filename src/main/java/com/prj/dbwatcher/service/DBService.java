package com.prj.dbwatcher.service;

import com.opencsv.CSVWriter;
import com.prj.dbwatcher.utils.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

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
    public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public Connection connection;

    public void connectToDB() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            logger.error("MySQL JDBC Driver not found: " + cnfe.getMessage());
        }

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://"
                            + AppProperties.getProps().getProperty(DATABASE_HOST) + ":"
                            + AppProperties.getProps().getProperty(DATABASE_PORT) + "/"
                            + AppProperties.getProps().getProperty(DATABASE_SCHEME),
                    AppProperties.getProps().getProperty(DATABASE_USER),
                    AppProperties.getProps().getProperty(DATABASE_PASSWORD));
        } catch (SQLException se) {
            logger.error("DB connection Failed!: " + se.getMessage());
        }
    }

    public void disconnectFromDB() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.error("Connection close Failed!: " + ex.getMessage());
            }
        }

    }

    public void dumpTableToCsv(String fileName) throws IOException {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            String query = "select * from " + AppProperties.getProps().getProperty(DATABASE_TABLE);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            writer.writeAll(rs, true);
            writer.close();
            rs.close();
            stmt.close();
            logger.info("CSV File is created successfully: " + fileName);
        } catch (SQLException ex) {
            logger.error("Connection close Failed!: " + ex.getMessage());
        }
    }
}
