var config = {
    type: "bar",
    x : "Process",
    highlight : "multi",
    charts : [{type: "bar",  y : "Time"}],
    maxLength: 200,
    xAxisAngle:true,
    padding: {"top": 10, "left": 80, "bottom": 150, "right": 0},
    transform:[60,70]
}


var jsonObj = [];

var callbackmethod = function(event, item) {

}

window.onload = function() {
    $.getJSON("/portal/store/carbon.super/fs/gadget/avg_time_vs_process_id/js/meta-data.json.js", function(result){
        $.each(result, function(i, field){
            jsonObj.push(field);
            drawAvgExecuteTimeVsProcessIdResult();
        });
    });
};

function drawAvgExecuteTimeVsProcessIdResult() {
    var body = {
        'startTime': $("#from").val()||0,
        'endTime': $("#to").val()||0,
        'order': $('#processIdAvgExecTimeOrder').val(),
        'count': parseInt($('#processIdAvgExecTimeCount').val())
    };

    $.ajax({
        url: '../../bpmn-analytics-explorer/avg_time_vs_process_id',
        type: 'POST',
        data: {'filters': JSON.stringify(body)},
        success: function (data) {
            // console.log(data);
            var responseJsonArr = [];
            if(!$.isEmptyObject(data))
                responseJsonArr = JSON.parse(data);

            var responseStr = '';
            for(var i = 0; i < responseJsonArr.length; i++) {
                var temp = '["' + responseJsonArr[i].processDefKey+'",'+responseJsonArr[i].avgExecutionTime+'],';
                responseStr+=temp;
            }
            responseStr = responseStr.slice(0,-1);
            var jsonArrObj = JSON.parse('[' + responseStr + ']');
            jsonObj[0].data = jsonArrObj;

            config.width = $('#chartA').width();
            config.height = $('#chartA').height();
            var barChart = new vizg(jsonObj, config);
            barChart.draw("#chartA", [{type:"click", callback:callbackmethod}]);
        },
        error: function () {
            
        }
    });
    $('#collapse').collapse("hide");
}
