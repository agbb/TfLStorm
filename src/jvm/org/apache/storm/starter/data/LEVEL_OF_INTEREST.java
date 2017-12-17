package org.apache.storm.starter.data;

public enum LEVEL_OF_INTEREST{
    
    High(2.0),
    Medium(1.0),
    Low(0.5);
    
    private double numVal;
    
    LEVEL_OF_INTEREST(double numVal){
       this.numVal = numVal;
    }
    
    public double getNumVal() {
        return numVal;
    }
    
}