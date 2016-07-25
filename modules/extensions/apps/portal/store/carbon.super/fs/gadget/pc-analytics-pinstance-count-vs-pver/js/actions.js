var config = {
    type: "bar",
    x : "Process Version",
    highlight : "multi",
    charts : [{type: "bar",  y : "Process Instance Count"}],
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
    $.getJSON("/portal/store/carbon.super/fs/gadget/pc-analytics-pinstance-count-vs-pver/js/meta-data.json.js", function(result){
        $.each(result, function(i, field) {
            jsonObj.push(field);
            loadProcessKeyList("processInstanceCountProcessVersionProcessList");
        });
    });

}

function drawProcessInstanceCountVsProcessVersionResult() {

    var processKey = $('#processInstanceCountProcessVersionProcessList').val();
    if (processKey != '') {
        var body = {
            'processKey': processKey,
            'order': $('#processInstanceCountProcessVersionOrder').val(),
            'count': parseInt($('#processInstanceCountProcessVersionCount').val())
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
            url: '../../bpmn-analytics-explorer/process_instance_count_vs_process_version',
            type: 'POST',
            data: {'filters': JSON.stringify(body)},
            success: function (data) {
                // console.log(data);
                var responseJsonArr = [];
                if (!$.isEmptyObject(data))
                    responseJsonArr = JSON.parse(data);

                var responseStr = '';
                for (var i = 0; i < responseJsonArr.length; i++) {
                    var temp = '["' + responseJsonArr[i].processVer + '",' + responseJsonArr[i].processInstanceCount + '],';
                    responseStr += temp;
                }
                responseStr = responseStr.slice(0, -1);
                var jsonArrObj = JSON.parse('[' + responseStr + ']');
                jsonObj[0].data = jsonArrObj;

                config.width = $('#chartA').width();
                config.height = $('#chartA').height() - $('#chartA').height()/5;
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
                drawProcessInstanceCountVsProcessVersionResult();
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}
