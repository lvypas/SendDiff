package com.softserveinc.senddiff.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Created by vvinton on 1/27/2017.
 */
public class InvokeAPIService {
    private static final Logger logger = LoggerFactory.getLogger(InvokeAPIService.class);
    public static String POST = "POST";

    public void invokeApi(String messageText) {

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = null;

            if (POST.equalsIgnoreCase(AppProperties.getProps().getProperty("api.httpMethod"))) {
                HttpPost httppost = new HttpPost(AppProperties.getProps().getProperty("api.baseUrl"));
                httppost.addHeader("content-type", "application/json");
                StringEntity params = new StringEntity(messageText);
                httppost.setEntity(params);
                response = httpclient.execute(httppost);
            } else {
                throw new RuntimeException("API method call not supported");
            }

            logger.info("Invoke API successfully: " + response.getStatusLine().toString());
        } catch (Exception e) {
            logger.info("Invoke API failed: " + e.getMessage());
        }
    }

    public static void main( String[] args ) {
        new InvokeAPIService().invokeApi("{\"id\":\"0\"}");
    }
}
