  /*
   * Copyright (c) 2015, WSO2 Inc. (http://wso2.com) All Rights Reserved.
   *
   * Licensed under the Apache License, Version 2.0 (the "License");
   * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
   *
   * http://www.apache.org/licenses/LICENSE-2.0
   *
   * Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS,
   * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   * See the License for the specific language governing permissions and
   * limitations under the License.
   */
  $(document).ready(function() {
      var viewFlag = false;

      var processPath = $("#divTextProcessPath").text().trim(); // process text resource path
      var processName = $("#divProcessName").text().trim(); //current process name
      var bpmnPath = $("#divBpmnPath").text().trim(); //bpmn xml resource path

      if (processPath) { // if process text added
          processPath = "/_system/governance/" + processPath;
      }
      if (bpmnPath) { // if bpmn path added
          bpmnPath = "/_system/governance/" + bpmnPath;
      }

      var preAssets = {
          data: []
      }

      var sucAssets = {
          data: []
      }
      var predecessors = " ";
      var successors = " ";

      $("#btnView").hide(); //by default hide
      $("#btnCollapse").hide();
      $("#collapsedProcessName").hide();

      $("#btnView").on("click", function() {
          $(".asset-description").hide();
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
          $("#collapsedProcessName").hide();
          // $("#processNameCollapsed").hide();


      });

      // on click of "Text" tab
      $("#tab-text").on("click", function() {

          if (viewFlag) {
              $("#btnView").hide();
              $("#btnCollapse").show();
          } else {
              $("#btnCollapse").hide();
              $("#btnView").show();
          }
      });
      // on click of "Associations" tab
      $("#tab-relations").on("click", function() {
          $("#btnView").hide();
          $("#btnCollapse").hide();
      });
      // on click of "bpmn model" tab
      $("#tab-model").on("click", function() {

          $("#btnView").hide();
          $("#btnCollapse").hide();

          // get bpmn model if available
          if (bpmnPath) {
              $.ajax({
                  type: "GET",
                  url: "/store/assets/process/apis/get_bpmn_content",
                  data: {
                      bpmn_content_path: bpmnPath
                  },
                  success: function(result) {
                      var bpmnObject = JSON.parse(result);
                      $("#bpmnImage").attr("src", "data:image/png;base64," + bpmnObject.bpmnImage);

                  }
                 /* error: function(jqXHR, exception) {
                      var msg = '';
                      if (jqXHR.status === 0) {
                          msg = 'Not connect.\n Verify Network.';
                      } else if (jqXHR.status == 404) {
                          msg = 'Requested page not found. [404]';
                      } else if (jqXHR.status == 500) {
                          msg = 'Internal Server Error [500].';
                      } else if (exception === 'parsererror') {
                          msg = 'Requested JSON parse failed.';
                      } else if (exception === 'timeout') {
                          msg = 'Time out error.';
                      } else if (exception === 'abort') {
                          msg = 'Ajax request aborted.';
                      } else {
                          msg = 'Uncaught Error.\n' + jqXHR.responseText;
                      }
                      $('#tab-bpmn').html(msg);
                  }*/
              });
          } else {
              $("#tab-bpmn").html("No bpmn model available");

          }
      });


      // get associations content if available
      $.get("/store/apis/assets?type=process", function(response) {
          for (var i in response.data) {
              var item = response.data[i];
              if (processName == item.attributes.overview_name) {
                  if (item.attributes.predecessor_Id) { //If predecessors exist
                      var list = item.attributes.predecessor_Id[0];
                      if (list !== " ") { //if multiple predecessors

                          for (var j in item.attributes.predecessor_Id) {
                              preAssets.data.push({
                                  "id": item.attributes.predecessor_Id[j],
                                  "name": item.attributes.predecessor_Name[j]
                              });
                          }
                      } //multiple pres
                      else { //only single predecessor
                          preAssets.data.push({
                              "id": item.attributes.predecessor_Id,
                              "name": item.attributes.predecessor_Name
                          });
                      }
                  } // end of predecessors

                  if (item.attributes.successor_Id) { //If predecessors exist
                      var sucList = item.attributes.successor_Id[0];
                      if (sucList !== " ") { //if multiple successors

                          for (var k in item.attributes.successor_Id) {
                              sucAssets.data.push({
                                  "id": item.attributes.successor_Id[k],
                                  "name": item.attributes.successor_Name[k]
                              });
                          }
                      } else {
                          sucAssets.data.push({
                              "id": item.attributes.successor_Id,
                              "name": item.attributes.successor_Name
                          });
                      }
                  } //end of successors
              }
          }

          for (var i = 0; i < preAssets.data.length; i++) {
              var id = preAssets.data[i].id;
              id = id.trim();
              // check if predecessor is published     
              predecessors += '<li><a href = /store/assets/process/details/' + id + '>' + preAssets.data[i].name + '</a></li>';
          }
          for (var i = 0; i < sucAssets.data.length; i++) {
              var id = sucAssets.data[i].id;
              id = id.trim();
              successors += '<li><a href = /store/assets/process/details/' + id + '>' + sucAssets.data[i].name + '</a></li>';
          }
          $("#preContent").html(predecessors);
          $("#sucContent").html(successors);
      });



      // Get text process content if available
      if (processPath) {

          $.ajax({
              type: "GET",
              url: "/store/assets/process/apis/TextContentRetriever",
              data: {
                  type: "GET",
                  processPath: processPath
              },
              success: function(result) {
                  $("#tab-properties").html("<br>" + result);
                  $("#btnView").show();
                  $("#btnCollapse").hide();

              }
            /*  error: function(jqXHR, exception) {
                  var msg = '';
                  if (jqXHR.status === 0) {
                      msg = 'Not connect.\n Verify Network.';
                  } else if (jqXHR.status == 404) {
                      msg = 'Requested page not found. [404]';
                  } else if (jqXHR.status == 500) {
                      msg = 'Internal Server Error [500].';
                  } else if (exception === 'parsererror') {
                      msg = 'Requested JSON parse failed.';
                  } else if (exception === 'timeout') {
                      msg = 'Time out error.';
                  } else if (exception === 'abort') {
                      msg = 'Ajax request aborted.';
                  } else {
                      msg = 'Uncaught Error.\n' + jqXHR.responseText;
                  }
                  $('#tab-properties').html(msg);
              }*/
          });
      } else {
          $("#tab-properties").html("No text content available");
          $("#btnView").hide();
          $("#btnCollapse").hide();
      }

  });