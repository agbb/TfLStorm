package org.apache.storm.starter.util;

import uk.me.jstott.jcoord.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.storm.starter.polygon.*;

public class CoordinateConverter{

   
    private static final Logger LOG = LoggerFactory.getLogger(CoordinateConverter.class);
    
    public static Point convertLLtoEN(Point input){

        LatLng latLng = new LatLng(input.y,input.x);
        latLng.toWGS84();
        OSRef en = latLng.toOSRef();
        double east = en.getEasting() + 220;
        double north = en.getNorthing() - 100;
        Point toReturn = new Point(east,north);
   
        return toReturn;
        
    }
}

