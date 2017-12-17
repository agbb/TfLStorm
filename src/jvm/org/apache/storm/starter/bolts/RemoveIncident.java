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

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveIncident extends BaseBasicBolt {
        RedisConnector RConnect;
        private static final Logger LOG = LoggerFactory.getLogger(RemoveIncident.class);
        @Override
        public void prepare(Map conf, TopologyContext context) {
            RConnect = new RedisConnector();
        }
        
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            
            Root.Disruptions.Disruption bean = (Root.Disruptions.Disruption) tuple.getValue(0);
            LOG.info("INCIDENT: incident for removeal submitted.");
            //Remove incident from store here.


        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            //declarer.declareStream("toPersist",new Fields("incidient"));
            //declarer.declareStream("toRemove",new Fields("incidient"));
        }
    }