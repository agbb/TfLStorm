package org.apache.storm.starter.data;

//Enumeration of TfL severity levels with tuned variables for severity calculations.
public enum SEVERITY{
 
    Severe(4.1),
    Serious(3.2),
    Moderate(2.1),
    Minimal(0.8);
    
    private double numVal;
    
    SEVERITY(double numVal){
        this.numVal = numVal;
    }
    
    public double getNumVal() {
        return numVal;
    }
}

