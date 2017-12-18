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
import org.apache.storm.starter.bolts.*;
import org.apache.storm.starter.spouts.*;
import org.apache.storm.starter.data.*;
import org.apache.storm.starter.timetable.*;


import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WordCount but teh spout does not stop, and the bolts are implemented in java.
 * This can show how fast the word count can run.
 */
public class PoopTopology {


    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
    
        builder.setSpout("ArrivalSpout", new ArrivalSpout(), 1);
        builder.setSpout("IncidentSpout", new IncidentSpout(), 1);
        builder.setSpout("TimetableSpout", new TimetableSpout(), 1);
        
        builder.setBolt("RetreiveAndCompareIncident", new RetreiveAndCompareIncident(), 4).shuffleGrouping("IncidentSpout");
        builder.setBolt("PersistIncident", new PersistIncident(), 4).shuffleGrouping("RetreiveAndCompareIncident","toPersist");
        builder.setBolt("RemoveIncident", new PersistIncident(), 4).shuffleGrouping("RetreiveAndCompareIncident","toRemove");
        builder.setBolt("ParsePoints", new ParsePoints(), 4).shuffleGrouping("ArrivalSpout");
        builder.setBolt("DetermineIncidentRadius", new DetermineIncidentRadius(),4).shuffleGrouping("ParsePoints");
        builder.setBolt("DetectIntersect", new DetectIntersect(), 4).shuffleGrouping("DetermineIncidentRadius");
        builder.setBolt("DetermineDisruption", new DetermineDisruption(), 4).shuffleGrouping("DetectIntersect","disruptedArrival");
        builder.setBolt("RetrieveAndCompareDisruption", new RetrieveAndCompareDisruption(), 4).shuffleGrouping("DetermineDisruption");
        builder.setBolt("PersistDisruption", new PersistDisruption(), 4).shuffleGrouping("RetrieveAndCompareDisruption");
        builder.setBolt("TimetableImporter", new TimetableImportBolt(), 4).shuffleGrouping("TimetableSpout");
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
