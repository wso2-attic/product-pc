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

import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNProcess;
import org.wso2.carbon.pc.core.ProcessCenterException;

import java.io.InputStream;

/**
 * The Process Server interface
 * Process Center can be integrated with Process server by extending this interface
 */
public interface ProcessServer {

    /**
     * Deploy given process package in the process server
     *
     * @param packageName        : The name of the package to be deployed.
     * @param packageInputStream
     * @return : package id.
     */
    public String deploy(String packageName, InputStream packageInputStream) throws ProcessCenterException;

    /**
     * Undeploy given process package in the process server
     *
     * @param packageName : The name of the package to be Undeployed.
     */
    public void unDeploy(String packageName) throws ProcessCenterException;


    /**
     * Get Process List for given deployment ID
     *
     * @param deploymentID
     * @throws ProcessCenterException
     */
    public BPMNProcess[] getProcessListByDeploymentID(String deploymentID) throws ProcessCenterException;

    /**
     * Get latest checksum of the current deployed package
     *
     * @param packageName
     * @return
     * @throws ProcessCenterException
     */
    public String getLatestDeploymentChecksum(String packageName) throws ProcessCenterException;

    /**
     * Get deploymentId for deployment Name
     * @param deploymentName
     * @return
     * @throws ProcessCenterException
     */
    public String getDeploymentID(String deploymentName) throws ProcessCenterException;

}
