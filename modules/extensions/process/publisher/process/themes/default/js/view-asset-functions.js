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

    $.fn.editable.defaults.mode = 'popup';
    $.fn.editable.defaults.ajaxOptions = {type: "POST"};

    $('#description').editable({
        type: 'text',
        url: '/publisher/assets/process/apis/update_description',
        pk: 1,
        display: function (value, response) {
            $('#description').html(value);
        },
        params: function (params) {
            params.processName = $('#view-header').text();
            params.processVersion = $('#process-version').text();
            return JSON.stringify(params);
        }
    });

    $('#owner').editable({
        url: '/publisher/assets/process/apis/update_owner',
        pk: 2,
        display: function (value, response) {
            $('#owner').html(value);
        },
        params: function (params) {
            params.processName = $('#view-header').text();
            params.processVersion = $('#process-version').text();
            return JSON.stringify(params);
        }
    });
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
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();

    $.ajax({
        url: '/publisher/assets/process/apis/get_bpmn_content?bpmn_content_path=/_system/governance/bpmn/' + fieldsName + "/" + fieldsVersion,
        type: 'GET',
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                var bpmnObject = JSON.parse(response.content);
                $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);
            } else {
                alertify.error(response.content);
            }
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
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                $("#processTextDiv").html(response.content);
            } else {
                alertify.error(response.content);
            }
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
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    if ($(e).attr('id') == 'processTextSaveBtn') {
                        alertify.success("Successfully saved the process content.");
                    }
                    $("#viewTextButton").show();
                    $("#addTextButton").hide();
                } else {
                    alertify.error(response.content);
                }
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
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").hide();
    $("#docViewDiv").hide();
}

function downloadDocument(relativePath) {
    $.ajax({
        url: '/publisher/assets/process/apis/download_document?process_doc_path=' + relativePath,
        type: 'GET',
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                var docNameWithExt = relativePath.substr(relativePath.lastIndexOf('/') + 1);
                var extension = docNameWithExt.split('.').pop().toLowerCase();
                var byteCharacters = atob(response.content);

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
            } else {
                alertify.error(response.content);
            }
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
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                alertify.success('Successfully deleted ' + documentName + ' from the document list.');
            } else {
                alertify.error(response.content);
            }
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
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                var byteCharacters = atob(response.content);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var contentType = 'application/pdf';
                var byteArray = new Uint8Array(byteNumbers);
                var file = new Blob([byteArray], {type: contentType});
                var fileURL = URL.createObjectURL(file);
                viewPDF(fileURL, heading, iteration);
            } else {
                alertify.error(response.content);
            }
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
    $("#flowChartEditorView").hide();
    $("#docUploaderDiv").show();
    $("#docViewDiv").show();
    $("#addNewDoc").toggle();

    $.ajax({
        url: '/publisher/assets/process/apis/get_process_doc?process_path=/_system/governance/processes/' + fieldsName + "/" + fieldsVersion,
        type: 'GET',
        success: function (data) {
            var responseObj = JSON.parse(data);
            if (responseObj.error === false) {
                var response = JSON.parse(responseObj.content);
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
                            if (permission) {
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

function associateDoc() {
    $("#overviewDiv").hide();
    $("#processTextContainer").hide();
    $("#processTextEditDiv").hide();
    $("#bpmnViewDiv").hide();
    $("#bpmnEditDiv").hide();
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
function isAlreadyExist(value, tableName) {
    var matched = false;
    $('#table_' + tableName + ' tbody tr').each(function () {
        if ($(this).find('td:eq(0) span').text() == value) {
            matched = true;
        }
    });
    return matched;
}

function subProcessNamesAutoComplete() {
    var temp = processNames.slice();
    for (var i = 0; i < processNames.length; i++) {
        if (isAlreadyExist(processNames[i], "subprocess") || processNames[i] == getMainProcess()) {
            temp[i] = "";
        }
    }

    $(".subprocess_Name").autocomplete({
        source: temp
    });
}

function successorNameAutoComplete() {
    var temp = processNames.slice();
    for (var i = 0; i < processNames.length; i++) {
        if (isAlreadyExist(processNames[i], "successor") || processNames[i] == getMainProcess()) {
            temp[i] = "";
        }
    }

    $(".successor_Name").autocomplete({
        source: temp
    });
}

function predecessorNameAutoComplete() {
    var temp = processNames.slice();
    for (var i = 0; i < processNames.length; i++) {
        if (isAlreadyExist(processNames[i], "predecessor") || processNames[i] == getMainProcess()) {
            temp[i] = "";
        }
    }

    $(".predecessor_Name").autocomplete({
        source: temp
    });
}

function getProcessList() {
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_list',
        type: 'GET',
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                processListObj = JSON.parse(response.content);
                for (var i = 0; i < processListObj.length; i++) {
                    processNames.push(processListObj[i].processname + "-" + processListObj[i].processversion);
                }
            } else {
                alertify.error(response.content);
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

function readUpdatedSubprocess(currentObj, count) {
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
            async: false,
            url: '/publisher/assets/process/apis/update_subprocess',
            type: 'POST',
            data: {'subprocessDetails': JSON.stringify(subProcessDetails)},
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    if (count == 1) {
                        alertify.success('Process ' + subprocessInput + ' successfully added to the subprocess list.');
                    }
                } else {
                    alertify.error(response.content);
                }
            },
            error: function () {
                alertify.error('Subprocess updating error');
            }
        });
    }
}

function readUpdatedSuccessor(currentObj, count) {
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
            async: false,
            url: '/publisher/assets/process/apis/update_successor',
            type: 'POST',
            data: {'successorDetails': JSON.stringify(successorDetails)},
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    if (count == 1) {
                        alertify.success('Process ' + successorInput + ' successfully added to the successor list.');
                    }
                } else {
                    alertify.error(response.content);
                }
            },
            error: function () {
                alertify.error('Successor updating error');
            }
        });
    }
}

function readUpdatedPredecessor(currentObj, count) {
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
            async: false,
            url: '/publisher/assets/process/apis/update_predecessor',
            type: 'POST',
            data: {'predecessorDetails': JSON.stringify(predecessorDetails)},
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    if (count == 1) {
                        alertify.success('Process ' + predecessorInput + ' successfully added to the predecessor list.');
                    }
                } else {
                    alertify.error(response.content);
                }
            },
            error: function () {
                alertify.error('Predecessor updating error');
            }
        });
    }
}

function deleteSubprocess(element) {
    var deleteSubInput = $(element).parent().closest("tr").find("span").text();
    var question = "Are you sure you want to delete sub process " + deleteSubInput + "?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
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
                success: function (data) {
                    var response = JSON.parse(data);
                    if (response.error === false) {
                        document.getElementById("table_subprocess").deleteRow($(element).parent().closest("tr").index() + 1);
                        alertify.success('Successfully deleted ' + deleteSubInput + ' from the subprocess list.');
                    } else {
                        alertify.error(response.content);
                    }
                },
                error: function () {
                    alertify.error('Subprocess deleting error');
                }
            });
        }
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
}

function deleteSuccessor(element) {
    var deleteSuccessorInput = $(element).parent().closest("tr").find("span").text();
    var question = "Are you sure you want to delete successor " + deleteSuccessorInput + "?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
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
                success: function (data) {
                    var response = JSON.parse(data);
                    if (response.error === false) {
                        document.getElementById("table_successor").deleteRow($(element).parent().closest("tr").index() + 1);
                        alertify.success('Successfully deleted ' + deleteSuccessorInput + ' from the successor list.');
                    } else {
                        alertify.error(response.content);
                    }
                },
                error: function () {
                    alertify.error('Successor deleting error');
                }
            });
        }
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
}

function deletePredecessor(element) {
    var deletePredecessorInput = $(element).parent().closest("tr").find("span").text();
    var question = "Are you sure you want to delete predecessor " + deletePredecessorInput + "?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
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
                success: function (data) {
                    var response = JSON.parse(data);
                    if (response.error === false) {
                        document.getElementById("table_predecessor").deleteRow($(element).parent().closest("tr").index() + 1);
                        alertify.success('Successfully deleted ' + deletePredecessorInput + ' from the predecessor list.');
                    } else {
                        alertify.error(response.content);
                    }
                },
                error: function () {
                    alertify.error('Predecessor deleting error');
                }
            });
        }
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
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
            var response = JSON.parse(data);
            if (response.error === false) {
                _loadEditableFlowChart(response.content, '#editor_canvas');
            } else {
                alertify.error(response.content);
            }
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
    var attachedDocuments = [];
    var isDocumentExists = false;
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_doc?process_path=/_system/governance/processes/' +
        fieldsName + "/" + fieldsVersion,
        type: 'GET',
        async: false,
        success: function (data) {
            var responseObj = JSON.parse(data);
            if (responseObj.error === false) {
               var response = JSON.parse(responseObj.content);
               if (response.length != 0) {
               for (var i = 0; i < response.length; i++) {
                attachedDocuments.push(response[i].path);
                }
              }
             }
           }
        });
    var registryPathPrefix = "doccontent/"+fieldsName+"/"+fieldsVersion+"/";

    for (i = 0; i < attachedDocuments.length; i++) {
    var storedFileName = attachedDocuments[i].substring(attachedDocuments[i].indexOf(registryPathPrefix) +
    registryPathPrefix.length, attachedDocuments[i].length);

    var fileExtension = $('#docLocation').val().split('.')[1];
    var currentFileName = document.getElementById('docName').value+"."+fileExtension;

    if(currentFileName == storedFileName){
        isDocumentExists = true;
      }
    }

     if (isDocumentExists) {
        alertify.error("File already exists. Please provide different filename");
        return false;
    }
    else if (document.getElementById('docName').value.length == 0) {
        alertify.error('Please enter document name.');
        return false;
    } else if ((!document.getElementById('optionsRadios7').checked) && (!document.getElementById('optionsRadios8').
          checked)) {
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

function deleteProcess(element) {
    var value = $(element.parentElement.parentElement).find(":input");
    if (value.length == 0) {
        if (element.getAttribute("data-name") == "subprocess")
            deleteSubprocess(element);
        else if (element.getAttribute("data-name") == "successor")
            deleteSuccessor(element);
        else
            deletePredecessor(element);
    }
    else {
        value = value.val();
        if (value != "") {
            var question = "Are you sure you want to delete " + element.getAttribute("data-name") + " " + value + "?";
            var confirmModal = confirmDialog(question);
            confirmModal.find('#okButton').click(function (event) {
                document.getElementById("table_" + element.getAttribute("data-name")).
                    deleteRow(element.parentElement.parentElement.rowIndex);
                confirmModal.modal('hide');
            });
            confirmModal.modal('show');
        } else {
            document.getElementById("table_" + element.getAttribute("data-name")).
                deleteRow(element.parentElement.parentElement.rowIndex);
        }
    }
}

$("#saveAsPNGBtn").click(function () {
    html2canvas($("#editor_canvas"), {
        onrendered: function (canvas) {
            ctx = canvas.getContext('2d');

            $elements = $(".jtk-node");
            if ($elements.length != 0) {
                $elements.each(function () {
                    $svg = $(this).find("p")[0];
                    $svg.style.whiteSpace = "pre-wrap";
                });

                $flows = $('.jsplumb-connector');
                $flows.each(function () {
                    $svg = $(this);
                    $svg.parent().find(":input")[0].style.zIndex = 50;
                    var text = $svg.parent().find(":input")[0];
                    $(text).css('z-index', '100');
                    offset = $svg.position();
                    svgStr = this.outerHTML;
                    ctx.drawSvg(svgStr, offset.left, offset.top);
                });

                $endpoints = $('.jsplumb-endpoint > svg');
                $endpoints.each(function () {
                    $svg = $(this);
                    //$svg.find(':input').css({'z-index':6});
                    offset = $svg.parent().position();
                    svgStr = this.outerHTML;
                    ctx.drawSvg(svgStr, offset.left, offset.top);
                });

                Canvas2Image.saveAsPNG(canvas);
            } else {
                alertify.error('Flowchart content is empty.');
            }
        }
    });
});

function deleteBPMNDiagram(processName, processVersion) {
    $.ajax({
        url: '/publisher/assets/process/apis/delete_bpmn',
        type: 'POST',
        data: {
            'processName': processName,
            'processVersion': processVersion
        },
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                window.location = "../../process/details/" + response.content;
            } else {
                alertify.error(response.content);
            }
        },
        error: function () {
            alertify.error('BPMN diagram deleting error');
        }
    });
}

function removeBPMNDiagramListener() {
    var processName = $("#bpmnViewProcessName").val();
    var processVersion = $("#bpmnViewProcessVersion").val();
    var question = "Are you sure you want to delete the BPMN diagram of process " + processName + " " + processVersion + " permanently ?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
        deleteBPMNDiagram(processName, processVersion);
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
}