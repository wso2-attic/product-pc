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
var allProcessTags = [];

window.onload = function () {
    getProcessList();
    getAllProcessTags();

    $('#tag-box').tokenfield({
        autocomplete: {
            source: allProcessTags,
            delay: 100
        },
        showAutocompleteOnFocus: true
    });

    $('#tag-box').on('tokenfield:createtoken', function (event) {
        var existingTokens = $(this).tokenfield('getTokens');
        $.each(existingTokens, function (index, token) {
            if (token.value === event.attrs.value)
                event.preventDefault();
        });
    });
    
};

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
        $("#docView").hide();
        $("#bpmnView").hide();
        $("#processTextView").hide();
    }
}

function associateDocument(element) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        $('#document-view-header').text($('#pName').val());
        saveProcess(element);
        $("#overviewDiv").hide();
        $("#processTextView").hide();
        $("#bpmnView").hide();
        $("#docView").show();
        $("#flowChartView").hide();
    }
}

function showMain() {
    $("#mainView").show();
    $("#bpmnView").hide();
    $("#processTextView").hide();
    $("#flowChartView").hide();
}

function saveProcess(currentElement) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        // save the process
        if ($(currentElement).attr('id') == 'saveProcessBtn') {
            var imageElement = $("#images_thumbnail");
            if (imageElement.val().length != 0) {
                var ext = imageElement.val().split('.').pop().toLowerCase();
                if ($.inArray(ext, ['png', 'jpeg']) == -1) {
                    alertify.error('invalid image extension!');
                    return;
                }
            }
        }
        $.ajax({
            url: 'apis/create_process',
            type: 'POST',       
            data: {'processInfo': getProcessInfo()},
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    $("#processTextOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    $("#bpmnOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    $("#pdfOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    $("#docOverviewLink").attr("href", "../../assets/process/details/" + response.content);

                    if ($(currentElement).attr('id') == 'saveProcessBtn') {
                        window.location = "../../assets/process/details/" + response.content;
                    }
                } else {
                    alertify.error(response.content);
                }
            },
            error: function () {
                alertify.error('Process saving error');
            }
        });

    }
}

function getProcessInfo() {
    tagList = $('#tag-box').val().split(",");
    var processDetails = {
        'processName': $("#pName").val(),
        'processVersion': $("#pVersion").val(),
        'processOwner': $("#pOwner").val(),
        'processDescription': $("#overview_description").val(),
        'processTags': tagList.toString(),
        'subprocess': readSubprocessTable(),
        'successor': readSuccessorTable(),
        'predecessor': readPredecessorTable(),
        'image': getImageData()
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
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    if ($(currentElement).attr('id') == 'processTxtSaveBtn') {
                        alertify.success("Successfully saved the process content.");
                    }
                } else {
                    alertify.error(response.content);
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

function isAlreadyExist(value, tableName) {
    var matched = false;
    $('#table_' + tableName + ' tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == value) {
            matched = true;
        }
    });
    return matched;
}

function subProcessNamesAutoComplete() {
    var temp = processNames.slice();
    for (var i = 0; i < processNames.length; i++) {
        if (isAlreadyExist(processNames[i], "subprocess")) {
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
        if (isAlreadyExist(processNames[i], "successor")) {
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
        if (isAlreadyExist(processNames[i], "predecessor")) {
            temp[i] = "";
        }
    }

    $(".predecessor_Name").autocomplete({
        source: temp
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

function readImage(element) {
    var reader = new FileReader();
    var file = element.files[0];
    var preview = document.getElementById("processImage");
    reader.onload = function (e) {
        var encodedImg = e.target.result.replace(/^data:image\/(png|jpg);base64,/, "");
        var ext = element.value.split('.').pop().toLowerCase();
        var imageObj = {
            imgValue: "images_thumbnail",
            extension: ext,
            binaryImg: btoa(encodedImg)
        };
        preview.value = JSON.stringify(imageObj);
    };
    if (file) {
        reader.readAsBinaryString(file);
    }
}

function getImageData() {
    var processImage = $("#processImage").val();
    if (processImage.length == 0) {
        return {};
    }
    return JSON.parse(processImage);
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
            alertify.error('Process List error');
        }
    });
}

function getAllProcessTags() {
    $.ajax({
        url: '/publisher/assets/process/apis/get_process_tags',
        type: 'GET',
        async: false,
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                var processTagsObj = JSON.parse(response.content);
                if (!$.isEmptyObject(processTagsObj)) {
                    for (var key in processTagsObj) {
                        if (processTagsObj.hasOwnProperty(key)) {
                            allProcessTags.push(key);
                        }
                    }
                }
            } else {
                alertify.error(response.content);
            }
        },
        error: function () {
            alert.error('Process tag list returning error');
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
        if ($.inArray(ext, ['docx', 'doc', 'pdf']) == -1) {
            alertify.error('invalid extension!');
            return false;
        }
        $("#docExtension").val(ext);
    }
    return true;
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

function showSearchModal(tableName) {
    setTableName(tableName);
    $("#process-search-results").html("");
    document.getElementById("process-search-form").reset();
    $("#searchModal").modal("show");
}

function deleteProcess(element) {
    document.getElementById("table_" + element.getAttribute("data-name")).
        deleteRow(element.parentElement.parentElement.rowIndex);
}

$("#saveAsPNGBtn").click(function () {
    html2canvas($("#canvas"), {
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
                    var text = $svg.parent().find(":input")[0];
                    $(text).css('z-index', '100');
                    offset = $svg.position();
                    svgStr = this.outerHTML;
                    ctx.drawSvg(svgStr, offset.left, offset.top);
                });

                $endpoints = $('.jsplumb-endpoint > svg');
                $endpoints.each(function () {
                    $svg = $(this);
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
