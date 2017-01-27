package com.softserveinc.senddiff.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by vvinton on 1/27/2017.
 */
public class AppProperties {
    private static Properties instance = null;
    private static final String FILENAME = "config.properties";
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

    public static Properties getProps() {
        if (instance == null) {
            instance = new Properties();
            try {
                instance.load(new FileInputStream(FILENAME));
            } catch (IOException e) {
                logger.error("config.properties read error" + e.getMessage());
            }
        }
        return instance;
    }
}
