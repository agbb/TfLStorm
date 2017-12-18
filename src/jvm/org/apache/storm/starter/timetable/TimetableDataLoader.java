package org.apache.storm.starter.timetable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
public class TimetableDataLoader{
    
    
    
    private static final Logger LOG = LoggerFactory.getLogger(TimetableDataLoader.class);
    
    private static TimetableDataLoader instance = null;
    protected TimetableDataLoader() {
      // Exists only to defeat instantiation.
       }
    
    public static TimetableDataLoader getInstance() {
      if(instance == null) {
         instance = new TimetableDataLoader();
      }
      return instance;
    }
    
     public void loadXmlFileList(){
        
         try{
            File folder = new File("/opt/apache-storm-1.1.1/examples/storm-starter/src/jvm/org/apache/storm/starter/timetabledata");
             
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            LOG.info("XMLFILE: Current relative path is: " + s);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                  if (listOfFiles[i].isFile()) {
                       LOG.info("XMLFILE: " + listOfFiles[i].getName());
                  } 
               }
          }
         catch(Exception e){
             LOG.error("XMLFILE: "+e);
         }
     }
    
}