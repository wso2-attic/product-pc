/*
 * Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

asset.server = function(ctx) {
    var type = ctx.type;
    return {
        onUserLoggedIn: function () {
        },
        endpoints: {
            apis: [{
                url: 'assets',
                path: 'assets.jag'
            }, {
                url: 'create_process',
                path: 'create_process.jag'
            }, {
                url: 'upload_bpmn',
                path: 'upload_bpmn.jag'
            }, {
                url: 'get_process_text',
                path: 'get_process_text.jag'
            }, {
                url: 'get_bpmn_content',
                path: 'get_bpmn_content.jag'
            }, {
                url: 'save_process_text',
                path: 'save_process_text.jag'
            }, {
                url: 'asset',
                path: 'asset.jag'
            }, {
                url: 'statistics',
                path: 'statistics.jag'
            }, {
                url: 'get_process_list',
                path: 'get_process_list.jag'
            }, {
                url: 'update_subprocess',
                path: 'update_subprocess.jag'
            }, {
                url: 'update_successor',
                path: 'update_successor.jag'
            }, {
                url: 'update_predecessor',
                path: 'update_predecessor.jag'
            }, {
                url: 'delete_subprocess',
                path: 'delete_subprocess.jag'
            }, {
                url: 'delete_successor',
                path: 'delete_successor.jag'
            }, {
                url: 'delete_Predecessor',
                path: 'delete_Predecessor.jag'
            }, {
                url: 'update_owner',
                path: 'update_owner.jag'
            }, {
                url: 'upload_documents',
                path: 'upload_documents.jag'
            }, {
                url: 'config_das_analtics',
                path: 'config_das_analytics.jag'
            }, {
                url: 'get_process_tags',
                path: 'get_process_tags.jag'
            }, {
                url: 'upload_pdf',
                path: 'upload_pdf.jag'
            },{
                url: 'get_process_pdf',
                path: 'get_process_pdf.jag'
            }, {
                url: 'save_process_variables',
                path: 'save_process_variables.jag'
            }]
        }
    }
};

asset.renderer = function(ctx) {

    return {
        details: function(page) {
            var log = new Log();

            var resourcePath = page.assets.path;
            if(log.isDebugEnabled()){
                log.debug(resourcePath);
            }

            if (page.assets.tables[1].fields.processtextpath.value == "NA") {
                page.processTextAvaliable = false;
            } else {
                page.processTextAvaliable = true;
            }

            if (page.assets.tables[1].fields.bpmnid.value == "NA") {
                page.bpmnAvaliable = false;
            } else {
                page.bpmnAvaliable = true;
            }

            importPackage(org.wso2.carbon.pc.core);
            var ps = new ProcessStore();
            var conData = ps.getSucessorPredecessorSubprocessList(resourcePath);
            var conObject = JSON.parse(conData);
            if(log.isDebugEnabled()){
                log.debug(conObject);
            }
            page.involveProcessList = conObject;
            if(log.isDebugEnabled()){
                log.debug(page);
            }
        }
    };
};


