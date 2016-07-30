var config = {
    type: "bar",
    x : "Completion Date",
    highlight : "multi",
    charts : [{type: "bar",  y : "Process Instance Count"}],
    maxLength: 200,
    xAxisAngle:true,
    padding: {"top": 20, "left": 80, "bottom": 100, "right": 0},
    transform:[60,70]
}

var jsonObj = [];

window.onload = function() {

    $.getJSON("/portal/store/carbon.super/fs/gadget/process_instance_count_vs_date/js/meta-data.json.js", function(result){
        $.each(result, function(i, field){

            var pname = getUrlVars()["pname"];
            jsonObj.push(field);
            if(pname) {
                var el = document.createElement("option");
                el.textContent = pname;
                el.value = pname;
                $(el).prop("selected", "selected");
                $("#processInstanceCountDateProcessList").append(el);
                $("#processInstanceCountDateProcessList").selectpicker("refresh");
                $('#processInstanceCountDateProcessList').prop( "disabled", true );
                drawDateVsProcessInstanceCountResult();
            } else {
                loadProcessList('processInstanceCountDateProcessList');
            }
            //   drawDateVsProcessInstanceCountResult();
        });
    });
    $('#collapse').collapse("hide");
}

var callbackmethod = function(event, item) {
    
}

function drawDateVsProcessInstanceCountResult() {

    var processIds = $('#processInstanceCountDateProcessList').val();
    var processIdArray = [];
    if (processIds != null) {
        $('#processInstanceCountDateProcessList :selected').each(function (i, selected) {
            processIdArray[i] = $(selected).val();
        });
    }

    var sDate = new Date();
    sDate.setMonth(sDate.getMonth() - 4);
    //sDate = (sDate.getMonth() + 1) + '/' + sDate.getDate() + '/' + sDate.getFullYear();


    var body = {
        'startTime': ($("#from").val()==0)?sDate.getTime():$("#from").val(),
        'endTime': ($("#to").val()==0)?new Date().getTime():$("#to").val(),
        'processIdList': processIdArray
    };

    $.ajax({
        url: '../../bpmn-analytics-explorer/process_instance_count_vs_date',
        type: 'POST',
        data: {'filters': JSON.stringify(body)},
        success: function (data) {
            var responseJsonArr = [];
            if(!$.isEmptyObject(data))
                responseJsonArr = JSON.parse(data);

            var responseStr = '';
            for(var i = 0; i < responseJsonArr.length; i++) {
                var temp = '["' + responseJsonArr[i].finishTime+'",'+responseJsonArr[i].processInstanceCount+'],';
                // var temp = '["' + responseJsonArr[i].processInstanceCount+'",'+responseJsonArr[i].finishTime+'],';
                responseStr+=temp;
            }
            responseStr = responseStr.slice(0,-1);
            var jsonArrObj = JSON.parse('[' + responseStr + ']');
            jsonObj[0].data = jsonArrObj;
            // console.log(jsonObj);
            config.width = $('#chartA').width();
            config.height = $('#chartA').height();

            var barChart = new vizg(jsonObj, config);
            barChart.draw("#chartA", [{type:"click", callback:callbackmethod}]);
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
    $('.collapse').collapse("hide");
}

function loadProcessList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;
    var url = "../../bpmn-analytics-explorer/process_definition_key_list";

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
                $(dropdownElementID).selectpicker("refresh");
                drawDateVsProcessInstanceCountResult();
            }
        },
        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
}

function getUrlVars() {
    var vars = [], hash;
    var hashes = top.location.href.slice(top.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}