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

/*
 Add a new row for a new process variable to the process Variables table in the Config Analytics view
 */
var beginRowNo = 0;
function addProcessVariableRow(tableID) {

    var table = document.getElementById(tableID);
    beginRowNo++;
    var row = table.insertRow(table.rows.length);

     row.innerHTML =
            '<td><input type="checkbox" name="chk"/></td>' +
            '<td><input type="text" name="txt" id="process_variable" onkeydown="processVariableAutoComplete(this)" style="display:table-cell; width:100%; padding-left: 8px;" width:100%"/>'
            + '<td>' +
            '<select id="selVarType_' + beginRowNo + '" name="varType" style="display:table-cell; width:100%" ' +
            'onchange="disableCheckBox(' + beginRowNo + ')">' +
            '<option value="int">int</option>' +
            '<option value="long">long</option>' +
            '<option value="double">double</option>' +
            '<option value="float">float</option>' +
            '<option value="string">string</option>' +
            '<option value="bool">bool</option>' +
            '</select>' + '</td>'+
            '<td align="center" style="outline: thin solid #66c2ff"><input id="chkAnalyzedData_' + beginRowNo + '" ' +
            'type="checkbox" name="chkAnalyzedData" /></td>' +
            '<td align="center" style="outline: thin solid #66c2ff"><input id="chkDrillData_' + beginRowNo + '" ' +
            'type="checkbox" name="chkDrillData" disabled/></td>';
}

    function processVariableAutoComplete(me){
      var processVariables = []
      var processVariablesObj
      $.ajax({
            url: '/designer/assets/process/apis/get_process_variables?processName='+$('#processNameHiddenElement').
            val()+'&processVersion='+$('#processVersionHiddenElement').val(),
            type: 'GET',
            async: false,
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error == false) {
                    processVariablesObj = JSON.parse(response.content);
                    for (var i = 0; i < Object.keys(processVariablesObj).length; i++) {
                        processVariables.push(Object.keys(processVariablesObj)[i]);
                    }
                } else {
                    alertify.error(response.content);
                }
            },

        });
       $(me).autocomplete(
           { source: processVariables,
           messages: {
        noResults: '',
        results: function() {} }},
           {select: function(e, ui) {
                   $(me).parent().parent().children().eq(2).children().eq(0).val(processVariablesObj[ui.item.value]);
                   $(me).parent().parent().children().eq(2).children().eq(0).prop('disabled', true);

                }
           }

       );
    }

/*
 Disable process variable's "Analyzing Data" and "Drill Down Variable" check boxes accordingly to the data type of the variable
 */
function disableCheckBox(rowIndex) {
    var e = document.getElementById("selVarType_" + rowIndex);
    var varType = e.options[e.selectedIndex].value;
    if (varType === "string" || varType == "bool") {
        document.getElementById("chkAnalyzedData_" + rowIndex).checked = false;
        document.getElementById("chkAnalyzedData_" + rowIndex).disabled = true;
        document.getElementById("chkDrillData_" + rowIndex).disabled = false;
    } else {
        document.getElementById("chkDrillData_" + rowIndex).checked = false;
        document.getElementById("chkDrillData_" + rowIndex).disabled = true;
        document.getElementById("chkAnalyzedData_" + rowIndex).disabled = false;
    }
}

/*
 Delete process variable related table row in the Config Analytics view
 */
function deleteProcessVariableRow(tableID) {
    try {
        var table = document.getElementById(tableID);
        var rowCount = table.rows.length;

        for (var i = 0; i < rowCount; i++) {
            var row = table.rows[i];
            var chkbox = row.cells[0].childNodes[0];
            if (null != chkbox && true == chkbox.checked) {
                if (rowCount <= 1) {
                    alert("No rows to delete.");
                    break;
                }
                table.deleteRow(i);
                rowCount--;
                i--;
            }
        }

    } catch (e) {
        alert(e);
    }
}

//Information on event stream payload data fields (this includes processVariables)
var eventStreamPayloadFields = [];

var processName;
var processVersion;

/*
 Save the process variables which need to be configured for analytics, in process rxt in Governance Registry
 */
function saveProcessVariables(tableID, callback) {
    var table = document.getElementById(tableID);
    var rowCount = table.rows.length;
    //object to keep meta data on process variables with related processName, processVersion.This will be sent to save
    // in process rxt
    var processVariablesInfo = {};
    var processVariables = {};

    if (rowCount <= 1) {
        alertify.success("No Process Variable To Config..!")
        return "ERROR";
    }

    processName = $('#processNameHiddenElement').val();
    processVersion = $('#processVersionHiddenElement').val();
    processVariablesInfo["processName"] = processName;
    processVariablesInfo["processVersion"] = processVersion;

    //add process instanceId field to the process variables objects array, so that there will be a field (as
    // 1st field) for that processInstanceId too in the event Stream that will be created in DAS
    var procInstancIdItem = {};
    procInstancIdItem["name"] = "processInstanceId";
    procInstancIdItem["type"] = "string";
    procInstancIdItem["isAnalyzeData"] = false;
    procInstancIdItem["isDrillDownData"] = false;
    eventStreamPayloadFields.push(procInstancIdItem);

    //add field to get process_variable_values availability string to the eventStreamPayloadFields array, so that there
    // will be a field (as 2nd field) for that valuesAvailability too in the event Stream that will be created in DAS
    var valuesAvailabilityField = {};
    valuesAvailabilityField["name"] = "valuesAvailability";
    valuesAvailabilityField["type"] = "string";
    valuesAvailabilityField["isAnalyzeData"] = false;
    valuesAvailabilityField["isDrillDownData"] = false;
    eventStreamPayloadFields.push(valuesAvailabilityField);

    for (var i = 1; i < rowCount; i++) {
        var item = {};
        var row = table.rows[i];
        if (!(row.cells[1].children[0].value)) {
            alertify.error("Process Variable Names Cannot Be Empty");
            return "ERROR";
        }
        var variableName = row.cells[1].children[0].value;
        var variableType = row.cells[2].children[0].value;
        var isAnalyzdData = row.cells[3].children[0].checked;
        var isDrillDownData = row.cells[4].children[0].checked;

        if (null != variableName && null != variableType) {
            processVariables[variableName] = variableType + "##" + isAnalyzdData + "##" + isDrillDownData;
            item["name"] = variableName;
            item["type"] = variableType;
            item["isAnalyzeData"] = isAnalyzdData;
            item["isDrillDownData"] = isDrillDownData;
            eventStreamPayloadFields.push(item);
        }
    }
    processVariablesInfo["processVariables"] = processVariables;

    $.ajax({
        url: '/designer/assets/process/apis/save_process_variables',
        type: 'POST',
        data: {'processVariablesInfo': JSON.stringify(processVariablesInfo)},
        async: false,
        success: function (response) {
            if (response == "FAIL") {
                alertify.error('Process variables saving error');
                returnValue = "ERROR";
                callback(returnValue);
            } else {
                returnValue = "SUCCESS";
                callback(returnValue);
            }
        },
        error: function (response) {
            alertify.error('Process variables saving error');
            returnValue = "ERROR";
            callback1(returnValue);
        }
    });
}

/*
 Configure DAS for analytics- called seperately for each process
 */
function configAnalytics() {
    flagToReturn = false;
    if (!$('#eventStreamName').val()) {
        alertify.error("Event Stream Name Cannot be Empty");
        flagToReturn = true;
    }
    if (!$('#eventStreamVersion').val()) {
        alertify.error("Event Stream Version Cannot be Empty");
        flagToReturn = true;
    }
    if (!$('#eventReceiverName').val()) {
        alertify.error("Event Receiver Name Cannot be Empty");
        flagToReturn = true;
    }
    if (flagToReturn) {
        return;
    }

    saveProcessVariables('dataTable', function (callbackVal) {
        if (callbackVal == "ERROR") {
            return;
        }
    });

    var pcProcessId = processName + ":" + processVersion;
    var eventStreamName = $('#eventStreamName').val();
    var eventStreamVersion = $('#eventStreamVersion').val();
    var eventStreamDescription = $('#eventStreamDescription').val();
    var eventStreamNickName = $('#eventStreamNickName').val();
    var eventReceiverName = $('#eventReceiverName').val();
    var processDefinitionId = $('#processDefinitionId').val();

    var eventStreamId = eventStreamName + ":" + eventStreamVersion;
    var dasConfigData = {};
    dasConfigData["processDefinitionId"] = processDefinitionId;
    dasConfigData["eventStreamName"] = eventStreamName;
    dasConfigData["eventStreamVersion"] = eventStreamVersion;
    dasConfigData["eventStreamDescription"] = eventStreamDescription;
    dasConfigData["eventStreamNickName"] = eventStreamNickName;
    dasConfigData["eventStreamId"] = eventStreamId;
    dasConfigData["eventReceiverName"] = eventReceiverName;
    dasConfigData["pcProcessId"] = pcProcessId;
    dasConfigData["processVariables"] = eventStreamPayloadFields;

    $.ajax({
        url: '/designer/assets/process/apis/config_das_analytics',
        type: 'POST',
        data: {
            'dasConfigData': JSON.stringify(dasConfigData),
            'processName': processName,
            'processVersion': processVersion
        },
        success: function (response) {
            if (response == "SUCCESS") {
                alertify.success("Analytics Configuration Success");
                $("#btn_save_analytics_configurations").attr("disabled", true);
                $("#btn_addProcessVariablesRow").prop("onclick", null);
                $("#btn_deleteProcessVariablesRow").prop("onclick", null);
                $('#eventStreamName').attr('readonly', "true");
                $('#eventStreamVersion').attr("readonly", "true");
                $('#eventStreamDescription').attr("readonly", "true");
                $('#eventStreamNickName').attr("readonly", "true");
                $('#eventReceiverName').attr("readonly", "true");
                $('#processDefinitionId').attr("readonly", "true");

                $("#dataTuuable tr").each(function () {
                    $(this).children().each(function () {
                        $(this).find("input").attr("readonly", "true");
                        $(this).find("select").attr("disabled", "true");
                    });
                });
                window.location = "../../process/details/"+ window.location.href.
                substring(window.location.href.lastIndexOf("/") + 1, window.location.href.length);

            } else {
                alertify.error("Error in creating Event Stream/Reciever in DAS")
            }
        },
        error: function () {
            alertify.error('Error in config_das_analytics.jag while creating Event Stream/Reciever in DAS');
        }
    });
}