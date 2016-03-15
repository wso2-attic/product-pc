function drawNodes(newGrouping, d) {

    var id = d.id;
    var activityType = d.activityType;

    if (activityType == 'circle') {

        newGrouping.append("circle")
            .attr("cx", d.radius)
            .attr("cy", d.radius)
            .attr("r", d.radius)
            .style("fill", "white")
            .style("stroke", "black")
            .attr('id', id)
            .attr("class", "circle")
            .attr('onclick', 'setSeletedShape("")');

    } else if (activityType == 'gateway') {

        newGrouping.append("polygon")
            .style("stroke", "black")
            .style("fill", "white")
            .attr("points", d.points)
            .attr("class", "gateway")
            .attr('id', id)
            .attr('onclick', 'setSeletedShape("")');

    } else if (activityType == 'task') {

        newGrouping.append("rect")
            .attr("width", d.width)
            .attr("height", d.height)
            .style("stroke", "black")
            .style("fill", "white")
            .attr("rx", "15")
            .attr("ry", "15")
            .attr('id', id)
            .attr("class", "task")
            .attr('onclick', 'setSeletedShape(this.id)');

    } else if (activityType == 'xor') {

        newGrouping.append("polygon")
            .style("stroke", "black")
            .style("fill", "white")
            .attr("points", d.points)
            .attr("class", "gateway")
            .attr('id', id)
            .attr('onclick', 'setSeletedShape("")');
        newGrouping.append("line")
            .style("stroke", "black")
            .attr("x1", "25")
            .attr("y1", "25")
            .attr("x2", "75")
            .attr("y2", "75");
        newGrouping.append("line")
            .style("stroke", "black")
            .attr("x1", "75")
            .attr("y1", "25")
            .attr("x2", "25")
            .attr("y2", "75");

    } else if (activityType == 'throw') {

        newGrouping.append("circle")
            .attr("cx", d.radius)
            .attr("cy", d.radius)
            .attr("r", d.radius)
            .style("fill", "white")
            .style("stroke", "black")
            .style("stroke-width", 3)
            .attr('id', id)
            .attr('onclick', 'setSeletedShape("")');

        newGrouping.append("circle")
            .attr("cx", d.radius)
            .attr("cy", d.radius)
            .attr("r", d.radius - 10)
            .style("fill", "white")
            .style("stroke", "black")
            .style("stroke-width", 1);

    } else if (activityType == 'end') {

        newGrouping.append("circle")
            .attr("cx", d.radius)
            .attr("cy", d.radius)
            .attr("r", d.radius - 5)
            .style("fill", "white")
            .style("stroke", "black")
            .style("stroke-width", 7)
            .attr('id', id)
            .attr('onclick', 'setSeletedShape("")');

    }
}