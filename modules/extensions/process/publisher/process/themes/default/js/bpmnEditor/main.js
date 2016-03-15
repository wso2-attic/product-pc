var clickedShape;
var updateStr = false;

function update(str, id) {
    // $('#' +id).css("backgroundColor", "white"); 
    clickedShape = str;
    if (clickedShape == 'circle') {

        $("#c1").attr('style', "stroke:red");
        $("#r1").attr('style', "stroke:black");
        $("#p1").attr('style', "stroke:black");
        $("#p2").attr('style', "stroke:black");
        $("#c2").attr('style', "stroke:black");
        $("#c3").attr('style', "stroke:black");
        // $('#' +id).css("backgroundColor", "red"); 
    } else if (clickedShape == 'task') {

        $("#r1").attr('style', "stroke:red");
        $("#c1").attr('style', "stroke:black");
        $("#p1").attr('style', "stroke:black");
        $("#p2").attr('style', "stroke:black");
        $("#c2").attr('style', "stroke:black");
        $("#c3").attr('style', "stroke:black");
    } else if (clickedShape == 'gateway') {

        $("#p1").attr('style', "stroke:red");
        $("#r1").attr('style', "stroke:black");
        $("#c1").attr('style', "stroke:black");
        $("#p2").attr('style', "stroke:black");
        $("#c2").attr('style', "stroke:black");
        $("#c3").attr('style', "stroke:black");
    } else if (clickedShape == 'xor') {

        $("#p2").attr('style', "stroke:red");
        $("#r1").attr('style', "stroke:black");
        $("#p1").attr('style', "stroke:black");
        $("#c1").attr('style', "stroke:black");
        $("#c2").attr('style', "stroke:black");
        $("#c3").attr('style', "stroke:black");
    } else if (clickedShape == 'throw') {

        $("#c2").attr('style', "stroke:red");
        $("#r1").attr('style', "stroke:black");
        $("#p1").attr('style', "stroke:black");
        $("#p2").attr('style', "stroke:black");
        $("#c1").attr('style', "stroke:black");
        $("#c3").attr('style', "stroke:black");
    } else if (clickedShape == 'end') {

        $("#c3").attr('style', "stroke:red");
        $("#r1").attr('style', "stroke:black");
        $("#p1").attr('style', "stroke:black");
        $("#p2").attr('style', "stroke:black");
        $("#c1").attr('style', "stroke:black");
        $("#c2").attr('style', "stroke:black");
    }

}

function unselectAll() {
    $("#c1").attr('style', "stroke:black");
    $("#r1").attr('style', "stroke:black");
    $("#p1").attr('style', "stroke:black");
    $("#p2").attr('style', "stroke:black");
    $("#c2").attr('style', "stroke:black");
    $("#c3").attr('style', "stroke:black");
   
  
}

$(document).keypress(function(e) {
    var id = selectedShapeId;
    var rct = $('#' + id);
    var width = rct.attr("width");
    var height = rct.attr("height");
    var newWidth = width;
    var newHeight = height;
    if (e.which == 61 && width <= 225 && height <= 115) {
        newWidth = parseInt(width) + 15;
        newHeight = parseInt(height) + 5;
        rct.attr("width", newWidth);
        rct.attr("height", newHeight);
        updateNodeDetails(id,newWidth,newHeight);
    } else if (e.which == 45 && width >= 120 && height >= 80) {
        newWidth = parseInt(width) - 15;
        newHeight = parseInt(height) - 5;
        rct.attr("width", newWidth);
        rct.attr("height", newHeight);
        updateNodeDetails(id,newWidth,newHeight);
    }
   
});

var selectedShapeId;

function setSeletedShape(id) {
  
    selectedShapeId = id;
}


/*
window.onbeforeunload = function() {
    return "Save the BPMN diagram  before leaving";
};
*/



function setLinksOnUpload(links, activities) {

    var newlinks = links;
    newlinks.forEach(function(e, i) {
        newlinks[i] = {
            source: activities.filter(function(n) {
                return n.id == e.source;
            })[0],
            target: activities.filter(function(n) {
                return n.id == e.target;
            })[0],
            sourceType: e.sourceType,
            targetType: e.targetType
        };
    });
    return newlinks;

}