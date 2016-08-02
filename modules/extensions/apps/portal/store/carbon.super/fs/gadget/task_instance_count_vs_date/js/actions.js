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

function drawGraph() {

    var startDate = document.getElementById("from");
    var startDateTemp = startDate.value;

    var endDate = document.getElementById("to");
    var endDateTemp = endDate.value;

    var taskIds = $('#taskInstanceCountDateTaskList').val();
    var taskIdArray = [];
    if (taskIds != null) {
        $('#taskInstanceCountDateTaskList :selected').each(function (i, selected) {
            taskIdArray[i] = $(selected).val();
        });
    }
    
    if(startDateTemp == 0|| endDateTemp == 0 ) {
        endDateTemp = new Date();
        startDateTemp = new Date(today.getFullYear(),(today.getMonth()+1-3),today.getDate());
    }

    var body = {
        'startTime': startDateTemp,
        'endTime': endDateTemp,
        'taskIdList': taskIdArray
    };

    $.ajax({
        type: 'POST',
        url: '../../bpmn-analytics-explorer/task_instance_count_vs_date',
        data: {'filters': JSON.stringify(body)},
        success: function (data) {

            var responseJsonArr = [];
            if (!$.isEmptyObject(data)) {
                responseJsonArr = JSON.parse(data);

                var responseStr = '';
                for (var i = 0; i < responseJsonArr.length; i++) {
                    var temp = '["' + responseJsonArr[i].finishTime + '",' + responseJsonArr[i].taskInstanceCount + '],';
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
}
function loadTaskList(dropdownId) {

    var dropdownElementID = document.getElementById(dropdownId);

    $.ajax({
        type: 'POST',
        url: '../../bpmn-analytics-explorer/task_definition_key_list',
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var dataStr = JSON.parse(data);
                for (var i = 0; i < dataStr.length; i++) {
                    var opt = dataStr[i].taskDefId;
                    var el = document.createElement("option");
                    el.textContent = opt;
                    el.value = opt;
                    dropdownElementID.appendChild(el);
                }
                $('.selectpicker').selectpicker('refresh');

                $.getJSON("/portal/store/carbon.super/fs/gadget/task_instance_count_vs_date/js/meta-data-taskInstanceCountVsDate.json.js", function (result) {
                    $.each(result, function (i, field) {
                        jsonObj.push(field);
                        drawGraph();
                    });

                });
            }
            else{
                console.log('Empty Task ID list.');
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}
