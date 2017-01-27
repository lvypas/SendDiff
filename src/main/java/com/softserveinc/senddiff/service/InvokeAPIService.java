package com.softserveinc.senddiff.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
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
    public static String GET = "GET";

    public void invokeApi(Map<String, String> params) {

        Properties apiProperties = new Properties();

        try {
            apiProperties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Properties file not found");
        }

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = null;

            if (POST.equalsIgnoreCase(apiProperties.getProperty("api.httpMethod"))) {
                HttpPost httppost = new HttpPost(apiProperties.getProperty("api.baseUrl"));
                final List<NameValuePair> sendObject = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()){
                    sendObject.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httppost.setEntity(new UrlEncodedFormEntity(sendObject, "UTF-8"));
                response = httpclient.execute(httppost);
            } else if (GET.equalsIgnoreCase(apiProperties.getProperty("api.httpMethod"))) {
                String baseUrl = apiProperties.getProperty("api.baseUrl") + "?";
                for (Map.Entry<String, String> entry : params.entrySet()){
                    baseUrl += entry.getKey() + "=" + entry.getValue() + "&";
                }
                baseUrl += "x=y";
                HttpGet httpget = new HttpGet(baseUrl);

                response = httpclient.execute(httpget);
            } else {
                throw new RuntimeException("API method call not supported");
            }

            logger.info("Invoke API successfully: " + response.getStatusLine().toString());
        } catch (Exception e) {
            logger.info("Invoke API failed: " + e.getMessage());
        }
    }

    public static void main( String[] args ) {
        Map params = new HashMap();
        params.put("param1", "value1");
        params.put("param2", "value2");
        new InvokeAPIService().invokeApi(params);
    }
}
