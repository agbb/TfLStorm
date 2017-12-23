package org.apache.storm.starter.data;

//Enumeration of TfL level of interest levels with tuned variables for severity calculations.
public enum LEVEL_OF_INTEREST{
    
    High(2.8),
    Medium(1.3),
    Low(0.8);
    
    private double numVal;
    
    LEVEL_OF_INTEREST(double numVal){
       this.numVal = numVal;
    }
    
    public double getNumVal() {
        return numVal;
    }
    
}
