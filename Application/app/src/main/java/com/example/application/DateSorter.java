package com.example.application;

import java.util.Comparator;

public class DateSorter implements Comparator<EventObject> {

    public int compare(EventObject eventOne, EventObject eventTwo){
        String eventOneDate = eventOne.getDate();
        String eventTwoDate = eventTwo.getDate();
        String eventOneYear = eventOneDate.substring(eventOneDate.length() - 4);
        String eventTwoYear = eventTwoDate.substring(eventTwoDate.length() - 4);
        if(Integer.parseInt(eventOneYear) > Integer.parseInt(eventTwoYear)) {
            return 1;
        }
        else if(Integer.parseInt(eventOneYear) < Integer.parseInt(eventTwoYear)) {
            return -1;
        }
        else {
            return 0;
        }
    }
}