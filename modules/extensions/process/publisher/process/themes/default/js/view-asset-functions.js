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

var processNames = [];
var processListObj;
var pid;
var pdfDoc = null;
var pageNum;
var ctx;
var fieldsName = $("#textProcessName").val();
var fieldsVersion = $("#textProcessVersion").val();

window.onload = function () {

    var url = window.location.toString();
    pid = url.substr(url.lastIndexOf('/') + 1);
    getProcessList();
};

function getMainProcess() {
    var mainProcess = $('#view-header').text() + "-" + $('#process-version').text();
    return mainProcess;
}

function showBPMN() {

    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").show();
    $("#bpmnEditDiv").hide();
    $("#pdfUploaderView").hide();
    $("#holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();

    $.ajax({
        url: '/publisher/assets/process/apis/get_bpmn_content?bpmn_content_path=/_system/governance/bpmn/' + fieldsName + "/" + fieldsVersion,
        type: 'GET',
        success: function (response) {
            var bpmnObject = JSON.parse(response);
            $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);
        },
        error: function () {
            alertify.error('BPMN diagram showing error');
        }
    });

}

function viewText() {
    loadProcessText();

    $("#overviewDiv").hide();
    $("#processTextContainer").show();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#pdfUploaderView").hide();
    $("#holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();
}

function editText() {
    $("#processContent").val($("#processTextDiv").html());
    showTextEditor();
}

function loadProcessText() {
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_text?process_text_path=/processText/' + fieldsName + "/" + fieldsVersion,
        type: 'GET',
        success: function (response) {
            $("#processTextDiv").html(response);
        },
        error: function () {
            alertify.error('Text editor error');
        }
    });
}

function saveProcessText(e) {
    var textContent = tinyMCE.get('processContent').getContent();
    if (textContent == "") {
        if ($(e).attr('id') == 'processTextSaveBtn') {
            alertify.error('Process content is empty.');
        }
    } else {
        // save the process

        $.ajax({
            url: '/publisher/assets/process/apis/save_process_text',
            type: 'POST',
            data: {
                'processName': $("#textProcessName").val(),
                'processVersion': $("#textProcessVersion").val(),
                'processText': textContent
            },
            success: function (response) {
                if ($(e).attr('id') == 'processTextSaveBtn') {
                    alertify.success("Successfully saved the process content.");
                }
                $("#viewTextButton").show();
                $("#addTextButton").hide();
            },
            error: function () {
                alertify.error('Process text error');
            }
        });
    }
}

function showTextEditor() {
    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").show();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#pdfUploaderView").hide();
    $("holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();

    tinymce.init({
        selector: "#processContent"
    });
}

function showBPMNUploader() {
    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").show();
    $("#pdfUploaderView").hide();
    $("#holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();
}

function showOverview(e) {
    if ($(e).attr('id') == 'processTextOverviewBtn') {
        saveProcessText(e);
    }
    $("#overviewDiv").show();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#pdfUploaderView").hide();
    $("#holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();
}

function downloadDocument(relativePath) {
    $.ajax({
        url: '/publisher/assets/process/apis/download_document?process_doc_path=' + relativePath,
        type: 'GET',
        success: function (response) {
            var docNameWithExt = relativePath.substr(relativePath.lastIndexOf('/') + 1);
            var extension = docNameWithExt.split('.').pop().toLowerCase();
            var byteCharacters = atob(response);

            var byteNumbers = new Array(byteCharacters.length);
            for (var i = 0; i < byteCharacters.length; i++) {
                byteNumbers[i] = byteCharacters.charCodeAt(i);
            }
            var contentType = 'application/msword';
            if (extension == "pdf") {
                contentType = 'application/pdf';
            }
            var byteArray = new Uint8Array(byteNumbers);
            var blob = new Blob([byteArray], {type: contentType});
            saveAs(blob, docNameWithExt);
        },
        error: function () {
            alertify.error('Text editor error');
        }
    });
}

function removeDocument(processName, processVersion, documentName, documentSummary, documentUrl, documentPath, idVal) {
    var currentElementId = "#" + idVal;
    $(currentElementId).parent().closest("tr").remove();

    if (document.getElementById("listDocs").rows.length == 0) {
        $('#listDocs').append('<tr><td colspan="6">No documentation associated with this process</td></tr>');
    }

    var removeDocInfo = {
        'name': documentName,
        'summary': documentSummary,
        'url': documentUrl,
        'path': documentPath
    };

    var removeDocObj = {
        'processName': processName,
        'processVersion': processVersion,
        'removeDocument': removeDocInfo
    };

    $.ajax({
        url: '/publisher/assets/process/apis/delete_document',
        type: 'POST',
        data: {'removeDocumentDetails': JSON.stringify(removeDocObj)},
        success: function (response) {
            alertify.success('Successfully deleted ' + documentName + ' from the document list.');
        },
        error: function () {
            alertify.error('Document deleting error');
        }
    });

}

function viewPDFDocument(relativePath, heading, iteration) {
    $.ajax({
        url: '/publisher/assets/process/apis/download_document?process_doc_path=' + relativePath,
        type: 'GET',
        success: function (response) {
            var byteCharacters = atob(response);
            var byteNumbers = new Array(byteCharacters.length);
            for (var i = 0; i < byteCharacters.length; i++) {
                byteNumbers[i] = byteCharacters.charCodeAt(i);
            }
            var contentType = 'application/pdf';
            var byteArray = new Uint8Array(byteNumbers);
            var file = new Blob([byteArray], {type: contentType});
            var fileURL = URL.createObjectURL(file);
            viewPDF(fileURL, heading, iteration);
        },
        error: function () {
            alertify.error('Text editor error');
        }
    });
}

function viewGoogleDocument(googleDocUrl, heading, iteration) {
    var googleDocDivElement = document.getElementById("googleDocViewer");
    var customGoogleDocUrl = googleDocUrl + "&embedded=true";
    var customHeading = "Document Name : " + heading;
    var modal = '<div id="docViewModal' + iteration + '" class="modal fade" role="dialog">';
    modal += '<div class="modal-dialog" style="width:840px;height:600px">';
    modal += '<div class="modal-content">';
    modal += '<div class="modal-header">';
    modal += '<a class="close" data-dismiss="modal">×</a>';
    modal += '<h4>' + customHeading + '</h4>';
    modal += '</div>';
    modal += '<div class="modal-body">';
    modal += '<iframe src="' + customGoogleDocUrl + '" style="width:800px;height:600px" frameborder="0">';
    modal += '</iframe>';
    modal += '</div>';
    modal += '<div class="modal-footer">';
    modal += '<span class="btn" data-dismiss="modal">';
    modal += 'Close';
    modal += '</span>'; // close button
    modal += '</div>';  // footer
    modal += '</div>'; //modal-content
    modal += '</div>'; //modal-header
    modal += '</div>';  // modalWindow
    googleDocDivElement.innerHTML += modal;
}

function viewPDF(pdfUrl, heading, iteration) {
    var pdfDocDivElement = document.getElementById("pdfDocViewer");
    var customHeading = "PDF Name : " + heading;
    var pdfModal = '<div id="pdfViewModal' + iteration + '" aria-labelledby="pdfModalLabel' + iteration + '" class="modal fade" role="dialog" aria-hidden="true">';
    pdfModal += '<div class="modal-dialog" style="width:840px;height:600px">';
    pdfModal += '<div class="modal-content">';
    pdfModal += '<div class="modal-header">';
    pdfModal += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
    pdfModal += '<h4 class="modal-title" id="pdfModalLabel' + iteration + '">' + customHeading + '</h4>';
    pdfModal += '</div>';
    pdfModal += '<div class="modal-body">';
    pdfModal += '<object type="application/pdf" data="' + pdfUrl + '" width="800" height="600">this is not working as expected</object>';
    pdfModal += '</div>';
    pdfModal += '<div class="modal-footer">';
    pdfModal += '<span class="btn" data-dismiss="modal">';
    pdfModal += 'Close';
    pdfModal += '</span>'; // close button
    pdfModal += '</div>'; // footer
    pdfModal += '</div>'; //modal-content
    pdfModal += '</div>'; //modal-header
    pdfModal += '</div>'; //modal window
    pdfDocDivElement.innerHTML += pdfModal;
}

function confirmDialog(question) {
    var confirmModal =
        $('<div class="modal fade">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<a class="close" data-dismiss="modal" >&times;</a>' +
        '<h3>Confirm delete</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<p>' + question + '</p>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<a href="#!" class="btn" data-dismiss="modal">cancel</a>' +
        '<a href="#!" id="okButton" class="btn btn-primary">delete</a>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>');
    return confirmModal;
}

function removeDocumentConfirmListener(processName, processVersion, documentName, documentSummary, documentUrl, documentPath, idVal) {
    var question = "Are you sure you want to delete " + documentName + " document permanently ?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
        removeDocument(processName, processVersion, documentName, documentSummary, documentUrl, documentPath, idVal);
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
}

function showDocument() {
    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#pdfUploaderView").hide();
    $("#holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").show();
    $("#docViewDiv").show();
    $("#addNewDoc").toggle();

    $.ajax({
        url: '/publisher/assets/process/apis/get_process_doc?process_path=/_system/governance/processes/' + fieldsName + "/" + fieldsVersion,
        type: 'GET',
        success: function (data) {
            var response = JSON.parse(data);
            if (response.length != 0) {
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
                        anchorGoogleDocViewElement.style.marginRight = "15px";
                        anchorGoogleDocViewElement.innerHTML = "view";

                        cellDocAction.appendChild(anchorUrlElement);
                        cellDocAction.appendChild(anchorGoogleDocViewElement);
                    } else if (response[i].path != "NA") {
                        var anchorElement = document.createElement("a");
                        anchorElement.setAttribute("id", "document" + i);
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
                            anchorPdfViewElement.style.marginRight = "15px";
                            anchorPdfViewElement.innerHTML = "view";
                            cellDocAction.appendChild(anchorPdfViewElement);
                        }
                    } else {
                        cellDocAction.innerHTML = "Not Available";
                    }
                    if (response[i].url != "NA" || response[i].path != "NA") {
                        var removeDocElement = document.createElement("a");
                        removeDocElement.setAttribute("id", "removeDocElement" + i);
                        removeDocElement.onclick = (function (processName, processVersion, docName, docSummary, docUrl, docPath, idVal) {
                            return function () {
                                removeDocumentConfirmListener(processName, processVersion, docName, docSummary, docUrl, docPath, idVal);
                            };
                        })(fieldsName, fieldsVersion, response[i].name, response[i].summary, response[i].url, response[i].path, "removeDocElement" + i);
                        removeDocElement.innerHTML = "remove";
                        cellDocAction.appendChild(removeDocElement);
                    }
                }
            }
        },
        error: function () {
            alertify.error('document retrieving error');
        }
    });
}

function associateDoc() {
    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#pdfUploaderView").hide();
    $("#holder").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").show();
    $("#docViewDiv").hide();
    $("#toggleAnchor").hide();
}

function newDocFormToggle() {
    $("#addNewDoc").toggle("slow");
}

function editProcessOwner(e) {
    if ($(e).text() == '') {
        alertify.error('Process owner field is empty.');
    } else {
        $('#processOwnerUpdateBtn').show();
    }
}

function subProcessNamesAutoComplete() {
    $(".subprocess_Name").autocomplete({
        source: processNames
    });
}

function successorNameAutoComplete() {
    $(".successor_Name").autocomplete({
        source: processNames
    });
}

function predecessorNameAutoComplete() {
    $(".predecessor_Name").autocomplete({
        source: processNames
    });
}

function getProcessList() {
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_list',
        type: 'GET',
        success: function (response) {
            processListObj = JSON.parse(response);
            for (var i = 0; i < processListObj.length; i++) {
                processNames.push(processListObj[i].processname + "-" + processListObj[i].processversion);
                if (processListObj[i].processid == pid) {
                    if (processListObj[i].pdfpath == "NA") {
                        //show pdf upload button
                        $("#pdfUploader").show();
                        $("#pdfViewer").hide();
                    }
                    else {
                        //show pdf view button
                        $("#pdfUploader").hide();
                        $("#pdfViewer").show();
                    }

                }
            }

        },
        error: function () {
            alertify.error('Process list returning error');
        }
    });
}

function isProcessNotAvailableInList(processName) {
    for (var i = 0; i < processNames.length; i++) {
        if (processNames[i] == processName) {
            return false;
        }
    }
    return true;
}

function readUpdatedSubprocess(currentObj) {
    var subprocessInput = $(currentObj).parent().closest("tr").find("input").val();

    if (subprocessInput == '') {
        alertify.error('Subprocess field is empty.');
    } else if (isProcessNotAvailableInList(subprocessInput)) {
        alertify.warning('Please select a process from the dropdown list.');
    } else if (subprocessInput == getMainProcess()) {
        alertify.warning('You cannot assign the process name as its subprocess.');
    } else {
        $(currentObj).parent().closest("tr").find("input").replaceWith("<span id='subprocess_Name' class='subprocess_Name'>" + subprocessInput + "</span>");
        $(currentObj).hide();
        var subprocessPath, subprocessId;
        for (var i = 0; i < processListObj.length; i++) {
            if (processListObj[i].processname == subprocessInput.split("-")[0] &&
                processListObj[i].processversion == subprocessInput.split("-")[1]) {
                subprocessPath = processListObj[i].path;
                subprocessId = processListObj[i].processid;
                break;
            }
        }

        var subprocessInfo = {
            name: subprocessInput.split("-")[0],
            path: subprocessPath,
            id: subprocessId
        };

        var subProcessDetails = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'subprocess': subprocessInfo
        };

        $.ajax({
            url: '/publisher/assets/process/apis/update_subprocess',
            type: 'POST',
            data: {'subprocessDetails': JSON.stringify(subProcessDetails)},
            success: function (response) {
                alertify.success('Process ' + subprocessInput + ' successfully added to the subprocess list.');
            },
            error: function () {
                alertify.error('Subprocess updating error');
            }
        });
    }
}

function readUpdatedSuccessor(currentObj) {
    var successorInput = $(currentObj).parent().closest("tr").find("input").val();

    if (successorInput == '') {
        alertify.error('Successor field is empty.');
    } else if (isProcessNotAvailableInList(successorInput)) {
        alertify.warning('Please select a process from the dropdown list.');
    } else if (successorInput == getMainProcess()) {
        alertify.warning('You cannot assign the process name as its successor.');
    } else {
        $(currentObj).parent().closest("tr").find("input").replaceWith("<span id='successor_Name' class='successor_Name'>" + successorInput + "</span>");
        $(currentObj).hide();
        var successorPath, successorId;
        for (var i = 0; i < processListObj.length; i++) {
            if (processListObj[i].processname == successorInput.split("-")[0] &&
                processListObj[i].processversion == successorInput.split("-")[1]) {
                successorPath = processListObj[i].path;
                successorId = processListObj[i].processid;
                break;
            }
        }

        var successorInfo = {
            name: successorInput.split("-")[0],
            path: successorPath,
            id: successorId
        };

        var successorDetails = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'successor': successorInfo
        };

        $.ajax({
            url: '/publisher/assets/process/apis/update_successor',
            type: 'POST',
            data: {'successorDetails': JSON.stringify(successorDetails)},
            success: function (response) {
                alertify.success('Process ' + successorInput + ' successfully added to the successor list.');
            },
            error: function () {
                alertify.error('Successor updating error');
            }
        });
    }
}

function readUpdatedPredecessor(currentObj) {
    var predecessorInput = $(currentObj).parent().closest("tr").find("input").val();

    if (predecessorInput == '') {
        alertify.error('Predecessor field is empty.');
    } else if (isProcessNotAvailableInList(predecessorInput)) {
        alertify.warning('Please select a process from the dropdown list.');
    } else if (predecessorInput == getMainProcess()) {
        alertify.warning('You cannot assign the process name as its predecessor.');
    } else {
        $(currentObj).parent().closest("tr").find("input").replaceWith("<span id='predecessor_Name' class='predecessor_Name'>" + predecessorInput + "</span>");
        $(currentObj).hide();
        var predecessorPath, predecessorId;
        for (var i = 0; i < processListObj.length; i++) {
            if (processListObj[i].processname == predecessorInput.split("-")[0] &&
                processListObj[i].processversion == predecessorInput.split("-")[1]) {
                predecessorPath = processListObj[i].path;
                predecessorId = processListObj[i].processid;
                break;
            }
        }

        var predecessorInfo = {
            name: predecessorInput.split("-")[0],
            path: predecessorPath,
            id: predecessorId
        };

        var predecessorDetails = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'predecessor': predecessorInfo
        };

        $.ajax({
            url: '/publisher/assets/process/apis/update_predecessor',
            type: 'POST',
            data: {'predecessorDetails': JSON.stringify(predecessorDetails)},
            success: function (response) {
                alertify.success('Process ' + predecessorInput + ' successfully added to the predecessor list.');
            },
            error: function () {
                alertify.error('Predecessor updating error');
            }
        });
    }
}

function deleteSubprocess(element) {
    var deleteSubInput = $(element).parent().closest("tr").find("span").text();
    if (!isProcessNotAvailableInList(deleteSubInput)) {
        var deleteSubPath, deleteSubId;
        for (var i = 0; i < processListObj.length; i++) {
            if (processListObj[i].processname == deleteSubInput.split("-")[0] &&
                processListObj[i].processversion == deleteSubInput.split("-")[1]) {
                deleteSubPath = processListObj[i].path;
                deleteSubId = processListObj[i].processid;
                break;
            }
        }

        var deleteSubInfo = {
            name: deleteSubInput.split("-")[0],
            path: deleteSubPath,
            id: deleteSubId
        };

        var deleteSubObj = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'deleteSubprocess': deleteSubInfo
        };

        $.ajax({
            url: '/publisher/assets/process/apis/delete_subprocess',
            type: 'POST',
            data: {'deleteSubprocessDetails': JSON.stringify(deleteSubObj)},
            success: function (response) {
                alertify.success('Successfully deleted ' + deleteSubInput + ' from the subprocess list.');
            },
            error: function () {
                alertify.error('Subprocess deleting error');
            }
        });
    }
}

function deleteSuccessor(element) {
    var deleteSuccessorInput = $(element).parent().closest("tr").find("span").text();
    if (!isProcessNotAvailableInList(deleteSuccessorInput)) {
        var deleteSuccessorPath, deleteSuccessorId;
        for (var i = 0; i < processListObj.length; i++) {
            if (processListObj[i].processname == deleteSuccessorInput.split("-")[0] &&
                processListObj[i].processversion == deleteSuccessorInput.split("-")[1]) {
                deleteSuccessorPath = processListObj[i].path;
                deleteSuccessorId = processListObj[i].processid;
                break;
            }
        }

        var deleteSuccessorInfo = {
            name: deleteSuccessorInput.split("-")[0],
            path: deleteSuccessorPath,
            id: deleteSuccessorId
        };

        var deleteSuccessorObj = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'deleteSuccessor': deleteSuccessorInfo
        };

        $.ajax({
            url: '/publisher/assets/process/apis/delete_successor',
            type: 'POST',
            data: {'deleteSuccessorDetails': JSON.stringify(deleteSuccessorObj)},
            success: function (response) {
                alertify.success('Successfully deleted ' + deleteSuccessorInput + ' from the successor list.');
            },
            error: function () {
                alertify.error('Successor deleting error');
            }
        });
    }
}

function deletePredecessor(element) {
    var deletePredecessorInput = $(element).parent().closest("tr").find("span").text();
    if (!isProcessNotAvailableInList(deletePredecessorInput)) {
        var deletePredecessorPath, deletePredecessorId;
        for (var i = 0; i < processListObj.length; i++) {
            if (processListObj[i].processname == deletePredecessorInput.split("-")[0] &&
                processListObj[i].processversion == deletePredecessorInput.split("-")[1]) {
                deletePredecessorPath = processListObj[i].path;
                deletePredecessorId = processListObj[i].processid;
                break;
            }
        }

        var deletePredecessorInfo = {
            name: deletePredecessorInput.split("-")[0],
            path: deletePredecessorPath,
            id: deletePredecessorId
        };

        var deletePredecessorObj = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'deletePredecessor': deletePredecessorInfo
        };

        $.ajax({
            url: '/publisher/assets/process/apis/delete_Predecessor',
            type: 'POST',
            data: {'deletePredecessorDetails': JSON.stringify(deletePredecessorObj)},
            success: function (response) {
                alertify.success('Successfully deleted ' + deletePredecessorInput + ' from the predecessor list.');
            },
            error: function () {
                alertify.error('Predecessor deleting error');
            }
        });
    }
}

function updateProcessOwner(element) {
    var processOwner = $(element).parent().closest("tr").find("td:eq(1)").text();
    if (processOwner == '') {
        alertify.error('Process owner field is empty.');
    } else {
        var ownerDetails = {
            'processName': $('#view-header').text(),
            'processVersion': $('#process-version').text(),
            'processOwner': processOwner
        };

        $.ajax({
            url: '/publisher/assets/process/apis/update_owner',
            type: 'POST',
            data: {'ownerDetails': JSON.stringify(ownerDetails)},
            success: function (response) {
                $(element).hide();
                alertify.success('Successfully updated the Process owner name.');
            },
            error: function () {
                alertify.error('Process owner updating error');
            }
        });
    }
}

function isInputFieldEmpty(tableName) {
    var isFieldEmpty = false;
    $('#table_' + tableName + ' tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == '') {
            isFieldEmpty = true;
        }
    });
    return isFieldEmpty;
}

function addUnboundedRow(element) {
    var tableName = $(element).attr('data-name');
    var table = $('#table_' + tableName);

    if (!isInputFieldEmpty(tableName)) {
        var referenceRow = $('#table_reference_' + tableName);
        var newRow = referenceRow.clone().removeAttr('id');
        $('input[type="text"]', newRow).val('');
        table.show().append(newRow);
    } else {
        alertify.error('Please fill the empty ' + tableName + ' field.');
    }
}

//****************************PDF rendering*************************************************

function showPDF() {
    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#holder").show();
    $("#pdfUploaderView").hide();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();

    if (pdfDoc == null) {
        loadPdf();
    }

}

function associatePdf(element) {
    $("#overviewDiv").hide();
    $("#processTextView").hide();
    $("#bpmnView").hide();
    $("#docView").hide();
    $("#pdfUploaderView").show();
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();
}

function loadPdf() {
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_pdf?process_pdf_path=/_system/governance/pdf/' + fieldsName + "/" + fieldsVersion,
        type: 'GET',
        dataType: 'text',
        success: function (data) {
            renderPDF(base64ToUint8Array(data));
        },
        error: function () {
            alertify.error('pdf retrieving error');
        }
    });
}

// Go to previous page
function goPrevious() {
    if (pageNum <= 1)
        return;
    pageNum--;
    renderPage(pageNum);
}

// Go to next page
function goNext() {
    if (pageNum >= pdfDoc.numPages)
        return;
    pageNum++;
    renderPage(pageNum);
}

function renderPDF(url) {
    pdfDoc = null;
    pageNum = 1;
    scale = 1.5;
    canvas = document.getElementById('the-canvas');
    ctx = canvas.getContext('2d');
    PDFJS.disableWorker = true;

    PDFJS.getDocument(url).then(function getPdf(_pdfDoc) {
        pdfDoc = _pdfDoc;
        renderPage(pageNum);

    });
}

// Get page info from document, resize canvas accordingly, and render page
function renderPage(num) {
    // Using promise to fetch the page
    pdfDoc.getPage(num).then(function (page) {
        var viewport = page.getViewport(scale);
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        // Render PDF page into canvas context
        var renderContext = {
            canvasContext: ctx,
            viewport: viewport
        };
        page.render(renderContext);
    });

    // Update page counters
    document.getElementById('page_num').textContent = pageNum;
    document.getElementById('page_count').textContent = pdfDoc.numPages;
}

function base64ToUint8Array(base64) {//base64 is an encoded byte Array sent from server-side

    var raw = atob(base64); //This is a native function that decodes a base64-encoded string.
    var uint8Array = new Uint8Array(new ArrayBuffer(raw.length));
    for (var i = 0; i < raw.length; i++) {
        uint8Array[i] = raw.charCodeAt(i);
    }
    return uint8Array;
}

function zoom(newScale) {
    // Using promise to fetch the page
    pdfDoc.getPage(pageNum).then(function (page) {
        var viewport = page.getViewport(newScale);
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        // Render PDF page into canvas context
        var renderContext = {
            canvasContext: ctx,
            viewport: viewport
        };
        page.render(renderContext);
    });

}

function zoomIn() {
    var scaleSelect = document.getElementById("scaleSelect");
    var last = scaleSelect.options.length - 1;

    if (scaleSelect.selectedIndex < last) {
        scale = scaleSelect.options[scaleSelect.selectedIndex + 1].value;
        scaleSelect.selectedIndex += 1;
        zoom(scale);
    }
}

function zoomOut() {
    var scaleSelect = document.getElementById("scaleSelect");
    var last = scaleSelect.options.length - 1;

    if (scaleSelect.selectedIndex > 0) {
        scale = scaleSelect.options[scaleSelect.selectedIndex - 1].value;
        scaleSelect.selectedIndex -= 1;
        zoom(scale);
    }
}

function zoomSelect() {
    var scaleSelect = document.getElementById("scaleSelect");
    scale = scaleSelect.options[scaleSelect.selectedIndex].value;
    zoom(scale);
}

//******************************Flowchart Editor***********************************
function associateEditorFlowChart() {
    $("#flowchartEditorDltBtn").hide();
    $("#overviewDiv").hide();
    $("#flowChartEditorView").show();
    $("#pdfUploader").hide();
    $("#docView").hide();
    $("#bpmnView").hide();
    $("#processTextView").hide();
}

function showFlowchartEditor(name, flowchartPath) {
    $("#overviewDiv").hide();
    $("#flowChartEditorView").show();
    $("#pdfUploader").hide();
    $("#docView").hide();
    $("#bpmnView").hide();
    $("#processTextView").hide();

    flowchartPath = "/_system/governance/" + flowchartPath;
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_flowchart',
        type: 'GET',
        dataType: 'text',
        data: {'flowchartPath': flowchartPath},
        success: function (data) {
            _loadEditableFlowChart(data, '#editor_canvas');
        },
        error: function () {
            alertify.error('Error retrieving flowchart');
        }
    });
}

function removeFlowchart() {
    var name = $("#fcProcessName").val();
    var version = $("#fcProcessVersion").val();
    var question = "Are you sure you want to delete the flowchart of process " + name + " " + version + " permanently?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
        _deleteFlowchart(name, version);
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
}

function redirectTo(element) {
    element.click();
}

function validateDocument() {
    if (document.getElementById('docName').value.length == 0) {
        alertify.error('Please enter document name.');
        return false;
    } else if ((!document.getElementById('optionsRadios7').checked) && (!document.getElementById('optionsRadios8').checked)) {
        alertify.error('Please select a source.');
        return false;
    } else if (document.getElementById('optionsRadios7').checked) {
        if (document.getElementById('docUrl').value.length == 0) {
            alertify.error('Please give the document url.');
            return false;
        }
    } else if (document.getElementById('optionsRadios8').checked) {
        var ext = $('#docLocation').val().split('.').pop().toLowerCase();
        if ($.inArray(ext, ['docx', 'doc', 'pdf']) == -1) {
            alertify.error('invalid document extension!');
            return false;
        }
        $("#docExtension").val(ext);
    }
    return true;
}


$('.view').click(function (e) {
    e.preventDefault();
    setTableName($(this).attr("data-name"));
    $("#process-search-results").html("");
    document.getElementById("process-search-form").reset();
    $("#searchModal").modal("show");
});