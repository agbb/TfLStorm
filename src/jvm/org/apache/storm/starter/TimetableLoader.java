package org.apache.storm.starter;

import org.apache.storm.starter.timetable.*;
import org.apache.storm.starter.connection.*;
import org.apache.storm.starter.timetable.xml.*;
import org.apache.storm.starter.util.*;
import java.util.ArrayList;

public class TimetableLoader{
 
    private static final String path = "/opt/apache-storm-1.1.1/examples/storm-starter/src/jvm/org/apache/storm/starter/timetabledata/";
    
    public static void main(String[] args) throws javax.xml.bind.JAXBException, java.io.IOException, java.lang.ClassNotFoundException{
     
        System.out.println("Starting data load");
        TimetableFilePathLoader tdl = TimetableFilePathLoader.getInstance();
        ArrayList<String> filePaths = tdl.loadXmlFileList();
        TimetableXmlParser TParse = new TimetableXmlParser();
        TimetableRedisConnector TConnect = new TimetableRedisConnector();
        TConnect.flushRedis();
        
        for(int i = 0; i<filePaths.size(); i++){
            try{
                TransXChange output = TParse.parseXml(filePaths.get(i));
                if(output != null){
                     String lineId = output.getServices().getService().getLines().getLine().getLineName();
                     TConnect.persistTimetable(lineId,output);
                     System.out.println("Done "+i+" of "+filePaths.size());
                }
            }catch(Exception e){
                 System.out.println("Failed to parse: "+e);
             }
        }
        System.out.println("Finished loading timetable data into Redis.");
    } 
}