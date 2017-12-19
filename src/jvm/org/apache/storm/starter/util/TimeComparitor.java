package org.apache.storm.starter.util;

import java.time.Instant;

public class TimeComparitor{

    public static boolean isInPast(String time){
        
        //long timeAsLong = Long.parseLong(time);
        long now = Instant.now().toEpochMilli();
        return false;
    }

}