console.log("hello");
var list = [];
var list2 = [];
$.getJSON('http://46.101.51.59:3000/disruption', function(data) {
    //data is the JSON string
    console.log(data);
    data.forEach(function(item){
        var output = item.disruptionBean.disruptionXml.levelOfInterest+" chance of disruption for "+item.arrivalBean.LineID+" at "+item.arrivalBean.StopPointName+" due to "+item.disruptionBean.disruptionXml.category+" at "+item.disruptionBean.disruptionXml.location+" Incident is "+item.disruptionBean.disruptionXml.status+" and causing "+item.disruptionBean.disruptionXml.severity+" disruption. Route is "+item.distance+"m from incident.";
       list.push({"message name":output,"song":item.disruptionBean.disruptionXml.levelOfInterest});
        
        var toPush = {
            'route':item.arrivalBean.LineID,
            'station':item.arrivalBean.StopPointName,
            'type':item.disruptionBean.disruptionXml.category,
            'status':item.disruptionBean.disruptionXml.status,
            'location':item.disruptionBean.disruptionXml.location,
            'probability':item.disruptionBean.disruptionXml.levelOfInterest,
            'distance':item.distance.toFixed(2),
            'severity':item.disruptionBean.disruptionXml.severity,
            'message':item.disruptionBean.disruptionXml.comments
        };
       list2.push(toPush);
    })
   //var obj = JSON.parse(data);
    
   var x = [
      {
        "message": "Weezer",
        "song": "El Scorcho"
      }
    ]
   
    $('#my-final-table').dynatable({
      dataset: {
        records: list2
      }
    });
});

