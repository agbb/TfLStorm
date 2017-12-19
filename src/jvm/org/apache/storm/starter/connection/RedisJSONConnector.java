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

//Connection to REDIS for persisting JSON data as String using the defualt schema. 
public class RedisJSONConnector implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(RedisJSONConnector.class);
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;
    

    public RedisJSONConnector() {
        LOG.info("REDIS: instantiated");
        makeConnection();
    }


    private void makeConnection() {

        redisClient = new RedisClient(RedisURI.create("redis://127.0.0.1:6379"));
        connection = redisClient.connect();
        syncCommands = connection.sync();
        LOG.info("REDIS: Connected to Redis");
        
       
    }

    public void shutDown() {
        syncCommands.close();
        redisClient.shutdown();
    }

    public void persistJSON(String key, String json){
               
        try{

            syncCommands.lpush(key, json);
            
        }catch(Exception e){
             LOG.error("REDIS: "+e.toString());   
        }
    }
    
   
    
    public ArrayList<String> getJSON(String key){
        
        
        ArrayList<String> output = new ArrayList<String>();
        Long len = syncCommands.llen(key);
        LOG.info("REDIS: attempting persist. Existing Length: "+len);
        Long start = 0L; 
        
        

        try {
            output = (ArrayList<String>) syncCommands.lrange(key, start, len-1);
            
        }catch (Exception e) {
            LOG.error("REDIS: "+e.toString());  
        }

        return output;
    }

//May need this if found to be persisting duplicates.
//     public void persistDisruption(ArrivalDisruptionPair candidate){
   
//         LOG.info("REDIS: new disruption, persisting");
//          try{
//             ByteArrayOutputStream bos = new ByteArrayOutputStream();
//             ObjectOutputStream out = new ObjectOutputStream(bos);
//             out.writeObject(candidate);
//             out.close();
//             byte[] buf = bos.toByteArray();   
//             syncCommands.lpush(disruptionKey, buf);
//             pairCacheInvalid = true;
//         }catch(Exception e){
//              LOG.error("REDIS: "+e.toString());   
//         }
//     }
    
}