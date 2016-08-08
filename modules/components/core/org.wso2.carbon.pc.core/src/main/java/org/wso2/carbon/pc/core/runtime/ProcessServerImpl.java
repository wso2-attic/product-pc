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
package org.wso2.carbon.pc.core.runtime;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axis2.AxisFault;
import org.apache.commons.io.IOUtils;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNDeployment;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNProcess;
import org.wso2.carbon.bpmn.stub.BPMNDeploymentServiceBPSFaultException;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.clients.BPMNUploaderClient;
import org.wso2.carbon.pc.core.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.core.clients.WorkflowServiceClient;

import javax.activation.DataHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * The Implementation of Process server interface
 * This class will represent the integration on WSO2 Process Center with WSO2 Business Process Server.
 */
public class ProcessServerImpl implements ProcessServer {

    LoginAdminServiceClient loginServiceClient;
    private String url;
    private String username;
    private String password;

    /**
     * Create new Process Server
     *
     * @param url
     * @param username
     * @param password
     */
    public ProcessServerImpl(String url, String username, String password) throws ProcessCenterException {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            loginServiceClient = new LoginAdminServiceClient(this.url);
        } catch (AxisFault axisFault) {
            throw new ProcessCenterException("Error while creating Login admin service client ", axisFault);
        }
    }

    @Override
    public String deploy(String packageName, InputStream packageInputStream) throws ProcessCenterException {
        try {
            BPMNUploaderClient uploaderClient = new BPMNUploaderClient(null, this.url, loginServiceClient
                    .authenticate(this.username, this.password.toCharArray()));
            uploaderClient.addUploadedFileItem(new DataHandler(new ByteArrayDataSource(IOUtils.toByteArray
                    (packageInputStream))), packageName, "bar");
            uploaderClient.uploadFileItems();
        } catch (LoginAuthenticationExceptionException e) {
            throw new ProcessCenterException("Authentication error while undeploying bpmn package to BPS server ", e);
        } catch (MalformedURLException e) {
            throw new ProcessCenterException("Error occurred while passing server Url ", e);
        } catch (IOException e) {
            throw new ProcessCenterException("Error occurred uploading bpmn package to BPS server ", e);
        }
        return null;
    }

    @Override
    public void unDeploy(String packageName) throws ProcessCenterException {
        try {
            WorkflowServiceClient workflowServiceClient = new WorkflowServiceClient(loginServiceClient.authenticate
                    (this.username, this.password.toCharArray()), this.url, null);
            workflowServiceClient.undeploy(packageName);

        } catch (LoginAuthenticationExceptionException e) {
            throw new ProcessCenterException("Authentication error while undeploying bpmn package to BPS server ", e);
        } catch (MalformedURLException e) {
            throw new ProcessCenterException("Error occurred while passing server Url ", e);
        } catch (BPMNDeploymentServiceBPSFaultException | RemoteException e) {
            throw new ProcessCenterException("Error occurred undeploying bpmn package to BPS server ", e);
        }
    }

    @Override
    public BPMNProcess[] getProcessListByDeploymentID(String deploymentID) throws ProcessCenterException {
        try {
            WorkflowServiceClient workflowServiceClient = new WorkflowServiceClient(loginServiceClient.authenticate
                    (this.username, this.password.toCharArray()), this.url, null);
            return workflowServiceClient.getProcessListByDeploymentID(deploymentID);
        } catch (LoginAuthenticationExceptionException e) {
            throw new ProcessCenterException("Authentication error while undeploying bpmn package to BPS server ", e);
        } catch (MalformedURLException e) {
            throw new ProcessCenterException("Error occurred while passing server Url ", e);
        } catch (RemoteException e) {
            throw new ProcessCenterException("Error occurred while getting process list fro deployment id " +
                    deploymentID, e);
        }
    }

    @Override
    public String getLatestDeploymentChecksum(String packageName) throws
            ProcessCenterException {
        try {
            WorkflowServiceClient workflowServiceClient = new WorkflowServiceClient(loginServiceClient.authenticate
                    (this.username, this.password.toCharArray()), this.url, null);
            return workflowServiceClient.getLatestChecksum(packageName);
        } catch (LoginAuthenticationExceptionException e) {
            throw new ProcessCenterException("Authentication error while undeploying bpmn package to BPS server ", e);
        } catch (MalformedURLException e) {
            throw new ProcessCenterException("Error occurred while passing server Url ", e);
        } catch (RemoteException e) {
            throw new ProcessCenterException("Error occurred while getting latest checksum of the package " +
                    packageName, e);
        }
    }

    @Override
    public String getLatestDeploymentID(String packageName) throws ProcessCenterException {
        try {
            WorkflowServiceClient workflowServiceClient = new WorkflowServiceClient(loginServiceClient.authenticate
                    (this.username, this.password.toCharArray()), this.url, null);
            BPMNDeployment[] deploymentsByName = workflowServiceClient.getDeploymentsByName(packageName);
            Arrays.sort(deploymentsByName, new Comparator<BPMNDeployment>() {
                public int compare(BPMNDeployment bpmnDeployment1, BPMNDeployment bpmnDeployment2) {
                    return Integer.parseInt(bpmnDeployment2.getDeploymentId()) - Integer.parseInt(bpmnDeployment1
                            .getDeploymentId());
                }
            });
            if (deploymentsByName != null && deploymentsByName.length > 0) {
                return deploymentsByName[0].getDeploymentId();
            }
        } catch (LoginAuthenticationExceptionException e) {
            throw new ProcessCenterException("Authentication error while undeploying bpmn package to BPS server ", e);
        } catch (MalformedURLException e) {
            throw new ProcessCenterException("Error occurred while passing server Url ", e);
        } catch (Exception e) {
            throw new ProcessCenterException("Error occurred while getting latest deployment id of the package " +
                    packageName, e);
        }
        return null;
    }

    @Override
    public BPMNDeployment[] getDeploymentsByName(String packageName) throws ProcessCenterException {
        try {
            WorkflowServiceClient workflowServiceClient = new WorkflowServiceClient(loginServiceClient.authenticate
                    (this.username, this.password.toCharArray()), this.url, null);
            BPMNDeployment[] deploymentsByName = workflowServiceClient.getDeploymentsByName(packageName);
            if (deploymentsByName != null) {
                Arrays.sort(deploymentsByName, new Comparator<BPMNDeployment>() {
                    public int compare(BPMNDeployment bpmnDeployment1, BPMNDeployment bpmnDeployment2) {
                        return Integer.parseInt(bpmnDeployment2.getDeploymentId()) - Integer.parseInt(bpmnDeployment1
                                .getDeploymentId());
                    }
                });
            }
            return deploymentsByName;
        } catch (LoginAuthenticationExceptionException e) {
            throw new ProcessCenterException("Authentication error while undeploying bpmn package to BPS server ", e);
        } catch (MalformedURLException e) {
            throw new ProcessCenterException("Error occurred while passing server Url ", e);
        } catch (Exception e) {
            throw new ProcessCenterException("Error occurred while getting latest deployment id of the package " +
                    packageName, e);
        }
    }
}
