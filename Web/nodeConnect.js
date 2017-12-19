var redis = require('redis');
var http = require('http');
var client = redis.createClient();

client.on('connect', function() {
    console.log('connected');
});

var disruptions;
var incidents;

client.lrange('DISRUPTION', 0, -1, function(err, reply) {
    if(err == null){
       disruptions = reply;
        console.log("Got disruption data");
    }else{
        console.log(err);
    }
});


client.lrange('INCIDENT', 0, -1, function(err, reply) {
    if(err == null){
       incidents = reply;
        console.log("got incident data");
    }else{
        console.log(err);
    }
});



var app = http.createServer(function(req,res){
    
    if(req.url == "/incident"){
        res.setHeader('Content-Type', 'application/json')
        res.write(JSON.stringify(incidents));
        res.end();
    }else if(req.url == "/disruption"){
        res.write(JSON.stringify(disruptions));
        res.end();
    }else{
         res.write("no good");
        res.end();
        console.log("got request at "+req.url);   
    }
    
});
app.listen(3000);

//Form data into a request 
//Make this act as server for the data from the system.