/*
 ~ Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~      http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 */
var appName = "bpmn-process-center-explorer";
var httpUrl = location.protocol + "//" + location.host;
var CONTEXT = "";

var processList;

if (BPSTenant != undefined && BPSTenant.length > 0) {
    CONTEXT = "t/" + BPSTenant + "/jaggeryapps/" + appName;
} else {
    CONTEXT = appName;
}

$("#btnView").show(); //by default hide
$("#btnCollapse").hide();
$("#collapsedProcessName").hide();



$("#btnView").on("click", function() {
    $(".asset-description").hide();
    $(".margin-bottom-double").hide();
    $("#btnView").hide();
    $("#btnCollapse").show();
    $("#collapsedProcessName").show();

    viewFlag = true;
});

$("#btnCollapse").on("click", function() {
    viewFlag = false;
    $("#btnView").show();
    $("#btnCollapse").hide();
    $(".asset-description").show();
    $(".margin-bottom-double").show();
    $("#collapsedProcessName").hide();
});

$("#tab-relations").on("click", function() {

});

$('#tab-text').on("click", function(){
    var processPath = "processText/abc/1.0.0";
    var url = "/" + CONTEXT + "/get_text_content";
    $.ajax({
        type: 'POST',
        url: httpUrl + url,
        data: {'filters': processPath},
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                alert(data);
            }else{
                var element="";
                element += "<div class=\"assets-container margin-top-double\">";
                element += "   <div class=\"top-assets-empty-assert\">We couldn&#x27;t find anything for you.<\/div>";
                element += "<\/div>";
                $('#search-null').append(element);
            }
        },

        error: function (xhr, status, error) {
            var errorJson = eval("(" + xhr.responseText + ")");
            alert(errorJson.message);
        }
    });
});