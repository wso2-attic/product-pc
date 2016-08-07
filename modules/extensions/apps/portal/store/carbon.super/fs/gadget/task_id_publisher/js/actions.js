var from,to;

$(document).ready(function () {
    $(function () {
        $("#taskStartDate").datepicker({}).on('focusin', function () {
            wso2.gadgets.controls.resizeGadget({
                height: '400px'
            });
        });
        $("#taskEndDate").datepicker({}).on('focusin', function () {
            wso2.gadgets.controls.resizeGadget({
                height: '400px'
            });
        });

        wso2.gadgets.controls.addLostFocusListener(function () {
            $("#startDate").datepicker('hide');
            $("#endDate").datepicker('hide');
            wso2.gadgets.controls.restoreGadget();
        });
    });

    $("#taskStartDate").datepicker({}).on('changeDate', function (e) {
        var fromdate = e.date;
        from = fromdate.getTime();
        $("#taskStartDate").datepicker('hide');
        wso2.gadgets.controls.restoreGadget();
    });

    $("#taskEndDate").datepicker({}).on('changeDate', function (e) {
        var todate = e.date;
        to = todate.getTime();
        $("#taskEndDate").datepicker('hide');
        wso2.gadgets.controls.restoreGadget();
    });
});

function loadTaskList(dropdownId) {
    var dropdownElementID = '#' + dropdownId;

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
                    $(dropdownElementID).append(el);
                }
                $(dropdownElementID).selectpicker("refresh");   
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


function publish() {
    var task_id = $('#taskIdList').val();
    if(!from) {
        from = 0;
    }
    if(!to) {
        to = 0;
    }
    console.log(from+" "+to);
    gadgets.Hub.publish('task_id', {
        task_id: task_id,
        from: from,
        to: to
    });
}

function reset() {
    $("#taskStartDate").val("");
    $("#taskEndDate").val("");
}