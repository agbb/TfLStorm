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
import org.apache.storm.starter.polygon.*;
import org.apache.storm.starter.data.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Streets.Street;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Boundary.Polygon;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Number;
import java.lang.Math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//Uses information provided about the severity of incidents to determine the area around the polygon or line that is affected by the incident.
public class DetermineIncidentRadius extends BaseBasicBolt {

    private static final Logger LOG = LoggerFactory.getLogger(DetermineIncidentRadius.class);

    public void prepare(Map conf, TopologyContext context) {

    }

    @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            ArrivalBean arrBean = (ArrivalBean) tuple.getValue(0);
            ParsedDisruptionBean distBean = (ParsedDisruptionBean) tuple.getValue(1);
            Disruption disruption = distBean.getDisruptionXml();
            
            double baseline = 200.0;
            String severity = disruption.getSeverity();
            String levelOfInterest = disruption.getLevelOfInterest();
            String category = disruption.getCategory().replaceAll(" ","");
            String subCategory = disruption.getSubCategory().replaceAll(" ","");

            Double severityMult = SEVERITY.valueOf(severity).getNumVal();
            Double levelOfInterestMult = LEVEL_OF_INTEREST.valueOf(levelOfInterest).getNumVal();
            
            double radiusOutput = baseline * Math.pow(severityMult,2) * levelOfInterestMult;
            
            distBean.setRadius(radiusOutput);
            collector.emit(new Values(arrBean,distBean));
        }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ArrivalBean","ParsedDisruptionBean"));
    }
}