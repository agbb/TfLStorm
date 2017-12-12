package org.apache.storm.starter;

import com.lambdaworks.redis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;

public class RedisConnector implements Serializable{

    private  final Logger LOG = LoggerFactory.getLogger(RedisConnector.class);
    private  RedisClient redisClient = null;
    private  RedisConnection<String, String> stringConnection;
  //  private  RedisConnector connectorSingleton;
    
    public RedisConnector() {
        LOG.info("REDIS: instantiated");
        makeConnection();
    }
//     public static RedisConnector getInstance(){
//         if(connectorSingleton == null){
//            connectorSingleton = new RedisConnector();
//         }
//         return connectorSingleton;
//     }
    private void makeConnection() {

        redisClient = new RedisClient(RedisURI.create("redis://127.0.0.1:6379"));
        stringConnection = redisClient.connect();

        LOG.info("REDIS: Connected to Redis");
    }

    public void shutDown(){
        stringConnection.close();
        redisClient.shutdown();   
    }
    
    public void persist(String key, String data) {
        stringConnection.set(key,data);
    }

    public String retreive(String key) {

        return stringConnection.get(key);
    }

}