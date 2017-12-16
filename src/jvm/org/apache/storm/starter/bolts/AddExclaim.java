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
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Streets.Street;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption.CauseArea.Boundary.Polygon;

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
            ArrayList<Disruption> dists = RConnect.getIncidentArray();
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
                LOG.info("INTERSECT: disruptions recovered: "+dists.size());
            
            //Emit unaffected arrivals.
            //Emit affected arrivals.
        }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ArrivalBean"));
    }

    private boolean getDisruptionAreaPolygon(Disruption disruption) {

        
        //If polygon: check if point is inside polygon. 
        //If no polygon: lines - check if stop falls on line or distance to line segment
        //https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
        
        try {
            //Disruption either defined by streets or polygon.
            
                //Get the list of streets out of the (VERY) poorly named Streets data structure.
            ArrayList<Street> streets = (ArrayList<Street>) disruption.getCauseArea()
                    .get(0).getStreets().get(0).getStreet();

            LOG.info("INTERSECT: number of streets in disruption: "+streets.size());
                
            for(int i =0; i<streets.size(); i++){
                ArrayList<Street.Link> links = (ArrayList<Street.Link>) streets.get(i).getLink();
                
                 LOG.info("INTERSECT: number of links in disruption: "+links.size());
                
                for(int j =0; j<links.size(); j++){
                   LOG.info("INTERSECT: link co-ordinates: "+links.get(i).getLine().get(0).getCoordinatesLL());
                }
            }
           }catch(java.lang.IndexOutOfBoundsException e){
                LOG.info("failed to find streets, looking for polygon.");
                try{
                    ArrayList<Polygon> polygons;
                    polygons = (ArrayList<Polygon>) disruption.getCauseArea()
                            .get(0).getBoundary().get(0).getPolygon();
                    for (int k = 0; k < polygons.size(); k++) {
                        LOG.info("INTERSECT: polygon cordinates are " + polygons.get(k).getCoordinatesEN() + " or "  + polygons.get(k).getCoordinatesLL());
                    }
                }catch(Exception e2){
                          LOG.error("INTERSECT: "+e2);
                }
        }
        
       return false;
    }
}