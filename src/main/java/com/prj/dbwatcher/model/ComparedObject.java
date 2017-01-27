package com.prj.dbwatcher.model;

/**
 * Created by lvypas on 27.01.2017.
 */
public class ComparedObject {
    public int key;
    public String oldValue;
    public String newValue;

    public ComparedObject(int key, String oldValue, String newValue) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
