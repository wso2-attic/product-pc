   processEditor.prototype.removeArrowLinksAssociatedToActivity = function(activity) {
       var bpmnProcessDiagram = this;

       var spliceFunc = bpmnProcessDiagram.links.filter(function(l) {
           return (l.source === activity || l.target === activity);
       });

       spliceFunc.map(function(l) {
           var index = bpmnProcessDiagram.links.indexOf(l);
           bpmnProcessDiagram.links.splice(index, 1);
       });
   };

   processEditor.prototype.replaceSelectedArrowLink = function(arrow, arrowData) {
       var bpmnProcessDiagram = this;

       //   arrow.classed(bpmnProcessDiagram.constants.selectedActivity, true);
       var selectedArrowLink = bpmnProcessDiagram.stateOfProcessDiagram.selectedArrowLink;

       if (selectedArrowLink) {
           bpmnProcessDiagram.removeSelectedFromArrowLink();
       }

       bpmnProcessDiagram.stateOfProcessDiagram.selectedArrowLink = arrowData;
   };

   processEditor.prototype.replaceSelectedActivity = function(activity, activityData) {
       var bpmnProcessDiagram = this;

       // activity.classed(this.constants.selectedActivity, true);
       var selectedActivity = bpmnProcessDiagram.stateOfProcessDiagram.selectedActivity;

       if (selectedActivity) {
           bpmnProcessDiagram.removeSelectedFromActivity();
       }

       bpmnProcessDiagram.stateOfProcessDiagram.selectedActivity = activityData;
   };

   processEditor.prototype.removeSelectedFromActivity = function() {
       var bpmnProcessDiagram = this;
       var idOfSelectedActivity = bpmnProcessDiagram.stateOfProcessDiagram.selectedActivity.id;

       bpmnProcessDiagram.polygons.filter(function(d) {
           return d.id === idOfSelectedActivity;

       });
       //.classed(bpmnProcessDiagram.constants.selectedActivity, false);

       bpmnProcessDiagram.stateOfProcessDiagram.selectedActivity = null;
   };

   processEditor.prototype.removeSelectedFromArrowLink = function() {
       var bpmnProcessDiagram = this;
       var selectedArrowLink = bpmnProcessDiagram.stateOfProcessDiagram.selectedArrowLink;

       bpmnProcessDiagram.paths.filter(function(d) {
           return d === selectedArrowLink;

       });

       //classed(bpmnProcessDiagram.constants.selectedActivity, false);

       bpmnProcessDiagram.stateOfProcessDiagram.selectedArrowLink = null;
   };

   //  deletes the graph
   processEditor.prototype.deleteGraph = function(boolean) {
       var bpmnProcessDiagram = this;
       if (boolean == true) {
           bpmnProcessDiagram.activities = [];
           bpmnProcessDiagram.links = [];
           bpmnProcessDiagram.drawGraph();
       } else {
           if (confirm("Do you want to delete this BPMN diagram?") == true) {
               bpmnProcessDiagram.activities = [];
               bpmnProcessDiagram.links = [];
               bpmnProcessDiagram.drawGraph();
           } else {}
       }
       

   };


  processEditor.prototype.updateNodeDetails = function(id,newWidth,newHeight){

    var activityArr = this.activities;
      console.log(activityArr);
    for (var i = 0, len = activityArr.length; i < len; i++) {
        if(activityArr[i].id == id){
           activityArr[i].width = newWidth;
           activityArr[i].height = newHeight;           
        } 
 
    }
    
};