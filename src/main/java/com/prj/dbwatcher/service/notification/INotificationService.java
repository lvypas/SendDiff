package com.prj.dbwatcher.service.notification;

import com.prj.dbwatcher.model.ComparedObject;

import java.util.List;

/**
 * Created by vvinton on 1/27/2017.
 */
public interface INotificationService {
    public void notify (List<ComparedObject> resutls);

    public String getMessageBody(List<ComparedObject> results);
}
