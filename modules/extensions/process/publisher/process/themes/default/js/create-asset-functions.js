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
var pname,pversion,PID;

window.onload = function () {
    getProcessList();
    getAllProcessTags();

    $('#tagbox').tokenfield({
        autocomplete: {
            source: allProcessTags,
            delay: 100
        },
        showAutocompleteOnFocus: true
    });

    $('#tagbox').on('tokenfield:createtoken', function (event) {
        var existingTokens = $(this).tokenfield('getTokens');
        $.each(existingTokens, function (index, token) {
            if (token.value === event.attrs.value)
                event.preventDefault();
        });
    });

    loadOverview();

};

function showTextEditor(element) {
    // if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
    //     alertify.error('please fill the required fields.');
    // } else {
    // saveProcess(element);


    completeTextDetails();
    $("#processTextDiv").show();
    $("#bpmnEditDiv").hide();
    $("#docEditDiv").hide();
    $("#flowChartView").hide();
    $("#processTextContainer").hide();
    $("#bpmnViewDiv").hide();
    $("#docViewDiv").hide();
    $('#textEditor').addClass("clicked");
    $('#bpmn').removeClass("clicked");
    $('#flowChart').removeClass("clicked");
    $('#doc').removeClass("clicked");

    tinymce.init({
        selector: "#processContent"
    });

    if($("#textadded").hasClass("fw-check")) {

        $.ajax({
            url: '/publisher/assets/process/apis/get_process_text?process_text_path=/processText/' + pname + "/" + pversion,
            type: 'GET',
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    $("#processText").html(response.content);
                } else {
                    alertify.error(response.content);
                }
            },
            error: function () {
                alertify.error('Text editor error');
            }
        });


        if (!($(element).attr('id') == 'editText')) {
            $("#processTextContainer").show();
            $("#processTextDiv").hide();

        }
    }

    // }
}


function associateBPMN(element) {
    // if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
    //     alertify.error('please fill the required fields.');
    // } else {

    //saveProcess(element);
    $("#bpmnProcessName").val(pname);
    $("#bpmnProcessVersion").val(pversion);
    $("#processTextDiv").hide();
    $("#docEditDiv").hide();
    $("#bpmnEditDiv").show();
    $("#flowChartView").hide();
    $("#processTextContainer").hide();
    $("#bpmnViewDiv").hide();
    $("#docViewDiv").hide();
    $('#textEditor').removeClass("clicked");
    $('#bpmn').addClass("clicked");
    $('#flowChart').removeClass("clicked");
    $('#doc').removeClass("clicked");

    if($("#bpmnadded").hasClass("fw-check")){


        $.ajax({
            url: '/publisher/assets/process/apis/get_bpmn_content?bpmn_content_path=/_system/governance/bpmn/' + pname + "/" + pversion,
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

        $("#bpmnViewDiv").show();
        $("#bpmnEditDiv").hide();

    }

    // }
}

function associateFlowChart(element) {
    // if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
    //     alertify.error('please fill the required fields.');
    // } else {
    // saveProcess(element);
    // $('#flow-chart-view-header').text($('#pName').val());
    // $("#overviewDiv").hide();
    $("#flowChartView").show();
    $("#docEditDiv").hide();
    $("#docViewDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#processTextDiv").hide();
    $("#processTextContainer").hide();
    $("#bpmnViewDiv").hide();
    $('#textEditor').removeClass("clicked");
    $('#bpmn').removeClass("clicked");
    $('#flowChart').addClass("clicked");
    $('#doc').removeClass("clicked");
    //}
}

function associateDocument(element) {
    // if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
    //     alertify.error('please fill the required fields.');
    // } else {
    // $('#document-view-header').text($('#pName').val());
    //saveProcess(element);
    // $("#overviewDiv").hide();
    $("#processTextDiv").hide();
    $("#bpmnEditDiv").hide();
    $("#docEditDiv").show();
    $("#docViewDiv").hide();
    $("#flowChartView").hide();
    $("#processTextContainer").hide();
    $("#bpmnViewDiv").hide();
    $('#textEditor').removeClass("clicked");
    $('#bpmn').removeClass("clicked");
    $('#flowChart').removeClass("clicked");
    $('#doc').addClass("clicked");

    if($("#docadded").hasClass("fw-check")){
        $("#docEditDiv").hide();
        showDocument();
        $("#docViewDiv").show();
    }

    // }
}


function showMain() {
    $("#mainView").show();
    $("#bpmnEditDiv").hide();
    $("#processTextView").hide();
    $("#flowChartView").hide();
}

function saveProcess(currentElement) {
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
    } else {
        // save the process
        pname=$("#pName").val();
        pversion=$("#pVersion").val();
        if ($(currentElement).attr('id') == 'saveProcessBtn' || $(currentElement).attr('id') == 'detailsProcessBtn') {
            var imageElement = $("#images_thumbnail");
            if (imageElement.val().length != 0) {
                var ext = imageElement.val().split('.').pop().toLowerCase();
                if ($.inArray(ext, ['png', 'jpeg','jpg','gif','ico']) == -1) {
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
                    // $("#processTextOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    // $("#bpmnOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    // $("#pdfOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    // $("#docOverviewLink").attr("href", "../../assets/process/details/" + response.content);
                    PID=response.content;

                    if ($(currentElement).attr('id') == 'saveProcessBtn') {
                        window.location = "../../assets/process/details/" + response.content;
                    }
                    else if($(currentElement).attr('id') == 'detailsProcessBtn'){
                        $('#stp1').removeClass("current");
                        $('#stp1').addClass("completed");
                        $('#stp2').addClass("current");
                        loadDetails();
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



function processAssociations(currentElement){

    $('#stp2').removeClass("current");
    $('#stp2').addClass("completed");
    $('#stp3').addClass("current");
    $('#detailDiv').remove();
    loadAssociations();

}

function loadOverview(){
    $("#overviewDiv").show();
    $("#detailDiv").hide();
    $("#associationDiv").hide();
}

function loadDetails() {
    $("#overviewDiv").hide();
    $("#detailDiv").show();
    $("#associationDiv").hide();
}

function loadAssociations() {
    $("#overviewDiv").hide();
    $("#detailDiv").hide();
    $("#associationDiv").show();
}

function getProcessInfo() {
    tagList = $('#tagbox').val().split(",");
    var list = [];
    var processDetails = {
        'processName': pname,
        'processVersion': pversion,
        'processOwner': $("#pOwner").val(),
        'processDescription': $("#overview_description").val(),
        'processTags': tagList.toString(),
        'subprocess': list,//readSubprocessTable(),
        'successor': list,//readSuccessorTable(),
        'predecessor': list,//readPredecessorTable(),
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
                'processName': pname,
                'processVersion': pversion,
                'processText': textContent
            },
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    if ($(currentElement).attr('id') == 'processTxtSaveBtn') {
                        alertify.success("Successfully saved the process content.");
                        $("#processTextDiv").hide();
                        $("#textadded").addClass("fw fw-check");
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
    $("#bpmnProcessName").val(pname);
    $("#bpmnProcessVersion").val(pversion);
    $("#bpmnEditDiv").hide();
    alertify.success("Successfully added BPMN model.");
    $("#bpmnadded").addClass("fw fw-check");
    return true;
}

function completeTextDetails() {
    $("#textProcessName").val(pname);
    $("#textProcessVersion").val(pversion);
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
    $("#docProcessName").val(pname);
    $("#docProcessVersion").val(pversion);
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
    $("#docEditDiv").hide();
    alertify.success("Document added successfully.");
    $("#docadded").addClass("fw fw-check");
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

function updateDetails(){

    if($("textEditor").cla){}
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

function isProcessNotAvailableInList(processName) {
    for (var i = 0; i < processNames.length; i++) {
        if (processNames[i] == processName) {
            return false;
        }
    }
    return true;
}

function getMainProcess() {
    var mainProcess = pname + "-" + pversion;
    return mainProcess;
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
            'processName': pname,
            'processVersion': pversion,
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
            'processName': pname,
            'processVersion': pversion,
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
            'processName': pname,
            'processVersion': pversion,
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

function saveFlowchart(){

    _saveFlowchart();
    $("#flowChartView").hide();
    $("#flowchartadded").addClass("fw fw-check");
}

function close() {
    window.close();
}


function editProcessText(element) {
    $("#processContent").val($("#processText").html());
    showTextEditor(element);
}

function deleteBPMNDiagram() {

    $("#bpmnadded").removeClass("fw-check");

    $.ajax({
        url: '/publisher/assets/process/apis/delete_bpmn',
        type: 'POST',
        data: {
            'processName': pname,
            'processVersion': pversion
        },
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                //window.location = "../../process/details/" + response.content;
            } else {
                alertify.error(response.content);
            }
        },
        error: function () {
            alertify.error('BPMN diagram deleting error');
        }
    });

    $("#bpmnViewDiv").hide();

}

function removeBPMNDiagramListener() {

    var question = "Are you sure you want to delete the BPMN diagram of process " + pname + " " + pversion + " permanently ?";
    var confirmModal = confirmDialog(question);
    confirmModal.find('#okButton').click(function (event) {
        deleteBPMNDiagram();
        confirmModal.modal('hide');
    });
    confirmModal.modal('show');
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
        url: '/publisher/assets/process/apis/get_process_doc?process_path=/_system/governance/processes/' + pname + "/" + pversion,
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

function loadDetail(){

    window.location = "../../assets/process/details/" + PID;

}