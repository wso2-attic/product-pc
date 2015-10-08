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

    var url = "/publisher/assets/process/apis/processes?type=process";
    var processName = $("#processName").val();
    var processId;
    var preAssets = {
        data: []
    }
    var sucAssets = {
        data: []
    }

    var genAssets = {
        data: []
    }
    var specAssets = {
        data: []
    }

    FindProcessProperties();

    function AddPredecessorsToField() {

        for (var i = 0; i < preAssets.data.length; i++) {

            $("#properties_predecessors").tokenInput("add", {
                id: preAssets.data[i].id,
                name: preAssets.data[i].name
            });
            //return preAssets.data;
        }


    }

    function AddSuccessorsToField() {

        for (var i = 0; i < sucAssets.data.length; i++) {

            $("#properties_successors").tokenInput("add", {
                id: sucAssets.data[i].id,
                name: sucAssets.data[i].name
            });
        }
    }

    function AddGeneralizationsToField() {

        for (var i = 0; i < genAssets.data.length; i++) {

            $("#properties_generalizations").tokenInput("add", {
                id: genAssets.data[i].id,
                name: genAssets.data[i].name
            });
        }
    }

    function AddSpecializationsToField() {

        for (var i = 0; i < specAssets.data.length; i++) {

            $("#properties_specializations").tokenInput("add", {
                id: specAssets.data[i].id,
                name: specAssets.data[i].name
            });
        }
    }


    function FindProcessProperties() {
        $.ajax({
            type: "GET",
            url: "/publisher/apis/assets?type=process",
            success: function(response) {
                for (var i in response.list) {
                    var item = response.list[i];
                    if (processName == item.attributes.overview_name) {
                        processId = item.id;
                        var predecessors = item.attributes.properties_predecessors;
                        var successors = item.attributes.properties_successors;
                        var generalizations = item.attributes.properties_generalizations;
                        var specializations = item.attributes.properties_specializations;
                        if (specializations) {
                            storeSpecList(specializations);
                        }
                        if (predecessors) {
                            storePreList(predecessors);
                        }
                        if (successors) {
                            storeSucList(successors);
                        }
                        if (generalizations) {
                            storeGenList(generalizations);
                        }

                        //ADD A LOOP EXIT HERE

                    }
                }
            }
        });
    }
    // get the name and id pairs stored for each predecessor
    function storePreList(preList) {
        var preIdList = preList.split(',');
        for (i = 0; i < preIdList.length; i++) {
            var id = preIdList[i];
            callPreAjax(id);
        }

    }

    function storeSucList(sucList) {
        var sucIdList = sucList.split(',');
        for (var i = 0; i < sucIdList.length; i++) {
            var id = sucIdList[i];
            callSucAjax(id);
        }
    }

    function storeGenList(genList) {
        var genIdList = genList.split(',');
        for (var i = 0; i < genIdList.length; i++) {
            var id = genIdList[i];
            callGenAjax(id);
        }
    }

    function storeSpecList(specList) {
        var specIdList = specList.split(',');
        for (var i = 0; i < specIdList.length; i++) {
            var id = specIdList[i];
            callSpecAjax(id);
        }
    }

    function callSucAjax(id) {
        $.ajax({
            type: "GET",
            url: "/publisher/apis/assets/" + id + "?type=process",
            success: function(response) {
                var item = response;
                sucAssets.data.push({
                    "id": item.id,
                    "name": item.attributes.overview_name
                });
                AddSuccessorsToField();
            }
        });
    }

    function callGenAjax(id) {
        $.ajax({
            type: "GET",
            url: "/publisher/apis/assets/" + id + "?type=process",
            success: function(response) {
                var item = response;
                genAssets.data.push({
                    "id": item.id,
                    "name": item.attributes.overview_name
                });
                AddGeneralizationsToField();
            }
        });
    }

    function callSpecAjax(id) {
        $.ajax({
            type: "GET",
            url: "/publisher/apis/assets/" + id + "?type=process",
            success: function(response) {
                var item = response;
                specAssets.data.push({
                    "id": item.id,
                    "name": item.attributes.overview_name
                });
                AddSpecializationsToField();
            }
        });
    }

    //get the name values of each process relation 
    function callPreAjax(id) {
        $.ajax({
            type: "GET",
            url: "/publisher/apis/assets/" + id + "?type=process",
            success: function(response) {
                var item = response;
                preAssets.data.push({
                    "id": item.id,
                    "name": item.attributes.overview_name
                });
                AddPredecessorsToField();
            }
        });
    }

    //Populates the token imput with the assets for the field properties_predecessors
    $("#properties_predecessors").tokenInput(url, {
        preventDuplicates: true,
        theme: "facebook",
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
            return "<li><a href =../../../assets/process/details/" + item.id +
                ">" + item.name + " </a></li>"
        }
    });

    //Populates the token imput with the assets for the field properties_successors
    $("#properties_successors").tokenInput(url, {
        preventDuplicates: true,
        theme: "facebook",
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
            return "<li><a href =../../../assets/process/details/" + item.id +
                ">" + item.name + " </a></li>"
        }
    });

    ////Populates the token imput with the assets for the field properties_generalizations
    $("#properties_generalizations").tokenInput(url, {
        preventDuplicates: true,
        theme: "facebook",
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
            return "<li><a href =../../../assets/process/details/" + item.id +
                ">" + item.name + " </a></li>"
        }
    });

    //Populates the token imput with the assets for the field properties_specializations
    $("#properties_specializations").tokenInput(url, {
        preventDuplicates: true,
        theme: "facebook",
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
            return "<li><a href =../../../assets/process/details/" + item.id +
                ">" + item.name + " </a></li>"
        }
    });



});