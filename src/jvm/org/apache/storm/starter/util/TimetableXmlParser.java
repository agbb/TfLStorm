package org.apache.storm.starter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.net.*;
import javax.xml.transform.stream.*;
import javax.xml.*;
import javax.xml.bind.*;
import java.util.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.apache.storm.starter.timetable.xml.*;

public class TimetableXmlParser {
 
    private static final Logger LOG = LoggerFactory.getLogger(TimetableXmlParser.class);
    
    public TimetableXmlParser(){
        
    }
    
    public ArrayList<TransXChange> parseXml(String[] paths){
       
        ArrayList<TransXChange> output = new ArrayList<TransXChange>();
        
        for(int i =0; i<paths.length; i++){
            try{

                JAXBContext jc = JAXBContext.newInstance("org.apache.storm.starter.timetable.xml");

                Unmarshaller unmarshaller = jc.createUnmarshaller();
                TransXChange item = (TransXChange) unmarshaller.unmarshal(new File("./timetabledata"));
                output.add(item);
                
            }catch(Exception e){
                LOG.error("XML: "+e.toString());
            }
        }
        return output;
    }
    
}