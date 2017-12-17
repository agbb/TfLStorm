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

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncidentSpout extends BaseRichSpout {
        SpoutOutputCollector _collector;
    public static ArrayList<Root.Disruptions.Disruption> incidentBeans = new ArrayList<Root.Disruptions.Disruption>();
        private static final IncidentCommunicator incidentComms = IncidentCommunicator.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(IncidentSpout.class);

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            _collector = collector;
            incidentComms.beginIncidentRequestLoop();
            
        }

        @Override
        public void nextTuple() {

            if(incidentBeans.size()>0){
                Root.Disruptions.Disruption nextBean = incidentBeans.remove(0);
                _collector.emit(new Values(nextBean), nextBean);
            }else{
                try{
                    Thread.sleep(10000);
                    LOG.info("INCIDENT:waiting for new incident data");
                    incidentBeans = incidentComms.getIncidentUpdates();
                    if(incidentBeans.size()>0){
                        LOG.info("INCIDENT: new - incidentBeans size:"+incidentBeans.size()+"");
                    }
                }
                catch(Exception e){
                    LOG.error("INCIDENT: sleep failed");
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

            declarer.declare(new Fields("Disruption"));
        }
    }