/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.starter;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WordCount but teh spout does not stop, and the bolts are implemented in java.
 * This can show how fast the word count can run.
 */
public class PoopTopology {

    public static ArrayList<ArrivalBean> arrivalBeans = new ArrayList<ArrivalBean>();
    public static ArrayList<Root.Disruptions.Disruption> incidentBeans = new ArrayList<Root.Disruptions.Disruption>();
    private static final Logger LOG = LoggerFactory.getLogger(PoopTopology.class);
    private static final IncidentCommunicator incidentComms = IncidentCommunicator.getInstance();
    private static final ArrivalCommunicator1 arrivalComms = ArrivalCommunicator1.getInstance();

    private static int iteration = 0;

    public static class ArrivalSpout extends BaseRichSpout {
        SpoutOutputCollector _collector;

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
                    LOG.info("waiting for new data");
                    arrivalBeans = arrivalComms.getArrivalUpdates();
                    if (arrivalBeans.size() > 0) {
                        LOG.info("NEW DATA - arrivalBeans size:" + arrivalBeans.size() + "");
                    }
                } catch (Exception e) {
                    LOG.error("sleep failed");
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

    public static class IncidentSpout extends BaseRichSpout {
        SpoutOutputCollector _collector;

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            _collector = collector;
            incidentComms.beginIncidentRequestLoop();
        }

        @Override
        public void nextTuple() {

            if(incidentBeans.size()>0){
                LOG.info("INCIDENT: emmiting disruption tuple");
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

    public static class AddExclaim extends BaseBasicBolt {
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {

            try {
                ArrivalBean bean = (ArrivalBean) tuple.getValue(0);
                String stopPointName = bean.getStopPointName();
                bean.setStopPointName(stopPointName + "!");
                LOG.info(bean.getStopPointName());
                collector.emit(new Values(bean, 1));
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

    public static class MakeSevere extends BaseBasicBolt {

        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            Root.Disruptions.Disruption bean = (Root.Disruptions.Disruption) tuple.getValue(0);
            String severity = bean.getSeverity();
            String location = bean.getLocation(); 
            LOG.info("INCIDENT: "+location+": "+severity);
            bean.setSeverity("High");
            collector.emit(new Values(bean, 1));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word", "count"));
        }
    }

    // public static class WordCount extends BaseBasicBolt {
    // Map<String, Integer> counts = new HashMap<String, Integer>();

    // @Override
    // public void execute(Tuple tuple, BasicOutputCollector collector) {
    // String word = tuple.getString(0);
    // Integer count = counts.get(word);
    // if (count == null)
    // count = 0;
    // count++;
    // counts.put(word, count);
    // collector.emit(new Values(word, count));
    // }

    // @Override
    // public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // declarer.declare(new Fields("word", "count"));
    // }
    // }

    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("arrivalSpout", new ArrivalSpout(), 1);
        builder.setSpout("incidentSpout", new IncidentSpout(), 1);
        
        builder.setBolt("AddExclaim", new AddExclaim(), 4).shuffleGrouping("arrivalSpout");
        builder.setBolt("MakeSevere", new AddExclaim(), 4).shuffleGrouping("incidentSpout");
        
        // builder.setBolt("count", new WordCount(), 4).fieldsGrouping("split",
        // new Fields("word"));

        Config conf = new Config();
        conf.registerSerialization(ArrivalBean.class);
        conf.registerSerialization(Root.Disruptions.Disruption.class);
        conf.registerMetricsConsumer(org.apache.storm.metric.LoggingMetricsConsumer.class);

        String name = "wc-test";
        if (args != null && args.length > 0) {
            name = args[0];
        }

        conf.setNumWorkers(1);
        StormSubmitter.submitTopologyWithProgressBar(name, conf, builder.createTopology());

        Map clusterConf = Utils.readStormConfig();
        clusterConf.putAll(Utils.readCommandLineOpts());
        Nimbus.Client client = NimbusClient.getConfiguredClient(clusterConf).getClient();

    }
}
