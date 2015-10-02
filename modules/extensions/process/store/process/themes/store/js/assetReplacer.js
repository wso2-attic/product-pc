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

    var ids_pre = "";
    var ids_suc = "";
    var ids_gen = "";
    var ids_spe = "";
    var assets = {
	data: []
    }
    var processName = $("#processName").text().trim();

	$.get( "/store/assets/process/apis/content", { name: processName, reqType:"get"} )
	 .done(function( result ) {
	result = $('<div>').html(result).text(); //decode html tags
            tinymce.init({
                selector: "#content",
                menubar: false,
                statusbar: false,
                toolbar: false,
                plugins: "noneditable",
                init_instance_callback: function(editor) {
                    editor.setContent(result);
                }
            });
	$("#content").append(result);
	});

    //Acquiring the asset ids from the fields
	$("#R1").each(function() {
		ids_pre = $(this).find("#td_pre").text();
		$(this).find("#td_pre").empty();
	});
	$("#R2").each(function() {
		ids_suc = $(this).find("#td_suc").text();
		$(this).find("#td_suc").empty();
	});
	$("#R3").each(function() {
		ids_gen = $(this).find("#td_gen").text();
		$(this).find("#td_gen").empty();
	});
	$("#R4").each(function() {
		ids_spe = $(this).find("#td_spec").text();
		$(this).find("#td_spec").empty();
	});

    //splitting and isolating the ids
	var tempPre = ids_pre.split(",");
	var tempSuc = ids_suc.split(",");
	var tempGen = ids_gen.split(",");
	var tempSpe = ids_spe.split(",");

	$.get("/store/apis/assets?type=process", function(response) {
		for (var i in response.data) {
			var item = response.data[i];
			assets.data.push({
				"id": item.id,
				"name": item.attributes.overview_name
				});
		}
		//replacing the ids with the asset name and the link
		for (var i = 0; i < tempPre.length; i++) {
			for (var j in assets.data) {
				if (tempPre[i] === assets.data[j].id) {
				   $("#R1").each(function() {
					   $(this).find("#td_pre").append('<li><a href = /publisher/assets/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');
		        	});
				}
			}
		};
		//replacing the ids with the asset name and the link
		for (var i = 0; i < tempSuc.length; i++) {
			for (var j in assets.data) {
				if (tempSuc[i] === assets.data[j].id) {
				   $("#R2").each(function() {
			           $(this).find("#td_suc").append('<li><a /publisher/assets/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');
					});
				}
			}
		};
		//replacing the ids with the asset name and the link
		for (var i = 0; i < tempGen.length; i++) {
			for (var j in assets.data) {
				if (tempGen[i] === assets.data[j].id) {
				   $("#R3").each(function() {
			           $(this).find("#td_gen").append('<li><a href = /publisher/assets/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');
					});
				}
			}
		};
		//replacing the ids with the asset name and the link
		for (var i = 0; i < tempSpe.length; i++) {
			for (var j in assets.data) {
				if (tempSpe[i] === assets.data[j].id) {
				   $("#R4").each(function() {
					   $(this).find("#td_spec").append('<li><a href = /publisher/assets/process/details/' + assets.data[j].id + '>' + assets.data[j].name + '</a></li>');
					});
				}
			}
		};
	});

    //setting the field to none if there are no assets
	if (tempPre == "") {
		$("#R1").each(function() {
		$(this).find("#td_pre").append('None');
		});
	}
	if (tempSpe == "") {
		$("#R4").each(function() {
		$(this).find("#td_spec").append('None');
		});
	}
	if (tempGen == "") {
		$("#R3").each(function() {
		$(this).find("#td_gen").append('None');
		});
	}
	if (tempSuc == "") {
		$("#R2").each(function() {
		$(this).find("#td_suc").append('None');
		});
	}
});
