$(document).ready(function() {
    $('#flowchartWindow1').resizable();
});

jsPlumb.ready(function () {

    var elementCount = 0;
    var instance = window.jsp = jsPlumb.getInstance({
        // default drag options
        DragOptions: { cursor: 'pointer', zIndex: 2000 },
        // the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
        // case it returns the 'labelText' member that we set on each connection in the 'init' method below.
        ConnectionOverlays: [
            [ "Arrow", {
                location: 1,
                visible:true,
                id:"ARROW",
                events:{
                    click:function() { alert("you clicked on the arrow overlay")}
                }
            } ],
            [ "Label", {
                location: 0.1,
                id: "label",
                cssClass: "aLabel"
            }]
        ],
        Container: "canvas"
    });

    var basicType = {
        connector: "StateMachine",
        paintStyle: { strokeStyle: "#216477", lineWidth: 4 },
        hoverPaintStyle: { strokeStyle: "blue" }
        //overlays: [
        //    "Arrow"
        //]
    };
    instance.registerConnectionType("basic", basicType);

    // this is the paint style for the connecting lines..
    var connectorPaintStyle = {
            lineWidth: 4,
            strokeStyle: "#61B7CF",
            joinstyle: "round",
            outlineColor: "white",
            outlineWidth: 2
        },
    // .. and this is the hover style.
        connectorHoverStyle = {
            lineWidth: 4,
            strokeStyle: "#216477",
            outlineWidth: 2,
            outlineColor: "white"
        },
        endpointHoverStyle = {
            fillStyle: "#216477",
            strokeStyle: "#216477"
        },
    // the definition of source endpoints (the small blue ones)
        sourceEndpoint = {
            endpoint: "Dot",
            paintStyle: {
                strokeStyle: "#7AB02C",
                fillStyle: "transparent",
                radius: 7,
                lineWidth: 3
            },
            isSource: true,
            connector: [ "Flowchart", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true } ],
            connectorStyle: connectorPaintStyle,
            hoverPaintStyle: endpointHoverStyle,
            connectorHoverStyle: connectorHoverStyle,
            dragOptions: {},
            overlays: [
                [ "Label", {
                    location: [0.5, 1.5],
                    label: "Drag",
                    cssClass: "endpointSourceLabel",
                    visible:false
                } ]
            ]
        },
    // the definition of target endpoints (will appear when the user drags a connection)
        targetEndpoint = {
            endpoint: "Dot",
            paintStyle: { fillStyle: "#7AB02C", radius: 11 },
            hoverPaintStyle: endpointHoverStyle,
            maxConnections: -1,
            dropOptions: { hoverClass: "hover", activeClass: "active" },
            isTarget: true,
            overlays: [
                [ "Label", { location: [0.5, -0.5], label: "Drop", cssClass: "endpointTargetLabel", visible:false } ]
            ]
        },
        init = function (connection, myLabel) {
            connection.getOverlay("label").setLabel(myLabel);
        };

    var _addEndpoints = function (toId, sourceAnchors, targetAnchors) {
        for (var i = 0; i < sourceAnchors.length; i++) {
            var sourceUUID = toId + sourceAnchors[i];
            instance.addEndpoint("flowchart" + toId, sourceEndpoint, {
                anchor: sourceAnchors[i], uuid: sourceUUID
            });
        }
        for (var j = 0; j < targetAnchors.length; j++) {
            var targetUUID = toId + targetAnchors[j];
            instance.addEndpoint("flowchart" + toId, targetEndpoint, { anchor: targetAnchors[j], uuid: targetUUID });
        }
    };

    // suspend drawing and initialise.
    instance.batch(function () {
        // listen for new connections; initialise them the same way we initialise the connections at startup.
        instance.bind("connection", function (connInfo, originalEvent) {
            var myLabel = prompt("Enter label text: ", "");
            if(myLabel != null)
                init(connInfo.connection, myLabel);
        });

        //
        // listen for clicks on connections, and offer to delete connections on click.
        //
        instance.bind("click", function (conn, originalEvent) {
           // if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
             //   instance.detach(conn);
            conn.toggleType("basic");
        });

        instance.bind("connectionDrag", function (connection) {
            console.log("connection " + connection.id + " is being dragged. suspendedElement is ", connection.suspendedElement, " of type ", connection.suspendedElementType);
        });

        instance.bind("connectionDragStop", function (connection) {
            console.log("connection " + connection.id + " was dragged");
        });

        instance.bind("connectionMoved", function (params) {
            console.log("connection " + params.connection.id + " was moved");
        });
    });

    $('#startEv').click(function(){
        elementCount++;
        var id = "flowchartWindow" + elementCount;
        var element="";
        element += "<div class=\"window start jtk-node jsplumb-connected ui-resizable\" id=\""+ id +"\" style=\"top: 5em; left: 5em;\"><strong>" +
        "<p>start<\/p><\/strong><br\/><br\/><\/div>"
        $('#canvas').append(element);
        var name = "Window" + elementCount;
        _addEndpoints(name, ["BottomCenter"], []);
        instance.draggable(jsPlumb.getSelector(".jtk-node"), { grid: [20, 20] });
    });

    $('#stepEv').click(function(){
        elementCount++;
        var id = "flowchartWindow" + elementCount;
        var element="";
        element += "<div class=\"window step jtk-node jsplumb-connected-step\" id=\""+ id +"\" style=\"top: 13em; left: 5em;\"><strong>" +
        "<p contenteditable=\"true\">step<\/p><\/strong><br\/><br\/><\/div>"
        $('#canvas').append(element);
        var name = "Window" + elementCount;
        _addEndpoints(name, ["BottomCenter", "RightMiddle"], ["TopCenter", "LeftMiddle"]);
        instance.draggable(jsPlumb.getSelector(".jtk-node"), { grid: [20, 20] });
    });

    $('#descEv').click(function(){
        elementCount++;
        var id = "flowchartWindow" + elementCount;
        var element="";
        element += "<div class=\"window diamond jtk-node jsplumb-connected-step\" id=\""+ id +"\" style=\"top: 23em; left: 5em;\">" +
        "<strong><p class=\"desc-text\" contenteditable=\"true\">decision<\/p><\/strong><br\/><br\/><\/div>";
        $('#canvas').append(element);
        var name = "Window" + elementCount;
        _addEndpoints(name, ["LeftMiddle", "RightMiddle", "BottomCenter"], ["TopCenter"]);
        instance.draggable(jsPlumb.getSelector(".jtk-node"), { grid: [20, 20] });
        $('flowchartWindow1').resizable();
    });

    $('#endEv').click(function(){
        elementCount++;
        var id = "flowchartWindow" + elementCount;
        var element="";
        element += "<div class=\"window start jtk-node jsplumb-connected-end\" id=\""+ id +"\" style=\"top: 23em; left: 5em;\">" +
        "<strong><p>end<\/p><\/strong><br\/><br\/><\/div>";
        $('#canvas').append(element);
        var name = "Window" + elementCount;
        _addEndpoints(name, [], ["TopCenter"]);
        instance.draggable(jsPlumb.getSelector(".jtk-node"), { grid: [20, 20] });
    });
    //
    //alert($('div#flowchartWindow2 > strong > p:eq(0)').text().length);

    //$('div#flowchartWindow2 > strong > p:eq(0)').keypress(function (e) {
    //    alert("hi");
    //    //if (e.keyCode == 13) {
    //    //    alert($('div#flowchartWindow1 > p:eq(0)').text().length);
    //    //}
    //});

    jsPlumb.fire("jsPlumbDemoLoaded", instance);

});