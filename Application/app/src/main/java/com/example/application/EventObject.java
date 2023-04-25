package com.example.application;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Event object to store JSONObject containing one single event's data
 * Made a new class because ListView shows user text from the toString of an object
 * This gives us control over what is shown to the users in the ListView
 */
public class EventObject {

    JSONObject jsonObject;

    public EventObject() {}

    public EventObject(JSONObject inputJsonObject){
        jsonObject = inputJsonObject;
    }

    public String getAttribute(String key) {
        try {
            return (String)jsonObject.get(key);
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
            return "";
        }
    }

    public void setObject(JSONObject inputJsonObject) {
        jsonObject = inputJsonObject;
    }

    public String getDate() {
        try {
            return (String)jsonObject.get("date");
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
            return e.toString();
        }
    }

    public String getName() {
        try {
            return (String)jsonObject.get("name");
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
            return e.toString();
        }
    }

    public String getLocation() {
        try {
            return (String)jsonObject.get("location");
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
            return e.toString();
        }
    }

    public String[] getJoinedUsers(){
        try {
            return (String[])jsonObject.get("joinedUsers");
        } catch (JSONException e) {
            // uh oh
            e.printStackTrace();
            return new String[]{e.toString()};
        }
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
