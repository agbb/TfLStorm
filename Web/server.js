var redis = require('redis');
var http = require('http');
var fs = require('fs');
var client = redis.createClient();

client.on('connect', function() {
    //console.log('connected');
});

var disruptions = [];
var incidents = [];

client.lrange('DISRUPTION', 0, -1, function(err, reply) {
    if(err == null){
         reply.forEach(function(item){

           item = JSON.parse(item);
           disruptions.push(item);
       });
    }else{
        console.log(err);
    }
});


client.lrange('INCIDENT', 0, -1, function(err, reply) {
    if(err == null){
        reply.forEach(function(item){

           item = JSON.parse(item);
           incidents.push(item);
       });


    }else{
       // console.log(err);
    }
});

console.log("JSON loaded");

var app = http.createServer(function(req,res){
    
    console.log("got request at "+req.url); 
    if(req.url == "/incident"){
        res.setHeader('Content-Type', 'application/json')
        res.write(JSON.stringify(incidents));
        res.end();
    }else if(req.url == "/disruption"){
        res.setHeader('Content-Type', 'application/json')
        res.write(JSON.stringify(disruptions));
        res.end();             
    }else if(req.url == "/"){
        fs.readFile('./index.html', function (err, data) {
            if (err) {
               console.log(err); 
            }else{       
                res.writeHeader(200, {"text/javascript": "text/html"});  
                res.write(data);  
                res.end();
            }
        });    
    }else{
        fs.readFile('.'+req.url, function (err, data) {
            if (err) {
               console.log(err); 
            }else{       
                res.writeHeader(200, {"text/javascript": "text/html"});  
                res.write(data);  
                res.end();
            }
        });    
    }
    
});
app.listen(3000);


