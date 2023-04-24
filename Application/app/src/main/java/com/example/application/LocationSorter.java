package com.example.application;

import java.util.Comparator;

public class LocationSorter implements Comparator<EventObject> {

    public int compare(EventObject eventOne, EventObject eventTwo){
        String eventOneLocation = eventOne.getLocation();
        String eventTwoLocation = eventTwo.getLocation();
        return eventOneLocation.compareTo(eventTwoLocation);
    }
}