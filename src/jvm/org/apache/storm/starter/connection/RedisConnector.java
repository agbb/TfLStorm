package org.apache.storm.starter.connection;

import com.lambdaworks.redis.*;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.codec.StringCodec;
import  com.lambdaworks.redis.codec.ByteArrayCodec;
import com.lambdaworks.redis.output.StatusOutput;
import com.lambdaworks.redis.protocol.CommandArgs;
import com.lambdaworks.redis.protocol.CommandType;
import com.lambdaworks.redis.api.sync.RedisCommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.storm.starter.xml.*;
import org.apache.storm.starter.data.*;

public class RedisConnector implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnector.class);
    private RedisClient redisClient;
    private StatefulRedisConnection<byte[], byte[]> connection;
    private RedisCommands<byte[], byte[]> syncCommands;
    private String keyString = "IncidentKey";
    private String disruptionString = "DisruptionKey";
    private byte[] incidentKey;
    private byte[] disruptionKey;
    private ArrayList<Root.Disruptions.Disruption> cachedIncidents;
    ArrayList<ArrivalDisruptionPair> cachedPairs;
    private boolean incidentCacheInvalid = true;
    private boolean pairCacheInvalid = true;
    // private RedisConnector connectorSingleton;

    public RedisConnector() {
        LOG.info("REDIS: instantiated");
        incidentKey = keyString.getBytes();
        disruptionKey = disruptionString.getBytes();
        makeConnection();
        cachedIncidents = new ArrayList<Root.Disruptions.Disruption>();
        cachedPairs = new ArrayList<ArrivalDisruptionPair>();
    }

    // public static RedisConnector getInstance(){
    // if(connectorSingleton == null){
    // connectorSingleton = new RedisConnector();
    // }
    // return connectorSingleton;
    // }
    private void makeConnection() {

        redisClient = new RedisClient(RedisURI.create("redis://127.0.0.1:6379"));
        connection = redisClient.connect(new ByteArrayCodec());
        syncCommands = connection.sync();
        syncCommands.del(incidentKey);
        syncCommands.del(disruptionKey);
        LOG.info("REDIS: Connected to Redis");
        
        //Periodically invalidate cache
        Timer t = new Timer( );
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                 incidentCacheInvalid = true;
            }
         }, 10000,10000);
    }

    public void shutDown() {
        syncCommands.close();
        redisClient.shutdown();
    }

    public void persistKeyValue(String key, String data) {
        syncCommands.set(key.getBytes(), data.getBytes());
    }

    public String retreive(String key) {

        return new String(syncCommands.get(key.getBytes()));
    }

    public void persistIncident(Root.Disruptions.Disruption disruption){
        
        incidentCacheInvalid = true;
        
        try{
            
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(disruption);
            out.close();
            byte[] buf = bos.toByteArray();   
            //syncCommands.lPush(key, buf);
            syncCommands.lpush(incidentKey, buf);
            
        }catch(Exception e){
             LOG.error("REDIS: "+e.toString());   
        }
    }
    
    public void invalidateIncidentCache(){
       incidentCacheInvalid = true;
    }
    public void invalidatePairCache(){
       pairCacheInvalid = true;
    }
    
    public ArrayList<Root.Disruptions.Disruption> getIncidentArray() {
        
        
        if(incidentCacheInvalid){
            ArrayList<Root.Disruptions.Disruption> output = new ArrayList<Root.Disruptions.Disruption>();
            Long incidentListLength = syncCommands.llen(incidentKey);
            Long start = 0L; 

            try {
                List<byte[]> optionalBytes = syncCommands.lrange(incidentKey, start, incidentListLength-1);
                if (optionalBytes.size()>0){

                    ArrayList<byte[]> outputBytes = (ArrayList<byte[]>)optionalBytes;
                    for(int i =0; i<outputBytes.size(); i++){
                        byte[] bytes = outputBytes.get(i);

                        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                        ObjectInput in = new ObjectInputStream(bis);
                        Root.Disruptions.Disruption disruptionObject = (Root.Disruptions.Disruption) in.readObject(); 
                        output.add(disruptionObject);
                    }
                }
            }catch (Exception e) {
                 LOG.error("REDIS: "+e.toString());  
            }
            cachedIncidents = output;
            incidentCacheInvalid = false;
            return output;
        }else{
            return cachedIncidents;
        }
    }
    
    public ArrayList<ArrivalDisruptionPair> getDisruption(){
        
        
        ArrayList<ArrivalDisruptionPair> output = new ArrayList<ArrivalDisruptionPair>();
        Long disruptionListLength = syncCommands.llen(disruptionKey);
        LOG.info("REDIS: attempting persist. Existing Length: "+disruptionListLength);
        Long start = 0L; 
        
        if(false){//!pairCacheInvalid){
         return cachedPairs;   
        }else{

            try {
                List<byte[]> optionalBytes = syncCommands.lrange(disruptionKey, start, disruptionListLength-1);
                if (optionalBytes.size()>0){
                    ArrayList<byte[]> outputBytes = (ArrayList<byte[]>)optionalBytes;
                    for(int i =0; i<outputBytes.size(); i++){
                        byte[] bytes = outputBytes.get(i);

                        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                        ObjectInput in = new ObjectInputStream(bis);
                        ArrivalDisruptionPair pair = (ArrivalDisruptionPair) in.readObject(); 
                        output.add(pair);
                    }
                }
            }catch (Exception e) {
                LOG.error("REDIS: "+e.toString());  
            }
        }
        cachedPairs = output;
        pairCacheInvalid = false;
        return output;
    }
    
    public void persistDisruption(ArrivalDisruptionPair candidate){
   
        LOG.info("REDIS: new disruption, persisting");
         try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(candidate);
            out.close();
            byte[] buf = bos.toByteArray();   
            syncCommands.lpush(disruptionKey, buf);
            pairCacheInvalid = true;
        }catch(Exception e){
             LOG.error("REDIS: "+e.toString());   
        }
    }
    
}