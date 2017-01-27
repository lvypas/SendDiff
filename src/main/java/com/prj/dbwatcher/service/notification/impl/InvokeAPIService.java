package com.prj.dbwatcher.service.notification.impl;

import com.prj.dbwatcher.model.ComparedObject;
import com.prj.dbwatcher.service.notification.INotificationService;
import com.prj.dbwatcher.utils.AppProperties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by vvinton on 1/27/2017.
 */
public class InvokeAPIService implements INotificationService {
    private static final Logger logger = LoggerFactory.getLogger(InvokeAPIService.class);
    public static String POST = "POST";

    public void notify(List<ComparedObject> results) {
        if (results.size() == 0) {
            logger.info("No data to send - skipping notification API invocation.");
            return;
        }
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = null;

            if (POST.equalsIgnoreCase(AppProperties.getProps().getProperty("notification.api.httpMethod"))) {
                HttpPost httppost = new HttpPost(AppProperties.getProps().getProperty("notification.api.baseUrl"));
                httppost.addHeader("content-type", "application/json");
                StringEntity params = new StringEntity(getMessageBody(results));
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

    public String getMessageBody(List<ComparedObject> results) {
        String body = "[";
        Iterator it = results.iterator();
        while (it.hasNext()) {
            ComparedObject obj = (ComparedObject)it.next();
            body += "{";
            body += ("\"id\":\"" + obj.key + "\",");
            body += ("\"oldValue\":\"" + obj.oldValue.replace("\"","") + "\",");
            body += ("\"newValue\":\"" + obj.newValue.replace("\"","") + "\"");
            body += "}";
            if (it.hasNext()) {
                body += ",";
            }
        }
        body += "]";
        return body;
    }
}
