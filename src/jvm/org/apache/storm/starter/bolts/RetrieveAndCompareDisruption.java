package org.apache.storm.starter.bolts;

import org.apache.storm.generated.*;
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
import org.apache.storm.starter.xml.*;
import org.apache.storm.starter.*;
import org.apache.storm.starter.polygon.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Streets.Street;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Boundary.Polygon;
import org.apache.storm.starter.data.*;
import org.apache.storm.starter.connection.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//Comapres a newly received disruption to existing ones to see if it is new and should be persisted, or if it is outdate and should trigger removal from persistence. 
public class RetrieveAndCompareDisruption extends BaseBasicBolt {
    RedisConnector RConnect;
    
    private static final Logger LOG = LoggerFactory.getLogger(RetrieveAndCompareDisruption.class);
        
    @Override
    public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisConnector();
    }
        
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        ArrivalDisruptionPair candidate = (ArrivalDisruptionPair) tuple.getValue(0);
        boolean found = false;
        
        ArrayList<ArrivalDisruptionPair> output =  RConnect.getDisruption();
        LOG.info("DISRUPTIONCOMP: Recevied item, comparing against: "+output.size());
        if(output.size()>0){
           
           for(int i = 0; i<output.size(); i++){
               String outputPK = output.get(i).arrivalBean.getStopCode2()+""+output.get(i).arrivalBean.getVehicleID();
               String candidatePK = candidate.arrivalBean.getStopCode2()+""+candidate.arrivalBean.getVehicleID();
              if(outputPK.equals(candidatePK)){
                 LOG.info("DISRUPTIONCOMP: item already exists.");
                 found = true;
                 break;
              }
           }
        }if(!found){
            LOG.info("DISRUPTIONCOMP: item is new.");
            collector.emit(new Values(candidate));
            RConnect.invalidatePairCache();
        }
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("NewArrivalDisruptionPair"));

    }

}


   











