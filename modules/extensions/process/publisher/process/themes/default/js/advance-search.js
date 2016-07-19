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
var options = [];

var processInputField = function (field) {
    var result = field;
    switch (field.type) {
        case 'text':
            result = field;
            break;
        default:
            break;
    }
    return result;
};
var getInputFields = function () {
    var obj = {};
    var fields = $(SEARCH_FORM).find(':input:not(#content)');
    var field;
    for (var index = 0; index < fields.length; index++) {
        field = fields[index];
        field = processInputField(field);
        if ((field.name) && (field.value)) {
            obj[field.name.substr(8)] = field.value;
        }
    }
    return obj;
};
var createQueryString = function (key, value) {
    return '"' + key + '":"' + value + '"';
};
var buildQuery = function () {
    var fields = getInputFields();
    var queryString = [];
    var value;
    for (var key in fields) {
        value = fields[key];
        queryString.push(createQueryString(key, value));
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
            if (response != null) {
                results = response.list;
            }
            if (results.length > 0) {
                if ($("#content").val()) {
                    contentSearch(results);//search using fields in rxt and content
                } else {  //no content search. only using the fields in rxt
                    renderCheckboxes(results);
                }
                //reset the form
                document.getElementById("process-search-form").reset();
                //to prevent the response from redirecting
                $(SEARCH_FORM).ajaxForm();
            } else {    //no search from rxt fields. only the content search.
                if ($('#content').val()) {
                    contentSearch(results);
                    //reset the form
                    document.getElementById("process-search-form").reset();
                    //to prevent the response from redirecting
                    $(SEARCH_FORM).ajaxForm();
                } else {  //no filter to search
                    $("#process-search-results").html("");
                    $("#process-search-results").append("<p>We are sorry but we could not find any matching assets</p>");
                }
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
    if (filters == "" && !$("#content").val()) {
        alertify.error("You have not entered anything");
    } else {
        searchProcesses();
    }
});

function renderCheckboxes(results) {
    $("#process-search-results").html("");
    if (results != null) {
        for (var i = 0; i < results.length; i++) {
            var id = "checkbox" + (i + 1);
            var checkbox = "";
            checkbox += "<div class=\"checkbox checkbox-primary\">";
            checkbox += "    <input id=\"" + id + "\" class=\"styled\" type=\"checkbox\">";
            checkbox += "    <label for=\"" + id + "\">" + results[i].name + "-" + results[i].version + "<\/label>";
            checkbox += "<\/div>";
            $("#process-search-results").append(checkbox);
        }
    }
}

$('#okButton').click(function () {
    var $elements = $("#process-search-results").find(":input:checked");
    if ($elements.length > 0) {
        for (var j = 0; j < $elements.length; j++) {
            var table = $('#table_' + tableName);
            var referenceRow = $('#table_reference_' + tableName);
            var newRow = referenceRow.clone().removeAttr('id');
            if (!isAlreadyExist($("label[for='" + $elements[j].id + "']").text(), tableName)) {
                $('input[type="text"]', newRow).val($("label[for='" + $elements[j].id + "']").text());
                table.show().append(newRow);
                if ($(this).attr('data-name') == "view") {
                    if (tableName == "subprocess") {
                        readUpdatedSubprocess($(newRow).find("span")[0], $elements.length);
                    }
                    if (tableName == "successor") {
                        readUpdatedSuccessor($(newRow).find("span")[0], $elements.length);
                    }
                    if (tableName == "predecessor") {
                        readUpdatedPredecessor($(newRow).find("span")[0], $elements.length);
                    }
                }
            }
        }
        if ($elements.length == 0) {
            alertify.error("You have not selected any process");
        } else {
            $("#searchModal").modal("hide");
            if ($(this).attr('data-name') == "view" && $elements.length > 1)
                alertify.success('Processes were successfully added to the subprocess list.');
        }
    } else {
        alertify.error("You have not selected any process");
    }
});

$('#form-clear-btn').click(function (e) {
    e.preventDefault();
    document.getElementById("process-search-form").reset();
    $("#process-search-results").html("");
})

function setTableName(tblName) {
    tableName = tblName;
}

function contentSearch(rxt_results) {

    var content = $("#content").val().trim();
    var media = JSON.stringify(options);
    var search_url = caramel.tenantedUrl('/apis/search');
    $.ajax({
        url: search_url,
        type: 'POST',
        data: {
            'search-query': content,
            'mediatype': media
        },
        success: function (data) {

            try {
                var response = JSON.parse(data);
                if (response.error === false) {
                    var results = JSON.parse(response.content);
                    if (rxt_results != "") {                     //get the intersection of the two searches.
                        var hashmap = {};
                        var intersection = [];
                        for (var i = 0; i < rxt_results.length; i++) {
                            var pid = rxt_results[i].id;
                            hashmap[pid] = rxt_results[i];
                        }
                        for (var i = 0; i < results.length; i++) {

                            var key = results[i].id;
                            if (hashmap.hasOwnProperty(key)) {
                                intersection.push(hashmap[key]);
                            }
                        }
                        results = intersection;
                    }

                    renderCheckboxes(results);
                }
                else {
                    alertify.error(response.content);
                }
            } catch (e) {
                alertify.error("We are sorry but we could not find any matching assets");
            }
        }, error: function (xhr, status, error) {
            alertify.error(error);
        }
    });
}

$('.dropdown-menu a').on('click', function (event) {

    var $target = $(event.currentTarget),
        val = $target.attr('data-value'),
        $inp = $target.find('input'),
        idx;

    if (( idx = options.indexOf(val) ) > -1) {
        options.splice(idx, 1);
        setTimeout(function () {
            $inp.prop('checked', false)
        }, 0);
    } else {
        options.push(val);
        setTimeout(function () {
            $inp.prop('checked', true)
        }, 0);
    }

    $(event.target).blur();
    return false;
});