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
    redirectToURL(document.getElementsByName("overview_owner"), e);
});

$('input[name=overview_name]').keydown(function(e){
    redirectToURL(document.getElementsByName("overview_name"), e);
});

$('input[name=overview_version]').keydown(function(e){
    redirectToURL(document.getElementsByName("overview_version"), e);
});

$('input[name=overview_createdtime]').keydown(function(e){
    redirectToURL(document.getElementsByName("overview_createdtime"), e);
});

$('input[name=properties_bpmnpath]').keydown(function(e){
    redirectToURL(document.getElementsByName("properties_bpmnpath"), e);
});

$('input[name=properties_bpmnid]').keydown(function(e){
    redirectToURL(document.getElementsByName("properties_bpmnid"), e);
});

$('input[name=properties_processtextpath]').keydown(function(e){
    redirectToURL(document.getElementsByName("properties_processtextpath"), e);
});

function redirectToURL(element, e){
    if(e.which == 13) {
        if (!jQuery.isEmptyObject(element[0].value)) {
            var url = "processes";
            if (checkValueNull(element[0].getAttribute("name"))) {
                url = "processes?q=" + "\""+ element[0].getAttribute("name") +"\":\"" + element[0].value + "\"";
            }else{
                url = getUrl();
            }
            window.location = url;
        }
    }
}

$('#search-button2').click(function () {
    window.location = getUrl();
});

function invokeAdvanceSearch(){
    var expression = location.search;
    var data = [];
    var labels = ["overview_owner", "overview_name", "overview_version", "overview_createdtime", "properties_bpmnpath", "properties_bpmnid", "properties_processtextpath"];
    if(expression.indexOf("overview") > -1 || expression.indexOf("properties") > -1){
        var words = expression.split("\%22");
        for(var i = 1; i < words.length - 3; (i = i + 2)){
            data[labels.indexOf(words[i])] = words[i + 2];
            i = i + 2;
        }
        for(var i = 0; i < labels.length; i++){
            if(expression.indexOf(labels[i]) == -1){
                data[i] = "";
            }
        }
        advanceSearch(data);
    }
}

function advanceSearch(dataList){
    var body = {
        'owner':dataList[0],
        'name':dataList[1],
        'version':dataList[2],
        'createdtime':dataList[3],
        'bpmnpath':dataList[4],
        'bpmnid':dataList[5],
        'processtextpath':dataList[6]
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
                        "processname": dataStr[i].name,
                        "processversion": dataStr[i].version,
                        "processowner":dataStr[i].owner,
                        "path": dataStr[i].path,
                        "processid": dataStr[i].processid,
                        "createdtime":dataStr[i].createdtime,
                        "bpmnpath": dataStr[i].bpmnpath,
                        "bpmnid": dataStr[i].bpmnid,
                        "processtextpath": dataStr[i].processtextpath
                    });
                }
                render('#section', dataset);
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
            element += "        <a class=\"ast-img \" href=\"details?q="+ dataset[i].processid +"\">";
            element += "            <img alt='thumbnail' src='\images\/default-thumbnail.png' class=\"img-responsive\" >";
            element += "        <\/a>";
            element += "        <div class=\"ast-content\">";
            element += "            <div class=\"ast-title padding\">";
            element += "                <a class=\"ast-name truncate processImg\" href=\"details\/"+ dataset[i].processid +"\" title=\"" + dataset[i].processname + "\">" + dataset[i].processname + "<\/a>";
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
            element += "                            data-aid=\""+ dataset[i].processid +"\" data-type=\"process\">";
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

function checkValueNull(exceptElement){
    var elements = document.getElementsByClassName("search-input");
    var count = 0;
    for(var i = 0; i < elements.length; i++){
        if(elements[i].getAttribute("name") != exceptElement){
            if(elements[i].value != ""){
                count++;
            }
        }
    }

    return (count == 0);
}

function getUrl(){
    var elements = document.getElementsByClassName("search-input");
    var url = "processes";
    for(var i=0; i<elements.length; i++){
        if(elements[i].value != ""){
            if(i == 0)
                url = "processes?q=";

            url += "\"" + elements[i].getAttribute("name") + "\":\"" + elements[i].value + "\"," ;
        }
    }
    return url;
}

function getProcessByProcessID(processID){
    if (processID != null) {
        var body = {
            'processid': processID
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
                            "processtextpath": dataStr[i].processtextpath,
                            "processowner":dataStr[i].processowner
                        });
                    }

                    renderProcess('#desc-div', dataset);
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

function renderProcess(renderElementID, dataset){
    if(dataset.length > 0){
        for(var i=0; i<dataset.length; i++){
            $('#processname').text(dataset[i].processname);
            $('#processversion').text("Version " + dataset[i].processversion);
            $('#processowner').text("by " + dataset[i].processowner);
            $('#btn-add-gadget').attr('data-aid', dataset[i].processid);
            $('#divProcessName').text(dataset[i].processname);
            $('#colProcess').text(dataset[i].processname + " - " + dataset[i].processversion);
            $('#viewProcess').attr("href", "details?q=" + dataset[i].processid);
        }

    }
}

