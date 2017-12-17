package org.apache.storm.starter.data;

import java.io.Serializable;
import org.apache.storm.starter.xml.*;
import java.util.ArrayList;
import org.apache.storm.starter.polygon.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;

public class ArrivalDisruptionPair implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public ParsedDisruptionBean disruptionBean;
    public ArrivalBean arrivalBean;
    public double distance;
    public double calculatedDelay;
    public double actualDelay;
    public String delayEstimateHumanReadable;
    
    public ArrivalDisruptionPair(){

    }
    

    
}