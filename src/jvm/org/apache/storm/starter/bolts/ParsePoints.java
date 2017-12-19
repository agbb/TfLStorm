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
import org.apache.storm.starter.connection.*;
import org.apache.storm.starter.data.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Class that takes the input polygon data and converts it into point representations that can be leveraged in the application. 
//This is a considerable computational burden. 
public class ParsePoints extends BaseBasicBolt {
    RedisConnector RConnect;
    private static final Logger LOG = LoggerFactory.getLogger(ParsePoints.class);

    public void prepare(Map conf, TopologyContext context) {
        RConnect = new RedisConnector();
    }

    @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            ArrayList<Disruption> dists = RConnect.getIncidentArray();
            int count = 0;
            
            try {
                ArrivalBean bean = (ArrivalBean) tuple.getValue(0);
                
                for(int i =0; i<dists.size(); i++){
                    
                    ArrayList<Point> points = getDisruptionAreaPolygon(dists.get(i));
                    if(points.size()>0){
                        ParsedDisruptionBean distBean = new ParsedDisruptionBean();
                        distBean.setDisruptionXml(dists.get(i));
                        distBean.setPointList(points);
                        //Save memory.
                        distBean.getDisruptionXml().nullCauseArea();
                        collector.emit(new Values(bean,distBean));
                    }
                        
                }
            } catch (Exception e) {
                LOG.error("POINTS:", e);
                collector.reportError(e);
            }
        }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ArrivalBean","ParsedDisruptionBean"));
    }

    private ArrayList<Point> getDisruptionAreaPolygon(Disruption disruption) {

        
        //If polygon: check if point is inside polygon. 
        //If no polygon: lines - check if stop falls on line or distance to line segment
        //https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
        ArrayList<Point> points= new ArrayList<Point>();
        try {
            //Disruption either defined by streets or polygon.
            
                //Get the list of streets out of the (VERY) poorly named Streets data structure.
            
            ArrayList<Street> streets = (ArrayList<Street>) disruption.getCauseArea()
                    .get(0).getStreets().get(0).getStreet();
            
            
                
            for(int i =0; i<streets.size(); i++){
                ArrayList<Street.Link> links = (ArrayList<Street.Link>) streets.get(i).getLink();
            
                for(int j =0; j<links.size(); j++){
                   String coords = links.get(i).getLine().get(0).getCoordinatesEN();
                   String[] coordList = coords.split(",");
                   
                   for(int k=0; k<coordList.length; k++){
                        coordList[k] = fixCoord(coordList[k]);
                   }
                    
                   //Flip lat and long.
                   Point pStart = new Point(Double.parseDouble(coordList[0]),Double.parseDouble(coordList[1]));
                   Point pEnd = new Point(Double.parseDouble(coordList[2]),Double.parseDouble(coordList[3]));
                   points.add(pStart);
                   points.add(pEnd);
                   
                }
                
                //LOG.info("POINTS: parsed "+points.size());
            }
           }catch(java.lang.IndexOutOfBoundsException e){
                try{
                    ArrayList<Polygon> polygons = new ArrayList<Polygon>();
                    try{
                        polygons = (ArrayList<Polygon>) disruption.getCauseArea()
                            .get(0).getBoundary().get(0).getPolygon();
                    }catch(Exception e3){
                        //LOG.error("POINTS: couldnt remove points from polygon."+e3);
                    }
                    for (int k = 0; k < polygons.size(); k++) {
                       String coords = polygons.get(k).getCoordinatesEN();
                       String[] coordList = coords.split(",");

                       for(int l=0; l<coordList.length; l++){
                            coordList[l] = fixCoord(coordList[l]);
                       }

                       //Flip lat and long.
                       Point pStart = new Point(Double.parseDouble(coordList[1]),Double.parseDouble(coordList[0]));
                       Point pEnd = new Point(Double.parseDouble(coordList[3]),Double.parseDouble(coordList[2]));
                       points.add(pStart);
                       points.add(pEnd);

                    }
                }catch(java.lang.IndexOutOfBoundsException e2){
                          //LOG.error("POINTS: second attempt at parse failed."+e2);
                }
        }
        
       return points;
    }
    
    //Coordinates unhelpfully dont include a leading zero. This makes: .1 or -.1 into 0.1 or -0.1.
    private String fixCoord(String coord){
       if(coord.contains("-.")){
           coord = coord.substring(0,1)+"0"+coord.substring(1,coord.length()-1);
        } 
       if(coord.substring(0,1).equals(".")){
           coord = "0"+coord;
       }
        return coord;
    }
}