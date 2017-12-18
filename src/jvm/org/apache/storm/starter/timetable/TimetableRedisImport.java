package org.apache.storm.starter.timetable;

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

public class TimetableRedisImport implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TimetableRedisImport.class);
    private RedisClient redisClient;
    private StatefulRedisConnection<byte[], byte[]> connection;
    private RedisCommands<byte[], byte[]> syncCommands;
    // private RedisConnector connectorSingleton;

    public TimetableRedisImport() {
        LOG.info("REDIS TIMETABLE: instantiated"); 
        makeConnection();
    }

    private void makeConnection() {

        redisClient = new RedisClient(RedisURI.create("redis://127.0.0.1:6379"));
        connection = redisClient.connect(new ByteArrayCodec());
        syncCommands = connection.sync();

        LOG.info("REDIS TIMETABLE: Connected to Redis");
        
        //PERSIST UNDER ROUTE NAME
    }

    public void shutDown() {
        syncCommands.close();
        redisClient.shutdown();
    }
    
}