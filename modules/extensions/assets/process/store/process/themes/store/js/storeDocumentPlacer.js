/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
function downloadDocument(relativePath) {
    $.ajax({
        url: '/explorer/assets/process/apis/download_document?process_doc_path=' + relativePath,
        type: 'GET',
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                var docNameWithExt = relativePath.substr(relativePath.lastIndexOf('/') + 1);
                var extension = docNameWithExt.split('.').pop().toLowerCase();
                var byteCharacters = atob(response.content);

                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var contentType = 'application/msword';
                if (extension == "pdf") {
                    contentType = 'application/pdf';
                }
                var byteArray = new Uint8Array(byteNumbers);
                var blob = new Blob([byteArray], {type: contentType});
                saveDoc(blob, docNameWithExt);
            } else {
                alertify.error(response.content);
            }
        },
        error: function () {
            alertify.error('Document download error.');
        }
    });
}

var saveDoc = (function () {
    var a = document.createElement("a");
    a.style = "display: none";
    return function (blob, fileName) {
        var reader  = new FileReader();
        reader.addEventListener("load", function () {
            a.href = this.result;
            a.download = fileName;
            document.body.appendChild(a);
            a.click( function () {
            });
        }, false);
        reader.readAsDataURL(blob);
    };
}());

function viewPDFDocument(relativePath, heading, iteration) {
    $.ajax({
        url: '/explorer/assets/process/apis/download_document?process_doc_path=' + relativePath,
        type: 'GET',
        success: function (data) {
            var response = JSON.parse(data);
            if (response.error === false) {
                var byteCharacters = atob(response.content);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var contentType = 'application/pdf';
                var byteArray = new Uint8Array(byteNumbers);
                var file = new Blob([byteArray], {type: contentType});
                var reader  = new FileReader();

                reader.addEventListener("load", function () {
                    viewPDF(this.result, heading, iteration);
                }, false);
                reader.readAsDataURL(file);
            } else {
                alertify.error(response.content);
            }
        },
        error: function () {
            alertify.error('PDF viewing error.');
        }
    });
}

function viewGoogleDocument(googleDocUrl, heading, iteration) {
    var googleDocDivElement = document.getElementById("googleDocViewer");
    var customGoogleDocUrl = googleDocUrl + "&embedded=true";
    var customHeading = "Document Name : " + heading;
    var modal = '<div id="docViewModal' + iteration + '" class="modal fade" role="dialog">';
    modal += '<div class="modal-dialog" style="width:840px;height:600px">';
    modal += '<div class="modal-content">';
    modal += '<div class="modal-header">';
    modal += '<a class="close" data-dismiss="modal">×</a>';
    modal += '<h4>' + customHeading + '</h4>';
    modal += '</div>';
    modal += '<div class="modal-body">';
    modal += '<iframe src="' + customGoogleDocUrl + '" style="width:800px;height:600px" frameborder="0">';
    modal += '</iframe>';
    modal += '</div>';
    modal += '<div class="modal-footer">';
    modal += '<span class="btn" data-dismiss="modal">';
    modal += 'Close';
    modal += '</span>'; // close button
    modal += '</div>';  // footer
    modal += '</div>'; //modal-content
    modal += '</div>'; //modal-header
    modal += '</div>';  // modalWindow
    googleDocDivElement.innerHTML += modal;
}

function viewPDF(pdfUrl, heading, iteration) {
    var pdfDocDivElement = document.getElementById("pdfDocViewer");
    var customHeading = "PDF Name : " + heading;
    var pdfModal = '<div id="pdfViewModal' + iteration + '" aria-labelledby="pdfModalLabel' + iteration + '" class="modal fade" role="dialog" aria-hidden="true">';
    pdfModal += '<div class="modal-dialog" style="width:840px;height:600px">';
    pdfModal += '<div class="modal-content">';
    pdfModal += '<div class="modal-header">';
    pdfModal += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
    pdfModal += '<h4 class="modal-title" id="pdfModalLabel' + iteration + '">' + customHeading + '</h4>';
    pdfModal += '</div>';
    pdfModal += '<div class="modal-body">';
    pdfModal += '<object type="application/pdf" data="' + pdfUrl + '" width="800" height="600">this is not working as expected</object>';
    pdfModal += '</div>';
    pdfModal += '<div class="modal-footer">';
    pdfModal += '<span class="btn" data-dismiss="modal">';
    pdfModal += 'Close';
    pdfModal += '</span>'; // close button
    pdfModal += '</div>'; // footer
    pdfModal += '</div>'; //modal-content
    pdfModal += '</div>'; //modal-header
    pdfModal += '</div>'; //modal window
    pdfDocDivElement.innerHTML += pdfModal;
}
