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

var resources = function (page, meta) {
    return {
        js:['jsPlumb-2.0.7.js','libs/jquery.form.min.js','publisher-utils.js','create_asset.js','resize/jquery-1.9.1.min.js','jquery-ui.js',
            'jquery-file-upload/alertify.js','jquery-file-upload/bootstrap-filestyle.js','jquery.cookie.js','common/option_text.js',
            "tinymce/tinymce.min.js",'tags/tags-init-create-asset.js','notify.min.js','messages.js','angular.js','angular-route.js',
            'jquery-file-upload/jquery.iframe-transport.js','jquery-file-upload/jquery.fileupload.js',
            'jquery-file-upload/vendor/jquery.ui.widget.js',
            'lib/jsBezier-0.7.js','lib/biltong-0.2.js','lib/katavorio-0.13.0.js','src/util.js','src/browser-util.js',
            'src/jsPlumb.js','src/dom-adapter.js','src/jsPlumb.js','src/dom-adapter.js','src/overlay-component.js','src/endpoint.js',
            'src/connection.js','src/anchors.js','src/defaults.js','src/connectors-bezier.js','src/connectors-statemachine.js',
            'src/connectors-flowchart.js','src/renderers-svg.js','src/base-library-adapter.js','src/dom.jsPlumb.js','demo.js','demo-list.js'],
        css:['font-awesome.css','bootstrap.min.css','bootstrap-select.min.css','alertify.css','default.css',
            'datepick/smoothness.datepick.css','select2.min.css','jquery-ui.css','jsPlumbToolkit-defaults.css','main.css',
            'jsPlumbToolkit-demo.css','demo.css'],
        code: ['publisher.assets.hbs']
    };
};