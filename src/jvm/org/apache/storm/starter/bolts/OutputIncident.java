package org.apache.storm.starter.bolts;

import org.apache.storm.generated.*;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.task.TopologyContext;

import org.apache.storm.starter.util.*;
import org.apache.storm.starter.*;
import org.apache.storm.starter.data.*;
import org.apache.storm.starter.connection.*;
import org.apache.storm.starter.xml.Root.*;
import org.apache.storm.starter.xml.Root.Disruptions.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Bolt for serialising and persisting the incident list into json for consumption in the web UI.
public class OutputIncident extends BaseBasicBolt {
    RedisJSONConnector RConnect;
    
    private static final Logger LOG = LoggerFactory.getLogger(OutputIncident.class);
    
    @Override
    public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisJSONConnector();
    }
        
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        
        Disruption incid = (Disruption) tuple.getValue(0);
        String json = JsonSerialiser.serialiseIncident(incid);
        RConnect.persistJSON("INCIDENT",json);
        ArrayList<String> confirm = RConnect.getJSON("DISRUPTION");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("incident"));
    }
}