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

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Bolt for serialising and persisting the disruption list into json for consumption in the web UI.
public class OutputDisruption extends BaseBasicBolt {
    RedisJSONConnector RConnect;
    
    private static final Logger LOG = LoggerFactory.getLogger(OutputDisruption.class);
    
    @Override
    public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisJSONConnector();
    }
        
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        
        ArrivalDisruptionPair pair = (ArrivalDisruptionPair) tuple.getValue(0);
        String json = JsonSerialiser.serialisePair(pair);
        RConnect.persistJSON("DISRUPTION",json);
        ArrayList<String> confirm = RConnect.getJSON("DISRUPTION");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("incident"));
    }
}