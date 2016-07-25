var config = {
    type: "bar",
    x : "Process",
    highlight : "multi",
    charts : [{type: "bar",  y : "Instance Count"}],
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
    
    $.getJSON("/portal/store/carbon.super/fs/gadget/pc-analytics-pid-vs-count/js/meta-data.json.js", function(result){
        $.each(result, function(i, field){
            jsonObj.push(field);
            drawProcessInstanceCountVsProcessIdResult();
        });
    });
}

function drawProcessInstanceCountVsProcessIdResult() {
    var body = {
        'startTime': $("#from").val()||0,
        'endTime': $("#to").val()||0,
        'order': $('#processInstanceCountProcessDefOrder').val(),
        'count': parseInt($('#processInstanceCountProcessDefCount').val())
    };

    $("g.mark-text").ready(function () {
        $("g.mark-text").first().children().hide();
        setTimeout(function() {
            $("svg").css("height", "100%");
            $("g.mark-text").first().children().show();
            $("g.mark-text").first().children().attr("text-anchor", "end");
            $("g.mark-text").first().children().each(function(){
                $(this).attr("transform", $(this).attr("transform") + " rotate(-65)");
            })
            $("#chartA").show();
        }, 200);
    })

    $.ajax({
        url: '../../bpmn-analytics-explorer/process_instance_count_vs_process_id',
        type: 'POST',
        data: {'filters': JSON.stringify(body)},
        success: function (data) {
            // console.log(data);
            var responseJsonArr = [];
            if(!$.isEmptyObject(data))
                responseJsonArr = JSON.parse(data);

            var responseStr = '';
            for(var i = 0; i < responseJsonArr.length; i++) {
                var temp = '["' + responseJsonArr[i].processDefKey+'",'+responseJsonArr[i].processInstanceCount+'],';
                responseStr+=temp;
            }
            responseStr = responseStr.slice(0,-1);
            var jsonArrObj = JSON.parse('[' + responseStr + ']');
            jsonObj[0].data = jsonArrObj;
            // console.log(jsonObj);

            config.width = $('#chartA').width();
            config.height = $('#chartA').height() - $('#chartA').height()/5;
            $("#chartA").hide();
            var barChart = new vizg(jsonObj, config);
            barChart.draw("#chartA", [{type:"click", callback:callbackmethod}]);
        },
        error: function () {
            
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
