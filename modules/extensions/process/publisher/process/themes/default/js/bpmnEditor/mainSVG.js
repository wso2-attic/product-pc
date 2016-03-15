processEditor.prototype.refreshWindow = function(svg) {

    var y = window.innerHeight;
    svg.attr("width", "100%").attr("height", y);
};
var y = window.innerHeight;
var xLoc = 500/2 - 25,
      yLoc = y/4;
/*
    var width = window.innerWidth ;*/
var height = window.innerHeight;
var activities = [{id: 0, title: "Activity",  x: xLoc, y: yLoc, activityType:"circle", radius:30}];
var links = [];
var arr = new Array();

// Main svg 
var mainDiv = d3.select("#svgM");
var svg = mainDiv.append("svg")
    .attr("id", "mainSvg")
    .attr("width", "100%")
    .attr("height", height);
var graph = new processEditor(svg, activities, links);
graph.setactivityId(1);
graph.drawGraph();