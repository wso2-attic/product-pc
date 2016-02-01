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

if (BPSTenant != undefined && BPSTenant.length > 0) {
    CONTEXT = "t/" + BPSTenant + "/jaggeryapps/" + appName;
} else {
    CONTEXT = appName;
}

function viewProcess(renderElement, processName) {
    if (processName != null) {
        var body = {
            'name': processName
        };
        var url = "/" + CONTEXT + "/search_process_by_name";
        $.ajax({
            type: 'POST',
            url: httpUrl + url,
            data: {'filters': JSON.stringify(body)},
            success: function (data) {
                var dataStr = JSON.parse(data);
                if (!$.isEmptyObject(dataStr)) {
                    var dataset = [];
                    for (var i = 0; i < dataStr.length; i++) {
                        dataset.push({
                            "processname": dataStr[i].processname,
                            "processversion": dataStr[i].processversion,
                            "path": dataStr[i].path,
                            "processid": dataStr[i].processid,
                            "bpmnpath": dataStr[i].bpmnpath,
                            "bpmnid": dataStr[i].bpmnid,
                            "processtextpath": dataStr[i].processtextpath
                        });
                    }
                    render(renderElement, dataset);
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
    }
}

function advanceSearchViewProcess(renderElement, elementName, elementValue){
    if (elementName != null && elementValue != null) {
        var body = {
            'attribute': elementName,
            'value':elementValue
        };
        var url = "/" + CONTEXT + "/advance_search";
        $.ajax({
            type: 'POST',
            url: httpUrl + url,
            data: {'filters': JSON.stringify(body)},
            success: function (data) {
                var dataStr = JSON.parse(data);
                if (!$.isEmptyObject(dataStr)) {
                    var dataset = [];
                    for (var i = 0; i < dataStr.length; i++) {
                        dataset.push({
                            "processname": dataStr[i].processname,
                            "processversion": dataStr[i].processversion,
                            "path": dataStr[i].path,
                            "processid": dataStr[i].processid,
                            "bpmnpath": dataStr[i].bpmnpath,
                            "bpmnid": dataStr[i].bpmnid,
                            "processtextpath": dataStr[i].processtextpath
                        });
                    }
                    render(renderElement, dataset);
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
    }
}

$('#advanced-search-btn').click(function () {
    $(this).next().toggle();
});

$('#search').click(function () {
    if ($('#advanced-search').is(':visible'))
        $('#advanced-search').toggle();
});
//
$('#search-button').click(function() {
    if (!jQuery.isEmptyObject(document.getElementById("search").value)) {
        var url = "processes?q=" + "\"name\":\"" + document.getElementById("search").value + "\"";
        window.location = url;
    }
});

$('#search').keydown(function(e){
    if(e.which == 13){
        if (!jQuery.isEmptyObject(document.getElementById("search").value)) {
            var url = "processes?q=" + "\"name\":\"" + document.getElementById("search").value + "\"";
            window.location = url;
        }
    }
});

$('input[name=overview_owner]').keydown(function(e){
    if(e.which == 13){
        if (!jQuery.isEmptyObject(document.getElementsByName("overview_owner")[0].value)) {
            var url = "processes?q=" + "\"overview_owner\":\"" + document.getElementsByName("overview_owner")[0].value + "\"";
            window.location = url;
        }
    }
});

$('input[name=overview_name]').keydown(function(e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(document.getElementsByName("overview_name")[0].value)) {
            var url = "processes?q=" + "\"overview_name\":\"" + document.getElementsByName("overview_name")[0].value + "\"";
            window.location = url;
        }
    }
});

$('input[name=overview_version]').keydown(function(e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(document.getElementsByName("overview_version")[0].value)) {
            var url = "processes?q=" + "\"overview_version\":\"" + document.getElementsByName("overview_version")[0].value + "\"";
            window.location = url;
        }
    }
});

$('input[name=overview_createdtime]').keydown(function(e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(document.getElementsByName("overview_createdtime")[0].value)) {
            var url = "processes?q=" + "\"overview_createdtime\":\"" + document.getElementsByName("overview_createdtime")[0].value + "\"";
            window.location = url;
        }
    }
});

$('input[name=properties_bpmnpath]').keydown(function(e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(document.getElementsByName("properties_bpmnpath")[0].value)) {
            var url = "processes?q=" + "\"properties_bpmnpath\":\"" + document.getElementsByName("properties_bpmnpath")[0].value + "\"";
            window.location = url;
        }
    }
});

$('input[name=properties_bpmnid]').keydown(function(e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(document.getElementsByName("properties_bpmnid")[0].value)) {
            var url = "processes?q=" + "\"properties_bpmnid\":\"" + document.getElementsByName("properties_bpmnid")[0].value + "\"";
            window.location = url;
        }
    }
});

$('input[name=properties_processtextpath]').keydown(function(e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(document.getElementsByName("properties_processtextpath")[0].value)) {
            var url = "processes?q=" + "\"properties_processtextpath\":\"" + document.getElementsByName("properties_processtextpath")[0].value + "\"";
            window.location = url;
        }
    }
});

$('#search-button2').click(function () {
    var data = [];
    data[0] = document.getElementsByName("overview_owner")[0].value;
    data[1] = document.getElementsByName("overview_name")[0].value;
    data[2] = document.getElementsByName("overview_version")[0].value;
    data[3] = document.getElementsByName("overview_createdtime")[0].value;
    data[4] = document.getElementsByName("properties_bpmnpath")[0].value;
    data[5] = document.getElementsByName("properties_bpmnid")[0].value;
    data[6] = document.getElementsByName("properties_processtextpath")[0].value;
    var nullCount = 0;
    for(var i=0; i<data.length; i++){
        if(data[i] == "")
            nullCount++;
    }

    if(nullCount > 0){
        advanceSearch(data);
    }
});

function advanceSearch(data){
    var body = {
        'owner':data[0],
        'name':data[1],
        'version':data[2],
        'createdtime':data[3],
        'bpmnpath':data[4],
        'bpmnid':data[5],
        'processtextpath':data[6]
    };

    var url = "/" + CONTEXT + "/advance_search";
    $.ajax({
        type: 'POST',
        url: httpUrl + url,
        data: {'filters': JSON.stringify(body)},
        success: function (data) {
            var dataStr = JSON.parse(data);
            if (!$.isEmptyObject(dataStr)) {
                var dataset = [];
                for (var i = 0; i < dataStr.length; i++) {
                    dataset.push({
                        "processname": dataStr[i].processname,
                        "processversion": dataStr[i].processversion,
                        "path": dataStr[i].path,
                        "processid": dataStr[i].processid,
                        "bpmnpath": dataStr[i].bpmnpath,
                        "bpmnid": dataStr[i].bpmnid,
                        "processtextpath": dataStr[i].processtextpath
                    });
                }
                render(renderElement, dataset);
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
}

function render(renderElementID, dataset) {
    if (dataset.length > 0) {
        for (var i = 0; i < dataset.length; i++) {
            var element = "";
            element += "<div class=\"ctrl-wr-asset\" id=\"process" + i + "\">";
            element += "    <div class=\"itm-ast\">";
            element += "        <a class=\"ast-img \" href=\"\">";
            element += "            <img alt='thumbnail' src='\images\/default-thumbnail.png' class=\"img-responsive\" >";
            element += "        <\/a>";
            element += "        <div class=\"ast-content\">";
            element += "            <div class=\"ast-title padding\">";
            element += "                <a class=\"ast-name truncate processImg\" href=\"\" title=\"" + dataset[i].processname + "\">" + dataset[i].processname + "<\/a>";
            element += "                <span class=\"ast-auth\" title=\"\"><\/span>";
            element += "                <span class=\"ast-ver\">" + dataset[i].processversion + "<\/span>";
            element += "            <\/div>";
            element += "            <div class=\"pull-left asset-rating-container\">";
            element += "                <span class=\"asset-rating\">";
            element += "                    <div style=\"width:0px\"><\/div>";
            element += "                <\/span>";
            element += "            <\/div>";
            element += "            <div class=\"pull-right\">";
            element += "                <div class=\"dropdown dropdown-asset-custom\">";
            element += "                    <a aria-expanded=\"false\" role=\"button\" data-toggle=\"dropdown\" class=\"dropdown-toggle more-list\" href=\"javascript:void(0)\"><i class=\"fw fw-dots fw-rotate-90\"><\/i>";
            element += "                    <\/a>";
            element += "                    <ul role=\"menu\" class=\"dropdown-menu\">";
            element += "                        <li>";
            element += "                            <a class=\"btn js_bookmark left\"";
            element += "                            href='#'";
            element += "                            data-aid=\"6014d43a-e165-43d6-a221-84a88551f7b4\" data-type=\"process\">";
            element += "                                <span id=\"main-bookmark\">";
            element += "                                Bookmark";
            element += "                                    <span class=\"sub-bookmark\"><\/span>";
            element += "                                <\/span>";
            element += "                            <\/a>";
            element += "                        <\/li>";
            element += "";
            element += "                            <!--<li><a href=\"#\">Download<\/a><\/li>-->";
            element += "                        <li><a href=\"\">More Details<\/a><\/li>";
            element += "                    <\/ul>";
            element += "                <\/div>";
            element += "            <\/div>";
            element += "            <div class=\"clearfix\"><\/div>";
            element += "        <\/div>";
            element += "    <\/div>";
            element += "<\/div>";
            element += "";

            $(renderElementID).append(element);
        }
        $('#search').val("");
    }else{
        var element="";
        element += "<div class=\"assets-container margin-top-double\">";
        element += "   <div class=\"top-assets-empty-assert\">We couldn&#x27;t find anything for you.<\/div>";
        element += "<\/div>";
        $('#search-null').append(element);
    }
}