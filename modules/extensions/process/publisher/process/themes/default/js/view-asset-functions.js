/*var processNames = [];
var processListObj;
var fieldsName = $("#textProcessName").val();
var fieldsVersion = $("#textProcessVersion").val();

window.onload = getProcessList;

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
}*/