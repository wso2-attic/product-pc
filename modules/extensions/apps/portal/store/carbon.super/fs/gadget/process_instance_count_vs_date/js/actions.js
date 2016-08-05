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

function getDummyData() {
    //var data1 = "[{\"processInstanceCount\":1,\"finishTime\":1470249000000},{\"processInstanceCount\":2,\"finishTime\":1470194400000}]";
    var data1 = "[{\"processInstanceCount\":2,\"finishTime\":1470108000000}]";
    //var data1 = "test1";
    return data1;
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

            if ($('#processInstanceCountDateDateRangeCheckBox').is(":checked") && responseJsonArr.length > 1) {
                responseJsonArr = fillEmptyDates(responseJsonArr);
                config.charts[0].type = "line";
                // responseJsonArr = "[{'finishTime':'2016-8-3','processInstanceCount':2}, {'finishTime':'2016-8-4','processInstanceCount':1}]"

            } else {
                responseJsonArr = formatDates(responseJsonArr);
                config.charts[0].type = "bar";
            }

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

function formatDates(valueDates) {
    var filledDates = [];

    for (var i = 0; i < valueDates.length; i++) {
        var fTime = valueDates[i].finishTime;
        var processInstanceCount = valueDates[i].processInstanceCount;
        var fDate = new Date(fTime);
        var formatedDate = getFormatedDate(fDate);
        filledDates.push({finishTime:formatedDate, processInstanceCount:processInstanceCount});
    }
    return filledDates;
}

/**
 Given a map M1 : (date -> value), creates a new map M2 : (date -> value), where each missing date in M1
 between its lowest and highest dates are filled with dates with 0 values.
 */
function fillEmptyDates(valueDates) {
    var valuesMap = {};
    var minDate = Number.MAX_VALUE;
    var maxDate = 0;

    for (var i = 0; i < valueDates.length; i++) {
        var fTime = valueDates[i].finishTime;
        if (fTime < minDate) {
            minDate = fTime;
        }
        if (fTime > maxDate) {
            maxDate = fTime;
        }

        var processInstanceCount = valueDates[i].processInstanceCount;
        var fDate = new Date(fTime);
        var formatedDate = getFormatedDate(fDate);
        valuesMap[formatedDate] = processInstanceCount;
    }
    var minD = new Date(minDate);
    var maxD = new Date(maxDate);
    var dayInMillis = 1000 * 60 * 60 * 24;

    var numDays = Math.ceil((maxDate - minDate) / dayInMillis);

    var filledDates = [];
    for (var k = 0; k < numDays + 1; k++) {
        var fillDate = new Date(minDate);
        fillDate.setDate(fillDate.getDate() + k);
        var formatedDate = getFormatedDate(fillDate);
        var piCount = valuesMap[formatedDate];
        if (piCount == null) piCount = 0;

        filledDates.push({finishTime:formatedDate, processInstanceCount:piCount});
    }
    return filledDates;
}

function getFormatedDate(fDate) {
    var formatedDate = fDate.getFullYear() + "-" + (fDate.getMonth() + 1) + "-" + fDate.getDate();
    return formatedDate;
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