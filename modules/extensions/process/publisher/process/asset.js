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
                url: 'config_das_analytics',
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
            }, {
                url: 'get_process_variables_list',
                path: 'get_process_variables_list.jag'
            },{
                url: 'upload_flowchart',
                path: 'upload_flowchart.jag'
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

            var processName=page.assets.tables[0].fields.name.value;
            var processVersion=page.assets.tables[0].fields.version.value;
            log.info("Process Name:" + page.assets.tables[0].fields.name.value);
            log.info("Process Version:" + page.assets.tables[0].fields.version.value);

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

            importPackage(org.wso2.carbon.pc.analytics.core.utils);
            //var au=new AnalyticsUtils();
            page.DASAnalyticsEnabled = AnalyticsUtils.isDASAnalyticsActivated();
            log.info("CZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ\n");

            log.info("string list:"+conData);
            log.info("json object:"+conObject);
            importPackage(org.wso2.carbon.pc.analytics.config.utils);
            page.DASAnalyticsConfigured=DASConfigurationUtils.isDASAnalyticsConfigured(processName,processVersion);
            //log.info(page);

            var processVariablesJObArrStr;
            if(page.DASAnalyticsConfigured) {
                processVariablesJObArrStr = ps.getProcessVariablesList(resourcePath);

                var processVariablesJObArr = JSON.parse(processVariablesJObArrStr);
                page.processVariableList = processVariablesJObArr;

                log.info("arrya sring:" + processVariablesJObArrStr);
                log.info("Array:" + processVariablesJObArr);
                log.info("List:" + page.processVariableList);
                log.info("It is:" + page.processVariableList[1]);

                for (var key in page.processVariableList) {
                    log.info("PPP");
                    if (page.processVariableList.hasOwnProperty(key)) {
                        log.info(key + " -> " + page.processVariableList["name"]);
                    }
                }
            }

            var flowchartPath = resourcePath.replace("processes", "flowchart");
            var flowchartString = ps.getFlowchart(flowchartPath);
            if(flowchartString != "NA"){
                page.flowchartAvailable = true;
                page.flowchartString = flowchartString.toString();
            }else{
                page.flowchartAvailable = false;
                page.flowchartString = null;
            }

            log.info(page);

        }
    };
};


