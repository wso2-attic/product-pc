function getFilters(){
    var elements = $('#process-search-form').find(":input");
    var filters = [];
    for(var i = 0; i < elements.length; i++){
        if(elements[i].value != ""){
            filters.push(elements[i].getAttribute("name").substr(8) + ":" + elements[i].value);
        }
    }
    return filters;
}

function searchProcesses(){
    var filters = [];
    filters = getFilters();
    $.ajax({
        url: '/publisher/assets/process/apis/process_advance_search',
        type: 'POST',
        data: {
            'filters': filters.toString()
        },
        success: function (response) {
            var processes = JSON.parse(response);
            if(processes.length > 0){
                $("#process-search-results").html("");
                for(var i=0; i<processes.length; i++){
                    var id = "checkbox" + (i + 1);
                    var checkbox="";
                    checkbox += "<div class=\"checkbox checkbox-primary\">";
                    checkbox += "    <input id=\"" + id + "\" class=\"styled\" type=\"checkbox\">";
                    checkbox += "    <label for=\""+ id +"\">" + processes[i].name + "-" + processes[i].version + "<\/label>";
                    checkbox += "<\/div>";
                    $("#process-search-results").append(checkbox);
                }
                document.getElementById("process-search-form").reset();
                $("#process-search-results").ajaxForm();
            }else{
                $("#process-search-results").append("<p>We are sorry but we could not find any matching assets</p>");
            }
        },
        error: function () {
            alertify.error('Process retrieving error');
        }
    });

}

$('#process-search-btn').click(function(e){
    e.preventDefault();
    var filters = getFilters();
    if(filters == ""){
        alertify.error("You have not entered anything");
    }else{
        searchProcesses();
    }
});

$('#okButton').click(function () {
    var $elements = $("#process-search-results").find(":input");
    if($elements.length > 0){
        var selected = 0;
        for(var i = 0; i < $elements.length; i++){
            if($elements[i].checked){
                selected++;
                var tableName = "subprocess";
                var table = $('#table_' + tableName);
                var referenceRow = $('#table_reference_' + tableName);
                var newRow = referenceRow.clone().removeAttr('id');
                if(!isAlreadyExist($("label[for='"+ $elements[i].id + "']").text(), tableName)) {
                    $('input[type="text"]', newRow).val($("label[for='" + $elements[i].id + "']").text());
                    table.show().append(newRow);
                }
            }
        }
        if(selected == 0){
            alertify.error("You have not selected any process");
        }else
            $("#searchModal").modal("hide");
    }else{
        alertify.error("You have not selected any process");
    }
})

$('#form-clear-btn').click(function(e){
    e.preventDefault();
    document.getElementById("process-search-form").reset();
    $("#process-search-results").html("");
})

function isInputFieldEmpty(tableName) {
    var isFieldEmpty = false;
    $('#table_' + tableName + ' tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == '') {
            isFieldEmpty = true;
        }
    });
    return isFieldEmpty;
}

function isAlreadyExist(value, tableName){
    var matched = false;
    $('#table_' + tableName + ' tbody tr').each(function () {
        if ($(this).find('td:eq(0) input').val() == value) {
            matched = true;
        }
    });
    return matched;
}