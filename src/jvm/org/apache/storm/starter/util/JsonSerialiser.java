package org.apache.storm.starter.util;

import org.apache.storm.starter.data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.storm.starter.xml.Root.*;
import org.apache.storm.starter.xml.Root.Disruptions.*;
import org.apache.storm.starter.xml.Root.Disruptions.Disruption;

import org.apache.storm.starter.data.*;


//Class making use of the gson library to serialise Java objects into json strings that can be persisted.
public class JsonSerialiser{
    
    public static String serialisePair(ArrivalDisruptionPair pair){
        
        Gson gson = new Gson();
        String json = gson.toJson(pair);
        return json;
    }
    
     public static String serialiseIncident(Disruption incident){
        
        Gson gson = new Gson();
        String json = gson.toJson(incident);
        return json;
    }
    
}