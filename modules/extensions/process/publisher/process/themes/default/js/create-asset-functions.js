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

var processNames = [];
var processListObj;
var tagList = [];

window.onload = function () {
    getProcessList();

    $('#span').click(function () {
        $("#tag-box").focus()
    });

    $("#tag-box").keyup(function (e) {

        var tagValue = $("#tag-box").val().trim();
        var duplicate = $.inArray(tagValue, tagList);

        if (duplicate >= 0) {
            $("#select2-results__option--highlighted").hide();
            $("#select2-results__option").hide();
            $('#tag-box').val('');
        }

        if (e.which == 13 && tagValue.length >= 2) {
            addNewTag();
            $("#select2-results__option").hide();
            $("#select2-results__option--highlighted").hide();
        }
        else {
            updateTextBox(tagValue);
        }
    });

    $("#tag-box").focusin(function () {

        var tagValue = $("#tag-box").val().trim();
        updateTextBox(tagValue);
    });


    $("#span").focusout(function () {
        $("#select2-results__option").hide();
        $("#select2-results__option--highlighted").hide();
    });
}

function showTextEditor(element) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        saveProcess(element);
        completeTextDetails();
        $("#processTextDiv").show();
        $("#overviewDiv").hide();
        $("#bpmnView").hide();
        $("#docView").hide();
        $("#pdfUploader").hide();
        $("#flowChartView").hide();

        tinymce.init({
            selector: "#processContent"
        });
    }
}

function associateBPMN(element) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        $('#create-view-header').text($('#pName').val());
        saveProcess(element);
        completeBPMNDetails();
        $("#overviewDiv").hide();
        $("#processTextView").hide();
        $("#docView").hide();
        $("#bpmnView").show();
        $("#pdfUploader").hide();
        $("#flowChartView").hide();

    }
}

function associateFlowChart(element) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        saveProcess(element);
        $('#flow-chart-view-header').text($('#pName').val());
        $("#overviewDiv").hide();
        $("#flowChartView").show();
        $("#pdfUploader").hide();
        $("#docView").hide();
        $("#bpmnView").hide();
        $("#processTextView").hide();
    }
}

function associateDocument(element) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        //$('#create-view-header').text($('#pName').val());
        saveProcess(element);
        //completeBPMNDetails();
        $("#overviewDiv").hide();
        $("#processTextView").hide();
        $("#bpmnView").hide();
        $("#docView").show();
        $("#flowChartView").hide();
    }
}

function newDocFormToggle() {
    $("#addNewDoc").toggle("slow");
}

function showMain() {
    $("#mainView").show();
    $("#bpmnView").hide();
    $("#processTextView").hide();
    $("#pdfUploader").hide();
    $("#flowChartView").hide();

}

function saveProcess(currentElement) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        // save the process

        $.ajax({
            url: 'apis/create_process',
            type: 'POST',
            data: {'processInfo': getProcessInfo()},
            success: function (response) {
                $("#processTextOverviewLink").attr("href", "../../assets/process/details/" + response);
                $("#bpmnOverviewLink").attr("href", "../../assets/process/details/" + response);
                $("#pdfOverviewLink").attr("href", "../../assets/process/details/" + response);
                $("#docOverviewLink").attr("href", "../../assets/process/details/" + response);

                if ($(currentElement).attr('id') == 'saveProcessBtn') {
                    window.location = "../../assets/process/details/" + response;
                }
            },
            error: function () {
                alertify.error('Process saving error');
            }
        });

    }
}

function getProcessInfo() {
    var tags = tagList.toString();
    var processDetails = {
        'processName': $("#pName").val(),
        'processVersion': $("#pVersion").val(),
        'processOwner': $("#pOwner").val(),
        'processTags': tags,
        'subprocess': readSubprocessTable(),
        'successor': readSuccessorTable(),
        'predecessor': readPredecessorTable()
    };
    return (JSON.stringify(processDetails));
}

function saveProcessText(currentElement) {
    var textContent = tinyMCE.get('processContent').getContent();
    if (textContent == "") {
        if ($(currentElement).attr('id') == 'processTxtSaveBtn') {
            alertify.error('Process content is empty.');
        }
    } else {
        // save the process

        $.ajax({
            url: 'apis/save_process_text',
            type: 'POST',
            data: {
                'processName': $("#pName").val(),
                'processVersion': $("#pVersion").val(),
                'processText': textContent
            },
            success: function (response) {
                if ($(currentElement).attr('id') == 'processTxtSaveBtn') {
                    alertify.success("Successfully saved the process content.");
                }
            },
            error: function () {
                alertify.error('Process text saving error');
            }
        });
    }
}

function completeBPMNDetails() {
    $("#bpmnProcessName").val($("#pName").val());
    $("#bpmnProcessVersion").val($("#pVersion").val());
    return true;
}

function completeTextDetails() {
    $("#textProcessName").val($("#pName").val());
    $("#textProcessVersion").val($("#pVersion").val());
    return true;
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

function readSubprocessTable() {
    var subprocessInfo = [];
    $('#table_subprocess tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == '') {
            //continue
        } else {
            var subprocessPath, subprocessId;
            for (var i = 0; i < processListObj.length; i++) {
                if (processListObj[i].processname == $(this).find('td:eq(0) input').val().split("-")[0] &&
                    processListObj[i].processversion == $(this).find('td:eq(0) input').val().split("-")[1]) {
                    subprocessPath = processListObj[i].path;
                    subprocessId = processListObj[i].processid;
                    break;
                }
            }

            subprocessInfo.push({
                name: $(this).find('td:eq(0) input').val().split("-")[0],
                path: subprocessPath,
                id: subprocessId
            });
        }
    });
    return subprocessInfo;
}

function readSuccessorTable() {
    var successorInfo = [];
    $('#table_successor tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == '') {
            //continue
        } else {
            var successorPath, successorId;
            for (var i = 0; i < processListObj.length; i++) {
                if (processListObj[i].processname == $(this).find('td:eq(0) input').val().split("-")[0] &&
                    processListObj[i].processversion == $(this).find('td:eq(0) input').val().split("-")[1]) {
                    successorPath = processListObj[i].path;
                    successorId = processListObj[i].processid;
                    break;
                }
            }

            successorInfo.push({
                name: $(this).find('td:eq(0) input').val().split("-")[0],
                path: successorPath,
                id: successorId
            });
        }
    });
    return successorInfo;
}

function readPredecessorTable() {
    var predecessorInfo = [];
    $('#table_predecessor tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == '') {
            //continue
        } else {
            var predecessorPath, predecessorId;
            for (var i = 0; i < processListObj.length; i++) {
                if (processListObj[i].processname == $(this).find('td:eq(0) input').val().split("-")[0] &&
                    processListObj[i].processversion == $(this).find('td:eq(0) input').val().split("-")[1]) {
                    predecessorPath = processListObj[i].path;
                    predecessorId = processListObj[i].processid;
                    break;
                }
            }

            predecessorInfo.push({
                name: $(this).find('td:eq(0) input').val().split("-")[0],
                path: predecessorPath,
                id: predecessorId
            });
        }
    });
    return predecessorInfo;
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
            alertify.error('Process List error');
        }
    });
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

function validateDocs() {
    $("#docProcessName").val($("#pName").val());
    $("#docProcessVersion").val($("#pVersion").val());
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
        if ($.inArray(ext, ['docx', 'doc']) == -1) {
            alertify.error('invalid extension!');
            return false;
        }
        $("#docExtension").val(ext);
    }
    return true;
}

function associatePdf(element) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        $('#pdf-create-view-header').text($('#pName').val());
        $("#ProcessName").attr("value", $("#pName").val());
        $("#ProcessVersion").attr("value", $("#pVersion").val());
        saveProcess(element);
        $("#overviewDiv").hide();
        $("#processTextView").hide();
        $("#bpmnView").hide();
        $("#docView").hide();
        $("#pdfUploader").show();
    }
}

function redirectToProcess(element) {
    element.click();
}

function updateTextBox(tagValue) {

    if (tagValue.length == 0) {
        $("#select2-results__option--highlighted").hide();
        $("#select2-results__option").text("Please enter 2 or more characters");
        $("#select2-results__option").show();


    }
    else if (tagValue.length == 1) {
        $("#select2-results__option--highlighted").hide();
        $("#select2-results__option").text("Please enter 1 or more characters");
        $("#select2-results__option").show();
    }
    else {
        $("#select2-results__option").hide();
        $("#select2-results__option--highlighted").text(tagValue);
        $("#select2-results__option--highlighted").show();


    }
}

function addNewTag() {
    var tagValue = $("#tag-box").val().trim();

    if (tagValue) {
        tagList.push(tagValue);
        $('#_tags').append($("<option></option>").attr("value", tagValue).text(tagValue));

        $("#tag-box-list").before('<li class="select2-selection__choice" title="' + tagValue + '">' +
        '<span class="select2-selection__choice__remove" role="presentation" onclick="removeTag(this)">×</span>' + tagValue + '</li>');
        $('#tag-box').val('');
    }
}

function removeTag(currentElement) {

    var parent = $(currentElement).parent();
    var tagName = parent.attr("title");

    $('#_tags option').each(function () {
        if ($(this).val() == tagName) {
            $(this).remove();
        }
    })
    $(parent).remove();

    var index = jQuery.inArray(tagName, tagList);
    if (index > -1) {
        tagList.splice(index, 1);
    }
}