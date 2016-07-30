var config = {
    type: "bar",
    x : "Task Instance Id",
    highlight : "multi",
    charts : [{type: "bar",  y : "Execution Time"}],
    maxLength: 200,
    xAxisAngle:true,
    padding: {"top": 10, "left": 80, "bottom": 100, "right": 0},
    transform:[60,70]
}

var jsonObj = [];

var callbackmethod = function(event, item) {
    
}

window.onload = function() {
    $.getJSON("/portal/store/carbon.super/fs/gadget/exec_time_vs_task_instance_id/js/meta-data.json.js", function(result){
        $.each(result, function(i, field){
            jsonObj.push(field);
            loadTaskList('taskInstanceIdExecTimeTaskList');
        });
    });
    $('.collapse').collapse("hide");
}

function drawExecutionTimeVsTaskInstanceIdResult() {

    var taskId = $('#taskInstanceIdExecTimeTaskList').val();

    if (taskId != '') {

        var body = {
            'startTime': $("#from").val()||0,
            'endTime': $("#to").val()||0,
            'taskId': taskId,
            'order': $('#taskInstanceIdExecTimeOrder').val(),
            'limit': parseInt($('#taskInstanceIdExecTimeLimit').val())
        };
        
            $.ajax({
                url: '../../bpmn-analytics-explorer/exec_time_vs_task_instance_id',
                type: 'POST',
                data: {'filters': JSON.stringify(body)},
                success: function (data) {
                    // console.log(data);
                    var responseJsonArr = [];
                    if (!$.isEmptyObject(data))
                        responseJsonArr = JSON.parse(data);
    
                    var responseStr = '';
                    for (var i = 0; i < responseJsonArr.length; i++) {
                        var temp = '["' + responseJsonArr[i].taskInstanceId + '",' + responseJsonArr[i].duration + '],';
                        responseStr += temp;
                    }
                    responseStr = responseStr.slice(0, -1);
                    var jsonArrObj = JSON.parse('[' + responseStr + ']');
                    jsonObj[0].data = jsonArrObj;
                    // console.log(jsonObj);

                    config.width = $('#chartA').width();
                    config.height = $('#chartA').height();
                    var barChart = new vizg(jsonObj, config);
                    barChart.draw("#chartA", [{type: "click", callback: callbackmethod}]);
                },
                error: function (xhr, status, error) {
                    var errorJson = eval("(" + xhr.responseText + ")");
                    alert(errorJson.message);
                }
            });
        $('.collapse').collapse("hide");
    }
}


function loadTaskList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;
    var url = "../../bpmn-analytics-explorer/task_definition_key_list";
    $.ajax({
        type: 'POST',
        url: url,
        success: function (data) {
            var dataStr = JSON.parse(data);
            if (!$.isEmptyObject(dataStr)) {
                for (var i = 0; i < dataStr.length; i++) {
                    var opt = dataStr[i].taskDefId;
                    var el = document.createElement("option");
                    el.textContent = opt;
                    el.value = opt;
                    $(dropdownElementID).append(el);
                }
                drawExecutionTimeVsTaskInstanceIdResult();
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}
