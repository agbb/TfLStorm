package org.apache.storm.starter.data;

//Enumeration of TfL hazard levels with tuned variables for severity calculations.
public enum HAZARD{
    
    Hazard(2.2),
    InfrastructureIssue(1.6),
    SpecialandPlannedEvents(1.2),
    TrafficIncidents(3.4),
    TrafficVolume(3.8),
    Works(2.0);
    
    private double numVal;
    
    HAZARD(double numVal){
       this.numVal = numVal;
    }
    
    public double getNumVal() {
        return numVal;
    }
    
}