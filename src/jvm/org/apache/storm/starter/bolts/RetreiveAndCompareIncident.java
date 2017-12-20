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
import org.apache.storm.starter.connection.*;
import org.apache.storm.starter.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Accepts as input new incident obejcts to be compared to existing ones to determine if they already exist or are new and should be persisted, or if they are no longer valid and should be removed from the store.
public class RetreiveAndCompareIncident extends BaseBasicBolt {
        RedisConnector RConnect;
        private static final Logger LOG = LoggerFactory.getLogger(RetreiveAndCompareIncident.class);
        @Override
        public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisConnector();
        
    }
        
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            

            Root.Disruptions.Disruption bean = (Root.Disruptions.Disruption) tuple.getValue(0);
            ArrayList<Root.Disruptions.Disruption> dists = RConnect.getIncidentArray();
           
            boolean found = false;
            boolean invalid = false;
            try{
                //Check if incident should be removed. 
                if(bean.getStatus().equals("Inactive") || bean.getStatus().equals("Scheduled")){
                     LOG.info("INCIDENT: found, but invalid");
                     invalid = true;   
                }else if(TimeComparitor.isInPast(bean.getEndTime())){
                    LOG.info("INCIDENT: found, but invalid");
                     invalid = true;   
                }if(bean.getStatus().equals("Scheduled")){
                     LOG.info("INCIDENT: found, but invalid");
                     invalid = true;   
                }if(bean.getStatus().equals("Recently Cleared")){
                     LOG.info("INCIDENT: found, but invalid");
                     invalid = true;   
                }
            }catch(Exception e){
                 LOG.error("INCIDENT: "+e);
            }
            for(int i =0; i<dists.size(); i++){

                if(bean.getId().equals(dists.get(i).getId())){
                    found = true;
                }
            }
            
            //A new, valid incident.
            if(!found && !invalid){

                  RConnect.invalidateIncidentCache();
                  collector.emit("toPersist",new Values(bean));
            }
            
            //An invalid incident, that exists. 
            if(found && invalid){
                 LOG.info("INCIDENT: invalid incident found, emmiting to remove."); 
                 collector.emit("toRemove",new Values(bean));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declareStream("toPersist",new Fields("incidient"));
            declarer.declareStream("toRemove",new Fields("incidient"));
        }
    }