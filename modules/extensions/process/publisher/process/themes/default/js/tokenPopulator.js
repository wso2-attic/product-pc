/*
* Copyright (c) 2014, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
  var url = "/publisher/asts/process/apis/processes?type=process";

  //Populates the token imput with the assets for the field properties_predecessors
  $("#properties_predecessors").tokenInput(url, {
    preventDuplicates: true, theme:"facebook",
       onResult: function(results) {
         var assets = {
           data: []
         }
         $.each(results, function() {
           for (var i in results) {
             var item = results[i];
              assets.data.push({
               "path": item.path,
               "id": item.id,
               "name": item.attributes.overview_name
             });
           };
         });
         return assets.data;
         console.log('' + JSON.stringify(arguments));
       },
       tokenFormatter: function(item) {
         return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id +
                                                                    ">" + item.name + " </a></li>"
       }
  });

  //Populates the token imput with the assets for the field properties_successors
  $("#properties_successors").tokenInput(url, {
    preventDuplicates: true,theme:"facebook",
    onResult: function(results) {
      var assets = {
        data: []
      }
      $.each(results, function() {
        for (var i in results) {
          var item = results[i];
          assets.data.push({
            "path": item.path,
            "id": item.id,
            "name": item.attributes.overview_name
          });
        };
      });
      return assets.data;
     console.log('' + JSON.stringify(arguments));
    },
    tokenFormatter: function(item) {
      return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id +
                                                                    ">" + item.name + " </a></li>"
    }
  });

  ////Populates the token imput with the assets for the field properties_generalizations
  $("#properties_generalizations").tokenInput(url, {
    preventDuplicates: true,theme:"facebook",
    onResult: function(results) {
      var assets = {
        data: []
      }
      $.each(results, function() {
        for (var i in results) {
          var item = results[i];
          assets.data.push({
            "path": item.path,
            "id": item.id,
            "name": item.attributes.overview_name
          });
        };
      });
      return assets.data;
      console.log('' + JSON.stringify(arguments));
    },
    tokenFormatter: function(item) {
      return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id +
                                                                    ">" + item.name + " </a></li>"
    }
  });

  //Populates the token imput with the assets for the field properties_specializations
  $("#properties_specializations").tokenInput(url, {
    preventDuplicates: true,theme:"facebook",
    onResult: function(results) {
      var assets = {
        data: []
      }
      $.each(results, function() {
       for (var i in results) {
          var item = results[i];
          assets.data.push({
            "path": item.path,
            "id": item.id,
            "name": item.attributes.overview_name
          });
        };
      });
      return assets.data;
      console.log('' + JSON.stringify(arguments));
    },
    tokenFormatter: function(item) {
      return "<li><a href = https://localhost:9443/publisher/asts/process/details/" + item.id +
                                                                    ">" + item.name + " </a></li>"
    }
  });
);