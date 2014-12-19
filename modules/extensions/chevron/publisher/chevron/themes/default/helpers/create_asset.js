/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
       	js:['libs/jquery.form.min.js','create_asset.js','common/option_text.js','jquery.jsPlumb-1.6.4-min.js','dom.jsPlumb-1.6.4-min.js','jquery-ui.min.js','chevron.js','xml2json.js','xml2json.min.js','token.input/jquery.tokeninput.js'], 
       	css:['bootstrap-select.min.css','datepick/smoothness.datepick.css','chevron.css','jquery-ui.css','token.input/css/token-input-facebook.css']
    };
};