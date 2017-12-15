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

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddExclaim extends BaseBasicBolt {
    RedisConnector RConnect;
    private static final Logger LOG = LoggerFactory.getLogger(AddExclaim.class);

    public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisConnector();
    }

    @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            ArrayList<Root.Disruptions.Disruption> dists = RConnect.getIncidentArray();
            int count = 0;
            try {
                ArrivalBean bean = (ArrivalBean) tuple.getValue(0);
                Double latitude = bean.getLatitude();
                Double longitude = bean.getLongitude();
                
                for(int i =0; i<dists.size(); i++){
                    
                    if(getDisruptionAreaPolygon(dists.get(i))){
                     
                        count++;
                     }
                        
                }
            } catch (Exception e) {
                LOG.error("INTERSECT:", e);
                collector.reportError(e);
            }
                LOG.info("INTERSECT: disruptions recovered: "+dists.size()+" with "+count+" polygons.");
            
            //Emit unaffected arrivals.
            //Emit affected arrivals.
        }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ArrivalBean"));
    }

    private boolean getDisruptionAreaPolygon(Root.Disruptions.Disruption disruption) {

        ArrayList<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon> polygons;
        //If polygon: check if point is inside polygon. 
        //If no polygon: lines - check if stop falls on line or distance to line segment
        //https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
        try {
            polygons = (ArrayList<Root.Disruptions.Disruption.CauseArea.Boundary.Polygon>) disruption.getCauseArea()
                    .get(0).getBoundary().get(0).getPolygon();
        } catch (Exception e) {
           //LOG.info("INTERSECT: no polygon");
            return false;
        }
        for (int k = 0; k < polygons.size(); k++) {
           // LOG.info("INTERSECT: polygon cordinates are " + polygons.get(k).getCoordinatesEN() + " or "
                  //  + polygons.get(k).getCoordinatesLL());
        }
        return true;

    }
}