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


//Uses provided information in incoming tuple to estimate the level of disruption caused by an incidient and arrival intersection. 
public class DetermineDisruption extends BaseBasicBolt {

        private static final Logger LOG = LoggerFactory.getLogger(DetermineDisruption.class);
        
        @Override
    public void prepare(Map conf, TopologyContext context) {
        
    }
        
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        ArrivalDisruptionPair pair = (ArrivalDisruptionPair) tuple.getValue(0);
        double distance = pair.distance;
        Disruption disruption = pair.disruptionBean.getDisruptionXml();
        
        String severity = disruption.getSeverity();
        String levelOfInterest = disruption.getLevelOfInterest();
        String category = disruption.getCategory().replaceAll(" ","");
        if(category.equals("Hazard(s)")){
          category = "Hazard";
        }

        LOG.info("DETERMINE: new pair found "+pair.arrivalBean.getStopPointName());
        
        Double severityMult = SEVERITY.valueOf(severity).getNumVal();
        Double levelOfInterestMult = LEVEL_OF_INTEREST.valueOf(levelOfInterest).getNumVal();
        Double hazardMult = HAZARD.valueOf(category).getNumVal();
        
        double result = (Math.pow(severityMult,2) + Math.pow(levelOfInterestMult,2) + Math.pow(hazardMult,2)) * (1/distance);
        
        pair.calculatedDelay = result;
        
        LOG.info("DETERMINE: "+result);
//         if(result> 0 && < 100){
//             pair.estimatedDelay = 60;
//                 delayEstimateHumanReadable
//         }else if(result > 100 && result < 300){
//             pair.estimatedDelay = 120;
//             delayEstimateHumanReadable
//         }else if(result > 300 && result < 500){
//             pair.estimatedDelay = 500;
//             delayEstimateHumanReadable
//         }else if(result > 500 && result < 1000){
//             pair.estimatedDelay = 700;
//             delayEstimateHumanReadable
//         }else if(result > 500 && result < 1000){
//             pair.estimatedDelay = 700;
//             delayEstimateHumanReadable
//         }else if(result > 1000){
//             pair.estimatedDelay = 1500;
//         }
        
        collector.emit(new Values(pair));
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ArrivalDisruptionPair"));
    }

}


   











