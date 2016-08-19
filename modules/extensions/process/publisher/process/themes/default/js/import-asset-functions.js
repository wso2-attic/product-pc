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
 *  KIND, either express or implied.w   See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

var processNames = [];
var processListObj;
var tagList = [];
var allProcessTags = [];
var pname, pversion, PID, textContent;

function importProcess(currentElement) {
alertify.error('Process import error');
    if ($("#pName").val() == "" || $("#pVersion").val() == "" || $("#pOwner").val() == "") {
        alertify.error('please fill the required fields.');
   } else {
        // import the process
        pname = $("#pName").val();
        pversion = $("#pVersion").val();
        alertify.error('please fill the required fields.' + pname);
//        if ($(currentElement).attr('id') == 'saveProcessBtn' || $(currentElement).attr('id') == 'detailsProcessBtn') {
//            var imageElement = $("#images_thumbnail");
//            if (imageElement.val().length != 0) {
//                var ext = imageElement.val().split('.').pop().toLowerCase();
//                if ($.inArray(ext, ['png', 'jpeg', 'jpg', 'gif', 'ico']) == -1) {
//                    alertify.error('invalid image extension!');
//                    return;
//                }
//            }
//        }
//        $.ajax({
//            url: 'apis/create_process',
//            type: 'POST',
//            data: {'processInfo': getProcessInfo()},
//            success: function (data) {
//                var response = JSON.parse(data);
//                if (response.error === false) {
//                    $("#processTextOverviewLink").attr("href", "../../assets/process/details/" + response.content);
//                    $("#bpmnOverviewLink").attr("href", "../../assets/process/details/" + response.content);
//                    $("#pdfOverviewLink").attr("href", "../../assets/process/details/" + response.content);
//                    $("#docOverviewLink").attr("href", "../../assets/process/details/" + response.content);
//                    PID = response.content;
//
//                    if ($(currentElement).attr('id') == 'saveProcessBtn') {
//                        window.location = "../../assets/process/details/" + response.content;
//                    }
//                    else if ($(currentElement).attr('id') == 'detailsProcessBtn') {
//                        $('#stp1').removeClass("current");
//                        $('#stp1').addClass("other");
//                        $('#stp2').removeClass("other");
//                        $('#stp2').addClass("current");
//                        loadDetails();
//                    }
//                } else {
//                    alertify.error(response.content);
//                }
//            },
//            error: function () {
//                alertify.error('Process saving error');
//            }
//        });

    }
}
