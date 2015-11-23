/*
 ~ Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~      http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 */
var appName = "bpmn-analytics-explorer";
var httpUrl = location.protocol + "//" + location.host;
var CONTEXT = "";

if (BPSTenant != undefined && BPSTenant.length > 0) {
    CONTEXT = "t/" + BPSTenant + "/jaggeryapps/" + appName;
} else {
    CONTEXT = appName;
}

//$( document ).ready(function() {
//    //drawAvgExecuteTimeVsProcessIdResults();
//});

/**
 Function to set date picker to date input elements
 */
function setDatePicker (dateElement) {
    var elementID = '#' + dateElement;
    $(elementID).daterangepicker({
        singleDatePicker: true,
        showDropdowns: true,
        locale: {
            format: 'MM/DD/YYYY'
        }
    });
}

function drawAvgExecuteTimeVsProcessIdResult(renderElement){
    var renderElementID = '#' + renderElement;
    var startDate = document.getElementById("processIdAvgExecTimeStartDate");
    var startDateTemp = null;
    alert("Start Date:" + startDate.value);
    if (startDate.value.length > 0) {
        startDateTemp = new Date(startDate.value).getTime();
        alert(startDateTemp);
    }

    var endDate = document.getElementById("processIdAvgExecTimeEndDate");
    var endDateTemp = null;
    alert("End Date:" + endDate.value);
    if (endDate.value.length > 0) {
        endDateTemp = new Date(endDate.value).getTime();
        alert(endDateTemp);
    }

    if(startDateTemp != null && endDate != null){
        var body = {
            'startTime': startDateTemp,
            'endTime': endDateTemp
        };

        //alert(JSON.stringify(body));

        var url = "/" + CONTEXT + "/avg_time_vs_process_id";

        $.ajax({
            type: 'POST',
            //contentType: "application/json",
            url: httpUrl + url,
            data: {'dateRange': JSON.stringify(body)},
            success: function(data){
                alert(data);
                var dataStr = JSON.parse(data);
                var dataset = [];
                for(var i = 0 ; i < dataStr.length ; i++){
                    dataset.push({
                        "yData": dataStr[i].processDefKey,
                        "xData": dataStr[i].avgExecutionTime
                    });
                }
                //alert(JSON.stringify(dataset));
                render(renderElementID, dataset, 'AVG Execution Time (ms)', 'Process Definition Key');
            },
            error: function (xhr, status, error) {
                var errorJson = eval("(" + xhr.responseText + ")");
                alert(errorJson.message);
            }
        });
    }
}

function drawProcessInstanceCountVsProcessIdResult(renderElement){
    var renderElementID = '#' + renderElement;
    var startDate = document.getElementById("processInstanceCountProcessDefStartDate");
    var startDateTemp = null;
    if (startDate.value.length > 0) {
        startDateTemp = new Date(startDate.value).getTime();
    }

    var endDate = document.getElementById("processInstanceCountProcessDefEndDate");
    var endDateTemp = null;
    if (endDate.value.length > 0) {
        endDateTemp = new Date(endDate.value).getTime();
    }

    if(startDateTemp != null && endDate != null){
        var body = {
            'startTime': startDateTemp,
            'endTime': endDateTemp
        };

        //alert(JSON.stringify(body));

        var url = "/" + CONTEXT + "/process_instance_count_vs_process_id";

        $.ajax({
            type: 'POST',
            //contentType: "application/json",
            url: httpUrl + url,
            data: {'dateRange': JSON.stringify(body)},
            success: function(data){
                var dataStr = JSON.parse(data);
                var dataset = [];
                for(var i = 0 ; i < dataStr.length ; i++){
                    dataset.push({
                        "yData": dataStr[i].processDefKey,
                        "xData": dataStr[i].processInstanceCount
                    });
                }
                //alert(JSON.stringify(dataset));
                render(renderElementID, dataset, 'Process Instance Count', 'Process Definition Key');
            },
            error: function (xhr, status, error) {
                var errorJson = eval("(" + xhr.responseText + ")");
                alert(errorJson.message);
            }
        });
    }
}

//function drawAvgExecuteTimeVsProcessIdResults(){
//    var url = "/" + CONTEXT + "/avg_time_vs_process_id";
//
//    $.ajax({
//        type: 'POST',
//        contentType: "application/json",
//        url: httpUrl + url,
//        //data: JSON.stringify(body),
//        success: function(data){
//            var dataStr = JSON.parse(data);
//            var dataset = [];
//            for(var i = 0 ; i < dataStr.length ; i++){
//                dataset.push({
//                    "yData": dataStr[i].values.processDefKey,
//                    "xData": dataStr[i].values.avgExecutionTime
//                });
//            }
//            render(dataset, 'AVG Execution Time (ms)', 'Process Definition Key');
//        },
//        error: function (xhr, status, error) {
//            var errorJson = eval("(" + xhr.responseText + ")");
//            alert(errorJson.message);
//        }
//    });
//}

function render(renderElementID, dataset, xTitle, yTitle){
 // Dimensions for the chart: height, width, and space b/t the bars
 var margins = {top: 30, right: 110, bottom: 70, left: 110}
 var height = 550 - margins.left - margins.right,
     width = 900 - margins.top - margins.bottom,
     barPadding = 5;

 // Create a scale for the x-axis based on data
 // >> Domain - min and max values in the dataset (input range)
 // >> Range - physical range of the scale (reversed) (output range)
 var xScale = d3.scale.linear()
     .domain([0, d3.max(dataset, function(d){
      return d.xData;
     })])
     .range([0, width]);

 // Implements the scale as an actual axis
 // >> Orient - places the axis on the left of the graph
 // >> Ticks - number of points on the axis, automated
 var xAxis = d3.svg.axis()
     .scale(xScale)
     .orient('bottom')
     .ticks(10);

 // Creates a scale for the y-axis based on process definition keys
 var yScale = d3.scale.ordinal()
     .domain(dataset.map(function(d){
      return d.yData;
     }))
     .rangeRoundBands([height, 0], 0.1);

 // Creates an axis based off the yScale properties
 var yAxis = d3.svg.axis()
     .scale(yScale)
     .orient('left');

 //add tooltip
 var tooltip = d3.select(renderElementID)
     .append("div")
     .attr("class", "d3-tip");

 //tooltip.append('div')
 //    .attr('class', 'label');

 tooltip.append('div')
     .attr('class', 'contentBox');
    /////////////////////////////////////////////
    d3.select(renderElementID).select("svg").remove();
    ////////////////////////////////////////////
 // Creates the initial space for the chart
 // >> Select - grabs the empty <div> above this script
 // >> Append - places an <svg> wrapper inside the div
 // >> Attr - applies our height & width values from above
 var chart = d3.select(renderElementID)
     .append('svg')
     .attr('width', width + margins.left + margins.right)

     .attr('height', height + margins.top + margins.bottom)
     .append('g')
     .attr('transform', 'translate(' + margins.left + ',' + margins.top + ')');


 // For each value in our dataset, places and styles a bar on the chart

 // Step 1: selectAll.data.enter.append
 // >> Loops through the dataset and appends a rectangle for each value
 chart.selectAll('rect')
     .data(dataset)
     .enter()
     .append('rect')

  // Step 2: X & Y
  // >> X - Places the bars in horizontal order, based on number of
  //        points & the width of the chart
  // >> Y - Places vertically based on scale
     .attr('x', 0)
     .attr('y', function(d){
      return yScale(d.yData);
     })

  // Step 3: Height & Width
  // >> Width - Based on barpadding and number of points in dataset
  // >> Height - Scale using avgExecution Time and height of the chart area
     .attr('height', (height / dataset.length) - barPadding)
     .attr('width', function(d){
      return xScale(d.xData);
     })
     .attr('fill', 'steelblue')

  // Step 4: Info for hover interaction
     .attr('class', function(d){
      return d.yData;

     })
     .attr('id', function(d){
      return d.xData;
     })
     .on("mouseover", function(d) {
      var pos = d3.mouse(this);
      console.log(pos);
      tooltip.style("left", (d3.event.pageX) + "px")
             .style("top", (d3.event.pageY - 100) + "px");

      //tooltip.select('.label').html('AVG Execution Time').style("color", "#000000");
      tooltip.select('.contentBox').html(d.xData);
      tooltip.style('display', 'block');
     })
     .on("mouseout", function() {
      tooltip.style('display', 'none');
     });

 // Renders the yAxis once the chart is finished
 // Moves it to the left 10 pixels so it doesn't overlap
 chart.append('g')
     .attr('class', 'axis')
     .attr('transform', 'translate(-10, 0)')
     .call(yAxis);

 // Appends the xAxis
 chart.append('g')
     .attr('class', 'axis')
     .attr('transform', 'translate(0,' + (height + 10) + ')')
     .call(xAxis);

 // Adds xAxis title
 chart.append('text')
     .text(xTitle)
     //.attr('transform', 'translate('+(width + 15)+', ' + (height + 15) + ')')
     .attr('transform', 'translate('+(width/2 - 50)+', ' + (height + 50) + ')')
 ;

 // Add yAxis title
 chart.append('text')
     .text(yTitle)
     .attr('transform', 'translate(-70, -20)');

}