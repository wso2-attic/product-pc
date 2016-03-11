 processEditor.prototype.mouseUpOnActivity = function(activity, d) {

     var bpmnProcessDiagram = this;
     var stateOfProcessDiagram = bpmnProcessDiagram.stateOfProcessDiagram;
     var constants = bpmnProcessDiagram.constants;

     // Reset the stateOfProcessDiagramOfProcessDiagram
     stateOfProcessDiagram.activityDragWithShift = false;
     activity.classed(constants.connectClass, false);

     var mouseDownOnActivity = stateOfProcessDiagram.mouseDownOnActivity;

     if (!mouseDownOnActivity) return;

     bpmnProcessDiagram.dragLine.classed("hidden", true);

     if (mouseDownOnActivity !== d) {
         // In a different node: create new edge for mousedown edge and add to graph

         var newArrow = {
             source: mouseDownOnActivity,
             target: d,
             sourceType: mouseDownOnActivity.activityType,
             targetType: d.activityType
         };

         var filter = bpmnProcessDiagram.paths.filter(function(d) {
             if (d.source === newArrow.target && d.target === newArrow.source) {
                 bpmnProcessDiagram.links.splice(bpmnProcessDiagram.links.indexOf(d), 1);
             }
             return d.source === newArrow.source && d.target === newArrow.target;
         });

         if (!filter[0].length) {
             bpmnProcessDiagram.links.push(newArrow);
             bpmnProcessDiagram.drawGraph();
         }
     } else {
            // In the same node
            if (stateOfProcessDiagram.dragActive) {               
                stateOfProcessDiagram.dragActive = false;
            } else {
               
                if (d3.event.shiftKey) {
                } else {
                    if (stateOfProcessDiagram.selectedArrowLink) {
                        bpmnProcessDiagram.removeSelectedFromArrowLink();
                    }
                    var prevNode = stateOfProcessDiagram.selectedActivity;

                    if (!prevNode || prevNode.id !== d.id) {
                        bpmnProcessDiagram.replaceSelectedActivity(activity, d);
                    } else {
                        bpmnProcessDiagram.removeSelectedFromActivity();
                    }
                }
            }
        }
     stateOfProcessDiagram.mouseDownOnActivity = null;
   
     return;

 };

 // Mousedown on node
 processEditor.prototype.mouseDownOnActivity = function(activity, d) {

     var bpmnProcessDiagram = this;
     var stateOfProcessDiagram = bpmnProcessDiagram.stateOfProcessDiagram;
     d3.event.stopPropagation();
     stateOfProcessDiagram.mouseDownOnActivity = d;

     //First time when trying to drag a link from a node : place where the arrow head appears
     if (d3.event.shiftKey) {

         stateOfProcessDiagram.activityDragWithShift = d3.event.shiftKey;
         // reposition dragged directed edge
         var startX = d.x + 85;
         var startY = d.y + 45;
         bpmnProcessDiagram.dragLine.classed('hidden', false)
             .attr('d', 'M' + startX + ',' + startY + 'L' + startX + ',' + startY);
         return;
     }
 };

 processEditor.prototype.editText = function(activity, d) {

     var bpmnProcessDiagram = this;

     var textOnActivity = bpmnProcessDiagram.EditTextOfActivity(activity, d);
     var text = textOnActivity.node();
     bpmnProcessDiagram.selectAllContentFromActivity(text);
     text.focus();

 };