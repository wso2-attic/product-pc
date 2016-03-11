var globalArr=[];

function saveNodeDetails(activityCoords, id, activityType) {

    var xycoords = activityCoords;
    

    d = {
        id: id,
        title: "Activity",
        x: xycoords[0],
        y: xycoords[1],
        activityType: activityType
    };
    if (activityType == 'circle') {

        d.radius = 30;

    } else if (activityType == 'gateway') {

        d.points = "0,50, 50,100, 100,50, 50,0";

    } else if (activityType == 'task') {
        d.height = 80;
        d.width = 120;

    } else if (activityType == 'xor') {
        d.points = "0,50, 50,100, 100,50, 50,0";

    } else if (activityType == 'throw') {
        d.radius = 30;

    } else if (activityType == 'end') {
        d.radius = 30;
    }

    return d;
}

function updateNodeDetails(id,newWidth,newHeight){
    
    globalArr.push({

        id: id,
        width:newWidth,
        height: newHeight
    });
    var activityArr = this.activities;
    var arr = d3.select(".nodes")[0];
    var nodess = arr[0].childNodes;
   
    for (var i = 0 ;i < nodess.length; i++) {
        
        var selectedId = nodess[i].childNodes[0].id;
       
        if(selectedId == id){
         
           nodess[i].childNodes[1].setAttribute("x", newWidth/2);
           nodess[i].childNodes[1].setAttribute("y", newHeight/2);

           var spanNodes = nodess[i].childNodes[1].getElementsByTagName("tspan");
           var spanNodelength = spanNodes.length;
          
           for(var j=1; j<spanNodes.length; j++){
                spanNodes[j].setAttribute("x", newWidth/2);
                spanNodes[j].setAttribute("dy", "15");
           }
                     
        }
 
    }
}

function saveDiagram(activities, arrows, processName,processVersion, state) {

    for (var i = 0 ; i < activities.length; i++) {
        for(var j = 0; j < globalArr.length; j++){
            if(activities[i].id == globalArr[j].id){
                activities[i].width = globalArr[j].width;
                activities[i].height = globalArr[j].height;
            }
        }
       
    }
    //Saving and downloading as a json file -> Include FileSaver.min.js
    /*var blob = new Blob([window.JSON.stringify({
        "activities": activities,
        "links": arrows
    })], {
        type: "text/plain;charset=utf-8"
    });

    saveAs(blob, "bpmn.json");*/

    var bpmnEditorObj = {
        "activities": activities,
            "links": arrows
    };

    $.ajax({
        url: '/publisher/assets/process/apis/upload_bpmnEditorDiagram',
        type: 'POST',
        data: {
            'processName': processName,
            'processVersion': processVersion,
            'bpmnEditorJson': JSON.stringify(bpmnEditorObj)
        },
        success: function (response) {
            alertify.success("Successfully saved the bpmn diagram.");
            if(state = "create") {
                $("#bpmnDesignOverviewLink").attr("href", "../process/details/" + response);
            } else {
                $("#beOverviewLink").attr("href", "../../../assets/process/details/" + response);
            }
        },
        error: function () {
            alertify.error('bpmn diagram saving error');
        }
    });

}