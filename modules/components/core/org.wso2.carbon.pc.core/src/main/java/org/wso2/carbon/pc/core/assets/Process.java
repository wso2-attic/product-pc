/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.pc.core.assets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.resources.BPMNResource;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

/**
 * Class represents Process asset
 */
public class Process {

    private static final Log log = LogFactory.getLog(Process.class);

    /**
     * Get Process Registry Path .
     * ex. /_system/governance/processes/ProcessName/
     *
     * @param prcoessName
     * @return
     */
    public static String getPackageRegistryPath(String prcoessName, String processVersion) {
        return ProcessCenterConstants.PROCESS_ASSET_ROOT + prcoessName + "/" + processVersion;
    }


    public String getBpmnResources(String processName, String processVersion, String username) throws
            ProcessCenterException {
        JSONArray bpmnResources = new JSONArray();
        String processRegistryPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                // Getting process and bpmn resource association
                Association[] processAssociations = userRegistry.getAssociations(processRegistryPath,
                        ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
                if (processAssociations != null && processAssociations.length > 0) {
                    for (Association processAssociation : processAssociations) {
                        Resource bpmnRegistryResource = userRegistry.get(processAssociation.getSourcePath());

                        JSONObject bpmnResource = new JSONObject();
                        String processID = bpmnRegistryResource.getProperty
                                (ProcessCenterConstants.PROCESS_ID);
                        if (processID != null) {
                            bpmnResource.put(ProcessCenterConstants.PROCESS_ID, processID);
                        }
                        String bpmnProcessName = bpmnRegistryResource.getProperty
                                (ProcessCenterConstants.PROCESS_NAME);
                        if (processID != null) {
                            bpmnResource.put(ProcessCenterConstants.PROCESS_ID, processID);
                        }
                        if (processName != null) {
                            bpmnResource.put(ProcessCenterConstants.PROCESS_NAME, bpmnProcessName);
                        }
                        bpmnResource.put(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME,
                                bpmnRegistryResource.getPath().replaceFirst
                                        ("/" + processAssociation.getSourcePath(), ""));
                        bpmnResources.put(bpmnResource);
                    }
                }
            }
        } catch (RegistryException | JSONException e) {
            String errMsg = "Error occurred while getting bpmn resources for process : " + processName + " version " +
                    processVersion ;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return bpmnResources.toString();
    }
}
