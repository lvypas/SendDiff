package com.softserveinc.senddiff;

/**
 * Created by lvypas on 27.01.2017.
 */
public class ComparedObject {
    int key;
    String oldValue;
    String newValue;

    public ComparedObject(int key, String oldValue, String newValue) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return "{\"Record\": {" +
                "\"id\":\"" + key + "\"" +
                "\"oldValue\":\"" + oldValue + "\"" +
                "\"newValue\":\"" + newValue + "\"" +
                "}}";
    }
}
