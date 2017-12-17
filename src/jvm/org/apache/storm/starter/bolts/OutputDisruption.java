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

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class OutputDisruption extends BaseBasicBolt {
    ReddisConnector RConnect;
    
    private static final Logger LOG = LoggerFactory.getLogger(OutputDisruption.class);
        
        @Override
    public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisConnector();
    }
        
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        ArrivalDisruptionPair arrBean = (ArrivalDisruptionPair) tuple.getValue(0);
        RConnect.persistDelay(arrBean);
        
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        

    }

}


   











