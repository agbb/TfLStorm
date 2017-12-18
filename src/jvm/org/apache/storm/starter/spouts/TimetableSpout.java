package org.apache.storm.starter.spouts;

import org.apache.storm.generated.*;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.starter.*;
import org.apache.storm.starter.xml.*;
import org.apache.storm.starter.connection.*;
import org.apache.storm.starter.timetable.*;


import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimetableSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    private static final TimetableDataLoader tdl = TimetableDataLoader.getInstance();
    
    private static final Logger LOG = LoggerFactory.getLogger(TimetableSpout.class);

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            _collector = collector;
            tdl.loadXmlFileList();
            
        }

        @Override
        public void nextTuple() {

            // _collector.emit(new Values(nextBean), nextBean);
        }

        @Override
        public void ack(Object id) {
            // Ignored
        }

        @Override
        public void fail(Object id) {
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

           // declarer.declare(new Fields("Disruption"));
        }
    }