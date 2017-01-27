package com.softserveinc.senddiff.service;

import com.softserveinc.senddiff.ComparedObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by lvypas on 27.01.2017.
 */
public class CompareService {

    private static final Logger logger = LoggerFactory.getLogger(CompareService.class);

    public List<ComparedObject> compareCsvFiles(String firstFile, String secondFile){
        HashSet<String> f1 = null;
        HashSet<String> f2 = null;
        HashSet<String> f2copy = null;
        try {
            f1 = new HashSet<String>(FileUtils.readLines(new File(firstFile)));
            f2 = new HashSet<String>(FileUtils.readLines(new File(secondFile)));
            f2copy = new HashSet<String>(FileUtils.readLines(new File(secondFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        f2.removeAll(f1); // f2 now contains only the lines which are not in f1
        f1.removeAll(f2copy); // f1 now contains only the lines which are not in f2
        logger.info("Was: " + f2);
        logger.info("Now: " + f1);

        List<ComparedObject> result = new ArrayList<ComparedObject>();
        Map<Integer, String> oldValues = transformSetToMap(f2);
        Map<Integer, String> newValues = transformSetToMap(f1);
        for (Integer entry : oldValues.keySet()) {
            ComparedObject comparedObject = new ComparedObject(entry, oldValues.get(entry), newValues.get(entry));
            result.add(comparedObject);
        }
        return result;
    }

    private Map<Integer, String> transformSetToMap(HashSet<String> source) {
        Iterator<String> iterator = source.iterator();
        Map<Integer, String> result = new HashMap<Integer, String>();
        while (iterator.hasNext()) {
           String line = iterator.next();
           // TODO: create map fro set
        }
        return result;
    }

    public void renameFile(String oldName, String newName) throws IOException {
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
