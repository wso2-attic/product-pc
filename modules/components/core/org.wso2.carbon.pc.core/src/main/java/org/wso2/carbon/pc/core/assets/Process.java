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

import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.resources.BPMN;
import org.wso2.carbon.pc.core.assets.common.BPMNResource;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.PCInputStreamProvider;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Class represents Process asset
 */
public class Process {

    private static final Log log = LogFactory.getLog(Process.class);
    private BPMN bpmn = null;
    private String processName;
    private String processVersion;
    private String username;

    /**
     *
     * @param username - username
     * @throws ProcessCenterException
     * @throws JSONException
     */
    public Process(String processName, String processVersion, String username) throws ProcessCenterException, JSONException {
        this.processName = processName;
        this.processVersion = processVersion;
        this.username = username;
        Document bpmn = getBpmnResource();
        if(bpmn != null) {
            this.bpmn = new BPMN(bpmn);
        }
    }

    /**
     * Get Process Registry Path .
     * ex. /_system/governance/processes/ProcessName/

     * @return process registry path
     */
    public String getProcessRegistryPath() {
        return ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
    }


    public Document getBpmnResource() throws
            ProcessCenterException {
        Document BPMNDocument = null;
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        String processRegistryPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                // Getting process and bpmn resource association
                Association[] processAssociations = userRegistry.getAssociations(processRegistryPath,
                        ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
                if (processAssociations != null && processAssociations.length > 0) {
                    Resource bpmnRegistryResource = userRegistry.get(processAssociations[0].getSourcePath());
                    byte[] bpmnContent = (byte[]) bpmnRegistryResource.getContent();
                    InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);
                    factory = DocumentBuilderFactory.newInstance();
                    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    builder = factory.newDocumentBuilder();
                    BPMNDocument = builder.parse(new InputSource(inputStreamProvider.getInputStream()));
                }
            }
        } catch (RegistryException | ParserConfigurationException | SAXException | IOException e) {
            String errMsg = "Error occurred while getting bpmn resources for process : " + processName + " version " +
                    processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }

        return BPMNDocument;
    }

    /**
     * Get BPMN Process
     * @return bpmn process
     */
    public BPMN getBpmn() {
        return bpmn;
    }

    /**
     * Gets Deployment Id of the process
     *
     * @return - Deployment ID of the Process
     * @throws ProcessCenterException
     */
    public String getProcessDeployedID() throws
            ProcessCenterException {
        String processRegistryPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                // Getting process and bpmn resource association
                Association[] processAssociations = userRegistry.getAssociations(processRegistryPath,
                        ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
                if (processAssociations != null && processAssociations.length > 0) {
                    //We can have only one association with given process
                    Resource bpmnRegistryResource = userRegistry.get(processAssociations[0].getSourcePath());
                    return bpmnRegistryResource.getProperty(ProcessCenterConstants.PROCESS_DEPLOYMENT_ID);
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting deployment id for process : " + processName + " version" +
                    processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return null;
    }
}