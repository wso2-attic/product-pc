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
var processEditor = function(svg, activities, links) {

    var bpmnProcessDiagram = this;
    bpmnProcessDiagram.activityId = 0;

    bpmnProcessDiagram.activities = activities || [];
    bpmnProcessDiagram.links = links || [];


    bpmnProcessDiagram.svg = svg;
    bpmnProcessDiagram.svgGrouping = svg.append("g")
        .classed(bpmnProcessDiagram.constants.bpmnModel, true);
    var svgGrouping = bpmnProcessDiagram.svgGrouping;

    bpmnProcessDiagram.stateOfProcessDiagram = {
        selectedActivity: null,
        selectedArrowLink: null,
        mouseDownOnActivity: null,
        mouseDownOnArrow: null,
        dragActive: false,
        scaleDiagram: false,
        lastKeyDown: -1,
        activityDragWithShift: false,
        selectedText: null
    };

    var definitions = svg.append('svg:defs');
    
     definitions.append('svg:marker')
        .attr('id', 'end-arrow')
        .attr('viewBox', '0 0 20 20')
        .attr('refX', 40)
        .attr('refY', 3.5)
        .attr('markerWidth',10)
        .attr('markerHeight', 10)
        .attr('markerUnits', "strokeWidth")
        .attr('orient', 'auto')
        .append('svg:path')
        .attr('d', 'M0,0 L0,7 L9,3.5 z')
        .attr('fill', '#333');

    // define arrow markers for leading arrow i.e. when dragging between activities
    definitions.append('svg:marker')
        .attr('id', 'mark-end-arrow')
        .attr('viewBox', '0 -5 10 10')
        .attr('refX', 45)
        .attr('markerWidth', 3.5)
        .attr('markerHeight', 3.5)
        .attr('orient', 'auto')
        .append('svg:path')
        .attr('d', 'M0,-5L10,0L0,5');
    // displayed when dragging between activities
    bpmnProcessDiagram.dragLine = svgGrouping.append('svg:path')
        .attr('class', 'link dragline hidden')
        .attr('stroke-dasharray','5,5')
        .attr('d', 'M0,0L0,0')
        .style('marker-end', 'url(#mark-end-arrow)');

    //Links or paths between activities
    bpmnProcessDiagram.paths = svgGrouping.append("g")
                                .attr("class", "paths")
                                .selectAll("g");
    //activities or Chevrons
    bpmnProcessDiagram.polygons = svgGrouping.append("g")
                                  .attr("class", "nodes")
                                  .selectAll("g");
    //Drag event of the graph
    bpmnProcessDiagram.drag = d3.behavior.drag()
        .origin(function(d) {
            return {
                x: d.x,
                y: d.y
            };
        })
        .on("drag", function(args) {
            bpmnProcessDiagram.stateOfProcessDiagram.dragActive = true;

            bpmnProcessDiagram.dragFunction.call(bpmnProcessDiagram, args);
        });



    d3.select(window).on("keydown", function() {
            bpmnProcessDiagram.keyDownOnMainSVG.call(bpmnProcessDiagram);
        })
        .on("keyup", function() {
            bpmnProcessDiagram.keyUpOnMainSVG.call(bpmnProcessDiagram);
        });
    svg.on("mousedown", function(d) {
        bpmnProcessDiagram.mouseDownOnMainSVG.call(bpmnProcessDiagram, d);
        
    });
    svg.on("mouseup", function(d) {
        bpmnProcessDiagram.mouseUpOnMainSVG.call(bpmnProcessDiagram, d);

    });

    //Commenting zooming of the graph
    //ZoomFunction() in processEditor.js
    /*var zoomDragged = d3.behavior.zoom()
        .on("zoom", function() {
            bpmnProcessDiagram.zoomFunction.call(bpmnProcessDiagram);
        })
        .on("zoomstart", function() {
            var activeElement = d3.select("#" + bpmnProcessDiagram.constants.activeEditId).node();
            if (activeElement) {
                activeElement.blur();
            }
            if (!d3.event.sourceEvent.shiftKey) d3.select('body').style("cursor", "move");
        })
        .on("zoomend", function() {
            d3.select('body').style("cursor", "auto");
        });

    svg.call(zoomDragged).on("dblclick.zoom", null);*/

    // listen for resize
    /*window.onresize = function() {
        bpmnProcessDiagram.refreshWindow(svg);
    };*/

    // handles the downloaded data in create-asset
    d3.select("#download").on("click", function() {
        var state = "create";
        var arrowFlows = [];

        bpmnProcessDiagram.links.forEach(function(val, i) {
            arrowFlows.push({
                source: val.source.id,
                target: val.target.id,
                sourceType: val.sourceType,
                targetType: val.targetType
            });
        });

        var processName = $("#pName").val();
        var processVersion = $("#pVersion").val();

        if (jQuery.isEmptyObject(bpmnProcessDiagram.activities) && jQuery.isEmptyObject(arrowFlows)) {

            if (confirm("There are no activities or links in the BPMN diagram to save! \n Do you want to proceed?") == true) {

                saveDiagram(bpmnProcessDiagram.activities, arrowFlows,processName,processVersion,state);
            } else {
            }
        } else {
            saveDiagram(bpmnProcessDiagram.activities, arrowFlows,processName,processVersion,state);
        }
    });


    // handles the downloaded data in view-asset
    d3.select("#downloadView").on("click", function() {
        var state = "view";
        var arrowFlows = [];

        bpmnProcessDiagram.links.forEach(function(val, i) {
            arrowFlows.push({
                source: val.source.id,
                target: val.target.id,
                sourceType: val.sourceType,
                targetType: val.targetType
            });
        });

        var processName = $("#beProcessName").val();
        var processVersion = $("#beProcessVersion").val();

        if (jQuery.isEmptyObject(bpmnProcessDiagram.activities) && jQuery.isEmptyObject(arrowFlows)) {

            if (confirm("There are no activities or links in the BPMN diagram to save! \n Do you want to proceed?") == true) {

                saveDiagram(bpmnProcessDiagram.activities, arrowFlows,processName,processVersion,state);
            } else {
            }
        } else {
            saveDiagram(bpmnProcessDiagram.activities, arrowFlows,processName,processVersion,state);
        }
    });

    d3.select("#showBPMNDesign").on("click", function(){
        updateStr = true;

        $("#overviewDiv").hide();
        $("#bpmnEditorView").show();
        $("#pdfUploader").hide();
        $("#docView").hide();
        $("#bpmnView").hide();
        $("#processTextView").hide();
        $("#flowChartEditorView").hide();

        var fieldsName = $("#beProcessName").val();
        var fieldsVersion = $("#beProcessVersion").val();

        $.ajax({
            url: '/publisher/assets/process/apis/get_bpmnEditorDiagram?process_bpmnDesign_path=/_system/governance/bpmnDesign/' + fieldsName + "/" + fieldsVersion,
            type: 'GET',
            dataType: 'text',
            success: function (data) {

                var jsonObj = JSON.parse(data);
                bpmnProcessDiagram.deleteGraph(true);
                bpmnProcessDiagram.activities = jsonObj.activities;
                var max = bpmnProcessDiagram.activities[0].id;
                for (var i = 1; i < bpmnProcessDiagram.activities.length; i++) {

                    if(bpmnProcessDiagram.activities[i].id > max){
                        max = bpmnProcessDiagram.activities[i].id;
                    }
                }
                bpmnProcessDiagram.setactivityId(max+1);

                var newlinks = setLinksOnUpload(jsonObj.links, bpmnProcessDiagram.activities);
                bpmnProcessDiagram.links = newlinks;
                bpmnProcessDiagram.drawGraph();

            },
            error: function () {
                alertify.error('bpmn diagram retrieving error');
            }
        });

    });

};