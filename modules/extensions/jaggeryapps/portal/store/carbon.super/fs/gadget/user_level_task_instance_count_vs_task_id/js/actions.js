/*
 ~ Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
var jsonObj = [];
var user;

function drawGraph() {
    var userId = $('#UserList').val();

    if (userId != '') {
        var body = {
            'processId' : $('#ProcessList').val(),
            'userId': userId,
            'order': $('#TaskDefOrder').val(),
            'count': parseInt($('#TaskDefCount').val())
        };

        $.ajax({
            type: 'POST',
            url: '../../designer/assets/process/apis/user_level_task_instance_count_vs_task_id',
            data: {'filters': JSON.stringify(body)},
            success: function (data) {
                var responseJsonArr = [];
                if (!$.isEmptyObject(data)) {

                    responseJsonArr = JSON.parse(data);

                    var responseStr = '';
                    for (var i = 0; i < responseJsonArr.length; i++) {
                        var temp = '["' + responseJsonArr[i].taskDefId + '",' + responseJsonArr[i].taskInstanceCount + '],';
                        responseStr += temp;
                    }

                    responseStr = responseStr.slice(0, -1);
                    var jsonArrObj = JSON.parse('[' + responseStr + ']');
                    jsonObj[0].data = jsonArrObj;
                    
                    config.width = $('#chartA').width();
                    config.height = $('#chartA').height();
                    var barChart = new vizg(jsonObj, config);
                    barChart.draw("#chartA", [{type: "click"}]);

                }
                else {
                    jsonObj[0].data = [];
                    config.width = $('#chartA').width();
                    config.height = $('#chartA').height();
                    var barChart = new vizg(jsonObj, config);
                    barChart.draw("#chartA", [{type: "click"}]);
                }
            },
            error: function (xhr, status, error) {
                var errorJson = eval("(" + xhr.responseText + ")");
                alert(errorJson.message);
            }
        });
    } else {
        alert("User id list is empty.");
    }
}
function loadUserList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;
    user = getUrlVars()["user"];

    $.getJSON("/portal/store/carbon.super/fs/gadget/user_level_task_instance_count_vs_task_id/js/meta-data-userleveltaskInstanceCountVsTaskID.json.js", function (result) {
        $.each(result, function (i, field) {
            jsonObj.push(field);
            if (user) {
                var el = document.createElement("option");
                el.textContent = user;
                el.value = user;
                $(dropdownElementID).append(el);
                $(dropdownElementID).attr("disabled", true);
                $(dropdownElementID).selectpicker("refresh");
                drawGraph();
            }
            else {
                loadList(dropdownElementID);
            }
        });
    });

}
function loadList(dropdownElementID) {
    $.ajax({
        type: 'POST',
        url: '../../designer/assets/process/apis/user_id_list',
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var dataStr = JSON.parse(data);
                for (var i = 0; i < dataStr.length; i++) {
                    var opt = dataStr[i].assignUser;
                    var el = document.createElement("option");
                    el.textContent = opt;
                    el.value = opt;
                    $(dropdownElementID).append(el);
                }
                $(dropdownElementID).selectpicker("refresh");
                drawGraph();
            }
            else {
                alert('Empty User ID list.');
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}

function loadProcessList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;
    $.ajax({
        type: 'POST',
        url: "../../designer/assets/process/apis/process_definition_key_list",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var dataStr = JSON.parse(data);
                for (var i = 0; i < dataStr.length; i++) {
                    var opt = dataStr[i].processDefKey;
                    var el = document.createElement("option");
                    el.textContent = opt;
                    el.value = opt;
                    $(dropdownElementID).append(el);
                }
                $(dropdownElementID).selectpicker("refresh");
            }
            loadUserList("UserList");
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}

function selectPickerValChange(selectPickerElement) {
    var idx = selectPickerElement.options.selectedIndex;
    if (selectPickerElement.options[idx].value == 'other') {
        var other = prompt("Please indicate 'other' value:");
        if (other != '' && isInteger(other)) {
            var opt = document.createElement('option');
            opt.value = other;
            opt.innerHTML = other;
            selectPickerElement.appendChild(opt);
            $(selectPickerElement).selectpicker('val', other);
        } else {
            selectPickerElement.selectedIndex = 1;
            $(selectPickerElement).selectpicker("refresh");
        }
    }
}

function isInteger(param) {
    return (Math.floor(param) == param && $.isNumeric(param));
}

function getUrlVars() {
    var vars = [], hash;
    var hashes = top.location.href.slice(top.location.href.indexOf('?') + 1).split('&');

    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }

    return vars;
}