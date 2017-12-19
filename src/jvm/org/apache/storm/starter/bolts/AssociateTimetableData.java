package org.apache.storm.starter.bolts;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.*;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.NimbusClient;
import org.apache.storm.utils.Utils;
import org.apache.storm.starter.xml.*;
import org.apache.storm.starter.*;
import org.apache.storm.starter.polygon.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Streets.Street;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Boundary.Polygon;
import org.apache.storm.starter.connection.*;
import org.apache.storm.starter.data.*;
import org.apache.storm.starter.timetable.*;
import org.apache.storm.starter.timetable.xml.*;

import javax.xml.datatype.XMLGregorianCalendar;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//June 2015 to June 2017T

// Associates the correct timetable data element with the arrival object so that in subequent arrival predictions we can check how accurate the prediction was.
public class AssociateTimetableData extends BaseBasicBolt {
    TimetableRedisConnector TConnect;
    private static final Logger LOG = LoggerFactory.getLogger(AssociateTimetableData.class);

    public void prepare(Map conf, TopologyContext context) {
        TConnect = new TimetableRedisConnector();
    }

    @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
           
            //Associating timetable data to live arrival allows us to see if the bus is late.
            //This allows us to compare our prediction when new data arrives the updated arrival estimate is compared to the time table to verify our prediction.
           ArrivalDisruptionPair pair = (ArrivalDisruptionPair) tuple.getValue(0);
           ArrivalBean bean = pair.arrivalBean;
           String lineId = bean.getLineID();
           LOG.info("TIMETABLE: new bolt "+lineId);
           try{
               TConnect.test();
               TransXChange routeTimetable = TConnect.getTimetable(lineId);
               
              
                   
               if(routeTimetable == null){
                   LOG.info("TIMETABLE: null");
               }else{
                    String JourneyRef = routeTimetable.getServices().getService().getStandardService().getJourneyPattern().get(0).getJourneyPatternSectionRefs();
               
                XMLGregorianCalendar departureTime = routeTimetable.getVehicleJourneys().getVehicleJourney().get(0).getDepartureTime();
                   
                   bean.setTimetableTime(departureTime);
                   bean.setJourneyId(JourneyRef);
                   LOG.info("TIMETABLE: not null"+JourneyRef+" "+departureTime);
               }
               
           }catch(Exception e){
               LOG.error("TIMETABLE: "+e);
           }

           pair.arrivalBean = bean;
           collector.emit(new Values(pair));
        }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ArrivalDisruptionPair"));
    }

}