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
import org.apache.storm.starter.timetable.*;
import org.apache.storm.starter.timetable.xml.*;

public class TimetableRedisConnector implements Serializable {

   private static final Logger LOG = LoggerFactory.getLogger(TimetableRedisConnector.class);
    private RedisClient redisClient;
    private StatefulRedisConnection<byte[], byte[]> connection;
    private RedisCommands<byte[], byte[]> syncCommands;
    // private RedisConnector connectorSingleton;

    public TimetableRedisConnector() {
        LOG.info("REDIS TIMETABLE: instantiated"); 
      makeConnection();
    }

    
    public void makeConnection() {

        redisClient = new RedisClient(RedisURI.create("redis://127.0.0.1:6379"));
        connection = redisClient.connect(new ByteArrayCodec());
        syncCommands = connection.sync();
        LOG.info("REDIS TIMETABLE: Connected to Redis");
        
        //PERSIST UNDER ROUTE NAME
    }
    
    public void test(){
         LOG.info("test");
    }
    
    public void persistTimetable(String routeName, TransXChange timetable) throws java.io.IOException{
        
        byte[] key = routeName.getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(timetable);
        out.close();
        byte[] buf = bos.toByteArray();   
        syncCommands.set(key, buf);
  
    }
    
    public TransXChange getTimetable(String routeName) throws java.io.IOException, java.lang.ClassNotFoundException{
        
        try{
            byte[] key = routeName.getBytes();
            byte[] data = syncCommands.get(key);
            LOG.info("REDIS TIMETABLE "+(key==null));
            LOG.info("REDIS TIMETABLE "+(data==null));
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            TransXChange timetableObject = (TransXChange) in.readObject(); 
            return timetableObject;
        }catch(Exception e){
            LOG.error("REDIS TIMETABLE: "+e);
        }
            return null;
        
        
    }
    
  
    public void flushRedis(){
         LOG.info("****FLUSHING REDIS****");
         syncCommands.flushall();   
    }
    
    public void shutDown() {
        syncCommands.close();
        redisClient.shutdown();
    }
    
}