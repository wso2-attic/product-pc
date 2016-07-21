/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

$('#bpmnDiagramView').on('hidden.bs.modal', function () {
    $("#processDiagram").attr("src", "");                
});
$('#bpmnDiagramView').on('shown.bs.modal', function (e) {
    e.preventDefault(); 
    var bpmnResourcePath = $(e.relatedTarget).closest('tr').children('td:eq(0)').text();
    if( bpmnResourcePath == undefined){
        messages.alertError('BPMN resource path is missing');
        return;
    }    
    $.ajax({
            url: '/publisher/assets/package/apis/packages/bpmn/',
            type: 'GET',
            data: {
                'packageName': $('#packageName').val(),
                'packageVersion': $('#packageVersion').val(),
                'bpmnResourcePath': bpmnResourcePath
            },
            success: function (data) {
                if (data.error === false) {
                    $("#processDiagram").attr("src", "data:image/png;base64," + data.content);
                    $('#bpmnDiagramView').modal('show');
                } else {
                    messages.alertErrorr(data.content);
                }
            },
            error: function () {
                messages.alertError('Error occurred while getting bpmn diagram');
            }
        });
});

$('#searchModal').on('shown.bs.modal', function (e) {
    e.preventDefault(); 

    var bpmnResourcePath = $(e.relatedTarget).closest('tr').children('td:eq(0)').text();
    $("#selectedBPMNFile").val(bpmnResourcePath);
});


$("#search-result-table").delegate('button', 'click', function(e) {
    $('#searchModal').modal('toggle'); 
    $.ajax({
        url: '/publisher/assets/package/apis/packages/associate',
        type: 'POST',
        data: {
            'packageName': $('#packageName').val(),
            'packageVersion': $('#packageVersion').val(),
            'bpmnResourcePath': $("#selectedBPMNFile").val(),
            'processName': $(this).closest('tr').children('td:eq(0)').text(),
            'processVersion': $(this).closest('tr').children('td:eq(1)').text()
        },
        success: function (data) {
            if (data.error === false) {
                location.reload(); // then reload the page.(3)
            } else {
                messages.alertError(data.message);
            }
        },
        error: function () {
            messages.alertError('Error occurred while associating process to bpmn resource');
        }
    });
    document.getElementById("process-search-form").reset();
    $('#search-result-table').empty();
});