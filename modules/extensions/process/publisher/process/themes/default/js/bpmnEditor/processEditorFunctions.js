/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 processEditor.prototype.setactivityId = function(activityId) {
     this.activityId = activityId;
 };

 //Dragging a node
 processEditor.prototype.dragFunction = function(d) {
     var bpmnProcessDiagram = this;
     //When dragging a link/edge from a node before placing it in the target node
     if (bpmnProcessDiagram.stateOfProcessDiagram.activityDragWithShift) {

         var grouping = bpmnProcessDiagram.svgGrouping.node();
        // console.log(d3.mouse(bpmnProcessDiagram.svgGrouping.node())[0]);
         var elements = grouping.getElementsByClassName('activityGrouping');
         var length = elements.length;
         if(length <= 1){
            bpmnProcessDiagram.dragLine.classed("hidden", true);
         } else {
          var startX = d.x + 40;
          var startY = d.y + 45;
          bpmnProcessDiagram.dragLine.attr('d', 'M' + startX + ',' + startY + 'L' + d3.mouse(bpmnProcessDiagram.svgGrouping.node())[0] + ',' + d3.mouse(this.svgGrouping.node())[1]);

         }

     } else {
         d.x += d3.event.dx;
         d.y += d3.event.dy;
         bpmnProcessDiagram.drawGraph();
     }

 };

 // Insert svg line breaks i.e. new line characters
 processEditor.prototype.newLineBreaksActivityText = function(activity, title, activityType,d) {
     // console.log(activityType);
     var xx, yy;
     if (activityType == 'circle' || activityType == 'throw' || activityType == 'end') {
         xx = d.radius;
         yy = d.radius * 3;
     } else if (activityType == 'xor') {
         xx = 48;
         yy = 120;
     } else if (activityType == 'gateway') {
         xx = 48;
         yy = 45;
     } else if (activityType == 'task') {

         xx =d.width/2;
         yy = d.height/2;
     }

    var temp = title;
    temp= temp.replace(/ [^ a-zA-Z0-9]/,"");

    var titleLength = title.length;
    var words = title.split(/\s/);
    var rectLength = xx;
    var titleArray = [];
    if(activityType == 'task'){
        rectLength = d.width;
        var maxChar = ((rectLength)/10)+2;
        var s = words[0];
        for(var i=1; i<words.length; i++){
            var afterAppend = s + " " + words[i];
            console.log("afterAppend " + afterAppend.length);
            if(afterAppend.length < maxChar){
                s = s + " " + words[i];
            } else {
                titleArray.push(s);
		        s = words[i];
                //s = s + words[i+1];
            }
            if(i+1 == words.length){
                titleArray.push(s);
            }
        }
        if(titleArray.length <= 0){
            titleArray.push(title);
        }

             var text = activity.append("text")
             .attr("text-anchor", "middle")
             .attr("alignment-baseline", "baseline")
             .attr("x", xx)
             .attr("y", yy)
             .attr("id", "textLabel");

        for (var i = 0; i < titleArray.length; i++) {
            var tspan = text.append('tspan').text(titleArray[i] + "   ").attr("text-anchor", "middle");
            if (i>0)
                 tspan.attr('x', xx).attr('dy', '15');
        }
    } else {

         var words = title.split(/\s/),
         nwords = words.length;

         var text = activity.append("text")
         .attr("text-anchor", "middle")
         .attr("alignment-baseline", "baseline")
         .attr("x", xx)
         .attr("y", yy)
         .attr("id", "textLabel");

         for (var i = 0; i < words.length; i++) {
             var tspan = text.append('tspan').text(words[i] + "   ").attr("text-anchor", "middle");
             if ((i % 2 != 0) && temp.length >= 10)
                 tspan.attr('x', xx).attr('dy', '15');
         }
    }
 };

 //Editing the text box with the editable text after adding a node
 processEditor.prototype.EditTextOfActivity = function(activity, d) {

     var bpmnProcessDiagram = this;
     var constants = bpmnProcessDiagram.constants;
     var textElement = activity.node();
     activity.selectAll("text").remove();

     //Returns a TextRectangle object that specifies the bounding rectangle of the current element or TextRange object,
     var activityType = d.activityType;
     var h,w;
      if (activityType == 'circle' || activityType == 'throw' || activityType == 'end') {
         h = d.radius* 2;
         w = d.radius * 2;
     } else if (activityType == 'xor') {
         h = 400;
         w = 100;
     } else if (activityType == 'gateway') {
         h = 400;
         w = 100;
     } else if (activityType == 'task') {
         h = d.height;
         w = d.width;
     }

     var boundingRect = textElement.getBoundingClientRect();
     var text = bpmnProcessDiagram.svg.selectAll("foreignObject")
         .data([d])
         .enter()
         .append("foreignObject")
         .attr("height", h)
         .attr("width", w)
         .style("position", "relative")
         .attr("x", d.x)
         .attr("y", d.y + boundingRect.height/4)
         .append("xhtml:p")
         .attr("id", constants.activeEditId)
         .attr("contentEditable", "true")
         .attr("class", "textBox")
         .text(d.title)
         .on("mousedown", function(d) {
             d3.event.stopPropagation(); // Prevent any parent handlers from being notified of the event.
         })
         .on("keydown", function(d) {
             d3.event.stopPropagation();
             if (d3.event.keyCode == constants.ENTER_KEY && !d3.event.shiftKey) {
                 this.blur();
             }
         })
         .on("blur", function(d) {
             d.title = this.textContent;
             bpmnProcessDiagram.newLineBreaksActivityText(activity, d.title, d.activityType, d);
             d3.select(this.parentElement).remove();
         });

     return text;
 };

 // Select all text in the element
 processEditor.prototype.selectAllContentFromActivity = function(activity) {
     var range = document.createRange();
     range.selectNodeContents(activity);
     var select = window.getSelection();
     select.removeAllRanges();
     select.addRange(range);
 };

 // Update changes to the graph
 processEditor.prototype.drawGraph = function() {

     var bpmnProcessDiagram = this;
     var constants = bpmnProcessDiagram.constants;
     var stateOfProcessDiagram = bpmnProcessDiagram.stateOfProcessDiagram;
     //Gets the links/paths of the graph
     bpmnProcessDiagram.paths = bpmnProcessDiagram.paths.data(bpmnProcessDiagram.links, function(d) {

         return String(d.source.id) + "+" + String(d.target.id) + "-" + String(d.sourceType) + "+" + String(d.targetType);
     });
     var paths = bpmnProcessDiagram.paths;


     // update existing paths --- UPDATE

     paths.style('marker-end', 'url(#end-arrow)')
         .classed(constants.selectedActivity, function(d) {
             return d === stateOfProcessDiagram.selectedArrowLink;
         })
         .attr("d", function(d) {
             return drawArrows(d);
         });

     // add new paths ----- CREATE PATHS
     paths.enter()
         .append("path")
         .classed("link", true)
         .attr("fill", "none")
         .attr("stroke", "#000")
         .attr("stroke-width", "4")
         .attr("marker-end", "url(#end-arrow)")
         .attr("d", function(d) {

             return drawArrows(d);

         })
         //When you press on the link
         .on("mousedown", function(d) {
             bpmnProcessDiagram.mouseDownOnArrowLink.call(bpmnProcessDiagram, d3.select(this), d);
         })
         .on("mouseup", function(d) {
             stateOfProcessDiagram.mouseDownOnArrow = null;
         });

     // remove old links
     paths.exit().remove();

     // update existing activities
     bpmnProcessDiagram.polygons = bpmnProcessDiagram.polygons.data(bpmnProcessDiagram.activities, function(d) {
         return d.id;
     });
     bpmnProcessDiagram.polygons.attr("transform", function(d) {
         return "translate(" + d.x + "," + d.y + ")";
     });


     var newGrouping = bpmnProcessDiagram.polygons.enter()
         .append("g");

     newGrouping.classed(constants.activitiyGrouping, true)
         .attr("transform", function(d) {
             return "translate(" + d.x + "," + d.y + ")";
         })
         .on("mouseover", function(d) {
             if (stateOfProcessDiagram.activityDragWithShift) {
                 d3.select(this).classed(constants.connectClass, true);
             } else {
                 /*d3.select(this).append("text")
                     .attr("class", "hover")
                     .attr('transform', function(d) {
                         return 'translate(5, -10)';
                     })
                     .text(d.title);*/
                     if(clickedShape != null){
                         d3.select(this)
                         .append("text")
                         .attr("class", "hover")
                         .attr("font-family", "sans-serif")
                         .attr("font-size", "18px")
                         .attr('transform', function(d) {
                             return 'translate(5, -10)';
                         })
                         .attr('text-anchor', 'middle')
                         .text("An element is already positioned");

                     }

             }
         })
         .on("mouseout", function(d) {
             d3.select(this).classed(constants.connectClass, false);
             d3.select(this).select("text.hover").remove();
         })
         .on("mousedown", function(d) {
             bpmnProcessDiagram.mouseDownOnActivity.call(bpmnProcessDiagram, d3.select(this), d);
         })
         .on("mouseup", function(d) {
             bpmnProcessDiagram.mouseUpOnActivity.call(bpmnProcessDiagram, d3.select(this), d);
         })
         .on("dblclick", function(d) {
             bpmnProcessDiagram.editText.call(bpmnProcessDiagram, d3.select(this), d);
         })
         .on("click", function(d) {
             d3.select(".selected").classed("selected", false);
             d3.select(this).classed("selected", true);
         })
         .call(bpmnProcessDiagram.drag);


     if (updateStr == false) {
         newGrouping.each(function(d) {
             if (clickedShape == null && d.id == 0) {
                drawNodes(newGrouping, d);
                bpmnProcessDiagram.newLineBreaksActivityText(d3.select(this), d.title, d.activityType,d);
             } else {
                 drawNodes(newGrouping, d);
                 bpmnProcessDiagram.newLineBreaksActivityText(d3.select(this), d.title, d.activityType,d);
             }

         });
     } else {
         newGrouping.each(function(d) {

             drawNodes(d3.select(this), d)
             bpmnProcessDiagram.newLineBreaksActivityText(d3.select(this), d.title, d.activityType,d);

         });
     }
     // remove old activities
     bpmnProcessDiagram.polygons.exit().remove();

 };



 //Zooming function
 processEditor.prototype.zoomFunction = function() {
     this.stateOfProcessDiagram.scaleDiagram = true;
     d3.select("." + this.constants.bpmnModel)
         .attr("transform", "translate(" + d3.event.translate + ") scale(" + d3.event.scale + ")");
 };
