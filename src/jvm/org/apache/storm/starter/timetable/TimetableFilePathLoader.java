package org.apache.storm.starter.timetable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;


//Finds and passes out the file paths to every timetable xml document. There are ~800 of them.
public class TimetableFilePathLoader{

    private static final Logger LOG = LoggerFactory.getLogger(TimetableFilePathLoader.class);
    
    private static TimetableFilePathLoader instance = null;
    protected TimetableFilePathLoader() {
      // Exists only to defeat instantiation.
       }
    
    public static TimetableFilePathLoader getInstance() {
      if(instance == null) {
         instance = new TimetableFilePathLoader();
      }
      return instance;
    }
    
     public ArrayList<String> loadXmlFileList(){
        ArrayList<String> output = new ArrayList<String>();
         try{
            File folder = new File("/opt/apache-storm-1.1.1/examples/storm-starter/src/jvm/org/apache/storm/starter/timetabledata");

            File[] files = folder.listFiles();
            for(int i =0; i<files.length; i++){
                output.add(files[i].getName());
            }

            
         }catch(Exception e){
             LOG.error("XMLFILE: "+e);
         }
         
         LOG.info("XMLFILE: outputing file paths: "+output.size());
         return output;
     }
         
    
}