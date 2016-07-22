/*
 * Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.pc.core.clients;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNDeployment;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNProcess;
import org.wso2.carbon.bpmn.stub.BPMNDeploymentServiceBPSFaultException;
import org.wso2.carbon.bpmn.stub.BPMNDeploymentServiceStub;
import org.wso2.carbon.pc.core.ProcessCenterConstants;

import java.rmi.RemoteException;

/**
 * Client for perform bpmn operations
 */
public class WorkflowServiceClient {

    private static Log log = LogFactory.getLog(WorkflowServiceClient.class);
    BPMNDeploymentServiceStub deploymentServiceStub = null;

    public WorkflowServiceClient(String cookie,
                                 String backendServerURL,
                                 ConfigurationContext configContext) throws AxisFault {

        String deploymentServiceURL = backendServerURL + "/" + ProcessCenterConstants.SERVICES + "/" +
                ProcessCenterConstants
                .BPMN_DEPLOYMENT_SERVICE;
        deploymentServiceStub = new BPMNDeploymentServiceStub(configContext, deploymentServiceURL);
        ServiceClient deploymentServiceClient = deploymentServiceStub._getServiceClient();
        Options option2 = deploymentServiceClient.getOptions();
        option2.setManageSession(true);
        option2.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);

    }

    /**
     * Get deployment list for given deployment name
     *
     * @param deploymentName
     * @return
     * @throws Exception
     */
    public BPMNDeployment[] getDeploymentsByName(String deploymentName) throws Exception {
        return deploymentServiceStub.getDeploymentsByName(deploymentName);
    }

    /**
     * Get process list on given deployment ID
     *
     * @param deploymentID
     * @return
     */
    public BPMNProcess[] getProcessListByDeploymentID(String deploymentID) {
        try {
            BPMNProcess bpmnProcess;
            return deploymentServiceStub.getProcessesByDeploymentId(deploymentID);

        } catch (RemoteException e) {
            log.error("Error getting process list for deployment id: " + deploymentID, e);
        }
        return null;
    }

    /**
     * Undeploy BPMN package
     *
     * @param deploymentName
     * @throws RemoteException
     * @throws BPMNDeploymentServiceBPSFaultException
     */
    public void undeploy(String deploymentName) throws RemoteException, BPMNDeploymentServiceBPSFaultException {
        deploymentServiceStub.undeploy(deploymentName);
    }

    /**
     * Get latest checksum for given deployment name
     *
     * @param deploymentName
     * @throws RemoteException
     */
    public String getLatestChecksum(String deploymentName) throws RemoteException {
        return deploymentServiceStub.getLatestChecksum(deploymentName);

    }
}
