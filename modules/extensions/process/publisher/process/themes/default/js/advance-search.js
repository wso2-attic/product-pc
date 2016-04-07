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
var SEARCH_FORM = "#process-search-form";

var processInputField = function(field){
    var result = field;
    switch(field.type) {
        case 'text':
            result = field;
            break;
        default:
            break;
    }
    return result;
};
var getInputFields = function(){
    var obj = {};
    var fields = $(SEARCH_FORM).find(':input');
    var field;
    for(var index = 0; index < fields.length; index++){
        field = fields[index];
        field = processInputField(field);
        if((field.name)&&(field.value)){
            obj[field.name.substr(8)] = field.value;
        }
    }
    return obj;
};
var createQueryString = function(key,value){
    return '"'+key+'":"'+value+'"';
};
var buildQuery = function(){
    var fields = getInputFields();
    var queryString =[];
    var value;
    for(var key in fields){
        value = fields[key];
        queryString.push(createQueryString(key,value));
    }
    return queryString.join(',');
};

//Search process using the given filters
function searchProcesses() {
    var query = buildQuery();
    var url = "/publisher/apis/assets?q=" + query;
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            var results = [];
            results = response.list;
            if(results.length > 0){
                $("#process-search-results").html("");
                for (var i = 0; i < results.length; i++) {
                    var id = "checkbox" + (i + 1);
                    var checkbox = "";
                    checkbox += "<div class=\"checkbox checkbox-primary\">";
                    checkbox += "    <input id=\"" + id + "\" class=\"styled\" type=\"checkbox\">";
                    checkbox += "    <label for=\"" + id + "\">" + results[i].name + "-" + results[i].version + "<\/label>";
                    checkbox += "<\/div>";
                    $("#process-search-results").append(checkbox);
                }
                document.getElementById("process-search-form").reset();//reset the form
                $(SEARCH_FORM).ajaxForm();//to prevent the response from redirecting
            }else{
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
    var filters = buildQuery();
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

function setTableName(tblName) {
    tableName = tblName;
}