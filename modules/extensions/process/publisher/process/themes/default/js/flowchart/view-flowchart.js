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
var _saveEditedFlowchart, _loadEditableFlowChart, _deleteFlowchart;
var editable = false,
    editableElmCount = 0; //counts the number of elements on the canvas at any given time
var instance; //the jsPlumb instance
var properties = []; //keeps the properties of each element
var loading = false;
var addedOverlay = false;

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
                return $("<input type=\"text\" value=\"" + label + "\" autofocus style=\"position:absolute; width: " +
                width + "\"\/>");
            },
            location: 0.5,
            id: "label",
            cssClass: "aLabel"
        }]);

        connection.addOverlay(["Custom", {
            create: function (component) {
                return $("<button title=\"Delete the connection\"><i class=\"fa fa-trash\"><\/i><\/button>");
            },
            location: 0.2,
            id: "close",
            cssClass: "close-mark btn btn-danger",
            events: {
                click: function () {
                    instance.detach(connection);
                    $(".start").css({'border': "2px solid green"})
                }
            }
        }]);

        $(".aLabel").css({
            'font-weight': 'bold',
            'text-align': 'center'
        });
    };

    //initialize the connection and show the close button when clicked on the connector
    instance.bind("connection", function (connInfo, originalEvent) {
        if (editable || !loading) {
            init(connInfo.connection, "", "20px");
        }

        connInfo.connection.bind("click", function (conn) {
            $(".jtk-node").css({'outline': "none"});
            conn.showOverlay("close");
        })
    });

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

    //take the x, y coordinates of the current mouse position
    var x, y;
    $(document).on("mousemove", function (event) {
        x = event.pageX;
        y = event.pageY;
        if (clicked) {
            properties[0].top = y - 358;
            properties[0].left = x - 308;
        }
    });

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
                "class='desc-text' contenteditable='true' ondblclick='$(this).focus();'>" + properties[0].label
                + "</p>";
            strong.append(p);
        }

        //set the class of the paragraph node within the parallelogram and the delete_element icon
        else if (properties[0].clsName == "window parallelogram step custom jtk-node jsplumb-connected-step") {
            elm.append("<i style='display: none' class=\"fa fa-trash fa-lg close-icon input-text\"><\/i>");
            var p = "<p style='line-height: 110%; margin-top: 25px' class='input-text' " +
                "contenteditable='true' ondblclick='$(this).focus();'>" + properties[0].label
                + "</p>";
            strong.append(p);
        }

        //set contenteditable and the delete_element icon
        else if (properties[0].contenteditable) {
            elm.append("<i style='display: none' class=\"fa fa-trash fa-lg close-icon\"><\/i>");
            var p = "<p style='line-height: 110%; margin-top: 25px' contenteditable='true' " +
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
        makeResizable('.custom.step');
        instance.draggable(instance.getSelector(".jtk-node"), {
            grid: [20, 20],
            filter: ".ui-resizable-handle"
        });
    }

    //make an element resizable
    function makeResizable(classname) {
        $(classname).resizable({
            resize: function (event, ui) {
                instance.revalidate(ui.helper);
                var marginLeft = $(this).outerWidth() + 8;
                $(this).find("i").css({'margin-left': marginLeft + "px"});
            },
            handles: "all"
        });
    }

    //*********** make the elements on the palette draggable ***************
    function makeDraggable(id, className, text) {
        $(id).draggable({
            helper: function () {
                return $("<div/>", {
                    text: text,
                    class: className
                });
            },
            stack: ".custom",
            revert: false
        });
    }

    makeDraggable("#startEv", "window start jsplumb-connected custom", "start");
    makeDraggable("#stepEv", "window step jsplumb-connected-step custom", "step");
    makeDraggable("#endEv", "window start jsplumb-connected-end custom", "end");

    $("#descEv").draggable({
        helper: function () {
            return createEditorElement("");
        },
        stack: ".custom",
        revert: false
    });

    $("#inpEv").draggable({
        helper: function () {
            return createEditorElement("");
        },
        stack: ".custom",
        revert: false
    });

    //*************************************************************************

    //make the editor canvas droppable
    $("#editor_canvas").droppable({
        accept: ".window",
        drop: function (event, ui) {
            if (clicked) {
                clicked = false;
                elementCount++;
                editableElmCount++;
                var name = "Window" + elementCount;
                var id = "flowchartWindow" + elementCount;
                element = createEditorElement(id);
                //the diagram should contain a start element
                if (editableElmCount == 1 && element.attr("class").indexOf("start") == -1) {
                    alertify.error("The flowchart diagram should have a start element");
                    elementCount = 0;
                    editableElmCount = 0;
                } else {
                    drawEditorElement(element, "#editor_canvas", name);
                }
                element = "";
            }
        }
    });

    //load properties of a start element once the start element in the palette is clicked
    $('#startEv').mousedown(function () {
        loadEditorProperties("window start custom jtk-node jsplumb-connected", "5em", "5em", "start", ["BottomCenter"],
            [], false);
        clicked = true;
    });

    //load properties of a step element once the step element in the palette is clicked
    $('#stepEv').mousedown(function () {
        loadEditorProperties("window step custom jtk-node jsplumb-connected-step", "5em", "5em", "step",
            ["BottomCenter"], ["TopCenter"], true);
        clicked = true;
    });

    //load properties of a decision element once the decision element in the palette is clicked
    $('#descEv').mousedown(function () {
        loadEditorProperties("window diamond custom jtk-node jsplumb-connected-step", "5em", "5em", "decision",
            ["LeftMiddle", "RightMiddle", "BottomCenter"], ["TopCenter"], true, 100, 100);
        clicked = true;
    });

    //load properties of a decision element once the input/output element in the palette is clicked
    $('#inpEv').mousedown(function () {
        loadEditorProperties("window parallelogram step custom jtk-node jsplumb-connected-step", "23em", "5em", "i/o",
            ["BottomCenter"], ["TopCenter"], true);
        clicked = true;
    });

    //load properties of a end element once the end element in the palette is clicked
    $('#endEv').mousedown(function () {
        loadEditorProperties("window end custom jtk-node jsplumb-connected-end", "5em", "5em", "end",
            [], ["TopCenter"], false);
        clicked = true;
    });

    //de-select all the selected elements and hide the delete buttons and highlight the selected element
    $('#editor_canvas').on('click', function (e) {
        $(".jtk-node").css({'outline': "none"});
        $(".close-icon").hide();
        $.each(instance.getConnections(), function (index, connection) {
            connection.hideOverlay("close");
        });
        if (e.target.nodeName == "P") {
            e.target.parentElement.parentElement.style.outline = "4px solid red";
        } else if (e.target.nodeName == "STRONG") {
            e.target.parentElement.style.outline = "4px solid red";
        } else if (e.target.getAttribute("class") != null && e.target.getAttribute("class").indexOf("jtk-node") > -1) {//when clicked the step, decision or i/o elements
            e.target.style.outline = "4px solid red";
        }
    });

    //to make the text field resizable when typing the input text.
    $.fn.textWidth = function (text, font) {//get width of text with font.  usage: $("div").textWidth();
        var temp = $('<span>').hide().appendTo(document.body).text(text || this.val() || this.text()).css('font', font || this.css('font')),
            width = temp.width();
        temp.remove();
        return width;
    };

    $.fn.autoresize = function (options) {//resizes elements based on content size.  usage: $('input').autoresize({padding:10,minWidth:0,maxWidth:100});
        options = $.extend({padding: 10, minWidth: 0, maxWidth: 10000}, options || {});
        $(this).on('input', function () {
            $(this).css('width', Math.min(options.maxWidth, Math.max(options.minWidth, $(this).textWidth() + options.padding)));
        }).trigger('input');
        return this;
    }

    //resize the label text field when typing
    $('#editor_canvas').on('keyup', '.jsplumb-overlay.aLabel', function () {
        $(this).css('font-weight', 'bold');
        $(this).css('text-align', 'center');
        $(this).autoresize({padding: 20, minWidth: 20, maxWidth: 100});
    });

    //when an item is selected, highlight it and show the delete icon
    $(document).on("click", ".custom", function () {
        if ($(this).attr("class").indexOf("diamond") == -1) {
            var marginLeft = $(this).outerWidth() + 8 + "px";
            $(".close-icon").prop("title", "Delete the element");
            $(this).find("i").css({'margin-left': marginLeft, 'margin-top': "-10px"}).show();
        } else {
            $(this).find("i").css({'margin-left': "35px", 'margin-top': "-40px"}).show();
        }
    });

    //when the close-icon of an element is clicked, delete that element together with its endpoints
    $(document).on("click", ".close-icon", function () {
        instance.remove($(this).parent().attr("id"));
        $(".start").css({'border-color': "green"});
        editableElmCount--;

        //if canvas has no element
        if (editableElmCount == 0) {
            elementCount = 0;   //ids start from 1
        }
        //delete the target endpoints
        for (var i = 0; i < editorEndpointList.length; i++) {
            if (editorEndpointList[i][0] == $(this).parent().attr("id")) {
                for (var j = 0; j < editorEndpointList[i].length; j++) {
                    instance.deleteEndpoint(editorEndpointList[i][j]);
                    editorEndpointList[i][j] = null;
                }
            }
        }
        //delete the source endpoints
        for (var i = 0; i < editorSourcepointList.length; i++) {
            if (editorSourcepointList[i][0] == $(this).parent().attr("id")) {
                for (var j = 0; j < editorSourcepointList[i].length; j++) {
                    instance.deleteEndpoint(editorSourcepointList[i][j]);
                    editorSourcepointList[i][j] = null;
                }
            }
        }
    });

    //save the edited flowchart to a json string
    _saveEditedFlowchart = function () {
        if (editableElmCount > 0) {
            var nodes = [];

            //check whether a start element is there in the diagram
            var elm = $(".start.jtk-node");
            if (elm.length == 0) {
                alertify.error("The flowchart diagram should have a start element");
            } else {
                $(".jtk-node").each(function (index, element) {
                    var $element = $(element);
                    var type = $element.attr('class').toString().split(" ")[1];
                    if (type == "step" || type == "diamond" || type == "parallelogram") {
                        nodes.push({
                            elementId: $element.attr('id'),
                            nodeType: type,
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
                $.each(instance.getConnections(), function (index, connection) {
                    connections.push({
                        connectionId: connection.id,
                        sourceUUId: connection.endpoints[0].getUuid(),
                        targetUUId: connection.endpoints[1].getUuid(),
                        label: connection.getOverlay("label").getElement().value,
                        labelWidth: connection.getOverlay("label").getElement().style.width
                    });
                });

                var flowchart = {};
                flowchart.nodes = nodes;
                flowchart.connections = connections;
                flowchart.numberOfElements = editableElmCount;
                flowchart.lastElementId = elementCount;

                $.ajax({
                    url: '/publisher/assets/process/apis/upload_flowchart',
                    type: 'POST',
                    data: {
                        'processName': $("#fcProcessName").val(),
                        'processVersion': $("#fcProcessVersion").val(),
                        'flowchartJson': JSON.stringify(flowchart)
                    },
                    success: function (response) {
                        alertify.success("Successfully saved the flowchart.");
                        $("#fcEditorOverviewLink").attr("href", "../../../assets/process/details/" + response);
                    },
                    error: function () {
                        alertify.error('Flowchart saving error');
                    }
                });

            }
        } else {
            alertify.error('Flowchart content is empty.');
        }
    }

    //load the flowchart from a json string
    _loadEditableFlowChart = function (flowchartString, canvasId) {
        var flowchart = JSON.parse(flowchartString);
        var nodes = flowchart.nodes;
        var connections = flowchart.connections;
        editableElmCount = flowchart.numberOfElements;
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
        loading = true;
        $.each(connections, function (index, element) {
            var width = element.labelWidth;
            if (width == undefined)
                width = "20px";
            var conn = instance.connect({uuids: [element.sourceUUId, element.targetUUId]});
            init(conn, element.label, element.labelWidth);
        });
        editable = true;
    }

    _deleteFlowchart = function (name, version) {
        if (editableElmCount > 0) {
            $.ajax({
                url: '/publisher/assets/process/apis/delete_flowchart',
                type: 'POST',
                data: {
                    'processName': name,
                    'processVersion': version
                },
                success: function (response) {
                    alertify.success("Successfully deleted the flowchart.");
                    var node = document.getElementById("editor_canvas");
                    while (node.hasChildNodes()) {
                        node.removeChild(node.lastChild);
                    }
                    editableElmCount = 0;
                },
                error: function () {
                    alertify.error('Flowchart deleting error');
                }
            });
        } else {
            alertify.error('Flowchart content is empty.');
        }
    }
});