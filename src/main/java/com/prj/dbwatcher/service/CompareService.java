package com.prj.dbwatcher.service;

import com.google.common.collect.Iterables;
import com.prj.dbwatcher.model.ComparedObject;
import com.prj.dbwatcher.utils.AppProperties;
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
    private List<String> skipIds = null;

    public CompareService() {
        skipIds = new ArrayList<String>(Arrays.asList(AppProperties.getProps().getProperty("dbwatcher.skipIds").split(",")));
    }

    public List<ComparedObject> compareCsvFiles(String firstFile, String secondFile) throws IOException {
        HashSet<String> f1 = null;
        HashSet<String> f2 = null;
        HashSet<String> f2copy = null;

        f1 = new HashSet<String>(FileUtils.readLines(new File(firstFile)));
        f2 = new HashSet<String>(FileUtils.readLines(new File(secondFile)));
        f2copy = new HashSet<String>(FileUtils.readLines(new File(secondFile)));

        f2.removeAll(f1); // f2 now contains only the lines which are not in f1
        f1.removeAll(f2copy); // f1 now contains only the lines which are not in f2
        logger.info("Was: " + f2);
        logger.info("Now: " + f1);

        List<ComparedObject> result = new ArrayList<ComparedObject>();
        Map<Integer, String> oldValues = transformSetToMap(f2);
        Map<Integer, String> newValues = transformSetToMap(f1);
        for (Integer entry : newValues.keySet()) {
            String oldValue = oldValues.get(entry);
            String newValue = newValues.get(entry);
            ComparedObject comparedObject =
                    new ComparedObject(entry, oldValue == null ? "item does not exists" : oldValue, newValue);
            result.add(comparedObject);
        }
        return result;
    }

    private Map<Integer, String> transformSetToMap(HashSet<String> source) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (String line: source) {
           int indexOf = line.indexOf(',');
           String stringifiedId = line.substring(0, indexOf).replace("\"","");

           if (!skipIds.contains(stringifiedId)) {
               try {
                   Integer key = Integer.parseInt(stringifiedId);
                   result.put(key, line.substring(indexOf+1));
               } catch (Exception e) {
                   continue; //found header of the table - skipping it
               }

           }
        }
        return result;
    }

    public void backUpProcessedDump(String newName, String oldName) throws IOException {
        File oldDump = new File(oldName);
        if (oldDump.exists()) oldDump.delete();

        File latestDump = new File(newName);
        latestDump.renameTo(new File(oldName));
    }

    public void createDumpFile(String dumpNewFileName) throws IOException {
        File newDupmPlaceHolder = new File(dumpNewFileName);
        if (!newDupmPlaceHolder.exists()) {
            newDupmPlaceHolder.createNewFile();
        }
    }
}
