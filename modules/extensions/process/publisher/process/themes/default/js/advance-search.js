/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

var tableName = "subprocess";

//get the filter values from the search form and combine them to a JSON string
function getFilters() {
    var elements = $('#process-search-form').find(":input");
    var filters = "{";
    var count = 0;
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].value != "") {
            count++;
            if (count > 1)
                filters = filters.concat(",");
            filters = filters.concat(elements[i].getAttribute("name").substr(8) + ":" + elements[i].value);
        }
    }
    filters = filters.concat("}");
    return filters;
}

//Search process using the given filters
function searchProcesses() {
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
            if (processes.length > 0) { //if there is a result, append it as a checkbox to the search result div
                $("#process-search-results").html("");
                for (var i = 0; i < processes.length; i++) {
                    var id = "checkbox" + (i + 1);
                    var checkbox = "";
                    checkbox += "<div class=\"checkbox checkbox-primary\">";
                    checkbox += "    <input id=\"" + id + "\" class=\"styled\" type=\"checkbox\">";
                    checkbox += "    <label for=\"" + id + "\">" + processes[i].name + "-" + processes[i].version + "<\/label>";
                    checkbox += "<\/div>";
                    $("#process-search-results").append(checkbox);
                }
                document.getElementById("process-search-form").reset();//reset the form
                $("#process-search-results").ajaxForm();//to prevent the response from redirecting
            } else {
                $("#process-search-results").html("");
                $("#process-search-results").append("<p>We are sorry but we could not find any matching assets</p>");
            }
        },
        error: function () {
            alertify.error('Process retrieving error');
        }
    });
}

$('#process-search-btn').click(function (e) {
    e.preventDefault();
    var filters = getFilters();
    if (filters == "{}") {
        alertify.error("You have not entered anything");
    } else {
        searchProcesses();
    }
});

$('#okButton').click(function () {
    var $elements = $("#process-search-results").find(":input");
    if ($elements.length > 0) {
        var selected = 0;
        for (var i = 0; i < $elements.length; i++) {
            if ($elements[i].checked) {
                selected++;
                var table = $('#table_' + tableName);
                var referenceRow = $('#table_reference_' + tableName);
                var newRow = referenceRow.clone().removeAttr('id');
                if (!isAlreadyExist($("label[for='" + $elements[i].id + "']").text(), tableName)) {
                    $('input[type="text"]', newRow).val($("label[for='" + $elements[i].id + "']").text());
                    table.show().append(newRow);
                }
            }
        }
        if (selected == 0) {
            alertify.error("You have not selected any process");
        } else
            $("#searchModal").modal("hide");
    } else {
        alertify.error("You have not selected any process");
    }
})

$('#form-clear-btn').click(function (e) {
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

function setTableName(tblName) {
    tableName = tblName;
}