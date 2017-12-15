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

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddExclaim extends BaseBasicBolt {

    
        private static final Logger LOG = LoggerFactory.getLogger(AddExclaim.class);
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {

            try {
                ArrivalBean bean = (ArrivalBean) tuple.getValue(0);
                String stopPointName = bean.getStopPointName();
                bean.setStopPointName(stopPointName + "!");
                LOG.info(bean.getStopPointName());
                collector.emit(new Values(bean));
            } catch (Exception e) {
                LOG.error("**ArrivalBean error**", e);
                collector.reportError(e);
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("ArrivalBean"));
        }
    }