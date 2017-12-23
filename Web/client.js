console.log("hello");
var list = [];
var list2 = [];
$.getJSON('http://46.101.51.59:3000/disruption', function(data) {
    //data is the JSON string
   // console.log(data);
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

    
    $.getJSON('http://46.101.51.59:3000/incident', function(data) {
        //data is the JSON string
        var output = [];
        console.log(data[0]);
        data.forEach(function(dataItem){
            var incident = {};
            var severity=0.0;
            var loi =0.0;
            
            if(dataItem.severity == "Minimal"){
                severity = 1.0;
            }else if(dataItem.severity == "Moderate"){
                severity = 2.0;
            }else if(dataItem.severity == "Serious"){
                severity = 3.0;
            }else if(dataItem.severity == "Severe"){
                severity = 4.0;
            }
            if(dataItem.levelOfInterest == "Low"){
                loi = 1.0;
            }else if(dataItem.levelOfInterest == "Medium"){
                loi = 2.0;
            }else if(dataItem.levelOfInterest == "high"){
                loi = 3.0;
            }
            incident.severity = severity+loi;
            var links = [];
            try{
                dataItem.causeArea[0].streets[0].street.forEach(function(street){
                    try{
                        street.link.forEach(function(link){

                            links.push(link.line[0].coordinatesLL);                     

                        })
                    }catch(err){

                    }
                })
            }catch(err){
                
            }
            incident.links = links;
            output.push(incident);
        }) 
        console.log(output);
    })
    
    $('#my-final-table').dynatable({
      dataset: {
        records: list2
      }
    });
});

