package org.apache.storm.starter.data;

public enum LEVEL_OF_INTEREST{
    
    High(3.0),
    Medium(1.5),
    Low(1.0);
    
    private double numVal;
    
    LEVEL_OF_INTEREST(double numVal){
       this.numVal = numVal;
    }
    
    public double getNumVal() {
        return numVal;
    }
    
}