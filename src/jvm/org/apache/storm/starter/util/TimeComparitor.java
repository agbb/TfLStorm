package org.apache.storm.starter.util;

import java.time.Instant;


//Determines if an arrival is out of date.
public class TimeComparitor{

    public static boolean isInPast(String time){
        
        try{
            long timeAsLong = Long.parseLong(time);
            long now = Instant.now().toEpochMilli();
            if(now>timeAsLong){
               return true;
            }
        }catch(Exception e){
         //LOG.error(e);   
        }
        return false;
    }

}