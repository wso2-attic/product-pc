/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.pc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.pc.core.assets.resources.BPMN;
import org.wso2.carbon.pc.core.assets.resources.FlowChart;
import org.wso2.carbon.pc.core.assets.resources.ProcessDocument;
import org.wso2.carbon.pc.core.assets.resources.ProcessText;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.audit.util.RegPermissionUtil;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.LogEntry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Tag;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.resource.services.utils.AddRolePermissionUtil;
import org.wso2.carbon.user.core.UserRealm;
import sun.misc.BASE64Decoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Arrays;
import java.util.List;

/**
 * Perform the process related common operations
 */
public class ProcessStore {

    private static final Log log = LogFactory.getLog(ProcessStore.class);

    public String createProcess(String processDetails, String userName, String processCreatedTime)
            throws ProcessCenterException {

        JSONObject response = new JSONObject();
        String processId = null;
        try {
            JSONObject processInfo = new JSONObject(processDetails);
            String processName = processInfo.getString("processName");
            String processVersion = processInfo.getString("processVersion");
            String processOwner = processInfo.getString("processOwner");
            String processUser = processInfo.getString("processUser");
            String processUserEmail = processInfo.getString("processUserEmail");
            String processDescription = processInfo.getString("processDescription");
            String processTags = processInfo.getString("processTags");
            JSONArray subprocess = processInfo.getJSONArray("subprocess");
            JSONArray successor = processInfo.getJSONArray("successor");
            JSONArray predecessor = processInfo.getJSONArray("predecessor");
            JSONObject imageObj = processInfo.getJSONObject("image");

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(userName);
                String processAssetPath = "processes/" + processName + "/" + processVersion;
                // Check whether process already exists with same name and version
                if (reg.resourceExists(processAssetPath)) {
                    response.put(ProcessCenterConstants.ERROR, true);
                    response.put(ProcessCenterConstants.MESSAGE, "Process already exists with name " + processName +
                            " version:" + processVersion);
                    return response.toString();
                }
                RegPermissionUtil
                        .setPutPermission(registryService, userName, ProcessCenterConstants.AUDIT.PROCESS_PATH);

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                String mns = "http://www.wso2.org/governance/metadata";
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(mns, "metadata");
                doc.appendChild(rootElement);

                Element overviewElement = Utils.append(doc, rootElement, "overview", mns);
                Utils.appendText(doc, overviewElement, "name", mns, processName);
                Utils.appendText(doc, overviewElement, "version", mns, processVersion);
                Utils.appendText(doc, overviewElement, "owner", mns, processOwner);
                Utils.appendText(doc, overviewElement, "businessuser", mns, processUser);
                Utils.appendText(doc, overviewElement, "businessuseremail", mns, processUserEmail);
                Utils.appendText(doc, overviewElement, "createdtime", mns, processCreatedTime);

                if ((processDescription != null) && (!processDescription.isEmpty())) {
                    Utils.appendText(doc, overviewElement, "description", mns, processDescription);
                } else {
                    Utils.appendText(doc, overviewElement, "description", mns, "NA");
                }

                Element propertiesElement = Utils.append(doc, rootElement, "properties", mns);
                Utils.appendText(doc, propertiesElement, "processtextpath", mns, "NA");

                // fill bpmn properties with NA values
                Utils.appendText(doc, propertiesElement, "bpmnpath", mns, "NA");
                Utils.appendText(doc, propertiesElement, "bpmnid", mns, "NA");

                if (subprocess.length() != 0) {

                    for (int i = 0; i < subprocess.length(); i++) {
                        reg.addAssociation("/processes/" + processName + "/" + processVersion, "/processes/" +
                                subprocess.getJSONObject(i).getString("name") + "/" +
                                subprocess.getJSONObject(i).getString("version"),
                                ProcessCenterConstants.SUBPROCESS_ASSOCIATION);
                        reg.addAssociation("/processes/" + subprocess.getJSONObject(i).getString("name") + "/" +
                                subprocess.getJSONObject(i).getString("version"), "/processes/" + processName + "/" +
                                processVersion, ProcessCenterConstants.PARENTPROCESS_ASSOCIATION);

                    }
                }

                if (successor.length() != 0) {
                    for (int i = 0; i < successor.length(); i++) {
                        reg.addAssociation("/processes/" + processName + "/" + processVersion, "/processes/" +
                                successor.getJSONObject(i).getString("name") + "/" +
                                successor.getJSONObject(i).getString("version"),
                                ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
                        reg.addAssociation("/processes/" + successor.getJSONObject(i).getString("name") + "/" +
                                successor.getJSONObject(i).getString("version"), "/processes/" +
                                processName + "/" + processVersion, ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                    }
                }

                if (predecessor.length() != 0) {
                    for (int i = 0; i < predecessor.length(); i++) {
                        reg.addAssociation("/processes/" + processName + "/" + processVersion, "/processes/" +
                                predecessor.getJSONObject(i).getString("name") + "/" +
                                predecessor.getJSONObject(i).getString("version"),
                                ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                        reg.addAssociation("/processes/" + predecessor.getJSONObject(i).getString("name") + "/" +
                                predecessor.getJSONObject(i).getString("version"), "/processes/" + processName + "/" +
                                processVersion, ProcessCenterConstants.SUCCESSOR_ASSOCIATION);

                    }
                }

                Element flowchartElement = Utils.append(doc, rootElement, "flowchart", mns);
                Utils.appendText(doc, flowchartElement, "path", mns, "NA");

                if (imageObj.length() != 0) {
                    Element imageElement = Utils.append(doc, rootElement, "images", mns);
                    Utils.appendText(doc, imageElement, "thumbnail", mns, imageObj.getString("imgValue"));
                }

                String processAssetContent = Utils.xmlToString(doc);
                Resource processAsset = reg.newResource();
                processAsset.setContent(processAssetContent);
                processAsset.setMediaType("application/vnd.wso2-process+xml");
                reg.put(processAssetPath, processAsset);

                // associate lifecycle with the process asset, so that it can be promoted to published state
                GovernanceUtils.associateAspect(processAssetPath, "DefaultProcessLifeCycle", reg);

                // apply tags to the resource
                String[] tags = processTags.split(",");
                for (String tag : tags) {
                    tag = tag.trim();
                    reg.applyTag(processAssetPath, tag);
                }
                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();

                if (imageObj.length() != 0) {
                    String imageRegPath = ProcessCenterConstants.IMAGE_PATH + processId + "/" +
                            imageObj.getString("imgValue");
                    Resource imageContentResource = reg.newResource();
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] imageContent = decoder.decodeBuffer(imageObj.getString("binaryImg"));
                    imageContentResource.setContent(imageContent);
                    reg.put(imageRegPath, imageContentResource);
                }

                setPermission(userName, processName, processVersion);

            }
            response.put(ProcessCenterConstants.ERROR, false);
            response.put(ProcessCenterConstants.ID, processId);
            response.put(ProcessCenterConstants.MESSAGE, "Process has been created successfully");
        } catch (Exception e) {
            String errMsg = "Create process error:" + processDetails;
            log.error("Create process error:" + processDetails, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return response.toString();
    }

    public String updateProcess(String processDetails, String userName) throws ProcessCenterException {
        String processId = "FAILED TO UPDATE PROCESS";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            JSONObject processInfo = new JSONObject(processDetails);
            String processName = processInfo.getString("processName");
            String processVersion = processInfo.getString("processVersion");
            String processOwner = processInfo.getString("processOwner");
            String processUser = processInfo.getString("processUser");
            String processUserEmail = processInfo.getString("processUserEmail");
            String processDescription = processInfo.getString("processDescription");
            String processTags = processInfo.getString("processTags");
            JSONObject imageObj = processInfo.getJSONObject("image");

            String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                    processVersion;

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(userName);
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                if (doc.getElementsByTagName("description").getLength() != 0)
                    doc.getElementsByTagName("description").item(0).setTextContent(processDescription);

                doc.getElementsByTagName("owner").item(0).setTextContent(processOwner);
                doc.getElementsByTagName("businessuser").item(0).setTextContent(processUser);
                doc.getElementsByTagName("businessuseremail").item(0).setTextContent(processUserEmail);

                if (imageObj.length() != 0) {
                    if (doc.getElementsByTagName("images").getLength() != 0) {
                        doc.getElementsByTagName("thumbnail").item(0).setTextContent(imageObj.getString("imgValue"));
                    } else {
                        Element rootElement = (Element) doc.getElementsByTagName("metadata").item(0);
                        Element imageElement = Utils.append(doc, rootElement, "images", ProcessCenterConstants.MNS);
                        Utils.appendText(doc, imageElement, "thumbnail", ProcessCenterConstants.MNS, imageObj.getString
                                ("imgValue"));
                    }
                }


                List<String> tags = Arrays.asList(processTags.split(","));
                Tag[] curTags = reg.getTags(processAssetPath);

                for (Tag tag : curTags) {
                    reg.removeTag(processAssetPath, tag.getTagName());
                }

                for (String tag : tags) {
                    tag = tag.trim();
                    reg.applyTag(processAssetPath, tag);
                }

                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);

                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();

                if (imageObj.length() != 0) {
                    String imageRegPath = ProcessCenterConstants.IMAGE_PATH + processId + "/" +
                            imageObj.getString("imgValue");
                    Resource imageContentResource = reg.newResource();
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] imageContent = decoder.decodeBuffer(imageObj.getString("binaryImg"));
                    imageContentResource.setContent(imageContent);
                    reg.put(imageRegPath, imageContentResource);
                }

            }

        } catch (Exception e) {
            String errMsg = "Update process error:" + processDetails;
            log.error("Update process error:" + processDetails, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    public String getProcesses() throws ProcessCenterException {

        String processDetails = "{}";

        try {
            JSONArray result = new JSONArray();

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                String[] processPaths = GovernanceUtils
                        .findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = Utils.stringToXML(processContent);
                    String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                    String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();
                    String processUser = processXML.getElementsByTagName("businessuser").item(0).getTextContent();
                    String processUserEmail = processXML.getElementsByTagName("businessuseremail").item(0).getTextContent();
                    String flowchartPath = processXML.getElementsByTagName("flowchart").item(0).getFirstChild()
                            .getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put("path", processPath);
                    processJSON.put("processid", processResource.getUUID());
                    processJSON.put("processname", processName);
                    processJSON.put("processversion", processVersion);
                    processJSON.put("processUser", processUser);
                    processJSON.put("processUserEmail", processUserEmail);
                    processJSON.put("flowchartpath", flowchartPath);
                    result.put(processJSON);
                }

                processDetails = result.toString();

            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String errMsg = "Failed to get all processes";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processDetails;
    }

    public boolean updateOwner(String ownerDetails, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(ownerDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String processOwner = processInfo.getString("value");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                doc.getElementsByTagName("owner").item(0).setTextContent(processOwner);

                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);
                reg.getRegistryContext().getLogWriter()
                        .addLog(ProcessCenterConstants.GREG_PATH + processAssetPath, reg.getUserName(), LogEntry.UPDATE,
                                "OWNER");
            }

        } catch (Exception e) {
            String errMsg = "Failed to update the process owner:" + ownerDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public String updateDescription(String descriptionDetails, String user) throws ProcessCenterException {
        String processId = "NA";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(descriptionDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String processDescription = processInfo.getString("value");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                if (doc.getElementsByTagName("description").getLength() != 0)
                    doc.getElementsByTagName("description").item(0).setTextContent(processDescription);

                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);
                reg.getRegistryContext().getLogWriter()
                        .addLog(ProcessCenterConstants.GREG_PATH + processAssetPath,
                                reg.getUserName(), LogEntry.UPDATE, "DESCRIPTION");
                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();
            }

        } catch (Exception e) {
            String errMsg = "Failed to update the process description:" + descriptionDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    /**
     * Delete the process related artifacts : Documents, BPMN model, Flow chart , Process text
     *
     * @param processName    name of the process
     * @param processVersion version of the process
     */
    public void deleteProcessRelatedArtifacts(String processName, String processVersion, String user)
            throws ProcessCenterException {

        String processResourcePath =
                ProcessCenterConstants.GREG_PATH + ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/"
                        + processVersion;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                //delete Process Documents
                ProcessDocument processDocument = new ProcessDocument();
                String uploadedDocs = processDocument.getUploadedDocumentDetails(processResourcePath);
                JSONArray uploadedDocJSNArray = new JSONArray(uploadedDocs);

                for (int i = 0; i < uploadedDocJSNArray.length(); i++) {
                    JSONObject deleteDocument = new JSONObject();
                    deleteDocument.put("processName", processName);
                    deleteDocument.put("processVersion", processVersion);
                    deleteDocument.put("removeDocument", uploadedDocJSNArray.get(i));
                    processDocument.deleteDocument(deleteDocument.toString(), user);
                }
                processDocument.deleteDocumentResourceCollection(processName, processVersion);

                //delete other associations
                new BPMN().removeBPMNDiagram(processName, processVersion, user);
                new FlowChart().deleteFlowchart(processName, processVersion, user);
                ProcessText processText = new ProcessText();
                processText.deleteProcessText(processName, processVersion);
            }
        } catch (Exception e) {
            String errMsg =
                    "Error in deleting process related aftifacts of the process " + processName + "-" + processVersion;
            throw new ProcessCenterException(errMsg, e);
        }
    }

    public String setPermission(String userName, String processName, String processVersion)
            throws ProcessCenterException {

        String status = "Failed to set permission";

        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceSystemRegistry();
                UserRealm userRealm = userRegistry.getUserRealm();
                String[] roles = userRealm.getUserStoreManager().getRoleListOfUser(userName);

                String path = "/_system/governance/processes/" + processName + "/" +
                        processVersion;

                for (String role : roles) {

                    if (role.equalsIgnoreCase("Internal/everyone") || role.equalsIgnoreCase("Internal/store") ||
                            role.equalsIgnoreCase("Internal/publisher")) {
                        continue;
                    } else {
                        //add read permission
                        AddRolePermissionUtil.addRolePermission(userRegistry, path, role, ProcessCenterConstants.READ,
                                ProcessCenterConstants.ALLOW);
                        //add write permission
                        AddRolePermissionUtil.addRolePermission(userRegistry, path, role, ProcessCenterConstants.WRITE,
                                ProcessCenterConstants.ALLOW);
                        //add authorize permission
                        AddRolePermissionUtil
                                .addRolePermission(userRegistry, path, role, ProcessCenterConstants.AUTHORIZE,
                                        ProcessCenterConstants.ALLOW);
                    }
                }
                status = "Permission set successfully";
            }
        } catch (Exception e) {
            String errMsg = "Failed to update Permission for process- " + processName + ":" + processVersion + " ,for "
                    + "user:" + userName;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return status;
    }
}