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
import org.wso2.carbon.pc.core.ProcessCenter;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.Package;
import org.wso2.carbon.pc.core.assets.common.Asset;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class Deployment extends Asset{

    private static final Log log = LogFactory.getLog(Deployment.class);
    /**
     * Deploy package in given server
     *
     * @param packageName
     * @param packageVersion
     * @param serverName
     */
    public String deploy(String packageName, String packageVersion, String serverName, String username) throws
            ProcessCenterException {
        JSONObject response = new JSONObject();
        try {
            log.info("Deploying " + packageName);
            ProcessCenter processCenter = ProcessCenterServerHolder.getInstance().getProcessCenter();
            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            String packageBPMNRegistryPath = Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName,
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
                        Resource packageArchive = userRegistry.get(packageBPMNRegistryPath
                                + packageFileName);
                        InputStream packageArchiveContentStream = packageArchive.getContentStream();
                        String archiveChecksum = Utils.getMD5Checksum(packageArchiveContentStream);
                        String deploymentName = FilenameUtils.getBaseName(packageFileName);
                        String latestChecksum = processCenter.getEnvironmentsRepository().getLatestDeploymentChecksum
                                (serverName, deploymentName);
                        String deploymentID = processCenter.getEnvironmentsRepository().deploy(serverName,
                                packageFileName,
                                packageArchiveContentStream);
                        String packageContent = new String((byte[]) packageAsset.getContent(), StandardCharsets.UTF_8);
                        Document packageDocument = stringToXML(packageContent);
                        Element rootElement = packageDocument.getDocumentElement();
                        //Evaluate XPath against Document itself
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        NodeList evaluate = ((NodeList) xPath.evaluate("/metadata/runtimeEnvironment/name[text()" +
                                        "='" + serverName + "']",
                                packageDocument
                                        .getDocumentElement(), XPathConstants.NODESET));
                        if (evaluate != null) {
                            for (int i = 0; i < evaluate.getLength(); i++) {
                                packageDocument.removeChild(evaluate.item(i).getParentNode());
                            }
                        }

                        Element runtimeElement = append(packageDocument, rootElement, "runtimeEnvironment",
                                ProcessCenterConstants.METADATA_NAMESPACE);
                        appendText(packageDocument, runtimeElement, "name", ProcessCenterConstants
                                .METADATA_NAMESPACE, serverName);
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
                        response.put("name", serverName);
                        response.put(ProcessCenterConstants.ERROR, false);
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while deploying the package " + packageName +
                    " version " + packageVersion + " in the server :" + serverName;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException e) {
            String errMsg = "Error occurred while deploying the package " + packageName +
                    " version " + packageVersion + " in the server :" + serverName;
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
    public String getDeploymentInformation(String packageName, String packageVersion, String userName, String
            serverName) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        JSONObject runtimeDeployment = new JSONObject();
        JSONArray runtimeEnvironments = new JSONArray();
        String deploymentID = null;
        ProcessCenter processCenter = ProcessCenterServerHolder.getInstance().getProcessCenter();
        String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
        try {
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
                    //Evaluate XPath against Document itself
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList runtimeEnvironment = ((NodeList) xPath.
                            evaluate("/metadata/runtimeEnvironment/name[text()='" + serverName + "']",
                                    packageDocument.getDocumentElement(), XPathConstants.NODESET));
                    if(runtimeEnvironment != null ) {
                        Element runtimeElement = (Element) (runtimeEnvironment);
                        NodeList serverNameNode = runtimeElement.getElementsByTagName("name");
                        NodeList deploymentIDNode = runtimeElement.getElementsByTagName("deploymentID");
                        NodeList latestChecksum = runtimeElement.getElementsByTagName("latestChecksum");
                        NodeList checkSum = runtimeElement.getElementsByTagName("checksum");
                        NodeList status = runtimeElement.getElementsByTagName("status");
                        NodeList lastupdatedUsername = runtimeElement.getElementsByTagName("username");
                        NodeList lastUpdatedTime = runtimeElement.getElementsByTagName("lastUpdatedTime");

                        if (deploymentIDNode != null && deploymentIDNode.getLength() > 0) {
                            deploymentID = deploymentIDNode.item(0).getTextContent();
                            serverName = serverNameNode.item(0).getTextContent();
                            runtimeDeployment.put("deploymentID", deploymentID);
                            if (serverNameNode != null && serverNameNode.getLength() > 0) {
                                runtimeDeployment.put("name", serverName);
                            }
                            if (status != null && status.getLength() > 0) {
                                runtimeDeployment.put("status", status.item(0));
                            }
                        } else if (serverNameNode != null && serverNameNode.getLength() > 0) {
                            serverName = serverNameNode.item(0).getTextContent();
                            if (status != null && status.getLength() > 0) {
                                if (status.item(0).equals("UPLOADED")) {
                                    String tempDeploymentID = processCenter.getEnvironmentsRepository()
                                            .getDeploymentID
                                                    (serverName, deploymentName);
                                    if (deploymentID != null) {
                                        String deployedChecksum = processCenter.getEnvironmentsRepository()
                                                .getLatestDeploymentChecksum(serverName,
                                                        deploymentName);
                                        if (deployedChecksum != null) {
                                            if (checkSum != null && checkSum.getLength() > 0 && checkSum.item(0)
                                                    .getTextContent().equals(deployedChecksum)) {
                                                //update deployment id
                                                appendText(packageDocument, runtimeElement, "deploymentID",
                                                        ProcessCenterConstants
                                                                .METADATA_NAMESPACE, deploymentID);
                                                status.item(0).setTextContent("DEPLOYED");
                                                String newProcessContent = xmlToString(packageDocument);
                                                packageAsset.setContent(newProcessContent);
                                                userRegistry.put(packageRegistryPath, packageAsset);
                                                deploymentID = tempDeploymentID;
                                                runtimeDeployment.put("deploymentID", deploymentID);
                                            } else if (latestChecksum != null && latestChecksum.getLength() > 0 &&
                                                    !latestChecksum
                                                            .item(0)
                                                            .getTextContent().equals(deployedChecksum)) {
                                                response.put(ProcessCenterConstants.ERROR, true);
                                                response.put(ProcessCenterConstants.MESSAGE, "Package has been " +
                                                        "replaced by another " +
                                                        "deployment. Please redeploy the package");
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (lastupdatedUsername != null && lastupdatedUsername.getLength() > 0) {
                            runtimeDeployment.put("name", lastupdatedUsername.item(0));
                        }
                        if (lastUpdatedTime != null && lastUpdatedTime.getLength() > 0) {
                            runtimeDeployment.put("name", lastUpdatedTime.item(0));
                        }
                        runtimeEnvironments.put(runtimeDeployment);
                    }
                    if (deploymentID != null & serverName != null) {

                        // Get ProcessList
                        processCenter.getEnvironmentsRepository().getProcessListByDeploymentID(deploymentID,
                                serverName);
                    }
                    response.put("RuntimeEnvironments", processCenter.getEnvironmentsRepository()
                            .getProcessServerMap().keySet());
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting deployment ID for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException | XPathExpressionException | IOException | SAXException | TransformerException
                | ParserConfigurationException
                e) {
            String errMsg = "Error occurred while getting deployment ID for package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }


    public String associateDeploymentID(String packageName, String packageVersion, String serverName, String userName)
            throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        JSONObject runtimeDeployment = new JSONObject();
        String deploymentID = null;
        ProcessCenter processCenter = ProcessCenterServerHolder.getInstance().getProcessCenter();
        String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
        try {
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
                    //Evaluate XPath against Document itself
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList runtimeEnvironment = ((NodeList) xPath.
                            evaluate("/metadata/runtimeEnvironment/name[text()='" + serverName + "']",
                                    packageDocument.getDocumentElement(), XPathConstants.NODESET));
                    Element runtimeElement = (Element) (runtimeEnvironment.item(0));
                    NodeList serverNameNode = runtimeElement.getElementsByTagName("name");
                    NodeList deploymentIDNode = runtimeElement.getElementsByTagName("deploymentID");
                    NodeList latestChecksum = runtimeElement.getElementsByTagName("latestChecksum");
                    NodeList checkSum = runtimeElement.getElementsByTagName("checksum");
                    NodeList status = runtimeElement.getElementsByTagName("status");
                    NodeList lastupdatedUsername = runtimeElement.getElementsByTagName("username");
                    NodeList lastUpdatedTime = runtimeElement.getElementsByTagName("lastUpdatedTime");

                    if (deploymentIDNode != null && deploymentIDNode.getLength() > 0) {
                        deploymentID = deploymentIDNode.item(0).getTextContent();
                        serverName = serverNameNode.item(0).getTextContent();
                        runtimeDeployment.put("deploymentID", deploymentID);
                        if (serverNameNode != null && serverNameNode.getLength() > 0) {
                            runtimeDeployment.put("name", serverName);
                        }
                        if (status != null && status.getLength() > 0) {
                            runtimeDeployment.put("status", status.item(0));
                        }
                    } else if (serverNameNode != null && serverNameNode.getLength() > 0) {
                        serverName = serverNameNode.item(0).getTextContent();
                        if (status != null && status.getLength() > 0) {
                            if (status.item(0).equals("UPLOADED")) {
                                String tempDeploymentID = processCenter.getEnvironmentsRepository()
                                        .getDeploymentID
                                                (serverName, deploymentName);
                                if (deploymentID != null) {
                                    String deployedChecksum = processCenter.getEnvironmentsRepository()
                                            .getLatestDeploymentChecksum(serverName,
                                                    deploymentName);
                                    if (deployedChecksum != null) {
                                        if (checkSum != null && checkSum.getLength() > 0 && checkSum.item(0)
                                                .getTextContent().equals(deployedChecksum)) {
                                            //update deployment id
                                            appendText(packageDocument, runtimeElement, "deploymentID",
                                                    ProcessCenterConstants
                                                            .METADATA_NAMESPACE, deploymentID);
                                            status.item(0).setTextContent("DEPLOYED");
                                            String newProcessContent = xmlToString(packageDocument);
                                            packageAsset.setContent(newProcessContent);
                                            userRegistry.put(packageRegistryPath, packageAsset);
                                            associateRuntimeProcesses(userRegistry,packageAsset,serverName,
                                                    packageName,packageVersion,deploymentID);
                                            deploymentID = tempDeploymentID;
                                            runtimeDeployment.put("deploymentID", deploymentID);
                                        } else if (latestChecksum != null && latestChecksum.getLength() > 0 &&
                                                !latestChecksum.item(0)
                                                        .getTextContent().equals(deployedChecksum)) {
                                            response.put(ProcessCenterConstants.ERROR, true);
                                            response.put(ProcessCenterConstants.MESSAGE, "Package has been " +
                                                    "replaced by another deployment. Please redeploy the package");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (lastupdatedUsername != null && lastupdatedUsername.getLength() > 0) {
                        runtimeDeployment.put("name", lastupdatedUsername.item(0));
                    }
                    if (lastUpdatedTime != null && lastUpdatedTime.getLength() > 0) {
                        runtimeDeployment.put("name", lastUpdatedTime.item(0));
                    }

                    response.put("RuntimeEnvironments", processCenter.getEnvironmentsRepository()
                            .getProcessServerMap().keySet());
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting deployment ID for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException | XPathExpressionException | IOException | SAXException | TransformerException
                | ParserConfigurationException
                e) {
            String errMsg = "Error occurred while getting deployment ID for package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }

    /**
     * Check whether the given xml is BPMN resource
     *
     * @param resourceName
     * @return
     */
    protected boolean isBpmnResource(String resourceName) {
        for (String suffix : ProcessCenterConstants.BPMN_RESOURCE_SUFFIXES) {
            if (resourceName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    private JSONArray associateRuntimeProcesses(UserRegistry userRegistry, Resource packageAsset, String serverName, String
            packageName,String packageVersion, String deploymentID) throws ProcessCenterException {
        JSONArray bpmnResources = new JSONArray();
        String packageBPMNContentRegistryPath = Package.getPackageBPMNContentRegistryPath
                (Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath(packageName, packageVersion)));
        try {

            BPMNProcess[] processListByDeploymentID = ProcessCenterServerHolder.getInstance().getProcessCenter()
                    .getEnvironmentsRepository()
                    .getProcessListByDeploymentID(serverName, deploymentID);

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
                                        bpmnResource.put("processId",bpmnProcess.getProcessId());
                                        break;
                                    }
                                }
                                bpmnResource.put(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME,
                                        bpmnRegistryResource.getPath()
                                                .replaceFirst
                                                        ("/" + packageBPMNContentRegistryPath, ""));
                                bpmnResources.put(bpmnResource);
                                bpmnResources.put(bpmnResource);
                            }
                        }
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Error occurred while getting deployment ID for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bpmnResources;

    }
}
