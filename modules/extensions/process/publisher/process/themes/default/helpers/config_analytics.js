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
var resources = function (page, meta) {
    return {
        js: ['config-analytics-functions.js','flowchart/jsPlumb-2.0.7.js', 'libs/jquery.form.min.js', 'publisher-utils.js', 'jquery-ui.js','jquery-file-upload/alertify.js', 'jquery-file-upload/bootstrap-filestyle.js', 'jquery.cookie.js', 'common/option_text.js',
                                                        "tinymce/tinymce.min.js", 'tags/tags-init-create-asset.js', 'notify.min.js', 'messages.js',
                                                        'jquery-file-upload/jquery.iframe-transport.js', 'jquery-file-upload/jquery.fileupload.js',
                                                        'jquery-file-upload/vendor/jquery.ui.widget.js', 'create-asset-functions.js', 'flowchart/create-flowchart.js',
                                                        'advance-search.js', 'jquery.form.js', 'flowchart/base64.js','flowchart/canvas2image.js', 'flowchart/canvg.js',
                                                        'flowchart/html2canvas.js', 'auto-complete-tags/bootstrap-tokenfield.min.js','audit-log-functions.js','create_asset.js'],
        css: ['flowchart/jsPlumb-defaults.css', 'flowchart/jsPlumb-demo.css', 'bootstrap-select.min.css',
                          'alertify.css', 'default.css', 'datepick/smoothness.datepick.css', 'grid.css',
                          'select2.min.css', 'jquery-ui.css', 'flowchart/flowchart.css', 'font-awesome.css', 'search.css', 'build.css',
                          'bootstrap-editable.css','add-process.css', 'custom.css','bootstrap-select.min.css', 'alertify.css']
    };
};