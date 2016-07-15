var config = {
    type: "bar",
    x : "Task Definition Key",
    highlight : "multi",
    charts : [{type: "bar",  y : "Avg Execution Time"}],
    maxLength: 200,
    padding: {"top": 50, "left": 80, "bottom": 80, "right": 0},
    transform:[60,70],
    width: 800,
    height: 400
}

var jsonObj = [];

var callbackmethod = function(event, item) {
    alert('chart clicked');
}

window.onload = function() {

    $.getJSON("/portal/store/carbon.super/fs/gadget/pc-analytics-tid-vs-time/js/meta-data.json.js", function(result){
        $.each(result, function(i, field){
            var pname = getUrlVars()["pname"];
            jsonObj.push(field);
            if(pname){
                var el = document.createElement("option");
                el.textContent = pname;
                el.value = pname;
                $(taskIdAvgExecTimeProcessList).append(el);
                $(taskIdAvgExecTimeProcessList).prop( "disabled", true );
                drawAvgExecuteTimeVsTaskIdResult();
            } else {
                loadProcessList('taskIdAvgExecTimeProcessList');
            }

        });
    });

}

function drawAvgExecuteTimeVsTaskIdResult() {

    var processId = $('#taskIdAvgExecTimeProcessList').val();

    if (processId != '') {
        var body = {
            'processId': processId,
            'order': $('#taskIdAvgExecTimeOrder').val(),
            'count': parseInt($('#taskIdAvgExecTimeCount').val())
        };

        $("g.mark-text").ready(function () {
            $("g.mark-text").first().children().hide();
            setTimeout(function () {
                $("svg").css("height", "100%");
                $("g.mark-text").first().children().show();
                $("g.mark-text").first().children().attr("text-anchor", "end");
                $("g.mark-text").first().children().each(function () {
                    $(this).attr("transform", $(this).attr("transform") + " rotate(-65)");
                });
                $("#chartA").show();
            }, 200);
        })

        $.ajax({
            url: '../../bpmn-analytics-explorer/avg_time_vs_task_id',
            type: 'POST',
            data: {'filters': JSON.stringify(body)},
            success: function (data) {
                // console.log(data);
                var responseJsonArr = [];
                if (!$.isEmptyObject(data))
                    responseJsonArr = JSON.parse(data);

                var responseStr = '';
                for (var i = 0; i < responseJsonArr.length; i++) {
                    var temp = '["' + responseJsonArr[i].taskDefId + '",' + responseJsonArr[i].avgExecutionTime + '],';
                    responseStr += temp;
                }
                responseStr = responseStr.slice(0, -1);
                var jsonArrObj = JSON.parse('[' + responseStr + ']');
                jsonObj[0].data = jsonArrObj;

                $("#chartA").hide();
                var barChart = new vizg(jsonObj, config);
                barChart.draw("#chartA", [{type: "click", callback: callbackmethod}]);
            },
            error: function (xhr, status, error) {
                var errorJson = eval("(" + xhr.responseText + ")");
                alert(errorJson.message);
            }
        });
    }
}

function loadProcessList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;
    var url = "/../../bpmn-analytics-explorer/process_definition_key_list";

    $.ajax({
        type: 'POST',
        url: url,
        success: function (data) {

            var dataStr = JSON.parse(data);
            if (!$.isEmptyObject(dataStr)) {
                for (var i = 0; i < dataStr.length; i++) {
                    var opt = dataStr[i].processDefKey;
                    var el = document.createElement("option");
                    el.textContent = opt;
                    el.value = opt;
                    $(dropdownElementID).append(el);
                }
                drawAvgExecuteTimeVsTaskIdResult();
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}

function getUrlVars()
{
    var vars = [], hash;
    var hashes = top.location.href.slice(top.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}
