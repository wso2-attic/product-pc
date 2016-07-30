var config = {
    type: "bar",
    x : "Process Version",
    highlight : "multi",
    charts : [{type: "bar",  y : "Time"}],
    maxLength: 200,
    padding: {"top": 10, "left": 80, "bottom": 150, "right": 0},
    xAxisAngle:true,
    transform:[60,70]
}

var jsonObj = [];

var callbackmethod = function(event, item) {
    alert('chart clicked');
}

window.onload = function() {
    $.getJSON("/portal/store/carbon.super/fs/gadget/avg_time_vs_process_version/js/meta-data.json.js", function(result){
        $.each(result, function(i, field){
            jsonObj.push(field);
            loadProcessKeyList(processVersionAvgExecTimeProcessList);
        });
    });
}

function drawAvgExecuteTimeVsProcessVersionResult() {
    var processKey = $('#processVersionAvgExecTimeProcessList').val();
    var body = {
        'processKey': processKey,
        'order': $('#processVersionAvgExecTimeOrder').val(),
        'count': parseInt($('#processVersionAvgExecTimeCount').val())
    };

    if (processKey != null) {
        $.ajax({
            url: '../../bpmn-analytics-explorer/avg_time_vs_process_version',
            type: 'POST',
            data: {'filters': JSON.stringify(body)},
            success: function (data) {
                var responseJsonArr = [];
                if (!$.isEmptyObject(data))
                    responseJsonArr = JSON.parse(data);

                var responseStr = '';
                for (var i = 0; i < responseJsonArr.length; i++) {
                    var temp = '["' + responseJsonArr[i].processVer + '",' + responseJsonArr[i].avgExecutionTime + '],';
                    responseStr += temp;
                }
                responseStr = responseStr.slice(0, -1);
                var jsonArrObj = JSON.parse('[' + responseStr + ']');
                jsonObj[0].data = jsonArrObj;

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
    } else {
        alert("Nothing selected");
    }
    $('.collapse').collapse("hide");
}

function loadProcessKeyList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;
    var url = "../../bpmn-analytics-explorer/process_key_list";
    $.ajax({
        type: 'POST',
        url: url,
        success: function (data) {
            var dataStr = JSON.parse(data);
            if (!$.isEmptyObject(dataStr)) {
                for (var i = 0; i < dataStr.length; i++) {
                    var opt = dataStr[i].processKey;
                    var el = document.createElement("option");
                    el.textContent = opt;
                    el.value = opt;
                    $(dropdownElementID).append(el);
                }
                drawAvgExecuteTimeVsProcessVersionResult();
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}