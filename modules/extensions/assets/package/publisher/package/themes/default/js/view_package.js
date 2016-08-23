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
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
$(function () {
    $('#a-download-package-zip').on('click', function(e) {
        var path = caramel.url('/assets/package/apis/packages/resources/archive/');
        $.ajax({
            url: path,
            type: 'GET',
            data: {
                'packageName': $('#packageName').val(),
                'packageVersion': $('#packageVersion').val()
            },
            success: function (data) {
                if (data.error === false) {
                var packageArchiveNameWithExt = $('#packageFileName').val();
                var extension = packageArchiveNameWithExt.split('.').pop().toLowerCase();
                var byteCharacters = atob(data.content);

                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var contentType = 'application/zip';
                var byteArray = new Uint8Array(byteNumbers);
                var blob = new Blob([byteArray], {type: contentType});
                saveAs(blob, packageArchiveNameWithExt);
                } else {
                    messages.alertErrorr(data.content);
                }
            },
            error: function () {
                messages.alertError('Error occurred while downloading package file');
            }
        });
    });    
});
