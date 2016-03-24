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
var editorEndpointList = [];
var editorSourcepointList = [];
var elementCount = 0; // keeps the total element count when creating the element ids
var _saveEditedFlowchart, _loadEditableFlowChart;
var editable = false,
    editableElmCount = 0; //counts the number of elements on the canvas at any given time
jsPlumb.ready(function () {
    var basicType = {
        connector: "StateMachine",
        paintStyle: {strokeStyle: "#216477", lineWidth: 4},
        hoverPaintStyle: {strokeStyle: "blue"}
    };
    jsPlumb.registerConnectionType("basic", basicType);

    var properties = [];
    var loading = false;
    var deleteConnection = null;

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
            paintStyle: {fillStyle: "#7AB02C", radius: 11},
            maxConnections: -1,
            dropOptions: {hoverClass: "hover", activeClass: "active"},
            isTarget: true
        };

    //set the label of the connector
    var init = function (connection, label, width) {
        connection.addOverlay(["Custom", {
            create:function(component) {
                return $("<input type=\"text\" value=\""+ label +"\" autofocus style=\"position:absolute; width: "+ width +"\"\/>");
            },
            location: 0.5,
            id: "label",
            cssClass: "aLabel"
        }]);
        $("input").css('font-weight', 'bold');
        $("input").css('text-align', 'center');
    };

    //to make the text field resizable when typing the input text.
    $.fn.textWidth = function(text, font){//get width of text with font.  usage: $("div").textWidth();
        var temp = $('<span>').hide().appendTo(document.body).text(text || this.val() || this.text()).css('font', font || this.css('font')),
            width = temp.width();
        temp.remove();
        return width;
    };

    $.fn.autoresize = function(options){//resizes elements based on content size.  usage: $('input').autoresize({padding:10,minWidth:0,maxWidth:100});
        options = $.extend({padding:10,minWidth:0,maxWidth:10000}, options||{});
        $(this).on('input', function() {
            $(this).css('width', Math.min(options.maxWidth,Math.max(options.minWidth,$(this).textWidth() + options.padding)));
        }).trigger('input');
        return this;
    }

    //resize the label text field when typing
    $('#editor_canvas').on('keyup', '._jsPlumb_overlay.aLabel', function () {
        $(this).css('font-weight', 'bold');
        $(this).css('text-align', 'center');
        $(this).autoresize({padding:20,minWidth:20,maxWidth:100});
    });

    jsPlumb.bind("connection", function (connInfo, originalEvent) {
        if(editable || !loading) {
            init(connInfo.connection, "", "20px");
        }
    });

    jsPlumb.bind("click", function (conn, originalEvent) {
        to_delete = "";
        jsPlumb.select().setPaintStyle({lineWidth: 4, strokeStyle: "#61B7CF"});
        conn.setPaintStyle({strokeStyle:"red", lineWidth:4});
        $('.step').css({'border-color': '#29e'});
        $('.diamond').css({'border-color': '#29e'});
        $('.start').css({'border-color': 'green'});
        $('.window.jsplumb-connected-end').css({'border-color': 'orangered'});
        deleteConnection = conn;
    });

    //add the endpoints for the elements
    var ep;
    var _addEditorEndpoints = function (toId, sourceAnchors, targetAnchors) {
        for (var i = 0; i < sourceAnchors.length; i++) {
            var sourceUUID = toId + sourceAnchors[i];
            ep = jsPlumb.addEndpoint("flowchart" + toId, sourceEndpoint, {
                anchor: sourceAnchors[i], uuid: sourceUUID
            });
            editorSourcepointList.push(["flowchart" + toId, ep]);
            ep = null;
        }
        for (var j = 0; j < targetAnchors.length; j++) {
            var targetUUID = toId + targetAnchors[j];
            ep = jsPlumb.addEndpoint("flowchart" + toId, targetEndpoint, {
                anchor: targetAnchors[j], uuid: targetUUID
            });
            editorEndpointList.push(["flowchart" + toId, ep]);
            ep = null;
        }
    };

    var element = "";
    var clicked = false;
    var to_delete = "";

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
        var elm = $('<div>').addClass(properties[0].clsName).attr('id', id);
        if (properties[0].clsName.indexOf("step") > -1) {
            elm.outerWidth(properties[0].width);
            elm.outerHeight(properties[0].height);
        }

        elm.css({
            'top': properties[0].top,
            'left': properties[0].left
        });

        var strong = $('<strong>');

        //if the inner label has more than one line
        var str = properties[0].label;
        if (properties[0].label.indexOf("div") > -1) {
            var lines = properties[0].label.split("<div>");
            str = "";
            for (var i = 0; i < lines.length - 1; i++) {
                lines[i] = lines[i].replace("</div>", "");
                str += lines[i] + "<br/>";
            }
            str += lines[lines.length - 1].replace("</div>", "");
        }
        //set the class of the paragraph node within the diamond
        if (properties[0].clsName == "window diamond custom jtk-node jsplumb-connected-step") {
            var p = "<p style='line-height: 110%; margin-top: 25px' class='desc-text' contenteditable='true' ondblclick='$(this).focus();'>" + str
                + "</p>";
            strong.append(p);
        }

        else if (properties[0].clsName == "window parallelogram step custom jtk-node jsplumb-connected-step") {
            var p = "<p style='line-height: 110%; margin-top: 25px' class='input-text' contenteditable='true' ondblclick='$(this).focus();'>" + str
                + "</p>";
            strong.append(p);
        }

        //set contenteditable
        else if (properties[0].contenteditable) {
            var p = "<p style='line-height: 110%; margin-top: 25px' contenteditable='true' ondblclick='$(this).focus();'>" + str + "</p>";
            strong.append(p);
        } else {
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
        jsPlumb.draggable(jsPlumb.getSelector(".jtk-node"), {grid: [20, 20]});
    }

    //make an element resizable
    function makeResizable(classname) {
        $(classname).resizable({
            resize: function (event, ui) {
                jsPlumb.repaint(ui.helper);
            }
        });
    }

    //load properties of a start element once the start element in the palette is clicked
    $('#startEv').click(function () {
        loadEditorProperties("window start custom jtk-node jsplumb-connected", "5em", "5em", "start", ["BottomCenter"],
            [], false);
        clicked = true;
    });

    //load properties of a step element once the step element in the palette is clicked
    $('#stepEv').click(function () {
        loadEditorProperties("window step custom jtk-node jsplumb-connected-step", "5em", "5em", "step",
            ["BottomCenter", "RightMiddle"], ["TopCenter", "LeftMiddle"], true);
        clicked = true;
    });

    //load properties of a decision element once the decision element in the palette is clicked
    $('#descEv').click(function () {
        loadEditorProperties("window diamond custom jtk-node jsplumb-connected-step", "5em", "5em", "decision",
            ["LeftMiddle", "RightMiddle", "BottomCenter"], ["TopCenter"], true);
        clicked = true;
    });

    //load properties of a decision element once the input/output element in the palette is clicked
    $('#inpEv').click(function () {
        loadEditorProperties("window parallelogram step custom jtk-node jsplumb-connected-step", "23em", "5em", "i/o",
            ["BottomCenter", "RightMiddle"], ["TopCenter", "LeftMiddle"], true);
        clicked = true;
    });

    //load properties of a end element once the end element in the palette is clicked
    $('#endEv').click(function () {
        loadEditorProperties("window end jtk-node jsplumb-connected-end", "5em", "5em", "end",
            [], ["TopCenter"], false);
        clicked = true;
    });

    //take the x, y coordinates of the current mouse position
    var x, y;
    $( document ).on( "mousemove", function( event ) {
        x = event.pageX;
        y = event.pageY;
        if(clicked){
            properties[0].top = y - 358;
            properties[0].left = x - 308;
        }
    });

    //once the user clicks on the editor canvas, the element is drawn
    $('#editorDiagram').not('[id^="flowchartWindow"]').click(function () {
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
                elementCount = 0; editableElmCount = 0;
            } else {
                drawEditorElement(element, "#editor_canvas", name);
            }
            element = "";
        }
    });

    //to delete and resize the elements
    $(document).keypress(function (e) {
        if (e.which == 127) {
            if (to_delete != "") {
                jsPlumb.remove(to_delete);
                editableElmCount--;

                //if canvas has no element
                if (editableElmCount == 0) {
                    elementCount = 0;   //ids start from 1
                }

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
            }else if(deleteConnection != null){
                jsPlumb.detach(deleteConnection);
                deleteConnection = null;
            }
        } else if (e.which == 43) {//enlarge diamond when user presses '+' key
            var elm = $('.diamond.custom').filter(function () {
                return $(this).css("border-color") == 'rgb(255, 0, 0)';
            });
            if (elm.outerWidth() < 150) {
                elm.outerWidth(elm.outerWidth() + 5);
                elm.outerHeight(elm.outerHeight() + 5);
                var p = elm.children()[0].firstChild;
                p.style.lineHeight = 110 + '%';
                jsPlumb.repaint(elm.attr("id"));
            }
        }
        else if (e.which == 45) {//shrink the diamond when user presses '-' key
            var elm = $('.diamond.custom').filter(function () {
                return $(this).css("border-color") == 'rgb(255, 0, 0)';
            });
            if (elm.outerWidth() > 80) {
                elm.outerWidth(elm.outerWidth() - 5);
                elm.outerHeight(elm.outerHeight() - 5);
                jsPlumb.repaint(elm.attr("id"));
                var p = elm.children()[0].firstChild;
                p.style.lineHeight = 110 + '%';
            }
        }
    });

    //select the current element and make its boarder red.
    $('#editor_canvas').on('click', '[id^="flowchartWindow"]', function () {
        deleteConnection = null;
        to_delete = $(this).attr("id");
        $('.step').not(this).css({'border-color': '#29e'});
        $('.diamond').not(this).css({'border-color': '#29e'});
        $('.start').not(this).css({'border-color': 'green'});
        $('.window.jsplumb-connected-end').not(this).css({'border-color': 'orangered'});
        jsPlumb.select().setPaintStyle({lineWidth: 4, strokeStyle: "#61B7CF"});
        $(this).css({'border-color': 'red'});
    });

    //save the edited flowchart to a json string
    _saveEditedFlowchart = function () {
        if (editableElmCount > 0) {
            var nodes = [];

            //check whether a start element is there in the diagram
            var elm = $(".start.jtk-node");
            if(elm.length == 0){
                alertify.error("The flowchart diagram should have a start element");
            }else{
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
                            label: $element.children()[0].firstChild.innerHTML,
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
                    ["BottomCenter", "RightMiddle"], ["TopCenter", "LeftMiddle"], true, element.width, element.height);
            } else if (element.nodeType == 'diamond') {
                loadEditorProperties(element.clsName.substr(0, 53), element.positionX, element.positionY, element.label,
                    ["LeftMiddle", "RightMiddle", "BottomCenter"], ["TopCenter"], true, element.width, element.height);
            } else if (element.nodeType == 'parallelogram') {
                loadEditorProperties(element.clsName.substr(0, 64), element.positionX, element.positionY, element.label,
                    ["BottomCenter", "RightMiddle"], ["TopCenter", "LeftMiddle"], true, element.width, element.height);
            }
            else if (element.nodeType == 'end') {
                loadEditorProperties(element.clsName.substr(0, 41), element.positionX, element.positionY, element.label,
                    [], ["TopCenter"], false, -1, -1);
            }
            var elm = createEditorElement(element.elementId);
            drawEditorElement(elm, canvasId, name);
        });
        loading = true;
        $.each(connections, function (index, element) {
            var width = element.labelWidth;
            if(width == undefined)
                width = "20px";
            var conn = jsPlumb.connect({uuids: [element.sourceUUId, element.targetUUId]});
            init(conn, element.label, element.labelWidth);
        });
        editable = true;
    }
});