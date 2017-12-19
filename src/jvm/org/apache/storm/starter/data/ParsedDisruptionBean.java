package org.apache.storm.starter.data;

import java.io.Serializable;
import org.apache.storm.starter.xml.*;
import java.util.ArrayList;
import org.apache.storm.starter.polygon.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;


//Represents a disruption object. Augmented with various extra peices of information. 
public class ParsedDisruptionBean implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Disruption disruptionXml;
    private ArrayList<Point> pointList;
    private double disruptionRadius = 0;
    
    public ParsedDisruptionBean(){
        
    }
    
    public Disruption getDisruptionXml(){
        return disruptionXml;
    }
    
    public void setDisruptionXml(Disruption disruptionXml){
        this.disruptionXml = disruptionXml;
    }
    
    public ArrayList<Point> getPointList(){
        return pointList;
    }
    
    public void setPointList(ArrayList<Point> pointList){
        this.pointList = pointList;
    }
    
    public double getRadius(){
         return disruptionRadius;
    }
    
    public void setRadius(double radius){
         this.disruptionRadius = radius;
    }
}