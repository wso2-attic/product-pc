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
package org.wso2.carbon.pc.core.assets.resources;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.derby.impl.store.access.UTF;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.common.AssetResource;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.PCInputStreamProvider;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Class for BPMN resource
 */
public class BPMNResource {

    private static final Log log = LogFactory.getLog(AssetResource.class);



    /**
     * Gets encoded image from registry path
     * @param path
     * @return
     * @throws ProcessCenterException
     */
    public String getEncodedBPMNImage(String path) throws ProcessCenterException {
        byte[] encoded = Base64.encodeBase64(getBPMNImage(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * Gets BPMN image for given bpmn file from registry
     * @param path
     * @return
     * @throws ProcessCenterException
     */
    private byte[] getBPMNImage(String path) throws ProcessCenterException {

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                org.wso2.carbon.registry.core.Resource bpmnXMLResource = reg.get(path);
                byte[] bpmnContent = (byte[]) bpmnXMLResource.getContent();
                InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);

                BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
                BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, false, false);

                ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
                InputStream imageStream = generator.generatePngDiagram(bpmnModel);

                return IOUtils.toByteArray(imageStream);
            } else {
                String msg = "Registry service not available for fetching the BPMN image.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String msg = "Failed to fetch BPMN model: " + path;
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
    }
}
