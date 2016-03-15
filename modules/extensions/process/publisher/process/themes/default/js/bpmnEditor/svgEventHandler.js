/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.w   See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
 // Mousedown on main svg
 processEditor.prototype.mouseDownOnMainSVG = function() {
     var bpmnProcessDiagram = this;
     bpmnProcessDiagram.dragLine.classed("hidden", true);
     this.stateOfProcessDiagram.graphMouseDown = true;     
 };

 // Mouseup on main svg
 processEditor.prototype.mouseUpOnMainSVG = function() {
     
     var bpmnProcessDiagram = this;
     var stateOfProcessDiagram = bpmnProcessDiagram.stateOfProcessDiagram;

     if (stateOfProcessDiagram.scaleDiagram) {
         // dragged not clicked
         stateOfProcessDiagram.scaleDiagram = false;
     } /*else if (clickedShape == null) {
         // confirm("Please select a shape from the palette before clicking on the svg");

     } */else if (stateOfProcessDiagram.graphMouseDown /*&& d3.event.shiftKey*/ && clickedShape != null) {
         // clicked not dragged from svg
         // Add a new node to the svg
               
        var actId = bpmnProcessDiagram.activityId++;
        
        var nodeDetail = saveNodeDetails(d3.mouse(bpmnProcessDiagram.svgGrouping.node()), actId, clickedShape);

         bpmnProcessDiagram.activities.push(nodeDetail);
         
         bpmnProcessDiagram.drawGraph();

         // make title of text immedietly editable
         var d3txt = bpmnProcessDiagram.EditTextOfActivity(bpmnProcessDiagram.polygons.filter(function(dval) {
                 return dval.id === nodeDetail.id;
             }), nodeDetail),
             txtNode = d3txt.node();
         bpmnProcessDiagram.selectAllContentFromActivity(txtNode);
         txtNode.focus();

     } else if (stateOfProcessDiagram.activityDragWithShift) {
         // dragged from node
         stateOfProcessDiagram.activityDragWithShift = false;
         bpmnProcessDiagram.dragLine.classed("hidden", true);
     }
     stateOfProcessDiagram.graphMouseDown = false;
     clickedShape = null;
     unselectAll();
 };

 // Keydown on main svg
 processEditor.prototype.keyDownOnMainSVG = function() {

     var bpmnProcessDiagram = this;
     var stateOfProcessDiagram = bpmnProcessDiagram.stateOfProcessDiagram;
     var constants = bpmnProcessDiagram.constants;

     if (stateOfProcessDiagram.lastKeyDown !== -1) return;

        stateOfProcessDiagram.lastKeyDown = d3.event.keyCode;
        var selectedActivity = stateOfProcessDiagram.selectedActivity;
        var selectedArrowLink = stateOfProcessDiagram.selectedArrowLink;

     if (d3.event.keyCode == constants.DELETE_KEY) {
        //d3.event.preventDefault();
         if (selectedActivity) {
             bpmnProcessDiagram.activities.splice(bpmnProcessDiagram.activities.indexOf(selectedActivity), 1);
             bpmnProcessDiagram.removeArrowLinksAssociatedToActivity(selectedActivity);
             stateOfProcessDiagram.selectedActivity = null;
             bpmnProcessDiagram.drawGraph();
         } else if (selectedArrowLink) {
             bpmnProcessDiagram.links.splice(bpmnProcessDiagram.links.indexOf(selectedArrowLink), 1);
             stateOfProcessDiagram.selectedArrowLink = null;
             bpmnProcessDiagram.drawGraph();
         }
     }

 };

 processEditor.prototype.keyUpOnMainSVG = function() {
     this.stateOfProcessDiagram.lastKeyDown = -1;
 };