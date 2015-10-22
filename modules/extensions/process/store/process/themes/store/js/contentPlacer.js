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
      
      var mainProcessPath;

      if (processPath !== "NA") { // if process text added
          processPath = "/_system/governance/" + processPath;
      }
      if (bpmnPath !== "NA") { // if bpmn path added
          bpmnPath = "/_system/governance/" + bpmnPath;
      }

      var preAssets = {
          data: []
      }
     var subAssets = {
          data: []
      }
      var sucAssets = {
          data: []
      }
      var predecessors = " ";
      var successors = " ";
      var subprocesses = " ";

         //get main process path
      $.get("/store/apis/assets?type=process", function(response) {
          for (var i in response.data) {
              var item = response.data[i];
              if (processName == item.attributes.overview_name) {
                    mainProcessPath = item.path;
              }
            }
            
            });

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
       //get associations on click 
       $("#tab-relations").on("click", function() {
        if(mainProcessPath !== "NA"){
            $.ajax({
                  type: "GET",
                  url: "/store/assets/process/apis/getAssociations",
                  data: {
                      processPath: mainProcessPath
                  },
                  success: function(result) {
                      var associationsObj = JSON.parse(result);
                        if(associationsObj.subprocesses.length > 0){
                          for(var i = 0; i < associationsObj.subprocesses.length; i++){
                              subAssets.data.push({
                                  "id": associationsObj.subprocesses[i].id,
                                  "name":associationsObj.subprocesses[i].name
                              });
                          }
                        }

                         if(associationsObj.successors.length > 0){
                          for(var i = 0; i < associationsObj.successors.length; i++){
                              sucAssets.data.push({
                                  "id": associationsObj.successors[i].id,
                                  "name":associationsObj.successors[i].name
                              });
                          }
                        }

                        if(associationsObj.predecessors.length > 0){
                          for(var i = 0; i < associationsObj.predecessors.length; i++){
                              preAssets.data.push({
                                  "id": associationsObj.predecessors[i].id,
                                  "name":associationsObj.predecessors[i].name
                              });
                          }
                        }

              for (var i = 0; i < preAssets.data.length; i++) {
              var id = preAssets.data[i].id;
              id = id.trim();
              // check if predecessor is published     
              predecessors += '<li><a href = /store/assets/process/details/' + id + '>' + preAssets.data[i].name + '</a></li>';
          }
          for (var i = 0; i < subAssets.data.length; i++) {
              var id = subAssets.data[i].id;
              id = id.trim();
              // check if predecessor is published     
              subprocesses += '<li><a href = /store/assets/process/details/' + id + '>' + subAssets.data[i].name + '</a></li>';
          }
          for (var i = 0; i < sucAssets.data.length; i++) {
              var id = sucAssets.data[i].id;
              id = id.trim();
              successors += '<li><a href = /store/assets/process/details/' + id + '>' + sucAssets.data[i].name + '</a></li>';
          }
           $("#preContent").html(predecessors);
           $("#sucContent").html(successors);
           $("#subContent").html(subprocesses);

                  }
                
              });

        }//end of if
        else{
            $("#tab-associations").html("No associations available");
        }

       }); //end of tab click
      
      // on click of "bpmn model" tab
      $("#tab-model").on("click", function() {

          // get bpmn model if available
          if (bpmnPath !== "NA") {
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
                
              });
          } else {
              $("#tab-bpmn").html("No bpmn model available");

          }
      });


     


      // Get text process content if available
      if (processPath !== "NA") {

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
            
          });
      } else {
          $("#tab-properties").html("No text content available");
         
      }

  });