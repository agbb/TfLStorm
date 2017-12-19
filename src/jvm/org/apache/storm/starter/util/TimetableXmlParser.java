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


//Loads xml timetable files one by one and passes them into Redis for in memory storage.  
public class TimetableXmlParser {
 
    private static final Logger LOG = LoggerFactory.getLogger(TimetableXmlParser.class);
    JAXBContext jc;
    Unmarshaller unmarshaller;
    
    public TimetableXmlParser() throws javax.xml.bind.JAXBException{
         try{
             jc = JAXBContext.newInstance("org.apache.storm.starter.timetable.xml");
             unmarshaller = jc.createUnmarshaller();
         }catch(javax.xml.bind.JAXBException e){
             //LOG.error("TIMETABLEXML: "+e);
             throw e;
        }
    }
    
    public TransXChange parseXml(String name) throws javax.xml.bind.JAXBException{
       
        TransXChange output = null;
        String path = "/opt/apache-storm-1.1.1/examples/storm-starter/src/jvm/org/apache/storm/starter/timetabledata/";
        LOG.info("XML attempting: "+name);
        try{

            jc = JAXBContext.newInstance("org.apache.storm.starter.timetable.xml");
            unmarshaller = jc.createUnmarshaller();
            TransXChange item = (TransXChange) unmarshaller.unmarshal(new File(path+name));
            output = item;

        }catch(javax.xml.bind.JAXBException e){
            //LOG.error("XML: "+e.toString());
            throw e;
        }

        return output;
    }
    
}