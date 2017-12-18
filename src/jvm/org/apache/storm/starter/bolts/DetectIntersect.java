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



public class DetectIntersect extends BaseBasicBolt {

        private static final Logger LOG = LoggerFactory.getLogger(DetectIntersect.class);
        
        @Override
    public void prepare(Map conf, TopologyContext context) {
        
    }
        
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {

        ArrivalBean arrBean = (ArrivalBean) tuple.getValue(0);
        ParsedDisruptionBean distBean = (ParsedDisruptionBean) tuple.getValue(1);
        ArrayList<Point> distPoints = distBean.getPointList();

        double minDist = Double.MAX_VALUE;
        Point arrivalPoint = arrBean.getCoords();

        for(int i = 0; i< distPoints.size()-1; i++){
            Point a = distPoints.get(i);
            Point b = distPoints.get(i+1);
            Line segment = new Line(a,b);
            double dist = segment.distanceToLineSegment(arrivalPoint);
            if(dist<minDist){
               minDist = dist;
            }
        }

         if(minDist<=distBean.getRadius()){    
             LOG.info("INTERSECT: Disruption intersect found! Raduis was"+distBean.getRadius()+ "and distance was: "+ minDist);
             
             ArrivalDisruptionPair newPair = new ArrivalDisruptionPair();
             newPair.arrivalBean = arrBean;
             newPair.disruptionBean = distBean;
             newPair.distance = minDist;
             collector.emit("disruptedArrival",new Values(newPair));
         }else{
            collector.emit("undisruptedArrival",new Values(arrBean));
         }
        
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("disruptedArrival",new Fields("ArrivalDisruptionPair"));
        declarer.declareStream("undisruptedArrival",new Fields("arrivalBean"));

    }

}


   










