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
    gadgets.Hub.publish('task_id', {
        task_id: task_id
    });
}