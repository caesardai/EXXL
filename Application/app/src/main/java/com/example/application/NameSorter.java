package com.example.application;

import java.util.Comparator;

public class NameSorter implements Comparator<EventObject> {

    public int compare(EventObject eventOne, EventObject eventTwo){
        String eventOneName = eventOne.getName();
        String eventTwoName = eventTwo.getName();
        return eventOneName.compareTo(eventTwoName);
    }
}