/*
 ~ Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~      http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 */
var appName = "bpmn-process-center-explorer";
var httpUrl = location.protocol + "//" + location.host;
var CONTEXT = "";

var processList;

if (BPSTenant != undefined && BPSTenant.length > 0) {
    CONTEXT = "t/" + BPSTenant + "/jaggeryapps/" + appName;
} else {
    CONTEXT = appName;
}

$("#btnView").show(); //by default hide
$("#btnCollapse").hide();
$("#collapsedProcessName").hide();



$("#btnView").on("click", function() {
    $(".asset-description").hide();
    $(".margin-bottom-double").hide();
    $("#btnView").hide();
    $("#btnCollapse").show();
    $("#collapsedProcessName").show();

    viewFlag = true;
});

$("#btnCollapse").on("click", function() {
    viewFlag = false;
    $("#btnView").show();
    $("#btnCollapse").hide();
    $(".asset-description").show();
    $(".margin-bottom-double").show();
    $("#collapsedProcessName").hide();
});

$("#tab-relations").on("click", function() {

});

function setProcessText(processPath) {
    if (processPath != "NA") {
        var url = "/pc/process-center/process/processText";
        $.ajax({
            type: 'POST',
            url: httpUrl + url,
            contentType: "application/json",
            dataType: "json",
            data: {"processTextPath": processPath},
            success: function (data) {
                //alert(!$.isEmptyObject(data));
                if (!$.isEmptyObject(data)) {
                    $("#tab-properties").html(data[0]);
                } else {
                    $("#tab-properties").html("No text content available");
                }
            },

            error: function (xhr, status, error) {
                var errorJson = eval("(" + xhr.responseText + ")");
                alert(errorJson.message);
            }
        });
    } else {
        $("#tab-properties").html("No text content available");
    }
}

var preAssets = {
    data: []
}
var subAssets = {
    data: []
}
var sucAssets = {
    data: []
}

var predecessors = " ";
var successors = " ";
var subprocesses = " ";

function setAssociations(resourcePath){
    var url = "/pc/process-center/process/associations";
    $.ajax({
        type: 'POST',
        url: httpUrl + url,
        contentType: "application/json",
        dataType: "json",
        data: {"resourcePath":resourcePath},
        success: function (result) {
            var associationsObj = JSON.parse(result);
            if(associationsObj.subprocesses.length > 0){
                for(var i = 0; i < associationsObj.subprocesses.length; i++){
                    subAssets.data.push({
                        "id": associationsObj.subprocesses[i].id,
                        "name":associationsObj.subprocesses[i].name
                    });
                }
            }

            if(associationsObj.successors.length > 0){
                for(var i = 0; i < associationsObj.successors.length; i++){
                    sucAssets.data.push({
                        "id": associationsObj.successors[i].id,
                        "name":associationsObj.successors[i].name
                    });
                }
            }

            if(associationsObj.predecessors.length > 0){
                for(var i = 0; i < associationsObj.predecessors.length; i++){
                    preAssets.data.push({
                        "id": associationsObj.predecessors[i].id,
                        "name":associationsObj.predecessors[i].name
                    });
                }
            }

            for (var i = 0; i < preAssets.data.length; i++) {
                var id = preAssets.data[i].id;
                id = id.trim();
                // check if predecessor is published
                predecessors += '<li><a href=details?q=' + id + '>' + preAssets.data[i].name + '</a></li>';
            }
            for (var i = 0; i < subAssets.data.length; i++) {
                var id = subAssets.data[i].id;
                id = id.trim();
                // check if predecessor is published
                subprocesses += '<li><a href = details?q=' + id + '>' + subAssets.data[i].name + '</a></li>';
            }
            for (var i = 0; i < sucAssets.data.length; i++) {
                var id = sucAssets.data[i].id;
                id = id.trim();
                successors += '<li><a href = details?q=' + id + '>' + sucAssets.data[i].name + '</a></li>';
            }
            $("#preContent").html(predecessors);
            $("#sucContent").html(successors);
            $("#subContent").html(subprocesses);
        },

        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}

function setBPMNModel(bpmnPath){
    // get bpmn model if available
    var url = "/pc/process-center/process/bpmnModel";
    if (bpmnPath !== "NA") {
        $.ajax({
            type: 'POST',
            url: httpUrl + url,
            contentType: "application/json",
            dataType: "json",
            data:{bpmnPath: bpmnPath},
            success: function(result) {
                var bpmnObject = JSON.parse(result);
                if(bpmnObject.bpmnImage != "") {
                    $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);
                }
                else{
                    $("#tab-bpmn").html("No bpmn model available");
                }
            }
        });
    } else {
        $("#tab-bpmn").html("No bpmn model available");
    }
}