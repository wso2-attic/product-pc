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
var editorEndpointList = [];
var editorSourcepointList = [];
var elementCount = 0; // keeps the total element count when creating the element ids
var _loadFlowChart;
var instance; //the jsPlumb instance
var properties = []; //keeps the properties of each element

jsPlumb.ready(function () {
    var element = "";   //the element which will be appended to the canvas
    var clicked = false;    //check whether an element from the palette was clicked

    instance = window.jsp = jsPlumb.getInstance({
        // default drag options
        DragOptions: {
            cursor: 'pointer',
            zIndex: 2000
        },
        //the arrow overlay for the connection
        ConnectionOverlays: [
            ["Arrow", {
                location: 1,
                visible: true,
                id: "ARROW"
            }]
        ],
        Container: "canvas"
    });

    //define basic connection type
    var basicType = {
        connector: "StateMachine",
        paintStyle: {strokeStyle: "#216477", lineWidth: 4},
        hoverPaintStyle: {strokeStyle: "blue"}
    };
    instance.registerConnectionType("basic", basicType);

    //style for the connector
    var connectorPaintStyle = {
            lineWidth: 4,
            strokeStyle: "#61B7CF",
            joinstyle: "round",
            outlineColor: "white",
            outlineWidth: 2
        },

    //style for the connector hover
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

    //the source endpoint definition from which a connection can be started
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
            hoverPaintStyle: endpointHoverStyle,
            connectorHoverStyle: connectorHoverStyle,
            EndpointOverlays: [],
            maxConnections: -1,
            dragOptions: {},
            connectorOverlays: [
                ["Arrow", {
                    location: 1,
                    visible: true,
                    id: "ARROW",
                    direction: 1
                }]
            ]
        },

    //definition of the target endpoint the connector would end
        targetEndpoint = {
            endpoint: "Dot",
            paintStyle: {fillStyle: "#7AB02C", radius: 9},
            maxConnections: -1,
            dropOptions: {hoverClass: "hover", activeClass: "active"},
            isTarget: true
        };

    //set the label of the connector and add the custom close button to the connector
    var init = function (connection, label, width) {
        connection.addOverlay(["Custom", {
            create: function (component) {
                return $("<input type=\"text\" value=\"" + label + "\" readonly style=\"position:absolute; width: " +
                width + "\"\/>");
            },
            location: 0.5,
            id: "label",
            cssClass: "aLabel"
        }]);

        $(".aLabel").css({
            'font-weight': 'bold',
            'text-align': 'center'
        });
    };

    //add the endpoints for the elements
    var ep;
    var _addEditorEndpoints = function (toId, sourceAnchors, targetAnchors) {
        for (var i = 0; i < sourceAnchors.length; i++) {
            var sourceUUID = toId + sourceAnchors[i];
            ep = instance.addEndpoint("flowchart" + toId, sourceEndpoint, {
                anchor: sourceAnchors[i], uuid: sourceUUID
            });
            editorSourcepointList.push(["flowchart" + toId, ep]);
            ep.canvas.setAttribute("title", "Drag a connection from here");
            ep = null;
        }
        for (var j = 0; j < targetAnchors.length; j++) {
            var targetUUID = toId + targetAnchors[j];
            ep = instance.addEndpoint("flowchart" + toId, targetEndpoint, {
                anchor: targetAnchors[j], uuid: targetUUID
            });
            editorEndpointList.push(["flowchart" + toId, ep]);
            ep.canvas.setAttribute("title", "Drop a connection here");
            ep = null;
        }
    };

    //load properties of a given element
    function loadEditorProperties(clsName, left, top, label, startpoints, endpoints, contenteditable, width, height) {
        properties = [];
        properties.push({
            clsName: clsName,
            left: left,
            top: top,
            label: label,
            startpoints: startpoints,
            endpoints: endpoints,
            contenteditable: contenteditable,
            width: width,
            height: height
        });
    }

    //create an element to be drawn on the canvas
    function createEditorElement(id) {
        var elm = $('<div>').addClass(properties[0].clsName).attr('id', id);//add class an id
        if (properties[0].width != -1 && properties[0].height != -1) {//set element width and height
            elm.outerWidth(properties[0].width);
            elm.outerHeight(properties[0].height);
        }

        elm.css({   //set element position
            'top': properties[0].top,
            'left': properties[0].left
        });

        var strong = $('<strong>');

        //set the class of the paragraph node within the diamond and the delete_element icon
        if (properties[0].clsName == "window diamond custom jtk-node jsplumb-connected-step") {
            elm.append("<i style='display: none; margin-left: -5px; margin-top: -50px' " +
            "class=\"fa fa-trash fa-lg close-icon desc-text\"><\/i>");
            var p = "<p style='line-height: 110%; margin-top: 25px; margin-left: -15px' " +
                "class='desc-text' ondblclick='$(this).focus();'>" + properties[0].label
                + "</p>";
            strong.append(p);
        }

        //set the class of the paragraph node within the parallelogram and the delete_element icon
        else if (properties[0].clsName == "window parallelogram step custom jtk-node jsplumb-connected-step") {
            elm.append("<i style='display: none' class=\"fa fa-trash fa-lg close-icon input-text\"><\/i>");
            var p = "<p style='line-height: 110%; margin-top: 25px' class='input-text' " +
                "ondblclick='$(this).focus();'>" + properties[0].label
                + "</p>";
            strong.append(p);
        }

        //set contenteditable and the delete_element icon
        else if (properties[0].contenteditable) {
            elm.append("<i style='display: none' class=\"fa fa-trash fa-lg close-icon\"><\/i>");
            var p = "<p style='line-height: 110%; margin-top: 25px'" +
                "ondblclick='$(this).focus();'>" + properties[0].label + "</p>";
            strong.append(p);
        } else {
            elm.append("<i style='display: none' class=\"fa fa-trash fa-lg close-icon\"><\/i>");
            var p = $('<p>').text(properties[0].label);
            strong.append(p);
        }
        elm.append(strong);
        return elm;
    }

    //draw elements on the canvas
    function drawEditorElement(element, canvasId, name) {
        $(canvasId).append(element);
        _addEditorEndpoints(name, properties[0].startpoints, properties[0].endpoints);
    }

    //load the flowchart from a json string
    _loadFlowChart = function (flowchartPath) {
        flowchartPath = "/_system/governance/flowchart/" + flowchartPath;
        var canvasId = "#editor_canvas";
        $.ajax({
            url: '/publisher/assets/process/apis/get_process_flowchart',
            type: 'GET',
            dataType: 'text',
            data: {'flowchartPath': flowchartPath},
            success: function (data) {
                var response = JSON.parse(data);
                if(response.error == false){
                    if(response.content != "NA") {
                        $('#editor_canvas').html('');
                        $('#fc-content').css('min-height', '400px');
                        var flowchart = JSON.parse(response.content);
                        var nodes = flowchart.nodes;
                        var connections = flowchart.connections;
                        elementCount = flowchart.lastElementId;
                        $.each(nodes, function (index, element) {
                            var name = element.elementId.substring(9);
                            if (element.nodeType == 'start') {
                                loadEditorProperties(element.clsName.substr(0, 46), element.positionX, element.positionY, element.label,
                                    ["BottomCenter"], [], false, -1, -1);
                            } else if (element.nodeType == 'step') {
                                loadEditorProperties(element.clsName.substr(0, 50), element.positionX, element.positionY, element.label,
                                    ["BottomCenter"], ["TopCenter"], true, element.width, element.height);
                            } else if (element.nodeType == 'diamond') {
                                loadEditorProperties(element.clsName.substr(0, 53), element.positionX, element.positionY, element.label,
                                    ["LeftMiddle", "RightMiddle", "BottomCenter"], ["TopCenter"], true, element.width, element.height);
                            } else if (element.nodeType == 'parallelogram') {
                                loadEditorProperties(element.clsName.substr(0, 64), element.positionX, element.positionY, element.label,
                                    ["BottomCenter"], ["TopCenter"], true, element.width, element.height);
                            }
                            else if (element.nodeType == 'end') {
                                loadEditorProperties(element.clsName.substr(0, 48), element.positionX, element.positionY, element.label,
                                    [], ["TopCenter"], false, -1, -1);
                            }
                            var elm = createEditorElement(element.elementId);
                            drawEditorElement(elm, canvasId, name);
                        });

                        $.each(connections, function (index, element) {
                            var width = element.labelWidth;
                            if (width == undefined)
                                width = "20px";
                            var conn = instance.connect({uuids: [element.sourceUUId, element.targetUUId]});
                            init(conn, element.label, element.labelWidth);
                        });
                    }else{
                        $('#editor_canvas').html('');
                        $("#editor_canvas").append("<p>No flowchart available</p>");
                        $('#fc-content').css('min-height', '153px');
                    }
                }

            },
            error: function () {
                alert('Error retrieving flowchart');
            }
        });
    }
});