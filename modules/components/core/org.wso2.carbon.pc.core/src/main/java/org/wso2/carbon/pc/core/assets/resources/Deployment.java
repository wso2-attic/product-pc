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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.bpmn.core.mgt.model.xsd.BPMNProcess;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.Package;
import org.wso2.carbon.pc.core.assets.common.AssetResource;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.runtime.ProcessServer;
import org.wso2.carbon.pc.core.util.Utils;
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
import java.security.NoSuchAlgorithmException;
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
     * @param packageName
     * @param packageVersion
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
                        String archiveChecksum = Utils.getMD5Checksum(packageArchive.getContentStream());
                        String latestChecksum = processServer.getLatestDeploymentChecksum(deploymentName);
                        String deploymentID = processServer.deploy(packageFileName, packageArchive.getContentStream());
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

                        Element runtimeElement = append(packageDocument, rootElement, "runtimeEnvironment",
                                ProcessCenterConstants.METADATA_NAMESPACE);
                        if (deploymentID != null) {
                            appendText(packageDocument, runtimeElement, "deploymentID", ProcessCenterConstants
                                    .METADATA_NAMESPACE, deploymentID);
                            appendText(packageDocument, runtimeElement, "status", ProcessCenterConstants
                                    .METADATA_NAMESPACE, "DEPLOYED");
                            response.put("deploymentID", deploymentID);
                        }
                        appendText(packageDocument, runtimeElement, "checksum", ProcessCenterConstants
                                .METADATA_NAMESPACE, archiveChecksum);
                        appendText(packageDocument, runtimeElement, "status", ProcessCenterConstants
                                .METADATA_NAMESPACE, "UPLOADED");
                        appendText(packageDocument, runtimeElement, "latestChecksum", ProcessCenterConstants
                                .METADATA_NAMESPACE, latestChecksum);
                        appendText(packageDocument, runtimeElement, "username", ProcessCenterConstants
                                .METADATA_NAMESPACE, username);
                        appendText(packageDocument, runtimeElement, "lastUpdatedTime", ProcessCenterConstants
                                .METADATA_NAMESPACE, String.valueOf(System.currentTimeMillis()));
                        String newProcessContent = xmlToString(packageDocument);
                        packageAsset.setContent(newProcessContent);
                        userRegistry.put(packageRegistryPath, packageAsset);
                        response.put("checksum", archiveChecksum);
                        response.put("latestChecksum", latestChecksum);
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
        } catch (XPathExpressionException | JSONException | TransformerException | NoSuchAlgorithmException |
                SAXException |
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
     * @param packageName
     * @param packageVersion
     * @param userName
     * @return
     * @throws ProcessCenterException
     */
    public String getDeploymentInformation(String packageName, String packageVersion, String userName) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        JSONObject runtimeDeployment = new JSONObject();
        String deploymentID = null;
        String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(userName);
                Resource packageAsset = userRegistry.get(packageRegistryPath);
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
                        NodeList deploymentIDNode = runtimeElement.getElementsByTagName("deploymentID");
                        NodeList status = runtimeElement.getElementsByTagName("status");
                        NodeList lastupdatedUsername = runtimeElement.getElementsByTagName("username");
                        NodeList lastUpdatedTime = runtimeElement.getElementsByTagName("lastUpdatedTime");

                        if (deploymentIDNode != null && deploymentIDNode.getLength() > 0) {
                            deploymentID = deploymentIDNode.item(0).getTextContent();
                            runtimeDeployment.put("deploymentID", deploymentID);
                        }
                        if (status != null && status.getLength() > 0) {
                            runtimeDeployment.put("status", status.item(0).getTextContent());
                        }
                        if (lastupdatedUsername != null && lastupdatedUsername.getLength() > 0) {
                            runtimeDeployment.put("username", lastupdatedUsername.item(0).getTextContent());
                        }
                        if (lastUpdatedTime != null && lastUpdatedTime.getLength() > 0) {
                            runtimeDeployment.put("lastUpdatedTime", lastUpdatedTime.item(0).getTextContent());
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            runtimeDeployment.put("lastUpdatedDate", dateFormatter.format(new Date(Long.parseLong
                                    (lastUpdatedTime.item(0).getTextContent()))));
                        }
                        if (deploymentID != null) {
                            // Get BPMN resources list
                            runtimeDeployment.put("bpmnResources", getBpmnResources(packageName, packageVersion,
                                    userName));
                        }
                    }
                }
            }
            response.put("runtimeDeployment", runtimeDeployment);
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting deployment ID for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException | XPathExpressionException | IOException | SAXException | ParserConfigurationException
                e) {
            String errMsg = "Error occurred while getting deployment ID for package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }

    /**
     * Associate runtime deployment for already uploaded package
     *
     * @param packageName
     * @param packageVersion
     * @param userName
     * @return
     * @throws ProcessCenterException
     */
    public String associateDeploymentID(String packageName, String packageVersion, String userName) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        String deploymentID = null;
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
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(userName);
                Resource packageAsset = userRegistry.get(packageRegistryPath);
                String processContent = new String((byte[]) packageAsset.getContent(),
                        StandardCharsets.UTF_8);
                String deploymentName = FilenameUtils.getBaseName(packageAsset.getProperty
                        (ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME));
                Document packageDocument = stringToXML(processContent);
                if (packageDocument != null) {
                    // Evaluate XPath against Document to get runtime environment
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList runtimeEnvironment = ((NodeList) xPath.
                            evaluate("/metadata/runtimeEnvironment",
                                    packageDocument.getDocumentElement(), XPathConstants.NODESET));
                    if (runtimeEnvironment != null && runtimeEnvironment.getLength() > 0) {
                        Element runtimeElement = (Element) (runtimeEnvironment.item(0));
                        NodeList deploymentIDNode = runtimeElement.getElementsByTagName("deploymentID");
                        NodeList latestChecksum = runtimeElement.getElementsByTagName("latestChecksum");
                        NodeList checkSum = runtimeElement.getElementsByTagName("checksum");
                        NodeList status = runtimeElement.getElementsByTagName("status");

                        if (deploymentIDNode != null && deploymentIDNode.getLength() > 0) {
                            deploymentID = deploymentIDNode.item(0).getTextContent();
                        } else if (status != null && status.getLength() > 0 && status.item(0).getTextContent().equals
                                ("UPLOADED")) {
                            // If status == UPLOADED we will check for deployment ID
                            String tempDeploymentID = processServer.getDeploymentID(deploymentName);
                            if (tempDeploymentID != null) {
                                // We will check for latest checksum to check whether the same package has been
                                // deployed to process server.
                                String deployedChecksum = processServer.getLatestDeploymentChecksum(deploymentName);
                                if (deployedChecksum != null) {
                                    if (checkSum != null && checkSum.getLength() > 0 && checkSum.item(0)
                                            .getTextContent().equals(deployedChecksum)) {
                                        //update deployment id
                                        appendText(packageDocument, runtimeElement, "deploymentID",
                                                ProcessCenterConstants.METADATA_NAMESPACE, tempDeploymentID);
                                        status.item(0).setTextContent("DEPLOYED");
                                        String newProcessContent = xmlToString(packageDocument);
                                        packageAsset.setContent(newProcessContent);
                                        associateRuntimeProcesses(userRegistry, processServer, packageAsset,
                                                packageName,
                                                packageVersion, tempDeploymentID);
                                        userRegistry.put(packageRegistryPath, packageAsset);
                                        deploymentID = tempDeploymentID;
                                    } else if (latestChecksum != null && latestChecksum.getLength() > 0 &&
                                            !latestChecksum.item(0).getTextContent().equals(deployedChecksum)) {
                                        // If new checksum does not match with previous checksum or deployed package
                                        // checksum , it may be due to another parallel deployment.
                                        response.put(ProcessCenterConstants.ERROR, true);
                                        response.put(ProcessCenterConstants.MESSAGE, "Package has been " +
                                                "replaced by another deployment. Please redeploy the package");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            response.put("deploymentID", deploymentID);
            response.put(ProcessCenterConstants.ERROR, false);
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting deployment ID for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException | XPathExpressionException | IOException | SAXException | TransformerException
                | ParserConfigurationException e) {
            String errMsg = "Error occurred while getting deployment ID for package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }


    /**
     * Associate runtime process to bpmn resources
     *
     * @param userRegistry
     * @param packageAsset
     * @param packageName
     * @param packageVersion
     * @param deploymentID
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
                                        bpmnRegistryResource.setProperty(ProcessCenterConstants.PROCESS_ID,
                                                bpmnProcess.getProcessId());
                                        userRegistry.put(bpmnResourcePath, bpmnRegistryResource);
                                        bpmnResource.put("processId", bpmnProcess.getProcessId());
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
     * @param packageName
     * @param packageVersion
     * @param userName
     * @return
     * @throws ProcessCenterException
     */
    public JSONArray getBpmnResources(String packageName, String packageVersion, String userName) throws
            ProcessCenterException {

        JSONArray bpmnResources = new JSONArray();
        String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNContentRegistryPath = Package.getPackageBPMNContentRegistryPath
                (Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName, packageVersion)));
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(userName);
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
                                    String processID = bpmnRegistryResource.getProperty
                                            (ProcessCenterConstants.PROCESS_ID);
                                    if (processID != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_ID, processID);
                                    }
                                    String processName = bpmnRegistryResource.getProperty
                                            (ProcessCenterConstants.PROCESS_NAME);
                                    if (processID != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_ID, processID);
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
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting bpmn resources for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException e) {
            String errMsg = "Error with JSON operation while getting bpmn resources for package: " + packageName + " " +
                    "version" + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return bpmnResources;
    }
}
