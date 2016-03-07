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
var endpointList = [];
var sourcepointList = [];
var _saveFlowchart, elementCount = 0;
jsPlumb.ready(function () {
    var basicType = {
        connector: "StateMachine",
        paintStyle: {strokeStyle: "#216477", lineWidth: 4},
        hoverPaintStyle: {strokeStyle: "blue"}
    };
    jsPlumb.registerConnectionType("basic", basicType);

    var properties = [];
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
            connector: ["Flowchart", {stub: [40, 60], gap: 5, cornerRadius: 5, alwaysRespectStubs: true}],
            connectorStyle: connectorPaintStyle,
            connectorHoverStyle: connectorHoverStyle,
            EndpointOverlays: [],
            maxConnections: -1,
            dragOptions: {},
            connectorOverlays: [
                ["Arrow", {
                    location: 1,
                    visible: true,
                    id: "ARROW",
                    events: {
                        click: function () {
                            alert("you clicked on the arrow overlay")
                        }
                    }
                }],
                ["Label", {
                    location: 0.3,
                    id: "label",
                    cssClass: "aLabel"
                }]
            ]
        },
// the definition of target endpoints (will appear when the user drags a connection)
        targetEndpoint = {
            endpoint: "Dot",
            paintStyle: {fillStyle: "#7AB02C", radius: 11},
            maxConnections: -1,
            dropOptions: {hoverClass: "hover", activeClass: "active"},
            isTarget: true
        };

    var initConn = function (connection) {
        var myLabel = prompt("Enter label text: ", "");
        if (myLabel != null)
            connection.getOverlay("label").setLabel(myLabel);
    };

    jsPlumb.bind("connection", function (connInfo, originalEvent) {
        initConn(connInfo.connection);
    });

    jsPlumb.bind("dblclick", function (conn, originalEvent) {
        if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
            jsPlumb.detach(conn);
    });

    var ep;
    var _addEndpoints = function (toId, sourceAnchors, targetAnchors) {
        for (var i = 0; i < sourceAnchors.length; i++) {
            var sourceUUID = toId + sourceAnchors[i];
            ep = jsPlumb.addEndpoint("flowchart" + toId, sourceEndpoint, {
                anchor: sourceAnchors[i], uuid: sourceUUID
            });
            sourcepointList.push(["flowchart" + toId, ep]);
            ep = null;
        }
        for (var j = 0; j < targetAnchors.length; j++) {
            var targetUUID = toId + targetAnchors[j];
            ep = jsPlumb.addEndpoint("flowchart" + toId, targetEndpoint, {
                anchor: targetAnchors[j], uuid: targetUUID
            });
            endpointList.push(["flowchart" + toId, ep]);
            ep = null;
        }
    };

    var element = "";
    var clicked = false;
    var to_delete = "";
    $('#startEv').click(function () {
        loadProperties("window start custom jtk-node jsplumb-connected", "5em", "5em", "start", ["BottomCenter"],
            [], false);
        clicked = true;
    });

    $('#stepEv').click(function () {
        loadProperties("window step custom jtk-node jsplumb-connected-step", "13em", "5em", "step",
            ["BottomCenter", "RightMiddle"], ["TopCenter", "LeftMiddle"], true);
        clicked = true;
    });

    $('#descEv').click(function () {
        loadProperties("window diamond custom jtk-node jsplumb-connected-step", "23em", "5em", "decision",
            ["LeftMiddle", "RightMiddle", "BottomCenter"], ["TopCenter"], true);
        clicked = true;
    });

    $('#endEv').click(function () {
        loadProperties("window end jtk-node jsplumb-connected-end", "23em", "15em", "end",
            [], ["TopCenter"], false);
        clicked = true;
    });

    $('#myDiagram').click(function () {
        if (clicked) {
            clicked = false;
            elementCount++;
            var name = "Window" + elementCount;
            var id = "flowchartWindow" + elementCount;
            element = createElement(id);
            if(elementCount == 1 && element.attr("class").indexOf("start") == -1){
                alertify.error("The flowchart diagram should contain a start activity");
                elementCount = 0;
            }else{
                drawElement(element, "#canvas", name);
            }
            element = "";
        }
    });

    function loadProperties(clsName, left, top, label, startpoints, endpoints, contenteditable) {
        properties = [];
        properties.push({
            left: left,
            top: top,
            clsName: clsName,
            label: label,
            startpoints: startpoints,
            endpoints: endpoints,
            contenteditable: contenteditable
        });
    }

    function createElement(id) {
        var elm = $('<div>').addClass(properties[0].clsName).attr('id', id);
        elm.css({
            'top': properties[0].top,
            'left': properties[0].left
        });

        var strong = $('<strong>');
        if (properties[0].clsName == "window diamond custom jtk-node jsplumb-connected-step") {
            var p = "<p style='line-height: 110%; margin-top: 25px' class='desc-text' contenteditable='true' ondblclick='$(this).focus();'>" + properties[0].label + "</p>";
            strong.append(p);
        }
        else if (properties[0].contenteditable) {
            var p = "<p style='line-height: 110%; margin-top: 25px' contenteditable='true' ondblclick='$(this).focus();'>" + properties[0].label + "</p>";
            strong.append(p);
        } else {
            var p = $('<p>').text(properties[0].label);
            strong.append(p);
        }
        elm.append(strong);
        return elm;
    }

    function drawElement(element, canvasId, name) {
        $(canvasId).append(element);
        _addEndpoints(name, properties[0].startpoints, properties[0].endpoints);
        makeResizable('.custom.step');
        jsPlumb.draggable(jsPlumb.getSelector(".jtk-node"), {grid: [20, 20]});
    }

    function makeResizable(classname) {
        $(classname).resizable({
            resize: function (event, ui) {
                jsPlumb.repaint(ui.helper);
            }
        });
    }

    $('#canvas').on('click', '[id^="flowchartWindow"]', function () {
        to_delete = $(this).attr("id");
        $('.step').not(this).css({'border-color': '#29e'});
        $('.diamond').not(this).css({'border-color': '#29e'});
        $('.start').not(this).css({'border-color': 'green'});
        $('.window.jsplumb-connected-end').not(this).css({'border-color': 'orangered'});
        $(this).css({'border-color': 'red'});
    });

    $(document).keypress(function (e) {
        if (e.which == 127) {
            if (to_delete != "") {
                jsPlumb.remove(to_delete);
                elementCount--;
                for (var i = 0; i < editorEndpointList.length; i++) {
                    if (editorEndpointList[i][0] == to_delete) {
                        for (var j = 0; j < editorEndpointList[i].length; j++) {
                            jsPlumb.deleteEndpoint(editorEndpointList[i][j]);
                            editorEndpointList[i][j] = null;
                        }
                    }
                }

                for (var i = 0; i < editorSourcepointList.length; i++) {
                    if (editorSourcepointList[i][0] == to_delete) {
                        for (var j = 0; j < editorSourcepointList[i].length; j++) {
                            jsPlumb.deleteEndpoint(editorSourcepointList[i][j]);
                            editorSourcepointList[i][j] = null;
                        }
                    }
                }
                to_delete = "";
            }
        }else if(e.which == 43){
            var elm = $('.diamond.custom').filter(function(){
                return $(this).css("border-color") == 'rgb(255, 0, 0)';
            });
            if(elm.outerWidth() < 150) {
                elm.outerWidth(elm.outerWidth() + 5);
                elm.outerHeight(elm.outerHeight() + 5);
                var p = elm.children()[0].firstChild;
                p.style.lineHeight = 110 +  '%';
                jsPlumb.repaint(elm.attr("id"));
            }
        }
        else if(e.which == 45){
            var elm = $('.diamond.custom').filter(function(){
                return $(this).css("border-color") == 'rgb(255, 0, 0)';
            });
            if(elm.outerWidth() > 80) {
                elm.outerWidth(elm.outerWidth() - 5);
                elm.outerHeight(elm.outerHeight() - 5);
                jsPlumb.repaint(elm.attr("id"));
                var p = elm.children()[0].firstChild;
                p.style.lineHeight = 110 +  '%';
            }
        }
    });

    _saveFlowchart = function () {
        if (elementCount > 0) {
            var nodes = [];
            $(".jtk-node").each(function (index, element) {
                var $element = $(element);
                var type = $element.attr('class').toString().split(" ")[1];
                if (type == "step" || type == "diamond") {
                    nodes.push({
                        elementId: $element.attr('id'),
                        nodeType: $element.attr('class').toString().split(" ")[1],
                        positionX: parseInt($element.css("left"), 10),
                        positionY: parseInt($element.css("top"), 10),
                        clsName: $element.attr('class').toString(),
                        label: $element.text(),
                        width: $element.outerWidth(),
                        height: $element.outerHeight()
                    });
                } else {
                    nodes.push({
                        elementId: $element.attr('id'),
                        nodeType: $element.attr('class').toString().split(" ")[1],
                        positionX: parseInt($element.css("left"), 10),
                        positionY: parseInt($element.css("top"), 10),
                        clsName: $element.attr('class').toString(),
                        label: $element.text()
                    });
                }
            });

            var connections = [];
            $.each(jsPlumb.getConnections(), function (index, connection) {
                connections.push({
                    connectionId: connection.id,
                    sourceUUId: connection.endpoints[0].getUuid(),
                    targetUUId: connection.endpoints[1].getUuid(),
                    label: connection.getOverlay("label").getLabel()
                });
            });

            var sourceEps = [];
            var targetEps = [];
            var flowchart = {};
            flowchart.nodes = nodes;
            flowchart.connections = connections;
            flowchart.numberOfElements = elementCount;

            $.ajax({
                url: '/publisher/assets/process/apis/upload_flowchart',
                type: 'POST',
                data: {
                    'processName': $("#pName").val(),
                    'processVersion': $("#pVersion").val(),
                    'flowchartJson': JSON.stringify(flowchart)
                },
                success: function (response) {
                    alertify.success("Successfully saved the flowchart.");
                    $("#flowchartOverviewLink").attr("href", "../process/details/" + response);
                },
                error: function () {
                    alertify.error('Flowchart saving error');
                }
            });

        } else {
            alertify.error('Flowchart content is empty.');
        }
    }
});