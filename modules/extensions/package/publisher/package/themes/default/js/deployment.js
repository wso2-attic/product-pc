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

$(document).ready(function() {

    $('#btn-deploy-con').on('click', function(e) {
        var path = caramel.url('/assets/package/apis/deployments/');
        $.ajax({
            url: path,
            type: 'POST',
            data: {
                'packageName': $('#packageName').val(),
                'packageVersion': $('#packageVersion').val()
            },
            success: function (data) {
            	messages.alertSuccess(data);
                if (data.error === false) {
                   location.reload();
                } else {
                    messages.alertError(data.content);
                }
            },
            error: function () {
                messages.alertError('Error occurred while deploying package');
            }
        });
    });

    $('#btn-undeploy-con').on('click', function(e) {
        var path = caramel.url('/assets/package/apis/deployments');
        $.ajax({
            url: path + "?packageName=" + $('#packageName').val() 
            + "&packageVersion=" + $('#packageVersion').val(),
            type: 'DELETE',
            success: function (data) {
                if (data.error === false) {
                    messages.alertSuccess("Undeployment Successful");
                   location.reload();
                } else {
                    messages.alertError(data.message);
                }
            },
            error: function () {
                messages.alertError('Error occurred while deploying package');
            }
        });
    });

    $('#btn-associate_deployment').on('click', function(e) {
        var path = caramel.url('/assets/package/apis/associations/deployment');
        $.ajax({
            url: path,
            type: 'POST',
            data: {
                'packageName': $('#packageName').val(),
                'packageVersion': $('#packageVersion').val()
            },
            success: function (data) {
                messages.alertSuccess(data);
                if (data.error === false) {
                   location.reload();
                } else {
                    messages.alertError(data.content);
                }
            },
            error: function () {
                messages.alertError('Error occurred while deploying package');
            }
        });
    });

    $('#btn-cancel-con').on('click', function(e) {
        var assetId = $('#asset-id').val();
        var assetType = $('#asset-type').val();
        var path = caramel.url('/assets/'+assetType + '/details/' + assetId);

        $.ajax({
            success : function(response) {
                window.location = path;
            }
        });
    });
});