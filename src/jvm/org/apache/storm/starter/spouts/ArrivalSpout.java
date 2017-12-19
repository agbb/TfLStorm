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
import org.apache.storm.starter.data.*;
import org.apache.storm.starter.connection.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//Emits arrivalBean objects that have been passed in from the arrival connector class from the http stream request. Entry point for arrival data into storm. 
public class ArrivalSpout extends BaseRichSpout {
        SpoutOutputCollector _collector;
        public static ArrayList<ArrivalBean> arrivalBeans = new ArrayList<ArrivalBean>();
        private static final Logger LOG = LoggerFactory.getLogger(ArrivalSpout.class);
        private static final ArrivalCommunicator1 arrivalComms = ArrivalCommunicator1.getInstance();
    
        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            _collector = collector;
            arrivalComms.beginArrivalRequestLoop();
        }

        @Override
        public void nextTuple() {

            if (arrivalBeans.size() > 0) {
                ArrivalBean nextBean = arrivalBeans.remove(0);
                _collector.emit(new Values(nextBean), nextBean);
            } else {
                try {
                    Thread.sleep(500);
                    arrivalBeans = arrivalComms.getArrivalUpdates();
                    if (arrivalBeans.size() > 0) {
                        LOG.info("ARRIVAL: - arrivalBeans size:" + arrivalBeans.size() + "");
                    }
                } catch (Exception e) {
                    LOG.error("ARRIVAL: sleep failed");
                }
            }
            return;
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

            declarer.declare(new Fields("ArrivalBean"));
        }
    }