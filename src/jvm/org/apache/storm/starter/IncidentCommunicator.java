package org.apache.storm.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.json.*;
public class IncidentCommunicator {
 
    private ArrayList<IncidentBean> beanList = new ArrayList<IncidentBean>();
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
         }, 1000,600000);
    }
    
    private void makeAndFormIncidentRequest(){
        LOG.info("XML: Attempting parse");
        xmlParser xmlP = new xmlParser();
        String output = xmlP.parseXml();
        LOG.info("XML: done parsing");
    }
    
   
    
    public ArrayList<IncidentBean> getIncidentUpdates(){
        
        if(beanList.size() > 0){
            ArrayList<IncidentBean> beansToReturn = new ArrayList<IncidentBean>();
            beansToReturn.addAll(beanList);
            beanList.clear();
            LOG.info("sending "+beansToReturn.size()+" to topology");
            return beansToReturn;
        }else{
            //Just send empty list.
            LOG.info("sending empty incident list to topology.");
            return beanList;
        }
    }
}










