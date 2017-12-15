package org.apache.storm.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.json.*;
import org.apache.storm.starter.xml.*;

public class IncidentCommunicator {
 
    private ArrayList<Root.Disruptions.Disruption> beanList = new ArrayList<Root.Disruptions.Disruption>();
    private static final Logger LOG = LoggerFactory.getLogger(IncidentCommunicator.class);
    private static final String arrivalUrl = "https://data.tfl.gov.uk/tfl/syndication/feeds/tims_feed.xml?app_id=ef3b4027&app_key=5c0b4a956599179156d4979df6bcb346";

    private static IncidentCommunicator instance = null;
    protected IncidentCommunicator() {
      // Exists only to defeat instantiation.
       }
    
    public static IncidentCommunicator getInstance() {
      if(instance == null) {
         instance = new IncidentCommunicator();
         instance.beginIncidentRequestLoop();
      }
      return instance;
    }
    
    public void beginIncidentRequestLoop(){
        
        Timer t = new Timer( );
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                  java.util.Date date = new java.util.Date();
                  makeAndFormIncidentRequest();
            }
         }, 1000,300000);
    }
    
    private void makeAndFormIncidentRequest(){
        LOG.info("XML: Attempting parse");
        xmlParser xmlP = new xmlParser();
        beanList = xmlP.parseXml();
        LOG.info("XML: done parsing");
    }
    
   
    
    public ArrayList<Root.Disruptions.Disruption> getIncidentUpdates(){
        
        if(beanList.size() > 0){
            ArrayList<Root.Disruptions.Disruption> beansToReturn = new ArrayList<Root.Disruptions.Disruption>();
            beansToReturn.addAll(beanList);
            beanList.clear();
            LOG.info("sending "+beansToReturn.size()+" to topology");
            return beansToReturn;
        }else{
            //Just send empty list.
            return beanList;
        }
    }
}










