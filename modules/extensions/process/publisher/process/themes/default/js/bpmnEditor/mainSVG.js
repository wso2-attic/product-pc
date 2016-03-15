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

var y = window.innerHeight;
var xLoc = 500/2 - 25,
      yLoc = y/4;

var height = window.innerHeight;
var activities = [{id: 0, title: "Activity",  x: xLoc, y: yLoc, activityType:"circle", radius:30}];
var links = [];
// Main svg 
var mainDiv = d3.select("#svgM");
var svg = mainDiv.append("svg")
    .attr("id", "mainSvg")
    .attr("width", "100%")
    .attr("height", height);
var graph = new processEditor(svg, activities, links);
graph.setactivityId(1);
graph.drawGraph();