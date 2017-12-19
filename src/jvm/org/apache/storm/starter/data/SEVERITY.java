package org.apache.storm.starter.data;

//Enumeration of TfL severity levels with tuned variables for severity calculations.
public enum SEVERITY{
 
    Severe(4.0),
    Serious(3.0),
    Moderate(2.0),
    Minimal(1.0);
    
    private double numVal;
    
    SEVERITY(double numVal){
        this.numVal = numVal;
    }
    
    public double getNumVal() {
        return numVal;
    }
}

