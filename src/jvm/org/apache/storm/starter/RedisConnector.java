package org.apache.storm.starter;

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
import org.apache.storm.starter.xml.*;

public class RedisConnector implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnector.class);
    private RedisClient redisClient;
    private StatefulRedisConnection<byte[], byte[]> connection;
    private RedisCommands<byte[], byte[]> syncCommands;
    private String keyString = "IncidentKey";
    private byte[] key;
    // private RedisConnector connectorSingleton;

    public RedisConnector() {
        LOG.info("REDIS: instantiated");
        key = keyString.getBytes();
        makeConnection();
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
        syncCommands.del(key);
        LOG.info("REDIS: Connected to Redis");
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
        
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(disruption);
            out.close();
            byte[] buf = bos.toByteArray();
            String bufferString = new String(buf, "UTF-8");
            LOG.info("REDIS: pesisting new item.");
            //syncCommands.lPush(key, buf);
            syncCommands.lpush(key, buf);
        }catch(Exception e){
             LOG.error("REDIS: "+e.toString());   
        }
    }
    
    
    
    public ArrayList<Root.Disruptions.Disruption> getIncidentArray() {
        ArrayList<Root.Disruptions.Disruption> output = new ArrayList<Root.Disruptions.Disruption>();
        Long incidentListLength = syncCommands.llen(key);
        LOG.info("REDIS: Request for removal of "+incidentListLength+" items");
        Long start = 0L; 
        
        try {
            ArrayList<byte[]> outputBytes = (ArrayList<byte[]>)syncCommands.lrange(key, start, incidentListLength-1);
            LOG.info("REDIS: Items found: "+outputBytes.size());
            for(int i =0; i<outputBytes.size(); i++){
                byte[] bytes = outputBytes.get(i);
                
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInput in = new ObjectInputStream(bis);
                Root.Disruptions.Disruption disruptionObject = (Root.Disruptions.Disruption) in.readObject(); 
                output.add(disruptionObject);
            }
            
            
            
        } catch (Exception e) {
               LOG.error("REDIS: "+e.toString());  
        }
        return output;
    }

}