/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
$(document).ready(function() {
    chevronProperties = []; //store properties of each chevron
    var processId = 0; // update process model Id
    $("#viewMainProps").show();
    $("#viewElementProps").hide();
    // ajax call to get the process name the view belongs to
    $.ajax({
        type: "GET",
        url: "/publisher/asts/chevron/apis/nameStore",
        data: {
            type: "GET"
        },
        success: function(result) {
            getXmlForChevron(result);
        }
    });
    $('#canvasArea').dblclick(function(e) {  // on double click of the canvas
        $("#viewMainProps").show();  //show main chevron diagram details
        $("#viewElementProps").hide();
    });
    //function to get the specific xml content for the given process
    function getXmlForChevron(chevronName) {
        $.ajax({
            type: "GET",
            url: "/publisher/asts/chevron/apis/chevronxml",
            data: {
                type: "GET",
                name: chevronName
            },
            success: function(xmlResult) {
                drawDiagramOnCanvas(xmlResult);
            }
        });
    }
    //function to draw retrived chevron content to canvas
    function drawDiagramOnCanvas(xmlResult) {
        var test = xmlResult;
        test = test.replace(/&quot;/g, '"');
        test = test.split(',');
        var jsonObj = eval("(" + test + ")");
        // get length of elements  array
        var numberOfElements = jsonObj.diagram[0].chevrons[0].element.length;
        //draw elements 
        for (var i = 0; i < numberOfElements; i++) {
            var chevronId = jsonObj.diagram[0].chevrons[0].element[i].chevronId;
            var chevronName = jsonObj.diagram[0].chevrons[0].element[i].chevronName;
            var positionX = jsonObj.diagram[0].styles[0].format[i].X;
            var positionY = jsonObj.diagram[0].styles[0].format[i].Y;
            var process = jsonObj.diagram[0].chevrons[0].element[i].associatedAsset;

            storePropertiesOfChevron(chevronId, chevronName, process);  // store details for each chevron
            var element1 = $('<div>').attr('id', 'chevronId').addClass('chevron');
            var textField = $('<textArea>').attr({
                name: chevronId
            }).addClass("chevron-textField");
            textField.val(chevronName);
            element1.append(textField);
            element1.find('.chevron-text').position({ // position text box in the center of element
                my: "center",
                at: "center",
                of: element1
            });
            element1.css({
                'top': positionY,
                'left': positionX
            });
            $('#canvasArea').append(element1);
            element1.click(chevronClicked);
        }
    }

    function replaceProcessText(processName) { // replace process model text into a link
        $.ajax({
            type: "GET",
            url: "../apis/processes?type=process",
            data: {
                q: processName
            },
            success: function(Result) {
                setIdOfProcess(Result, processName);
            }
        });
    }

    function setIdOfProcess(results, processName) { // set id of process to the process model link
        var obj = eval("(" + results + ")");
        for (var i in obj) {
            var item = obj[i];
            processId = item.id;
        }
        $("#td_mod").append('<li><a href = ../../../asts/process/details/' + processId + '>' + processModel + '</a></li>');
    }

    function storePropertiesOfChevron(id, name, process) {  // store  details for each chevron
        if (process == '' || process == null) {
            process = "None";
        }
        chevronProperties.push({
            id: id,
            name: name,
            process: process
        });
    }

    function chevronClicked() { // on click retrieve stored properties of each chevron into table fields
        $("#td_mod").html("");
        $("#viewElementProps").show();
        var clickedElement = $(this);
        var id = clickedElement.find('.chevron-textField').attr('name');
        $("#viewMainProps").hide();
        for (var i = 0; i < chevronProperties.length; i++) {
            if (id == chevronProperties[i].id) {
                name = chevronProperties[i].name;
                processModel = chevronProperties[i].process;
                $("#td_name1").html(name);
                if (processModel !== "None") {
                    // set id and name of process as a link
                    replaceProcessText(processModel);
                } else {
                    $("#td_mod").html("None");
                }
            }
        }
    }
});