package com.softserveinc.senddiff;


import com.softserveinc.senddiff.service.DBService;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main( String[] args )
    {
        DBService dbService = new DBService();
        dbService.connectToDB();
        try {
            renameFile("test_new.csv", "test_old.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbService.tableToCsv("test_new.csv");
        String diff = dbService.compareCsvFiles("test_new.csv", "test_old.csv");
        dbService.closeConnection();
    }

    public static void renameFile(String oldName, String newName) throws IOException {
        File srcFile = new File(oldName);
        boolean bSucceeded = false;
        try {
            File destFile = new File(newName);
            if (destFile.exists()) {
                if (!destFile.delete()) {
                    throw new IOException(oldName + " was not successfully renamed to " + newName);
                }
            }
            if (!srcFile.renameTo(destFile))        {
                throw new IOException(oldName + " was not successfully renamed to " + newName);
            } else {
                bSucceeded = true;
            }
        } finally {
            if (bSucceeded) {
                srcFile.delete();
            }
        }
    }
}
