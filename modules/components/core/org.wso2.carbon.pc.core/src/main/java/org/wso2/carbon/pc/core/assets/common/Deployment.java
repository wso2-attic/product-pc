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
package org.wso2.carbon.pc.core.assets.common;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNDeployment;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNProcess;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.Package;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.runtime.ProcessServer;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for package Deployment
 */
public class Deployment extends AssetResource {

    private static final Log log = LogFactory.getLog(Deployment.class);

    /**
     * Deploy package in given server
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     */
    public String deploy(String packageName, String packageVersion, String username) throws
            ProcessCenterException {
        JSONObject response = new JSONObject();
        try {
            ProcessServer processServer = ProcessCenterServerHolder.getInstance().getProcessCenter().getProcessServer();
            if (processServer == null) {
                // If runtime environment is not configured we cannot do any process server operations.
                response.put(ProcessCenterConstants.ERROR, true);
                response.put(ProcessCenterConstants.MESSAGE, "Runtime Environment has not been configured.");
                return response.toString();
            }
            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            String packageBPMNRegistryPath = Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath
                    (packageName,
                            packageVersion));
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {

                    if (userRegistry.resourceExists(packageRegistryPath) && userRegistry.resourceExists
                            (packageBPMNRegistryPath)) {
                        Resource packageAsset = userRegistry.get(packageRegistryPath);
                        String packageFileName = packageAsset.getProperty(ProcessCenterConstants
                                .PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                        if (!userRegistry.resourceExists(packageBPMNRegistryPath + packageFileName)) {
                            response.put(ProcessCenterConstants.ERROR, true);
                            response.put(ProcessCenterConstants.MESSAGE, "Package file name " + packageName + " " +
                                    "doesn't exist");
                            return response.toString();

                        }
                        Resource packageArchive = userRegistry.get(packageBPMNRegistryPath + packageFileName);
                        String deploymentName = FilenameUtils.getBaseName(packageFileName);
                        /**
                         * archiveChecksum- We need to store checksum of the package to check wheter the deployed
                         * package is same as
                         * uploaded package. This is because another user will deploy another package with same name
                         * so final deployed package may be not same as uploaded package.
                         *
                         * latestChecksum - we also needs latest checksum to check whether there are any parallel
                         * deployments of same package
                         *
                         */
                        String archiveChecksum = packageAsset.getProperty(ProcessCenterConstants.CHECKSUM);
                        String latestChecksum = processServer.getLatestDeploymentChecksum(deploymentName);
                        String deploymentID = processServer.deploy(deploymentName.concat(ProcessCenterConstants
                                        .BAR_EXTENSION),
                                packageArchive
                                        .getContentStream
                                                ());
                        String packageContent = new String((byte[]) packageAsset.getContent(), StandardCharsets.UTF_8);
                        Document packageDocument = stringToXML(packageContent);
                        Element rootElement = packageDocument.getDocumentElement();
                        //Evaluate XPath against RXT document to get runtime environments
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        NodeList evaluate = ((NodeList) xPath.evaluate("/metadata/runtimeEnvironment",
                                packageDocument.getDocumentElement(), XPathConstants.NODESET));
                        if (evaluate != null) {
                            for (int i = 0; i < evaluate.getLength(); i++) {
                                evaluate.item(i).getParentNode().removeChild(evaluate.item(i));
                            }
                        }

                        Element runtimeElement = append(packageDocument, rootElement, ProcessCenterConstants
                                        .RUNTIME_ENVIRONMENT,
                                ProcessCenterConstants.METADATA_NAMESPACE);
                        if (deploymentID != null) {
                            appendText(packageDocument, runtimeElement, ProcessCenterConstants.DEPLOYMENT_ID,
                                    ProcessCenterConstants
                                            .METADATA_NAMESPACE, deploymentID);
                            appendText(packageDocument, runtimeElement, ProcessCenterConstants.STATUS,
                                    ProcessCenterConstants
                                            .METADATA_NAMESPACE, ProcessCenterConstants.PACKAGE_STATUS.ASSOCIATED
                                            .toString());
                            associateRuntimeProcesses(userRegistry, processServer, packageAsset, packageName,
                                    packageVersion, deploymentID);
                            response.put(ProcessCenterConstants.DEPLOYMENT_ID, deploymentID);
                            response.put(ProcessCenterConstants.MESSAGE, "Package has been deployed successfully");
                        } else {
                            appendText(packageDocument, runtimeElement, ProcessCenterConstants.STATUS,
                                    ProcessCenterConstants
                                            .METADATA_NAMESPACE, ProcessCenterConstants.PACKAGE_STATUS.UPLOADED
                                            .toString());
                            response.put(ProcessCenterConstants.MESSAGE, "Package has been uploaded successfully");
                        }

                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.LATEST_CHECKSUM,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, latestChecksum);
                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.USERNAME,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, username);
                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.LAST_UPDATED_TIME,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, String.valueOf(System.currentTimeMillis()));
                        String newProcessContent = xmlToString(packageDocument);
                        packageAsset.setContent(newProcessContent);
                        userRegistry.put(packageRegistryPath, packageAsset);
                        response.put(ProcessCenterConstants.CHECKSUM, archiveChecksum);
                        response.put(ProcessCenterConstants.LATEST_CHECKSUM, latestChecksum);
                        response.put(ProcessCenterConstants.ERROR, false);
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while deploying the package " + packageName +
                    " version " + packageVersion + " in the server";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " version " + packageVersion + " in the server ";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (XPathExpressionException | JSONException | TransformerException | SAXException |
                ParserConfigurationException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }

        return response.toString();
    }

    /**
     * Get deployment
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @return JSON string of deployment information
     * @throws ProcessCenterException
     */
    public String getDeploymentInformation(String packageName, String packageVersion, String username) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        JSONObject runtimeDeployment = new JSONObject();
        String deploymentID = null;
        String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                Resource packageAsset = userRegistry.get(packageRegistryPath);
                if (packageAsset != null) {
                    String packageFileName = packageAsset.getProperty(ProcessCenterConstants
                            .PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                    String processContent = new String((byte[]) packageAsset.getContent(),
                            StandardCharsets.UTF_8);
                    Document packageDocument = stringToXML(processContent);
                    if (packageDocument != null) {
                        //Evaluate XPath against RXT Document to get runtime environment
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        NodeList runtimeEnvironment = ((NodeList) xPath.evaluate("/metadata/runtimeEnvironment",
                                packageDocument.getDocumentElement(), XPathConstants.NODESET));
                        if (runtimeEnvironment != null && runtimeEnvironment.getLength() > 0) {
                            Element runtimeElement = (Element) (runtimeEnvironment.item(0));
                            NodeList deploymentIDNode = runtimeElement.getElementsByTagName(ProcessCenterConstants
                                    .DEPLOYMENT_ID);
                            NodeList status = runtimeElement.getElementsByTagName(ProcessCenterConstants.STATUS);
                            NodeList lastupdatedUsername = runtimeElement.getElementsByTagName(ProcessCenterConstants
                                    .USERNAME);
                            NodeList lastUpdatedTime = runtimeElement.getElementsByTagName(ProcessCenterConstants
                                    .LAST_UPDATED_TIME);

                            if (deploymentIDNode != null && deploymentIDNode.getLength() > 0) {
                                deploymentID = deploymentIDNode.item(0).getTextContent();
                                runtimeDeployment.put(ProcessCenterConstants.DEPLOYMENT_ID, deploymentID);
                            }
                            if (status != null && status.getLength() > 0) {
                                runtimeDeployment.put(ProcessCenterConstants.STATUS, status.item(0).getTextContent());
                            }
                            if (lastupdatedUsername != null && lastupdatedUsername.getLength() > 0) {
                                runtimeDeployment.put(ProcessCenterConstants.USERNAME, lastupdatedUsername.item(0)
                                        .getTextContent());
                            }
                            if (lastUpdatedTime != null && lastUpdatedTime.getLength() > 0) {
                                runtimeDeployment.put(ProcessCenterConstants.LAST_UPDATED_TIME, lastUpdatedTime.item
                                        (0).getTextContent());
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                runtimeDeployment.put("lastUpdatedDate", dateFormatter.format(new Date(Long.parseLong
                                        (lastUpdatedTime.item(0).getTextContent()))));
                            }
                            if (deploymentID != null) {
                                // Get BPMN resources list
                                runtimeDeployment.put(ProcessCenterConstants.BPMN_RESOURCES, getBpmnResources
                                        (packageName, packageVersion,
                                                username));
                            }
                        }
                    }
                    if (packageFileName != null) {
                        // When getting deployment list we use package file name
                        JSONArray deploymentList = getDeploymentList(packageName, packageVersion, FilenameUtils
                                .getBaseName(packageFileName));
                        if (deploymentList != null) {
                            runtimeDeployment.put(ProcessCenterConstants.DEPLOYMENTS, deploymentList);
                        }
                        runtimeDeployment.put("packageFileName", packageFileName);
                    }
                }

            }
            response.put("runtimeDeployment", runtimeDeployment);
        } catch (RegistryException e) {
            String errMsg = "Error occurred while accessing registry for getting deployment information for package: " +
                    packageName + " " + "version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException | XPathExpressionException | IOException | SAXException | ParserConfigurationException
                e) {
            String errMsg = "Error occurred while getting deployment information for package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }

    /**
     * Associate runtime deployment for already uploaded package
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param deploymentID   - Deployment Id needs to be associated with package
     * @param username       - Provider of the Package
     * @return associated deployment id
     * @throws ProcessCenterException
     */
    public String associateDeploymentID(String packageName, String packageVersion, String deploymentID, String
            username) throws ProcessCenterException {
        JSONObject response = new JSONObject();
        try {
            ProcessServer processServer = ProcessCenterServerHolder.getInstance().getProcessCenter().getProcessServer();
            if (processServer == null) {
                // If runtime environment is not configured we cannot do any process server operations.
                response.put(ProcessCenterConstants.ERROR, true);
                response.put(ProcessCenterConstants.MESSAGE, "Runtime Environment has not been configured.");
                return response.toString();
            }
            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                Resource packageAsset = userRegistry.get(packageRegistryPath);
                String processContent = new String((byte[]) packageAsset.getContent(),
                        StandardCharsets.UTF_8);
                String packageFileName = packageAsset.getProperty(ProcessCenterConstants
                        .PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                String deploymentName = FilenameUtils.getBaseName(packageFileName);
                Document packageDocument = stringToXML(processContent);
                if (packageDocument != null) {
                    // Evaluate XPath against Document to get runtime environment
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList runtimeEnvironment = ((NodeList) xPath.
                            evaluate("/metadata/runtimeEnvironment",
                                    packageDocument.getDocumentElement(), XPathConstants.NODESET));
                    if (runtimeEnvironment != null && runtimeEnvironment.getLength() > 0) {
                        Element runtimeElement = (Element) (runtimeEnvironment.item(0));
                        NodeList deploymentIDNode = runtimeElement.getElementsByTagName(ProcessCenterConstants
                                .DEPLOYMENT_ID);
                        NodeList latestChecksum = runtimeElement.getElementsByTagName(ProcessCenterConstants
                                .LATEST_CHECKSUM);
                        String checkSum = packageAsset.getProperty(ProcessCenterConstants.CHECKSUM);
                        NodeList status = runtimeElement.getElementsByTagName(ProcessCenterConstants.STATUS);
                        if (deploymentID != null) {

                            // If deployment id provided we will associate that deployment id
                            if (deploymentIDNode != null && deploymentIDNode.getLength() > 0) {
                                deploymentIDNode.item(0).setTextContent(deploymentID);
                            } else {
                                appendText(packageDocument, runtimeElement, ProcessCenterConstants
                                                .DEPLOYMENT_ID,
                                        ProcessCenterConstants.METADATA_NAMESPACE, deploymentID);
                            }
                            if (status != null && status.getLength() > 0) {
                                status.item(0).setTextContent(ProcessCenterConstants.PACKAGE_STATUS.ASSOCIATED
                                        .toString());
                            } else {
                                appendText(packageDocument, runtimeElement, ProcessCenterConstants.STATUS,
                                        ProcessCenterConstants.METADATA_NAMESPACE, ProcessCenterConstants
                                                .PACKAGE_STATUS.ASSOCIATED.toString());
                            }
                            String newProcessContent = xmlToString(packageDocument);
                            packageAsset.setContent(newProcessContent);
                            associateRuntimeProcesses(userRegistry, processServer, packageAsset, packageName,
                                    packageVersion, deploymentID);
                            userRegistry.put(packageRegistryPath, packageAsset);
                            response.put(ProcessCenterConstants.DEPLOYMENT_ID, deploymentID);
                            response.put(ProcessCenterConstants.MESSAGE, "Package has been successfully " +
                                    "associated with deployment");
                        } else if (status != null && status.getLength() > 0 && status.item(0).getTextContent()
                                .equals
                                        (ProcessCenterConstants.PACKAGE_STATUS.UPLOADED.toString())) {
                            // If status == UPLOADED we will check for deployment ID
                            String tempDeploymentID = processServer.getLatestDeploymentID(deploymentName);
                            if (tempDeploymentID != null) {
                                // We will check for latest checksum to check whether the same package has been
                                // deployed to process server.
                                String deployedChecksum = processServer.getLatestDeploymentChecksum(deploymentName);
                                if (deployedChecksum != null) {
                                    if (checkSum != null && checkSum.equals(deployedChecksum)) {
                                        //update deployment id
                                        appendText(packageDocument, runtimeElement, ProcessCenterConstants
                                                        .DEPLOYMENT_ID,
                                                ProcessCenterConstants.METADATA_NAMESPACE, tempDeploymentID);
                                        status.item(0).setTextContent(ProcessCenterConstants.PACKAGE_STATUS.ASSOCIATED
                                                .toString());
                                        String newProcessContent = xmlToString(packageDocument);
                                        packageAsset.setContent(newProcessContent);
                                        associateRuntimeProcesses(userRegistry, processServer, packageAsset,
                                                packageName,
                                                packageVersion, tempDeploymentID);
                                        userRegistry.put(packageRegistryPath, packageAsset);
                                        response.put(ProcessCenterConstants.DEPLOYMENT_ID, tempDeploymentID);
                                        response.put(ProcessCenterConstants.MESSAGE, "Package has been successfully " +
                                                "associated with deployment");
                                    } else if (latestChecksum != null && latestChecksum.getLength() > 0 &&
                                            !latestChecksum.item(0).getTextContent().equals(deployedChecksum)) {
                                        // If new checksum does not match with previous checksum or deployed package
                                        // checksum , it may be due to another parallel deployment.
                                        response.put(ProcessCenterConstants.ERROR, true);
                                        response.put(ProcessCenterConstants.MESSAGE, "Package has been " +
                                                "replaced by another deployment. Please redeploy the package");
                                        return response.toString();
                                    } else {
                                        response.put(ProcessCenterConstants.MESSAGE, "Package has not been deployed " +
                                                "in server. Try again later");
                                    }
                                } else {
                                    response.put(ProcessCenterConstants.MESSAGE, "Package has not been deployed " +
                                            "in server. Try again later");
                                }
                            } else {
                                response.put(ProcessCenterConstants.MESSAGE, "Package has not been deployed " +
                                        "in server. Try again later");
                            }
                        }
                    } else if (deploymentID != null) {
                        Element rootElement = packageDocument.getDocumentElement();
                        Element runtimeElement = append(packageDocument, rootElement, ProcessCenterConstants
                                        .RUNTIME_ENVIRONMENT,
                                ProcessCenterConstants.METADATA_NAMESPACE);
                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.DEPLOYMENT_ID,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, deploymentID);
                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.STATUS,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, ProcessCenterConstants.PACKAGE_STATUS.ASSOCIATED
                                        .toString());
                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.USERNAME,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, username);
                        appendText(packageDocument, runtimeElement, ProcessCenterConstants.LAST_UPDATED_TIME,
                                ProcessCenterConstants
                                        .METADATA_NAMESPACE, String.valueOf(System.currentTimeMillis()));
                        String newProcessContent = xmlToString(packageDocument);
                        packageAsset.setContent(newProcessContent);
                        associateRuntimeProcesses(userRegistry, processServer, packageAsset, packageName,
                                packageVersion, deploymentID);
                        userRegistry.put(packageRegistryPath, packageAsset);
                        response.put(ProcessCenterConstants.DEPLOYMENT_ID, deploymentID);
                        response.put(ProcessCenterConstants.MESSAGE, "Package has been successfully " +
                                "associated with deployment");
                    }
                }
            }
            response.put(ProcessCenterConstants.ERROR, false);

        } catch (RegistryException e) {
            String errMsg = "Error occurred while accessing registry for associating deployment ID for package: " +
                    packageName + " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException | XPathExpressionException | IOException | SAXException | TransformerException
                | ParserConfigurationException e) {
            String errMsg = "Error occurred while associating deployment ID for package " + packageName + " file " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }


    /**
     * Associate runtime process to bpmn resources
     *
     * @param userRegistry   User registry where bpmn xml files are stored
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param packageAsset   - Package registry resource
     * @param processServer  - Process server
     * @param deploymentID   - Deployment id in the process server
     * @return
     * @throws ProcessCenterException
     */
    private void associateRuntimeProcesses(UserRegistry userRegistry, ProcessServer processServer, Resource
            packageAsset, String packageName, String packageVersion, String deploymentID) throws
            ProcessCenterException {
        String packageBPMNContentRegistryPath = Package.getPackageBPMNContentRegistryPath
                (Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName, packageVersion)));
        try {
            BPMNProcess[] processListByDeploymentID = processServer.getProcessListByDeploymentID(deploymentID);

            if (processListByDeploymentID != null && processListByDeploymentID.length > 0) {
                if (packageAsset != null) {
                    String bpmnResourceCount = packageAsset.getProperty(ProcessCenterConstants
                            .BPMN_RESOURCES_COUNT);
                    if (bpmnResourceCount != null) {
                        // Get bpmn file registry registry collection
                        Collection bpmnResourceCollection = userRegistry.get(packageBPMNContentRegistryPath, 0,
                                Integer.parseInt(bpmnResourceCount));
                        if (bpmnResourceCollection != null) {
                            String[] bpmnResourcePaths = (String[]) bpmnResourceCollection.getContent();
                            // Read each bpmn file
                            for (String bpmnResourcePath : bpmnResourcePaths) {
                                Resource bpmnRegistryResource = userRegistry.get(bpmnResourcePath);
                                JSONObject bpmnResource = new JSONObject();
                                String bpmnProcessName = bpmnRegistryResource.getProperty(ProcessCenterConstants
                                        .PROCESS_NAME);
                                for (BPMNProcess bpmnProcess : processListByDeploymentID) {
                                    if (bpmnProcess.getName().equals(bpmnProcessName)) {
                                        bpmnRegistryResource.setProperty(ProcessCenterConstants.PROCESS_DEPLOYMENT_ID,
                                                bpmnProcess.getProcessId());
                                        userRegistry.put(bpmnResourcePath, bpmnRegistryResource);
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_DEPLOYMENT_ID, bpmnProcess
                                                .getProcessId());
                                        break;
                                    }
                                }
                                bpmnResource.put(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME,
                                        bpmnRegistryResource.getPath()
                                                .replaceFirst("/" + packageBPMNContentRegistryPath, ""));
                            }
                        }
                    }
                }
            }
        } catch (RegistryException | JSONException e) {
            String errMsg = "Error occurred while associating runtime processes for package: " + packageName +
                    " version " + packageVersion + " deployment ID: " + deploymentID;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Get list of  BPMN resources related to the Package
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @return
     * @throws ProcessCenterException
     */
    public JSONArray getBpmnResources(String packageName, String packageVersion, String username) throws
            ProcessCenterException {

        JSONArray bpmnResources = new JSONArray();
        String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNContentRegistryPath = Package.getPackageBPMNContentRegistryPath
                (Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName, packageVersion)));
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                Resource packageAsset = userRegistry.get(packageRegistryPath);
                if (packageAsset != null) {
                    String bpmnResourceCount = packageAsset.getProperty(ProcessCenterConstants
                            .BPMN_RESOURCES_COUNT);
                    if (bpmnResourceCount != null) {
                        // Get bpmn file registry registry collection
                        Collection bpmnResourceCollection = userRegistry.get(packageBPMNContentRegistryPath, 0,
                                Integer.parseInt(bpmnResourceCount));
                        if (bpmnResourceCollection != null) {
                            String[] bpmnResourcePaths = (String[]) bpmnResourceCollection.getContent();
                            // Read each bpmn file
                            for (String bpmnResourcePath : bpmnResourcePaths) {
                                Resource bpmnRegistryResource = userRegistry.get(bpmnResourcePath);
                                if (bpmnRegistryResource != null) {
                                    JSONObject bpmnResource = new JSONObject();
                                    String processDeploymentID = bpmnRegistryResource.getProperty
                                            (ProcessCenterConstants.PROCESS_DEPLOYMENT_ID);
                                    if (processDeploymentID != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_DEPLOYMENT_ID,
                                                processDeploymentID);
                                    }
                                    String processName = bpmnRegistryResource.getProperty
                                            (ProcessCenterConstants.PROCESS_NAME);
                                    if (processDeploymentID != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_DEPLOYMENT_ID,
                                                processDeploymentID);
                                    }
                                    if (processName != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_NAME, processName);
                                    }
                                    bpmnResource.put(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME,
                                            bpmnRegistryResource.getPath().replaceFirst
                                                    ("/" + packageBPMNContentRegistryPath, ""));
                                    bpmnResources.put(bpmnResource);
                                }
                            }
                        }
                    }
                }
            }
        } catch (RegistryException | JSONException e) {
            String errMsg = "Error occurred while getting bpmn resources for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return bpmnResources;
    }


    /**
     * Get list of  Deployments related to the Package
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param deploymentName Name of the Deployment
     * @return
     * @throws ProcessCenterException
     */
    public JSONArray getDeploymentList(String packageName, String packageVersion, String deploymentName) throws
            ProcessCenterException {
        ProcessServer processServer = ProcessCenterServerHolder.getInstance().getProcessCenter().getProcessServer();
        try {
            if (processServer != null) {
                BPMNDeployment[] deploymentsByName = processServer.getDeploymentsByName(deploymentName);
                if (deploymentsByName != null) {
                    JSONArray deployments = new JSONArray();
                    for (int i = 0; i < deploymentsByName.length; i++) {
                        JSONObject deployment = new JSONObject();
                        deployment.put(ProcessCenterConstants.DEPLOYMENT_ID, deploymentsByName[i].getDeploymentId());
                        deployment.put(ProcessCenterConstants.NAME, deploymentsByName[i].getDeploymentName());
                        if (i == 0) {
                            deployment.put(ProcessCenterConstants.LATEST, true);
                        }
                        deployments.put(deployment);
                    }
                    return deployments;
                }
            }
        } catch (JSONException e) {
            String errMsg = "Error occurred while getting deployments package " + packageName +
                    "version " + packageVersion + " deploymentName " + deploymentName;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return null;
    }

    /**
     * Undeploy package
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @return JSON string contains the status of the undeployment
     * @throws ProcessCenterException
     */
    public String undeploy(String packageName, String packageVersion, String username) throws
            ProcessCenterException {
        JSONObject response = new JSONObject();
        try {
            ProcessServer processServer = ProcessCenterServerHolder.getInstance().getProcessCenter().getProcessServer();
            if (processServer == null) {
                // If runtime environment is not configured we cannot do any process server operations.
                response.put(ProcessCenterConstants.ERROR, true);
                response.put(ProcessCenterConstants.MESSAGE, "Runtime Environment has not been configured.");
                return response.toString();
            }
            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            String packageBPMNContentRegistryPath = Package.getPackageBPMNContentRegistryPath
                    (Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName,
                            packageVersion)));
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {

                    if (userRegistry.resourceExists(packageRegistryPath)) {
                        Resource packageAsset = userRegistry.get(packageRegistryPath);
                        if (packageAsset != null) {

                            String packageContent = new String((byte[]) packageAsset.getContent(), StandardCharsets
                                    .UTF_8);
                            String packageFileName = packageAsset.getProperty(ProcessCenterConstants
                                    .PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                            if (packageFileName != null) {
                                processServer.unDeploy(FilenameUtils.getBaseName(packageFileName));
                            }

                            Document packageDocument = stringToXML(packageContent);
                            //Evaluate XPath against RXT document to get runtime environments
                            XPath xPath = XPathFactory.newInstance().newXPath();
                            NodeList evaluate = ((NodeList) xPath.evaluate("/metadata/runtimeEnvironment",
                                    packageDocument.getDocumentElement(), XPathConstants.NODESET));
                            if (evaluate != null) {
                                for (int i = 0; i < evaluate.getLength(); i++) {
                                    evaluate.item(i).getParentNode().removeChild(evaluate.item(i));
                                }
                            }
                            String newProcessContent = xmlToString(packageDocument);
                            packageAsset.setContent(newProcessContent);
                            userRegistry.put(packageRegistryPath, packageAsset);
                            String bpmnResourceCount = packageAsset.getProperty(ProcessCenterConstants
                                    .BPMN_RESOURCES_COUNT);
                            if (bpmnResourceCount != null) {
                                // Get bpmn file registry registry collection
                                Collection bpmnResourceCollection = userRegistry.get(packageBPMNContentRegistryPath, 0,
                                        Integer.parseInt(bpmnResourceCount));
                                if (bpmnResourceCollection != null) {
                                    String[] bpmnResourcePaths = (String[]) bpmnResourceCollection.getContent();
                                    // Read each bpmn file
                                    for (String bpmnResourcePath : bpmnResourcePaths) {
                                        Resource bpmnRegistryResource = userRegistry.get(bpmnResourcePath);
                                        bpmnRegistryResource.removeProperty(ProcessCenterConstants
                                                .PROCESS_DEPLOYMENT_ID);
                                        userRegistry.put(bpmnResourcePath, bpmnRegistryResource);

                                    }
                                }
                            }
                        }
                        response.put(ProcessCenterConstants.ERROR, false);
                    } else {
                        response.put(ProcessCenterConstants.ERROR, true);
                        response.put(ProcessCenterConstants.MESSAGE, "Package Asset resource cannot be found.");
                        return response.toString();
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while deploying the package " + packageName +
                    " version " + packageVersion + " in the server";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " version " + packageVersion + " in the server ";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (XPathExpressionException | JSONException | TransformerException | SAXException |
                ParserConfigurationException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }

        return response.toString();
    }

    /**
     * Remove deployment Association from package
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @return JSON string contains the status of the undeployment
     * @throws ProcessCenterException
     */
    public String removeAssociation(String packageName, String packageVersion, String username) throws
            ProcessCenterException {
        JSONObject response = new JSONObject();
        try {

            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            String packageBPMNContentRegistryPath = Package.getPackageBPMNContentRegistryPath
                    (Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName,
                            packageVersion)));
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {

                    if (userRegistry.resourceExists(packageRegistryPath)) {
                        Resource packageAsset = userRegistry.get(packageRegistryPath);
                        if (packageAsset != null) {

                            String packageContent = new String((byte[]) packageAsset.getContent(), StandardCharsets
                                    .UTF_8);
                            Document packageDocument = stringToXML(packageContent);
                            //Evaluate XPath against RXT document to get runtime environments
                            XPath xPath = XPathFactory.newInstance().newXPath();
                            NodeList evaluate = ((NodeList) xPath.evaluate("/metadata/runtimeEnvironment",
                                    packageDocument.getDocumentElement(), XPathConstants.NODESET));
                            if (evaluate != null) {
                                for (int i = 0; i < evaluate.getLength(); i++) {
                                    evaluate.item(i).getParentNode().removeChild(evaluate.item(i));
                                }
                            }
                            String newProcessContent = xmlToString(packageDocument);
                            packageAsset.setContent(newProcessContent);
                            userRegistry.put(packageRegistryPath, packageAsset);
                            String bpmnResourceCount = packageAsset.getProperty(ProcessCenterConstants
                                    .BPMN_RESOURCES_COUNT);
                            if (bpmnResourceCount != null) {
                                // Get bpmn file registry registry collection
                                Collection bpmnResourceCollection = userRegistry.get(packageBPMNContentRegistryPath, 0,
                                        Integer.parseInt(bpmnResourceCount));
                                if (bpmnResourceCollection != null) {
                                    String[] bpmnResourcePaths = (String[]) bpmnResourceCollection.getContent();
                                    // Read each bpmn file
                                    for (String bpmnResourcePath : bpmnResourcePaths) {
                                        Resource bpmnRegistryResource = userRegistry.get(bpmnResourcePath);
                                        bpmnRegistryResource.removeProperty(ProcessCenterConstants
                                                .PROCESS_DEPLOYMENT_ID);
                                        userRegistry.put(bpmnResourcePath, bpmnRegistryResource);

                                    }
                                }
                            }
                        }
                        response.put(ProcessCenterConstants.ERROR, false);
                    } else {
                        response.put(ProcessCenterConstants.ERROR, true);
                        response.put(ProcessCenterConstants.MESSAGE, "Package Asset resource cannot be found.");
                        return response.toString();
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while deploying the package " + packageName +
                    " version " + packageVersion + " in the server";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " version " + packageVersion + " in the server ";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (XPathExpressionException | JSONException | TransformerException | SAXException |
                ParserConfigurationException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }

        return response.toString();
    }

    public boolean isBPSRuntimeEnvironmentEnabled() {
        return ProcessCenterServerHolder.getInstance().getProcessCenter()
                                        .getProcessCenterConfiguration()
                                        .isRuntimeEnvironmentEnabled();
    }

    public String getBPSRuntimeEnvironmentURL() {
        return ProcessCenterServerHolder.getInstance().getProcessCenter()
                                        .getProcessCenterConfiguration().getRuntimeEnvironmentURL();
    }
}
