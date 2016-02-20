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

var processNames = [];
var processListObj;
var processPath;
var processTextPath;
var bpmnPath;


/**
 * search by name function
 */
function search(renderElement, searchString) {  //render element = list-assets-content    searchString = process1

 var searchAssetString;

 if(searchString == "" || searchString == null) {
  searchAssetString = $('#inp_searchAsset').val();
  //var searchPrefix = $('#search-prefix').val();
 }
 else{
  searchAssetString = searchString;
 }
 if (searchAssetString != "") {
  // var link = '/apis/normalSearch.jag' + searchPrefix + '/?query=' + searchAssetString;
  //window.location = link;


  var renderElementID = '#' + renderElement;

//#div3
  var body = "";

  var url = "/" + CONTEXT + "/processcenterservice/getprocesses/";
  $.ajax({
   type: 'POST',
   url: httpUrl + url,
   data: {'filters': JSON.stringify(body)},
   success: function (data) {
    var dataStr = JSON.parse(data);
    if (!$.isEmptyObject(dataStr)) {
     var dataset = [];
     for (var i = 0; i < dataStr.length; i++) {

      if(dataStr[i].processname.toString() == searchAssetString){
       dataset.push({
        "name": dataStr[i].processname,
        "version": dataStr[i].processversion

       });
      }
     }
     render(renderElementID, dataset);

    }},
   error: function (xhr, status, error) {
    var errorJson = eval("(" + xhr.responseText + ")");
    alert(errorJson.message);
   }
  });
 }
}



//function listProcesses(){
// var link = '/apis/get_process_list';
// window.location = link;
//}



/**
 * Function to obtain process list from the api
 * @param renderElement is the id of the rendering section
 */
function getProcessList(renderElement) {

 var url = httpUrl+"/pc/processcenterservice/getprocesses/";


 $.ajax({
  type: 'POST',
  url:url,
  dataType: "json",
  //data: {'filters': JSON.stringify(body)},
  success: function (data) {
   processListObj = data;
   //alert(processListObj);

   if (!$.isEmptyObject(processListObj)) {
    var dataset = [];

    for (var i = 0; i < processListObj.length; i++) {
     dataset.push({
      "name": processListObj[i].processname,
      "version": processListObj[i].processversion,
      "processPath": processListObj[i].path,
      "pid": processListObj[i].processid


     });
     processNames.push(processListObj[i].processname + "-" + processListObj[i].processversion);

    }
    if(renderElement != null) {

     var renderElementID = '#' + renderElement;
     render(renderElementID, dataset);

    }

   }
  },
  error: function (xhr, status, error) {
   alert("error "+ xhr.responseText);
   var errorJson = eval("(" + xhr.responseText + ")");
   alert(errorJson.message);
  }
 });
}


//$(document).ready(function(){
// //$.get("partials/processView.jag", function(data) {
// // $("#list_assets_content").html(data);
// getProcessList("list_assets_content");
// });


/**
 * Function to render a bar chart to the given data set
 * @param renderElementID is to hold the rendering element
 * @param dataset values are used to draw the bar chart
 * @param xTitle is the title for the x-axis
 * @param yTitle is the title for the y-axis
 */
function render(renderElementID, dataset) {
 // Dimensions for the chart: height, width, and space b/t the bars



 var arrayLength = dataset.length;
 for (var i = 0; i < arrayLength; i++) {
  //log.info(dataset[i].name);
  // log.info(dataset[i].version);


  $(renderElementID).append('<div class="ctrl-wr-asset">'
      +'<div class="itm-ast">'
      +'<a class="ast-img" href="process-details?q='+dataset[i].pid.toString()+'">'
      +'<img alt="thumbnail" src="images/default-thumbnail.png" style="top:0px; left:0px; height:100%; width:100%;" '
      +'class="img-responsive">'
      +'</a>'
      +'<div class="ast-desc">'
      +'<a href="process-details?q='+dataset[i].pid.toString()+'">'
      +'<h3 class="ast-name" title="process1">'+dataset[i].name.toString()+'</h3>'
      +'</a>'
      +'<span class="ast-ver">V'+dataset[i].version.toString()+', </span><span class="ast-auth" title=""></span>'
      +'<span class="ast-published"></span>'
      +'<span class="lifecycle-state"><small><i class="icon-circle lc-state-Initial"></i> Initial</small></span>'
      +'</div>'+
      +'<br class="c-both">'+
      +'</div><br class="c-both">'+
      +'</div>'
  );
  //Do something
 }



}



$('#searchButton').click(function(){

 var search = $('#inp_searchAsset').val();

 //alert(window.location.href);
 //window.location.assign(window.location.href+="?q=\"name\":"+search+"\"");

 if(!jQuery.isEmptyObject(search)){ // true)
  window.location.assign("designer?q=\"name\":\"" + search + "\"");
 }
 else{
  window.location.assign("designer");
 }
 //var url = window.location.href;
 //
 //window.location.href = url+ "=q?'name':"+search+"'";

});

function insertParam2(key,value)
{
 key = encodeURIComponent(key); value = encodeURIComponent(value);

 var s = document.location.search;
 var kvp = key+":"+value;

 var r = new RegExp("(&|\\?)"+key+"=[^\&]*");

 s = s.replace(r,"$1"+kvp);

 if(!RegExp.$1) {s += (s.length>0 ? '&' : '?') + kvp;};

 //again, do what you will here
 document.location.search = s;
}


function advanced_search(){

 var processName = $('#name').val();
 var provider = $('#provider').val();
 var version = $('#version').val();
 var lifeCycle = $('#lcState').val();
 var tags = $('#tags').val();
}


/**
 * *************** Create process *********************************
 */

//var processListObj;

//window.onload = getProcessList;

function showTextEditor(element) {
 if($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == ""){
  alertify.error('please fill the required fields.');
 }else{
  saveProcess(element);
  completeTextDetails();
  $("#processTextDiv").show();
  $("#overviewDiv").hide();
  $("#bpmnView").hide();
  tinymce.init({
   selector: "#processContent"
  });
 }
}

function associateBPMN(element) {
 if($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == ""){
  alertify.error('please fill the required fields.');
 }else{
  $('#create-view-header').text($('#pName').val());
  saveProcess(element);
  completeBPMNDetails();
  $("#overviewDiv").hide();
  $("#processTextView").show();
  $("#bpmnView").show();
 }
}

function showMain() {
 $("#mainView").show();
 $("#bpmnView").hide();
 $("#processTextView").hide();
}

function saveProcess(currentElement) {
 if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
  alertify.error('please fill the required fields.');
 } else {
  // save the process
  var url = httpUrl+ "/pc/processcenterservice/createprocess/";
  var processInfoString = getProcessInfo();
  //var processInfoObj = JSON.parse(processInfoString); // parse a string into JSON

//  var e = Base64.encode(processInfoString); //base64-encode the JSON object
  //var d = Base64.decode(e);
  //var value = "{"++"}";
  $.ajax({
   url:  url,
   type: 'POST',
   dataType: "text",
   contentType: "application/json",
   data: { 'processInfo': processInfoString },
   success: function (response) {

    var processid = JSON.stringify(response).slice(1,-1);
    $("#processTextOverviewLink").attr("href", "/process-details?q=" + processid);
    //https://10.100.4.122:9445/carbon/$("#bpmnhttps://10.100.4.122:9445/carbon/wLink").attr("href", "/process-details" + response);

    if($(currentElement).attr('id') == 'saveProcessBtn'){
     window.location = "process-details?q=" + processid;
    }
   },
   error: function (xhr, status, error) {
    alert("error"+ xhr.responseText);
    alertify.error('Process saving error');
   }
  });

 }
}

function getProcessInfo(){
 var processDetails = {
  'processName': $("#pName").val(),
  'processVersion' : $("#pVersion").val(),
  'processOwner' : $("#pOwner").val(),
  'processTags' : $("#pTags").val(),
  'subprocess' : readSubprocessTable(),
  'successor' : readSuccessorTable(),
  'predecessor' : readPredecessorTable()
 };
 return (JSON.stringify(processDetails));
}

function saveProcessText(currentElement) {
 var textContent = tinyMCE.get('processContent').getContent();
 if (textContent == "") {
  if($(currentElement).attr('id') == 'processTxtSaveBtn'){
   alertify.error('Process content is empty.');
  }
 } else {
  // save the process


  var url = "/" + CONTEXT + "/save_process_text";
  $.ajax({
   url:  httpUrl + url,
   type: 'POST',
   data: { 'processName': $("#pName").val(), 'processVersion' : $("#pVersion").val(), 'processText' : textContent},
   success: function (response) {
    if($(currentElement).attr('id') == 'processTxtSaveBtn'){
     alertify.success("Successfully saved the process content.");
    }
   },
   error: function () {
    alertify.error('Process text saving error');
   }
  });
 }
}

function updateProcessText(currentElement) {


 var textContent = tinyMCE.get('processContent').getContent();
 var name = document.getElementById("view-header").innerHTML;
 var version = document.getElementById("process-version").innerHTML;
 if (textContent == "") {
  if($(currentElement).attr('id') == 'processTextSaveBtn'){
   alertify.error('Process content is empty.');
  }
 } else {
  // save the process

  var body = { "processName": name, "processVersion" : version, "processText" : textContent};
//alert("saving.........");
  var url = httpUrl+"/pc/processcenterservice/saveprocesstext";
  alert(url);
  $.ajax({
   url:  url,
   type: 'POST',
   contentType: 'application/json',
   data: JSON.stringify(body),
   success: function (data) {

    if($(currentElement).attr('id') == 'processTextSaveBtn'){
     alertify.success("Successfully saved the process content.");
    }
   },
   error: function (xhr, status, error) {
    alert(error);
    alertify.error('Process text saving error');
   }
  });
 }
}


function completeBPMNDetails() {
 $("#bpmnProcessName").val($("#pName").val());
 $("#bpmnProcessVersion").val($("#pVersion").val());
 return true;
}

function completeTextDetails() {
 $("#textProcessName").val($("#pName").val());
 $("#textProcessVersion").val($("#pVersion").val());
 return true;
}

function subProcessNamesAutoComplete(){

 $(".subprocess_Name").autocomplete({
  source: processNames
 });
}

function successorNameAutoComplete(){
 $(".successor_Name").autocomplete({
  source: processNames
 });
}

function predecessorNameAutoComplete(){
 $(".predecessor_Name").autocomplete({
  source: processNames
 });
}

function readSubprocessTable(){
 var subprocessInfo = [];
 $('#table_subprocess tbody tr').each(function(){
  if($(this).find('td:eq(0) input').val() == ''){
   //continue
  }else{
   var subprocessPath, subprocessId;
   for(var i = 0 ; i < processListObj.length ; i++){
    if(processListObj[i].processname == $(this).find('td:eq(0) input').val().split("-")[0] &&
        processListObj[i].processversion == $(this).find('td:eq(0) input').val().split("-")[1]){
     subprocessPath = processListObj[i].path;
     subprocessId = processListObj[i].processid;
     break;
    }
   }

   subprocessInfo.push({
    name: $(this).find('td:eq(0) input').val().split("-")[0],
    path: subprocessPath,
    id: subprocessId
   });
  }
 });
 return subprocessInfo;
}

function readSuccessorTable(){
 var successorInfo = [];
 $('#table_successor tbody tr').each(function(){
  if($(this).find('td:eq(0) input').val() == ''){
   //continue
  }else{
   var successorPath, successorId;
   for(var i = 0 ; i < processListObj.length ; i++){
    if(processListObj[i].processname == $(this).find('td:eq(0) input').val().split("-")[0] &&
        processListObj[i].processversion == $(this).find('td:eq(0) input').val().split("-")[1]){
     successorPath = processListObj[i].path;
     successorId = processListObj[i].processid;
     break;
    }
   }

   successorInfo.push({
    name: $(this).find('td:eq(0) input').val().split("-")[0],
    path: successorPath,
    id: successorId
   });
  }
 });
 return successorInfo;
}

function readPredecessorTable(){
 var predecessorInfo = [];
 $('#table_predecessor tbody tr').each(function(){
  if($(this).find('td:eq(0) input').val() == ''){
   //continue
  }else{
   var predecessorPath, predecessorId;
   for(var i = 0 ; i < processListObj.length ; i++){
    if(processListObj[i].processname == $(this).find('td:eq(0) input').val().split("-")[0] &&
        processListObj[i].processversion == $(this).find('td:eq(0) input').val().split("-")[1]){
     predecessorPath = processListObj[i].path;
     predecessorId = processListObj[i].processid;
     break;
    }
   }

   predecessorInfo.push({
    name: $(this).find('td:eq(0) input').val().split("-")[0],
    path: predecessorPath,
    id: predecessorId
   });
  }
 });
 return predecessorInfo;
}

//function getProcessList(){
// $.ajax({
//  url: '/publisher/assets/process/apis/get_process_list',
//  type: 'GET',
//  success: function (response) {
//   processListObj = JSON.parse(response);
//   for(var i = 0 ; i < processListObj.length ; i++){
//    processNames.push(processListObj[i].processname + "-" + processListObj[i].processversion);
//   }
//  },
//  error: function () {
//   alertify.error('Process List error');
//  }
// });
//}

function isInputFieldEmpty(tableName){
 var isFieldEmpty = false;
 $('#table_' + tableName + ' tbody tr').each(function(){
  if($(this).find('td:eq(0) input').val() == ''){
   isFieldEmpty = true;
  }
 });
 return isFieldEmpty;
}

function addUnboundedRow(element){
 var tableName = $(element).attr('data-name');
 var table = $('#table_'+tableName);

 if(! isInputFieldEmpty(tableName)) {
  var referenceRow = $('#table_reference_' + tableName);
  var newRow = referenceRow.clone().removeAttr('id');
  $('input[type="text"]', newRow).val('');
  table.show().append(newRow);
 }else{
  alertify.error('Please fill the empty ' + tableName + ' field.');
 }
}

function blurIt(elementid, placeholder) {
// document.getElementsByClassName("form-control").style.backgroundColor = "";

 if(document.getElementById(elementid).value != "") {
  document.getElementById(elementid).style.backgroundColor = '#E0E0E0';
 }

}



function isProcessNotAvailableInList(processName){
 for(var i = 0 ; i < processNames.length ; i++){
  if(processNames[i] == processName){
   return false;
  }
 }
 return true;
}

function readUpdatedSubprocess(currentObj){

 var subprocessInput = $(currentObj).parent().closest("tr").find("input").val();

 if(subprocessInput == ''){
  alertify.error('Subprocess field is empty.');
 }else if(isProcessNotAvailableInList(subprocessInput)){
  alertify.warning('Please select a process from the dropdown list.');
 }else if(subprocessInput == getMainProcess()){
  alertify.warning('You cannot assign the process name as its subprocess.');
 }else{
  // $(currentObj).parent().closest("tr").find("input").replaceWith("<span id='subprocess_Name' class='subprocess_Name'>"+subprocessInput+"</span>");
  $(currentObj).parent().closest("tr").hide();

  $("#subprocesstablebody").append('<tr><td valign="top" style="width: 90%;">'+
  '<span id="subprocess_Name" class="subprocess_Name ui-autocomplete-input">'+subprocessInput+'</span>'+
  '<span role="status" aria-live="polite" class="ui-helper-hidden-accessible"></span></td>'+
  '<td style="width: 5%;"></td><td style="width: 5%;"><a class="js-remove-row" onclick="deleteSubprocess(this)">'+
  '<i class="fa fa-trash"></i></a> </td></tr>');

  var subprocessPath, subprocessId;
  for(var i = 0 ; i < processListObj.length ; i++){
   if(processListObj[i].processname == subprocessInput.split("-")[0] &&
       processListObj[i].processversion == subprocessInput.split("-")[1]){
    subprocessPath = processListObj[i].path;
    subprocessId = processListObj[i].processid;
    break;
   }
  }

  var subprocessInfo = {
   name: subprocessInput.split("-")[0],
   path: subprocessPath,
   id: subprocessId
  };

  var subProcessDetails = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'subprocess' : subprocessInfo
  };

  var url = "/" + CONTEXT + "/update_subprocess";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'subprocessDetails': JSON.stringify(subProcessDetails) },
   success: function (response) {
    alertify.success('Process ' + subprocessInput + ' successfully added to the subprocess list.');
   },
   error: function () {
    alertify.error('Subprocess updating error');
   }
  });
 }
}

function readUpdatedSuccessor(currentObj){
 var successorInput = $(currentObj).parent().closest("tr").find("input").val();

 if(successorInput == ''){
  alertify.error('Successor field is empty.');
 }else if(isProcessNotAvailableInList(successorInput)){
  alertify.warning('Please select a process from the dropdown list.');
 }else if(successorInput == getMainProcess()){
  alertify.warning('You cannot assign the process name as its successor.');
 }else{
  //$(currentObj).parent().closest("tr").find("input").replaceWith("<span id='successor_Name' class='successor_Name'>"+successorInput+"</span>");
  //$(currentObj).hide();
  $(currentObj).parent().closest("tr").hide();

  $("#successortablebody").append('<tr><td valign="top" style="width: 90%;">'+
  '<span id="successor_Name" class="successor_Name ui-autocomplete-input">'+successorInput+'</span>'+
  '<span role="status" aria-live="polite" class="ui-helper-hidden-accessible"></span></td>'+
  '<td style="width: 5%;"></td><td style="width: 5%;"><a class="js-remove-row" onclick="deleteSuccessor(this)">'+
  '<i class="fa fa-trash"></i></a> </td></tr>');
  var successorPath, successorId;
  for(var i = 0 ; i < processListObj.length ; i++){
   if(processListObj[i].processname == successorInput.split("-")[0] &&
       processListObj[i].processversion == successorInput.split("-")[1]){
    successorPath = processListObj[i].path;
    successorId = processListObj[i].processid;
    break;
   }
  }

  var successorInfo = {
   name: successorInput.split("-")[0],
   path: successorPath,
   id: successorId
  };

  var successorDetails = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'successor' : successorInfo
  };

  var url = "/" + CONTEXT + "/update_successor";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'successorDetails': JSON.stringify(successorDetails) },
   success: function (response) {
    alertify.success('Process ' + successorInput + ' successfully added to the successor list.');
   },
   error: function () {
    alertify.error('Successor updating error');
   }
  });
 }
}

function readUpdatedPredecessor(currentObj){
 var predecessorInput = $(currentObj).parent().closest("tr").find("input").val();

 if(predecessorInput == ''){
  alertify.error('Predecessor field is empty.');
 }else if(isProcessNotAvailableInList(predecessorInput)){
  alertify.warning('Please select a process from the dropdown list.');
 }else if(predecessorInput == getMainProcess()){
  alertify.warning('You cannot assign the process name as its predecessor.');
 }else{
  //$(currentObj).parent().closest("tr").find("input").replaceWith("<span id='predecessor_Name' class='predecessor_Name'>"+predecessorInput+"</span>");
  //$(currentObj).hide();
  $(currentObj).parent().closest("tr").hide();

  $("#predecessortablebody").append('<tr><td valign="top" style="width: 90%;">'+
  '<span id="predecessor_Name" class="predecessor_Name ui-autocomplete-input">'+predecessorInputInput+'</span>'+
  '<span role="status" aria-live="polite" class="ui-helper-hidden-accessible"></span></td>'+
  '<td style="width: 5%;"></td><td style="width: 5%;"><a class="js-remove-row" onclick="deletePredecessor(this)">'+
  '<i class="fa fa-trash"></i></a> </td></tr>');
  var predecessorPath, predecessorId;
  for(var i = 0 ; i < processListObj.length ; i++){
   if(processListObj[i].processname == predecessorInput.split("-")[0] &&
       processListObj[i].processversion == predecessorInput.split("-")[1]){
    predecessorPath = processListObj[i].path;
    predecessorId = processListObj[i].processid;
    break;
   }
  }

  var predecessorInfo = {
   name: predecessorInput.split("-")[0],
   path: predecessorPath,
   id: predecessorId
  };

  var predecessorDetails = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'predecessor' : predecessorInfo
  };

  var url = "/" + CONTEXT + "/update_predecessor";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'predecessorDetails': JSON.stringify(predecessorDetails) },
   success: function (response) {
    alertify.success('Process ' + predecessorInput + ' successfully added to the predecessor list.');
   },
   error: function () {
    alertify.error('Predecessor updating error');
   }
  });
 }
}

function deleteSubprocess(element){
 var deleteSubInput = $(element).parent().closest("tr").find("span").text();
 if(! isProcessNotAvailableInList(deleteSubInput)){
  var deleteSubPath, deleteSubId;
  for(var i = 0 ; i < processListObj.length ; i++){
   if(processListObj[i].processname == deleteSubInput.split("-")[0] &&
       processListObj[i].processversion == deleteSubInput.split("-")[1]){
    deleteSubPath = processListObj[i].path;
    deleteSubId = processListObj[i].processid;
    break;
   }
  }

  var deleteSubInfo = {
   name: deleteSubInput.split("-")[0],
   path: deleteSubPath,
   id: deleteSubId
  };

  var deleteSubObj = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'deleteSubprocess' : deleteSubInfo
  };

  var url = "/" + CONTEXT + "/delete_subprocess";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'deleteSubprocessDetails': JSON.stringify(deleteSubObj) },
   success: function (response) {
    alertify.success('Successfully deleted ' + deleteSubInput + ' from the subprocess list.');
    $(element).parent().closest("tr").hide();
   },
   error: function () {
    alertify.error('Subprocess deleting error');
   }
  });
 }
}

function deleteSuccessor(element){
 var deleteSuccessorInput = $(element).parent().closest("tr").find("span").text();
 if(! isProcessNotAvailableInList(deleteSuccessorInput)){
  var deleteSuccessorPath, deleteSuccessorId;
  for(var i = 0 ; i < processListObj.length ; i++){
   if(processListObj[i].processname == deleteSuccessorInput.split("-")[0] &&
       processListObj[i].processversion == deleteSuccessorInput.split("-")[1]){
    deleteSuccessorPath = processListObj[i].path;
    deleteSuccessorId = processListObj[i].processid;
    break;
   }
  }

  var deleteSuccessorInfo = {
   name: deleteSuccessorInput.split("-")[0],
   path: deleteSuccessorPath,
   id: deleteSuccessorId
  };

  var deleteSuccessorObj = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'deleteSuccessor' : deleteSuccessorInfo
  };

  var url = "/" + CONTEXT + "/delete_successor";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'deleteSuccessorDetails': JSON.stringify(deleteSuccessorObj) },
   success: function (response) {
    alertify.success('Successfully deleted ' + deleteSuccessorInput + ' from the successor list.');
    $(element).parent().closest("tr").hide();

   },
   error: function () {
    alertify.error('Successor deleting error');
   }
  });
 }
}

function deletePredecessor(element){
 var deletePredecessorInput = $(element).parent().closest("tr").find("span").text();
 if(! isProcessNotAvailableInList(deletePredecessorInput)){
  var deletePredecessorPath, deletePredecessorId;
  for(var i = 0 ; i < processListObj.length ; i++){
   if(processListObj[i].processname == deletePredecessorInput.split("-")[0] &&
       processListObj[i].processversion == deletePredecessorInput.split("-")[1]){
    deletePredecessorPath = processListObj[i].path;
    deletePredecessorId = processListObj[i].processid;
    break;
   }
  }

  var deletePredecessorInfo = {
   name: deletePredecessorInput.split("-")[0],
   path: deletePredecessorPath,
   id: deletePredecessorId
  };

  var deletePredecessorObj = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'deletePredecessor' : deletePredecessorInfo
  };

  var url = "/" + CONTEXT + "/delete_predecessor";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'deletePredecessorDetails': JSON.stringify(deletePredecessorObj) },
   success: function (response) {
    alertify.success('Successfully deleted ' + deletePredecessorInput + ' from the predecessor list.');
    $(element).parent().closest("tr").hide();

   },
   error: function () {
    alertify.error('Predecessor deleting error');
   }
  });
 }
}

function updateProcessOwner(element){
 var processOwner = $(element).parent().closest("tr").find("td:eq(1)").text();
 if(processOwner == ''){
  alertify.error('Process owner field is empty.');
 }else{
  var ownerDetails = {
   'processName': $('#view-header').text(),
   'processVersion' : $('#process-version').text(),
   'processOwner' : processOwner
  };

  var url = "/" + CONTEXT + "/update_owner";

  $.ajax({
   url: httpUrl+url,
   type: 'POST',
   data: { 'ownerDetails': JSON.stringify(ownerDetails) },
   success: function (response) {
    $(element).hide();
    alertify.success('Successfully updated the Process owner name.');
   },
   error: function () {
    alertify.error('Process owner updating error');
   }
  });
 }
}

function isInputFieldEmpty(tableName){
 var isFieldEmpty = false;
 $('#table_' + tableName + ' tbody tr').each(function(){
  if($(this).find('td:eq(0) input').val() == ''){
   isFieldEmpty = true;
  }
 });
 return isFieldEmpty;
}

function addUnboundedRow(element){
 var tableName = $(element).attr('data-name');
 var table = $('#table_'+tableName);

 if(! isInputFieldEmpty(tableName)) {
  var referenceRow = $('#table_reference_' + tableName);
  var newRow = referenceRow.clone().removeAttr('id');
  $('input[type="text"]', newRow).val('');
  table.show().append(newRow);
 }else{
  alertify.error('Please fill the empty ' + tableName + ' field.');
 }
}

function getMainProcess(){
 var mainProcess = $('#view-header').text() + "-" + $('#process-version').text();
 return mainProcess;
}

function editText() {
 $("#processContent").val($("#processTextDiv").html());
 showTextEditor();
}


function showBPMNUploader() {
 $("#overviewDiv").hide();
 $("#processTextContainer").hide();
 $("#processTextEditDiv").hide();
 $("#bpmnViewDiv").hide();
 $("#bpmnEditDiv").show();
}

function showBPMN() {

 $("#overviewDiv").hide();
 $("#processTextContainer").hide();
 $("#processTextEditDiv").hide();
 $("#bpmnViewDiv").show();
 $("#bpmnEditDiv").hide();


 $.ajax({
  url: httpUrl+"/pc/processcenterservice/getbpmn/",
  type: 'POST',
  contentType:'application/json',
  dataType: 'json',
  data: {'bpmnPath': "/_system/governance/"+bpmnPath},
  success: function (data) {
   alert(JSON.stringify(data));

   var bpmnObject = data;
   $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);
  },
  error: function () {
   alertify.error('BPMN diagram showing error');
  }
 });
}

function loadBPMN(){

 var url = httpUrl+"/pc/processcenterservice/getbpmn/";
 $.ajax({
  url: url,
  type: 'POST',
  contentType:'application/json',
  dataType: 'text',
  data: {'bpmnPath': bpmnPath},
  success: function (data) {
   alert(JSON.stringify(data));
   var bpmnObject = data;
   $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);
  },
  error: function () {
   alertify.error('BPMN diagram showing error');
  }
 });
}

function uploadBPMN(){


 var url = httpUrl + "/pc/processcenterservice/uploadbpmn/";

 var bpmnName = $("#bpmnProcessName").attr("value");
 var bpmnVersion = $("#bpmnProcessVersion").attr("value");
 alert(bpmnName+ bpmnVersion);

 var processid = "NA";


//var files = request.getAllFiles();
//for (var name in files) {
 // processid = ps.createBPMN(bpmnName, bpmnVersion, files[name].getStream());
//}

}






function showOverview(e) {
 if($(e).attr('id') == 'processTextOverviewBtn'){
  saveProcessText(e);
 }
 $("#overviewDiv").show();
 $("#processTextContainer").hide();
 $("#processTextEditDiv").hide();
 $("#bpmnViewDiv").hide();
 $("#bpmnEditDiv").hide();
}

function editProcessOwner(e){
 if($(e).text() == ''){
  alertify.error('Process owner field is empty.');
 }else{
  $('#processOwnerUpdateBtn').show();
 }
}

function loadProcessById(processid){


 //getProcessList();

 // var hash = window.location.hash;  //retreives the encoded json object
 //var d = Base64.decode(hash.slice(1));
 //var processInfoObj = JSON.parse(d); // this is how you parse a string into JSON

 //alert(d);

 //var pid = hash.slice(1);
 // alert(pid);
 //var segment_str = window.location.pathname; // return segment1/segment2/segment3/segment4
 //var segment_array = segment_str.split( '/' );
 //var last_segment = segment_array.pop();
 // alert(last_segment);
 var url = httpUrl+"/pc/processcenterservice/getprocesses/";
 $.ajax({
  url: url,
  type: 'POST',
  dataType: "json",

  success: function (data) {
   processListObj = data;
   alert(JSON.stringify(processListObj));
   for(var i = 0 ; i < processListObj.length ; i++){
    processNames.push(processListObj[i].processname + "-" + processListObj[i].processversion);
    if(processListObj[i].processid == processid ){

     document.getElementById('view-header').innerHTML = processListObj[i].processname.toString();
     document.getElementById('process-version').innerHTML = processListObj[i].processversion.toString();
     // document.getElementById('process-owner').innerHTML = processListObj[i].processowner.toString();

     $("#bpmnProcessName").attr("value", processListObj[i].processname.toString());
     $("#bpmnProcessVersion").attr("value", processListObj[i].processversion.toString());
     processPath = processListObj[i].path.toString();
     $("#Version").attr("href", "process_version?Url="+processListObj[i].path.toString());
     $("#Edit").attr("href", "process_update?Url="+processListObj[i].path.toString());


     //fetching the subprocesses, successors, predecessors
     getAssociationsByProcessPath(processListObj[i].path);

     //fetching all the process details of process i and loading the process text, and bpmn
     getProcessDetails(processListObj[i].path);

    }
   }
  },
  error: function () {
   alertify.error('Process List error');
  }
 });

 //processtest = getProcessById('c401d9b7-c3a5-435d-aa57-8c3d925bb578');
}

function getAssociationsByProcessPath(resourcePath){



 var url = httpUrl+"/pc/processcenterservice/getassociations/";

 $.ajax({
  url: url,
  type: 'POST',
  dataType: "json",
  contentType: "application/json",
  //processData: false,
  data: { 'processPath': '/_system/governance'+resourcePath},
  success: function (data) {

   var processInfoObj = data;
   // if($(e).attr('id') == 'processTextSaveBtn'){
   //     alertify.success("Successfully saved the process content.");
   // }
   // $("#viewTextButton").show();
   // $("#addTextButton").hide();


//load sub process table
   for(var i=0 ; i<processInfoObj.subprocesses.length; i++){

    var name = processInfoObj.subprocesses[i].name;
//var version = processInfoObj.subprocesses[i].path.split(/[/ ]+/).pop();
    var version = processInfoObj.subprocesses[i].version;
    $("#subprocesstablebody").append('<tr><td valign="top" style="width: 90%;"><span id="subprocess_Name" class="subprocess_Name">'+name+'-'+version+'</span></td>'+
    '<td style="width: 5%;"></td><td style="width: 5%;"><a class="js-remove-row" onclick="deleteSubprocess(this)"><i class="fa fa-trash"></i></a> </td>'+
    '</tr>');

   }

//load successor table        process9-9.0
   for(var i=0 ; i<processInfoObj.successors.length; i++){

    var name = processInfoObj.successors[i].name;
    var version = processInfoObj.successors[i].path.split(/[/ ]+/).pop();
    $("#successortablebody").append('<tr><td valign="top" style="width: 90%;"><span id="successor_Name" class="successor_Name">'+name+'-'+version+'</span></td>'+
    '<td style="width: 5%;"></td>'+
    '<td style="width: 5%;"><a class="js-remove-row" onclick="deleteSuccessor(this)"><i class="fa fa-trash"></i></a> </td>'+
    '</tr>');

   }

//load predecessor table
   for(var i=0 ; i<processInfoObj.predecessors.length; i++){
    var name = processInfoObj.predecessors[i].name;
    var version = processInfoObj.predecessors[i].path.split(/[/ ]+/).pop();
    $("#predecessortablebody").append('<tr><td valign="top" style="width: 90%;"><span id="predecessor_Name" class="predecessor_Name">'+name+'-'+version+'</span></td>'+
    '<td style="width: 5%;"></td>'+
    '<td style="width: 5%;"><a class="js-remove-row" onclick="deletePredecessor(this)"><i class="fa fa-trash"></i></a> </td>'+
    '</tr>');
   }
  },
  error: function () {
   alertify.error('Process text error');
  }
 });
}

//set the selected file name to the text box value
function setfilename(val)
{
 var fileName = val.substr(val.lastIndexOf("\\")+1, val.length);
 document.getElementById("filename").value = fileName;
}

// $('#form-bar-create').submit(function(e){

// var url = "/" + CONTEXT + "/upload_bpmn";
//     e.preventDefault();
//     $.ajax({
//         url: httpUrl+url,
//         type:'post',
//         data:$('#form-bar-create').serialize(),
//         success:function(){
//             //whatever you wanna do after the form is successfully submitted
//             alert('success');
//         }
//     });
// });

function getProcessDetails(processPath){

 var url = httpUrl+"/pc/processcenterservice/processdetails/";
 $.ajax({
  url: url,
  type: 'POST',
  dataType: "json",
  contentType: "application/json",
  //processData: false,
  data: {"processPath": processPath},
  success: function (data) {

   processTextPath = data[0].processtextpath.toString();
   bpmnPath = data[0].bpmnpath.toString();

   if(data[0].processtextpath.toString() != "NA"){
//loadProcessText(data[0].processtextpath.toString());
    $("#viewTextButton").show();
    $("#addTextButton").hide();
   }
   else{
    $("#viewTextButton").hide();
    $("#addTextButton").show();
   }

   if(data[0].bpmnpath.toString() != "NA"){
    $("#bpmnviewer").show();
    $("#bpmnuploader").hide();
   }
   else{
    $("#bpmnviewer").hide();
    $("#bpmnuploader").show();
   }
//loadBPMN(data[0].bpmnpath);
   //loadProcessText(data)
//load bpmn


  },
  error: function () {
   alertify.error('Process List error');
  }
 });

}

function viewText() {
 loadProcessText();

 $("#overviewDiv").hide();
 $("#processTextContainer").show();
 $("#processTextEditDiv").hide();
 $("#bpmnViewDiv").hide();
 $("#bpmnEditDiv").hide();
}


function loadProcessText() {
 var url = httpUrl+"/pc/processcenterservice/getprocesstext/";

 //var ppath = 'apis/get_process_text?process_text_path=NA';

 $.ajax({
  url: url,
  type: 'POST',
  dataType:'text',
  contentType:'application/json',
  data: { 'textPath': processTextPath},
  //processData: false,
  success: function (data) {
   $("#processTextDiv").html(data);

  },
  error: function (xhr, status, error) {
   alert(xhr.responseText);
   alertify.error('Text editor error');
  }
 });
}




// function saveProcessText(e) {
//  var textContent = tinyMCE.get('processContent').getContent();
//  if (textContent == "") {
//   if($(e).attr('id') == 'processTextSaveBtn'){
//    alertify.error('Process content is empty.');
//   }
//  } else {
//   // save the process

//   $.ajax({
//    url: 'https://localhost:9443/publisher/assets/process/apis/save_process_text',
//    type: 'POST',
//    data: { 'processName': $("#textProcessName").val(), 'processVersion' : $("#textProcessVersion").val(), 'processText' : textContent},
//    success: function (response) {
//     if($(e).attr('id') == 'processTextSaveBtn'){
//      alertify.success("Successfully saved the process content.");
//     }
//     $("#viewTextButton").show();
//     $("#addTextButton").hide();
//    },
//    error: function () {
//     alertify.error('Process text error');
//    }
//   });
//  }
// }

function showTextEditor() {
 $("#overviewDiv").hide();
 $("#processTextContainer").hide();
 $("#processTextEditDiv").show();
 $("#bpmnViewDiv").hide();
 $("#bpmnEditDiv").hide();

 tinymce.init({
  selector: "#processContent"
 });
}



function validateForm(oForm) {


 var _validFileExtensions = [".xml", ".bpmn"];

 var arrInputs = oForm.getElementsByTagName("input");
 for (var i = 0; i < arrInputs.length; i++) {
  var oInput = arrInputs[i];
  if (oInput.type == "file") {
   var sFileName = oInput.value;
   if (sFileName.length > 0) {
    var blnValid = false;
    for (var j = 0; j < _validFileExtensions.length; j++) {
     var sCurExtension = _validFileExtensions[j];
     if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
      blnValid = true;
      break;
     }
    }

    if (!blnValid) {
     alertify.error("Sorry, " + sFileName + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
     return false;
    }
   }
   else{
    alertify.error("no files added");
    return false;
   }
  }
 }

 return true;
}

function updateVersion() {
 var url = httpUrl+"/pc/processcenterservice/updateversion/";

 //var ppath = 'apis/get_process_text?process_text_path=NA';
 var newVersion = $('#new-version').val();
 if(newVersion){
 }

 var processId = $('#asset-id').val();
 var processPath =$('#asset-path').val();
     $('#btn-create-version').addClass('disabled');
 $('#new-version-loading').removeClass('hide');
  var body = { "processPath": processPath, "updatedVersion" : newVersion};

 $.ajax({
  url: url,
  type: 'POST',
  contentType:'application/json',
  dataType: 'text',
  data: JSON.stringify(body),
  success : function(data) {
    var redirect = "process-details?q="+data;
   $('.alert-success').html('Asset version created successfully! <a href='+redirect+'> View </a>');
   $('.alert-success').removeClass('hide');
   $('#btn-create-version').removeClass('disabled');
   $('#new-version-loading').addClass('hide');
  },
  error : function(xhr, status, error) {
   $('.alert-success').text('Error while creating the version!');
   $('.alert-success').removeClass('hide');
   $('#btn-create-version').removeClass('disabled');
   $('#new-version-loading').addClass('hide');
  }
 });
 //$("#newVersionModal").modal('hide');

}

function renderProcessDetails(processPath){

 var url = httpUrl+"/pc/processcenterservice/processdetails/";
 $.ajax({
  url: url,
  type: 'POST',
  dataType: "json",
  contentType: "application/json",
  //processData: false,
  data: {"processPath": processPath},
  success: function (data) {

    alert(JSON.stringify(data));
    alert(data[0].processversion.toString());
    $(".asset-name").html(data[0].proceessname);
    $("#overview_owner").attr("value", data[0].processowner);
    $("#overview_name").attr("value", data[0].processname);
    $("#overview_version").attr("value", data[0].processversion);
    $("#overview_description").attr("value","" );
    $("#properties_bpmnpath").attr("value", data[0].bpmnpath);
    $("#properties_bpmnid").attr("value", data[0].bpmnid);
    $("#properties_processtextpath").attr("value", data[0].processtextpath);
    $("#properties_server").attr("value", data[0].server );



    



  },
  error: function () {
   alertify.error('Process List error');
  }
 });

}
