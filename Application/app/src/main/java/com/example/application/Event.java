package com.example.application;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Event object to store JSONObject containing one single event's data
 * Made a new class because ListView shows user text from the toString of an object
 * This gives us control over what is shown to the users in the ListView
 */
public class Event {

    JSONObject jsonObject;

    public Event(JSONObject inputJsonObject){
        jsonObject = inputJsonObject;
    }

    @Override
    public String toString(){
        try {
            return (String)jsonObject.get("name") + "\n" + (String)jsonObject.get("location");
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
            return e.toString();
        }
    }
}