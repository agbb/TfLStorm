console.log("hello");
var list = [];
$.getJSON('http://46.101.51.59:3000/disruption', function(data) {
    //data is the JSON string
    console.log(data[0]);
    data.forEach(function(item){
        var output = item.disruptionBean.disruptionXml.levelOfInterest+" chance of disruption for "+item.arrivalBean.LineID+" at "+item.arrivalBean.StopPointName+" due to "+item.disruptionBean.disruptionXml.category+" at "+item.disruptionBean.disruptionXml.location+" Incident is "+item.disruptionBean.disruptionXml.status+" and causing "+item.disruptionBean.disruptionXml.severity+" disruption. Route is "+item.distance+"m from incident.";
        var toPush = {'record':output};
        list.push(toPush);
    });
   //var obj = JSON.parse(data);
   console.log(list.length);
    $('#my-table').dynatable({
      dataset: {
        records: list
      },
    });
});

