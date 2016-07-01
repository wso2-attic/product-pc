/*
 * Copyright (c) 2015, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
$(document).ready(function () {
    var viewFlag = false;

    var processPath = $("#divTextProcessPath").text().trim(); // process text resource path
    var processName = $("#divProcessName").text().trim(); //current process name
    var bpmnPath = $("#divBpmnPath").text().trim(); //bpmn xml resource path

    var mainProcessPath;

    if (processPath !== "NA") { // if process text added
        processPath = "/_system/governance/" + processPath;
    }
    if (bpmnPath !== "NA") { // if bpmn path added
        bpmnPath = "/_system/governance/" + bpmnPath;
    }

    //get main process path
    $.get("/store/apis/assets?type=process", function (response) {
        for (var i in response.data) {
            var item = response.data[i];
            if (processName == item.attributes.overview_name) {
                mainProcessPath = item.path;
            }
        }

    });

    $("#btnView").hide(); //by default hide
    $("#btnCollapse").show();
    $("#collapsedProcessName").hide();

    $("#btnView").on("click", function () {
        $(".asset-description").show();
        $(".margin-bottom-double").show();
        $("#btnView").hide();
        $("#btnCollapse").show();
        $("#collapsedProcessName").hide();

        viewFlag = true;
    });

    $("#btnCollapse").on("click", function () {
        viewFlag = false;
        $("#btnView").show();
        $("#btnCollapse").hide();
        $(".asset-description").hide();
        $(".margin-bottom-double").hide();
        $("#collapsedProcessName").show();
    });
    //get associations on click
    $("#tab-relations").on("click", function () {
        document.getElementById("predecessor-list-group").innerHTML = "";
        document.getElementById("subprocess-list-group").innerHTML = "";
        document.getElementById("successor-list-group").innerHTML = "";
        if (mainProcessPath !== "NA") {
            $.ajax({
                type: "GET",
                url: "/store/assets/process/apis/getAssociations",
                data: {
                    processPath: mainProcessPath
                },
                success: function (result) {
                    var associationsObj = JSON.parse(result);

                    var preAssets = {
                        data: []
                    };
                    var subAssets = {
                        data: []
                    };
                    var sucAssets = {
                        data: []
                    };

                    if (associationsObj.subprocesses.length > 0) {

                        for (var i = 0; i < associationsObj.subprocesses.length; i++) {
                            subAssets.data.push({
                                "id": associationsObj.subprocesses[i].id,
                                "name": associationsObj.subprocesses[i].name,
                                "processId": associationsObj.subprocesses[i].processId,
                                "LCState": associationsObj.subprocesses[i].LCState

                            });

                        }
                    }

                    if (associationsObj.successors.length > 0) {
                        for (var i = 0; i < associationsObj.successors.length; i++) {
                            sucAssets.data.push({
                                "id": associationsObj.successors[i].id,
                                "name": associationsObj.successors[i].name,
                                "processId": associationsObj.successors[i].processId,
                                "LCState": associationsObj.successors[i].LCState
                            });
                        }
                    }

                    if (associationsObj.predecessors.length > 0) {
                        for (var i = 0; i < associationsObj.predecessors.length; i++) {
                            preAssets.data.push({
                                "id": associationsObj.predecessors[i].id,
                                "name": associationsObj.predecessors[i].name,
                                "processId": associationsObj.predecessors[i].processId,
                                "LCState": associationsObj.predecessors[i].LCState
                            });
                        }
                    }

                    for (var i = 0; i < preAssets.data.length; i++) {
                        var id = preAssets.data[i].processId;
                        id = id.trim();
                        // check if predecessor is published
                        var processNameStyle = '<span class="glyphicon glyphicon-chevron-right"></span>' + preAssets.data[i].name;

                        if(preAssets.data[i].LCState == 'Published'){
                            processNameStyle = '<a href = /store/assets/process/details/' + id + '>' + processNameStyle + '</a>';
                        }
                        document.getElementById("predecessor-list-group").innerHTML +='<li class="list-group-item">'+ processNameStyle + '</li>';
                    }
                    for (var i = 0; i < subAssets.data.length; i++) {

                        var id = subAssets.data[i].processId;
                        id = id.trim();
                        // check if predecessor is published
                        var processNameStyle = '<span class="glyphicon glyphicon-chevron-right"></span>' + subAssets.data[i].name;
                        if(subAssets.data[i].LCState == 'Published'){
                            processNameStyle = '<a href = /store/assets/process/details/' + id + '>' + processNameStyle + '</a>';
                        }

                        document.getElementById("subprocess-list-group").innerHTML +='<li class="list-group-item">'+ processNameStyle + '</li>';
                    }
                    for (var i = 0; i < sucAssets.data.length; i++) {
                        var id = sucAssets.data[i].processId;
                        id = id.trim();
                        var processNameStyle = '<span class="glyphicon glyphicon-chevron-right"></span>' + sucAssets.data[i].name;

                        if(sucAssets.data[i].LCState == 'Published'){
                            processNameStyle = '<a href = /store/assets/process/details/' + id + '>' + processNameStyle + '</a>';
                        }
                        document.getElementById("successor-list-group").innerHTML +='<li class="list-group-item">'+ processNameStyle + '</li>';
                    }

                }

            });

        }//end of if
        else {
            $("#tab-associations").html("No associations available");
        }

    }); //end of tab click

    // on click of "bpmn model" tab
    $("#tab-model").on("click", function () {
        // get bpmn model if available
        if (bpmnPath !== "NA") {
            $.ajax({
                type: "GET",
                url: "/store/assets/process/apis/get_bpmn_content",
                data: {
                    bpmn_content_path: bpmnPath
                },
                success: function (data) {
                    var response = JSON.parse(data);
                    if (response.error === false) {
                        var bpmnObject = JSON.parse(response.content);
                        $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);
                    } else {
                        alertify.error(response.content);
                    }
                }
            });
        } else {
            $("#tab-bpmn").html("No bpmn model available");
        }
    });

    $("#tab-doc-list").on("click", function () {
        if (mainProcessPath !== "NA") {
            $.ajax({
                url: '/store/assets/process/apis/get_process_doc?process_path=' + mainProcessPath,
                type: 'GET',
                success: function (data) {
                    var responseObj = JSON.parse(data);
                    if (responseObj.error === false) {
                        var response = JSON.parse(responseObj.content);
                        if (response.length != 0) {
                            if (document.getElementById("listDocs").rows.length == 0) {
                                for (var i = 0; i < response.length; i++) {
                                    var table = document.getElementById("listDocs");
                                    var rowCount = table.rows.length;
                                    var row = table.insertRow(rowCount);
                                    var cellDocName = row.insertCell(0);
                                    var cellDocSummary = row.insertCell(1);
                                    var cellDocAction = row.insertCell(2);
                                    cellDocName.innerHTML = response[i].name;
                                    cellDocSummary.innerHTML = response[i].summary;

                                    if (response[i].url != "NA") {
                                        var anchorUrlElement = document.createElement("a");
                                        anchorUrlElement.setAttribute("id", "documentUrl" + i);
                                        anchorUrlElement.setAttribute("href", response[i].url);
                                        anchorUrlElement.setAttribute('target', '_blank');
                                        anchorUrlElement.style.marginRight = "15px";
                                        anchorUrlElement.innerHTML = "open";

                                        viewGoogleDocument(response[i].url, response[i].name, i);
                                        var anchorGoogleDocViewElement = document.createElement("a");
                                        anchorGoogleDocViewElement.setAttribute("id", "googleDocumentView" + i);
                                        anchorGoogleDocViewElement.setAttribute("data-toggle", "modal");
                                        anchorGoogleDocViewElement.setAttribute("data-target", "#docViewModal" + i);
                                        anchorGoogleDocViewElement.setAttribute("href", "#");
                                        anchorGoogleDocViewElement.style.marginRight = "15px";
                                        anchorGoogleDocViewElement.innerHTML = "view";

                                        cellDocAction.appendChild(anchorUrlElement);
                                        cellDocAction.appendChild(anchorGoogleDocViewElement);
                                    } else if (response[i].path != "NA") {
                                        var anchorElement = document.createElement("a");
                                        anchorElement.setAttribute("id", "document" + i);
                                        anchorElement.setAttribute("href", "#");
                                        anchorElement.onclick = (function (currentPath) {
                                            return function () {
                                                downloadDocument(currentPath);
                                            };
                                        })(response[i].path);
                                        anchorElement.innerHTML = "download";
                                        anchorElement.style.marginRight = "15px";
                                        cellDocAction.appendChild(anchorElement);

                                        if (response[i].path.split('.').pop().toLowerCase() == "pdf") {
                                            viewPDFDocument(response[i].path, response[i].name, i);
                                            var anchorPdfViewElement = document.createElement("a");
                                            anchorPdfViewElement.setAttribute("id", "pdfDocumentView" + i);
                                            anchorPdfViewElement.setAttribute("data-toggle", "modal");
                                            anchorPdfViewElement.setAttribute("data-target", "#pdfViewModal" + i);
                                            anchorPdfViewElement.setAttribute("href", "#");
                                            anchorPdfViewElement.style.marginRight = "15px";
                                            anchorPdfViewElement.innerHTML = "view";
                                            cellDocAction.appendChild(anchorPdfViewElement);
                                        }
                                    } else {
                                        cellDocAction.innerHTML = "Not Available";
                                    }
                                }
                            }
                        } else {
                            if (document.getElementById("listDocs").rows.length == 0) {
                                $('#listDocs').append('<tr><td colspan="6">No documentation associated with this process</td></tr>');
                            }
                        }
                    } else {
                        alertify.error(responseObj.content);
                    }
                },
                error: function () {
                    alertify.error('document retrieving error');
                }
            });
        }
    });

    // Get text process content if available
    if (processPath !== "NA") {

        $.ajax({
            type: "GET",
            url: "/store/assets/process/apis/TextContentRetriever",
            data: {
                type: "GET",
                processPath: processPath
            },
            success: function (result) {
                $("#tab-properties").html("<br>" + result);
                $("#btnView").show();
                $("#btnCollapse").hide();
            }
        });
    } else {
        $("#tab-properties").html("No text content available");
    }
});