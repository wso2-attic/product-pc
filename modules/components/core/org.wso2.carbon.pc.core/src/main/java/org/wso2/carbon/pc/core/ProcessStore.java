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

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaggeryjs.hostobjects.stream.StreamHostObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.Tag;
import org.wso2.carbon.registry.core.exceptions.ResourceNotFoundException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;
import sun.misc.BASE64Decoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ProcessStore {

    private static final Log log = LogFactory.getLog(ProcessStore.class);

    private static final String mns = "http://www.wso2.org/governance/metadata";
    private static final String OK = "OK";

    private Element append(Document doc, Element parent, String childName, String childNS) {
        Element childElement = doc.createElementNS(childNS, childName);
        parent.appendChild(childElement);
        return childElement;
    }

    private Element appendText(Document doc, Element parent, String childName, String childNS, String text) {
        Element childElement = doc.createElementNS(childNS, childName);
        childElement.setTextContent(text);
        parent.appendChild(childElement);
        return childElement;
    }

    private String xmlToString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    private Document stringToXML(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        return document;
    }

    public String createProcess(String processDetails) throws ProcessCenterException {
        String processId = "FAILED TO ADD PROCESS";
        try {
            JSONObject processInfo = new JSONObject(processDetails);
            String processName = processInfo.getString("processName");
            String processVersion = processInfo.getString("processVersion");
            String processOwner = processInfo.getString("processOwner");
            String processDescription = processInfo.getString("processDescription");
            String processTags = processInfo.getString("processTags");
            JSONArray subprocess = processInfo.getJSONArray("subprocess");
            JSONArray successor = processInfo.getJSONArray("successor");
            JSONArray predecessor = processInfo.getJSONArray("predecessor");
            JSONObject imageObj = processInfo.getJSONObject("image");

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                String mns = "http://www.wso2.org/governance/metadata";
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(mns, "metadata");
                doc.appendChild(rootElement);

                Element overviewElement = append(doc, rootElement, "overview", mns);
                appendText(doc, overviewElement, "name", mns, processName);
                appendText(doc, overviewElement, "version", mns, processVersion);
                appendText(doc, overviewElement, "owner", mns, processOwner);

                if ((processDescription != null) && (!processDescription.isEmpty())) {
                    appendText(doc, overviewElement, "description", mns, processDescription);
                }else{
                    appendText(doc, overviewElement, "description", mns, "NA");
                }

                Element propertiesElement = append(doc, rootElement, "properties", mns);
                appendText(doc, propertiesElement, "processtextpath", mns, "NA");

                // fill bpmn properties with NA values
                appendText(doc, propertiesElement, "bpmnpath", mns, "NA");
                appendText(doc, propertiesElement, "bpmnid", mns, "NA");

                if (subprocess.length() != 0) {
                    for (int i = 0; i < subprocess.length(); i++) {
                        Element successorElement = append(doc, rootElement, "subprocess", mns);
                        appendText(doc, successorElement, "name", mns, subprocess.getJSONObject(i).getString("name"));
                        appendText(doc, successorElement, "path", mns, subprocess.getJSONObject(i).getString("path"));
                        appendText(doc, successorElement, "id", mns, subprocess.getJSONObject(i).getString("id"));
                    }
                }

                if (successor.length() != 0) {
                    for (int i = 0; i < successor.length(); i++) {
                        Element successorElement = append(doc, rootElement, "successor", mns);
                        appendText(doc, successorElement, "name", mns, successor.getJSONObject(i).getString("name"));
                        appendText(doc, successorElement, "path", mns, successor.getJSONObject(i).getString("path"));
                        appendText(doc, successorElement, "id", mns, successor.getJSONObject(i).getString("id"));
                    }
                }

                if (predecessor.length() != 0) {
                    for (int i = 0; i < predecessor.length(); i++) {
                        Element predecessorElement = append(doc, rootElement, "predecessor", mns);
                        appendText(doc, predecessorElement, "name", mns,
                                predecessor.getJSONObject(i).getString("name"));
                        appendText(doc, predecessorElement, "path", mns,
                                predecessor.getJSONObject(i).getString("path"));
                        appendText(doc, predecessorElement, "id", mns, predecessor.getJSONObject(i).getString("id"));
                    }
                }

                Element flowchartElement = append(doc, rootElement, "flowchart", mns);
                appendText(doc, flowchartElement, "path", mns, "NA");

                if(imageObj.length() != 0) {
                    Element imageElement = append(doc, rootElement, "images", mns);
                    appendText(doc, imageElement, "thumbnail", mns, imageObj.getString("imgValue"));
                }

                String processAssetContent = xmlToString(doc);
                Resource processAsset = reg.newResource();
                processAsset.setContent(processAssetContent);
                processAsset.setMediaType("application/vnd.wso2-process+xml");
                String processAssetPath = "processes/" + processName + "/" + processVersion;
                reg.put(processAssetPath, processAsset);

                // associate lifecycle with the process asset, so that it can be promoted to published state
                GovernanceUtils.associateAspect(processAssetPath, "SampleLifeCycle2", reg);

                // apply tags to the resource
                String[] tags = processTags.split(",");
                for (String tag : tags) {
                    tag = tag.trim();
                    reg.applyTag(processAssetPath, tag);
                }
                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();

                if(imageObj.length() != 0) {
                    String imageRegPath = ProcessStoreConstants.IMAGE_PATH + processId + "/" +
                                          imageObj.getString("imgValue");
                    Resource imageContentResource = reg.newResource();
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] imageContent = decoder.decodeBuffer(imageObj.getString("binaryImg"));
                    imageContentResource.setContent(imageContent);
                    reg.put(imageRegPath, imageContentResource);
                }
            }
        } catch (Exception e) {
            String errMsg = "Create process error:" + processDetails;
            log.error("Create process error:" + processDetails, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    public boolean saveProcessText(String processName, String processVersion, String processText)
            throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // get process asset content
                String processPath = "processes/" + processName + "/" + processVersion;
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document doc = stringToXML(processContent);

                // store process text as a separate resource
                String processTextResourcePath = "processText/" + processName + "/" + processVersion;
                reg.addAssociation(processTextResourcePath, processPath, ProcessContentSearchConstants.ASSOCIATION_TYPE);

                if (processText != null && processText.length() > 0) {
                    Resource processTextResource = reg.newResource();
                    processTextResource.setContent(processText);
                    processTextResource.setMediaType("text/html");
                    reg.put(processTextResourcePath, processTextResource);
                    doc.getElementsByTagName("processtextpath").item(0).setTextContent(
		                    processTextResourcePath);
                } else {
                    reg.delete(processTextResourcePath);
                    doc.getElementsByTagName("processtextpath").item(0).setTextContent("NA");
                }

                // update process asset
                String newProcessContent = xmlToString(doc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);
            }
        } catch (Exception e) {
            String errMsg = "Save process text error for " + processName + " - " + processVersion + " : " + processText;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public void processBPMN(String version, String type, StreamHostObject s) {

        InputStream barStream = s.getStream();

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                if (type.equals("bpmn_file")) {

                } else {

                    // add each bpmn file as a new asset
                    byte[] barContent = IOUtils.toByteArray(barStream);
                    ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(barContent));
                    ZipEntry entry = null;
                    while ((entry = zip.getNextEntry()) != null) {
                        String entryName = entry.getName();
                        if (entryName.endsWith(".bpmn") || entryName.endsWith(".bpmn20.xml")) {

                            // add bpmn xml as a separate resource
                            StringBuilder sb = new StringBuilder();
                            byte[] buffer = new byte[1024];
                            int read = 0;
                            while ((read = zip.read(buffer, 0, 1024)) >= 0) {
                                sb.append(new String(buffer, 0, read));
                            }
                            String bpmnContent = sb.toString();
                            Resource bpmnResource = reg.newResource();
                            bpmnResource.setContent(bpmnContent);
                            bpmnResource.setMediaType("text/xml");
                            String bpmnResourcePath = "bpmn_xml/" + entryName + "/" + version;
                            reg.put(bpmnResourcePath, bpmnResource);

                            // add bpmn asset
                            String bpmnAssetName = entryName;
                            String displayName = entryName;
                            if (displayName.endsWith(".bpmn")) {
                                displayName = displayName.substring(0, (entryName.length() - 5));
                            }
                            String bpmnAssetPath = "bpmn/" + entryName + "/" + version;
                            Document bpmnDoc = docBuilder.newDocument();
                            Element bpmnRoot = bpmnDoc.createElementNS(mns, "metadata");
                            bpmnDoc.appendChild(bpmnRoot);
                            Element bpmnOverview = append(bpmnDoc, bpmnRoot, "overview", mns);
                            appendText(bpmnDoc, bpmnOverview, "name", mns, displayName);
                            appendText(bpmnDoc, bpmnOverview, "version", mns, version);
                            appendText(bpmnDoc, bpmnOverview, "processpath", mns, "");
                            appendText(bpmnDoc, bpmnOverview, "description", mns, "");
                            Element bpmnAssetContentElement = append(bpmnDoc, bpmnRoot, "content", mns);
                            appendText(bpmnDoc, bpmnAssetContentElement, "contentpath", mns, bpmnResourcePath);
                            String bpmnAssetContent = xmlToString(bpmnDoc);

                            Resource bpmnAsset = reg.newResource();
                            bpmnAsset.setContent(bpmnAssetContent);
                            bpmnAsset.setMediaType("application/vnd.wso2-bpmn+xml");
                            reg.put(bpmnAssetPath, bpmnAsset);
                            Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                            String bpmnAssetID = storedBPMNAsset.getUUID();
                        }
                    }
                }

            } else {
                String msg = "Registry service is not available.";
                throw new ProcessCenterException(msg);
            }

        } catch (Exception e) {
            log.error("Failed to store bar archive: " + version, e);
        }
    }

    public String createBPMN(String bpmnName, String bpmnVersion, Object o)
            throws ProcessCenterException {
        String processId = "FAILED TO CREATE BPMN";
        if (log.isDebugEnabled()) {
            log.debug("Creating BPMN resource:" + bpmnName + " - " + bpmnVersion);
        }
        try {
            StreamHostObject s = (StreamHostObject) o;
            InputStream bpmnStream = s.getStream();
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // store bpmn content as a registry resource
                Resource bpmnContentResource = reg.newResource();
                byte[] bpmnContent = IOUtils.toByteArray(bpmnStream);
                String bpmnText = new String(bpmnContent);
                bpmnContentResource.setContent(bpmnText);
                bpmnContentResource.setMediaType("application/xml");
                String bpmnContentPath = "bpmncontent/" + bpmnName + "/" + bpmnVersion;
                reg.put(bpmnContentPath, bpmnContentResource);

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // add bpmn asset pointing to above bpmn content
                String mns = "http://www.wso2.org/governance/metadata";
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(mns, "metadata");
                doc.appendChild(rootElement);

                Element overviewElement = append(doc, rootElement, "overview", mns);
                appendText(doc, overviewElement, "name", mns, bpmnName);
                appendText(doc, overviewElement, "version", mns, bpmnVersion);
                appendText(doc, overviewElement, "description", mns, "");
                String processPath = "processes/" + bpmnName + "/" + bpmnVersion;
                appendText(doc, overviewElement, "processpath", mns, processPath);

                Element contentElement = append(doc, rootElement, "content", mns);
                appendText(doc, contentElement, "contentpath", mns, bpmnContentPath);

                String bpmnAssetContent = xmlToString(doc);
                Resource bpmnAsset = reg.newResource();
                bpmnAsset.setContent(bpmnAssetContent);
                bpmnAsset.setMediaType("application/vnd.wso2-bpmn+xml");

                String bpmnAssetPath = "bpmn/" + bpmnName + "/" + bpmnVersion;
                reg.put(bpmnAssetPath, bpmnAsset);
                Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                String bpmnAssetID = storedBPMNAsset.getUUID();

                // update process by linking the bpmn asset

                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document pdoc = stringToXML(processContent);
                pdoc.getElementsByTagName("bpmnpath").item(0).setTextContent(bpmnAssetPath);
                pdoc.getElementsByTagName("bpmnid").item(0).setTextContent(bpmnAssetID);
                String newProcessContent = xmlToString(pdoc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            String errMsg = "Create BPMN error:" + bpmnName + " - " + bpmnVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    public String removeBPMNDiagram(String processName, String processVersion)
		    throws ProcessCenterException {
	    String processId = "";
        try {
	        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
	        if (registryService != null) {
		        UserRegistry reg = registryService.getGovernanceSystemRegistry();
		        String bpmnContentPath = ProcessStoreConstants.BPMN_CONTENT_PATH + processName + "/" + processVersion;
		        if (reg.resourceExists(bpmnContentPath)) {
			        reg.delete(bpmnContentPath);
		        }
		        String bpmnAssetPath = ProcessStoreConstants.BPMN_PATH + processName + "/" + processVersion;
		        if (reg.resourceExists(bpmnAssetPath)) {
			        reg.delete(bpmnAssetPath);
		        }
		        String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
		        if(reg.resourceExists(processPath)) {
			        Resource processResource = reg.get(processPath);

			        String processContent = new String((byte[]) processResource.getContent());
			        Document processXML = stringToXML(processContent);
			        processXML.getElementsByTagName("bpmnpath").item(0).setTextContent("NA");
			        processXML.getElementsByTagName("bpmnid").item(0).setTextContent("NA");

			        String newProcessContent = xmlToString(processXML);
			        processResource.setContent(newProcessContent);
			        reg.put(processPath, processResource);

			        Resource storedProcessAsset = reg.get(processPath);
			        processId = storedProcessAsset.getUUID();
		        }
	        }
        } catch (Exception e) {
            String errMsg = "Error has been occurred while removing BPMN diagram in the process:"
                            + processName + "-" + processVersion;
	        log.error(errMsg, e);
	        throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    public void processBAR(String barName, String barVersion, String description, StreamHostObject s) {

        log.debug("Processing BPMN archive " + barName);
        InputStream barStream = s.getStream();
        if (description == null) {
            description = "";
        }

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // add the bar asset
                //                String barAssetContent = "<metadata xmlns='http://www.wso2.org/governance/metadata'>" +
                //                        "<overview><name>" + barName + "</name><version>" + barVersion + "</version><description>This is a test process2</description></overview></metadata>";

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                String mns = "http://www.wso2.org/governance/metadata";
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(mns, "metadata");
                doc.appendChild(rootElement);

                Element overviewElement = append(doc, rootElement, "overview", mns);
                appendText(doc, overviewElement, "name", mns, barName);
                appendText(doc, overviewElement, "version", mns, barVersion);
                appendText(doc, overviewElement, "description", mns, description);

                // add binary data of the bar asset as a separate resource
                Resource barResource = reg.newResource();
                byte[] barContent = IOUtils.toByteArray(barStream);
                barResource.setContent(barContent);
                barResource.setMediaType("application/bar");
                String barResourcePath = "bpmn_archives_binary/" + barName + "/" + barVersion;
                reg.put(barResourcePath, barResource);

                Element contentElement = append(doc, rootElement, "content", mns);
                appendText(doc, contentElement, "contentpath", mns, barResourcePath);

                //                reg.addAssociation(barAssetPath, barResourcePath, "has_content");

                // add each bpmn file as a new asset
                ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(barContent));
                ZipEntry entry = null;
                while ((entry = zip.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.endsWith(".bpmn") || entryName.endsWith(".bpmn20.xml")) {

                        // add bpmn xml as a separate resource
                        StringBuilder sb = new StringBuilder();
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = zip.read(buffer, 0, 1024)) >= 0) {
                            sb.append(new String(buffer, 0, read));
                        }
                        String bpmnContent = sb.toString();
                        Resource bpmnResource = reg.newResource();
                        bpmnResource.setContent(bpmnContent);
                        bpmnResource.setMediaType("text/xml");
                        String bpmnResourcePath = "bpmn_xml/" + barName + "." + entryName + "/" + barVersion;
                        reg.put(bpmnResourcePath, bpmnResource);

                        // add bpmn asset
                        String bpmnAssetName = barName + "." + entryName;
                        String displayName = entryName;
                        if (displayName.endsWith(".bpmn")) {
                            displayName = displayName.substring(0, (entryName.length() - 5));
                        }
                        String bpmnAssetPath = "bpmn/" + barName + "." + entryName + "/" + barVersion;
                        //                        String bpmnAssetContent = "<metadata xmlns='http://www.wso2.org/governance/metadata'>" +
                        //                                "<overview><name>" + bpmnAssetName + "</name><version>" + barVersion + "</version>" +
                        //                                "<description>This is a BPMN asset</description></overview></metadata>";

                        Document bpmnDoc = docBuilder.newDocument();
                        Element bpmnRoot = bpmnDoc.createElementNS(mns, "metadata");
                        bpmnDoc.appendChild(bpmnRoot);
                        Element bpmnOverview = append(bpmnDoc, bpmnRoot, "overview", mns);
                        //                        appendText(bpmnDoc, bpmnOverview, "name", mns, bpmnAssetName);
                        appendText(bpmnDoc, bpmnOverview, "name", mns, displayName);
                        appendText(bpmnDoc, bpmnOverview, "version", mns, barVersion);
                        appendText(bpmnDoc, bpmnOverview, "package", mns, barName);
                        appendText(bpmnDoc, bpmnOverview, "description", mns, "");
                        Element bpmnAssetContentElement = append(bpmnDoc, bpmnRoot, "content", mns);
                        appendText(bpmnDoc, bpmnAssetContentElement, "contentpath", mns, bpmnResourcePath);
                        String bpmnAssetContent = xmlToString(bpmnDoc);

                        Resource bpmnAsset = reg.newResource();
                        bpmnAsset.setContent(bpmnAssetContent);
                        bpmnAsset.setMediaType("application/vnd.wso2-bpmn+xml");
                        reg.put(bpmnAssetPath, bpmnAsset);
                        Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                        String bpmnAssetID = storedBPMNAsset.getUUID();

                        Element bpmnElement = append(doc, rootElement, "bpmn", mns);
                        appendText(doc, bpmnElement, "Name", mns, bpmnAssetName);
                        appendText(doc, bpmnElement, "Path", mns, bpmnAssetPath);
                        appendText(doc, bpmnElement, "Id", mns, bpmnAssetID);

                        //                        reg.addAssociation(barAssetPath, bpmnAssetPath, "contains");
                        //                        reg.addAssociation(bpmnAssetPath, bpmnResourcePath, "has_content");
                    }
                }

                String barAssetContent = xmlToString(doc);
                Resource barAsset = reg.newResource();
                barAsset.setContent(barAssetContent);
                barAsset.setMediaType("application/vnd.wso2-bar+xml");
                String barAssetPath = "bar/admin/" + barName + "/" + barVersion;
                reg.put(barAssetPath, barAsset);

            } else {
                String msg = "Registry service is not available.";
                throw new ProcessCenterException(msg);
            }

        } catch (Exception e) {
            log.error("Failed to store bar archive: " + barName + " - " + barVersion, e);
        }
    }

    public String getBAR(String barPath) {

        String barString = "";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                barPath = barPath.substring("/_system/governance/".length());
                Resource barAsset = reg.get(barPath);
                String barContent = new String((byte[]) barAsset.getContent());

                //                String barContent = "<metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><name>b4444</name><version>1.0.0</version><description>hfrefgygeofyure</description></overview><content><contentPath>bpmn_archives_binary/b4444/1.0.0</contentPath></content><bpmn><Name>test_name</Name><Path>bpmn/b4444.TestProcess1.bpmn/1.0.0</Path><Id>815fe296-9363-4895-b386-23162b78bec4</Id></bpmn></metadata>";

                JSONObject bar = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(barContent)));
                Element overviewElement = (Element) document.getElementsByTagName("overview").item(0);
                String barName = overviewElement.getElementsByTagName("name").item(0).getTextContent();
                String barVersion = overviewElement.getElementsByTagName("version").item(0).getTextContent();
                String barDescription = overviewElement.getElementsByTagName("description").item(0).getTextContent();

                bar.put("name", barName);
                bar.put("version", barVersion);
                bar.put("description", barDescription);

                Element contentElement = (Element) document.getElementsByTagName("content").item(0);
                String contentPath = contentElement.getElementsByTagName("contentpath").item(0).getTextContent();
                bar.put("contentpath", contentPath);

                JSONArray jbpmns = new JSONArray();
                bar.put("bpmnModels", jbpmns);

                NodeList bpmnElements = document.getElementsByTagName("bpmn");
                for (int i = 0; i < bpmnElements.getLength(); i++) {
                    Element bpmnElement = (Element) bpmnElements.item(i);
                    String bpmnId = bpmnElement.getElementsByTagName("Id").item(0).getTextContent();
                    String bpmnName = bpmnElement.getElementsByTagName("Name").item(0).getTextContent();
                    String bpmnPath = bpmnElement.getElementsByTagName("Path").item(0).getTextContent();

                    JSONObject jbpmn = new JSONObject();
                    jbpmn.put("bpmnId", bpmnId);
                    jbpmn.put("bpmnName", bpmnName);
                    jbpmn.put("bpmnPath", bpmnPath);
                    jbpmns.put(jbpmn);
                }

                barString = bar.toString();
            }
        } catch (Exception e) {
            log.error("Failed to fetch BPMN archive: " + barPath);
        }

        return barString;
    }

    public String getProcessText(String textPath) throws ProcessCenterException {
        String textContent = "FAILED TO GET TEXT CONTENT";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource textResource = reg.get(textPath);
                textContent = new String((byte[]) textResource.getContent());
            }
        } catch (Exception e) {
            String errMsg = "Process text retrieving error: " + textPath;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return textContent;
    }

    public String getBPMN(String bpmnPath) throws ProcessCenterException {

        String bpmnString = "";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                bpmnPath = bpmnPath.substring("/_system/governance/".length());
                Resource bpmnAsset = reg.get(bpmnPath);
                String bpmnContent = new String((byte[]) bpmnAsset.getContent());
                JSONObject bpmn = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(bpmnContent)));
                Element overviewElement = (Element) document.getElementsByTagName("overview").item(0);
                String bpmnName = overviewElement.getElementsByTagName("name").item(0).getTextContent();
                String bpmnVersion = overviewElement.getElementsByTagName("version").item(0).getTextContent();
                String bpmnDescription = overviewElement.getElementsByTagName("description").item(0).getTextContent();
                String processPath = overviewElement.getElementsByTagName("processpath").item(0).getTextContent();

                bpmn.put("name", bpmnName);
                bpmn.put("version", bpmnVersion);
                bpmn.put("description", bpmnDescription);
                bpmn.put("processPath", processPath);

                Element contentElement = (Element) document.getElementsByTagName("content").item(0);
                String contentPath = contentElement.getElementsByTagName("contentpath").item(0).getTextContent();
                bpmn.put("contentpath", contentPath);
                String encodedBPMNImage = getEncodedBPMNImage(contentPath);
                bpmn.put("bpmnImage", encodedBPMNImage);

                bpmnString = bpmn.toString();
            }
        } catch (Exception e) {
            String errMsg = "Failed to fetch BPMN model: " + bpmnPath;
            log.error("Failed to fetch BPMN model: " + bpmnPath);
            throw new ProcessCenterException(errMsg, e);
        }

        return bpmnString;
    }

    public String getEncodedBPMNImage(String path) throws ProcessCenterException {
        byte[] encoded = Base64.encodeBase64(getBPMNImage(path));
        String imageString = new String(encoded);
        return imageString;
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
                    Document processXML = stringToXML(processContent);
                    String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                    String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();
                    String flowchartPath = processXML.getElementsByTagName("flowchart").item(0).getFirstChild()
                            .getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put("path", processPath);
                    processJSON.put("processid", processResource.getUUID());
                    processJSON.put("processname", processName);
                    processJSON.put("processversion", processVersion);
                    processJSON.put("flowchartpath", flowchartPath);
                    result.put(processJSON);
                }

                processDetails = result.toString();

                int a = 1;

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

    public byte[] getBPMNImage(String path) throws ProcessCenterException {

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource bpmnXMLResource = reg.get(path);
                byte[] bpmnContent = (byte[]) bpmnXMLResource.getContent();
                InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);

                BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
                BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, false, false);

                ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
                InputStream imageStream = generator.generatePngDiagram(bpmnModel);

                byte[] imageContent = IOUtils.toByteArray(imageStream);
                return imageContent;
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

    public byte[] getBPMNImage2(String path) throws ProcessCenterException {

        try {
            byte[] bpmnContent = FileUtils.readFileToByteArray(new File(path));
            InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);

            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, false, false);

            ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generatePngDiagram(bpmnModel);

            byte[] imageContent = IOUtils.toByteArray(imageStream);
            return imageContent;
        } catch (Exception e) {
            String msg = "Failed to fetch BPMN model: " + path;
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
    }

    public String getSucessorPredecessorSubprocessList(String resourcePath) {
        String resourceString = "";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());

                JSONObject conObj = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(resourceContent)));

                JSONArray subprocessArray = new JSONArray();
                JSONArray successorArray = new JSONArray();
                JSONArray predecessorArray = new JSONArray();

                conObj.put("subprocesses", subprocessArray);
                conObj.put("successors", successorArray);
                conObj.put("predecessors", predecessorArray);

                NodeList subprocessElements = ((Element) document.getFirstChild()).getElementsByTagName("subprocess");
                NodeList successorElements = ((Element) document.getFirstChild()).getElementsByTagName("successor");
                NodeList predecessorElements = ((Element) document.getFirstChild()).getElementsByTagName("predecessor");

                if (subprocessElements.getLength() != 0) {
                    for (int i = 0; i < subprocessElements.getLength(); i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName = subprocessElement.getElementsByTagName("name").item(0).getTextContent();
                        String subprocessPath = subprocessElement.getElementsByTagName("path").item(0).getTextContent();
                        String subprocessId = subprocessElement.getElementsByTagName("id").item(0).getTextContent();
                        String subprocessVersion = subprocessPath.substring(subprocessPath.lastIndexOf("/") + 1).trim();

                        JSONObject subprocess = new JSONObject();
                        subprocess.put("name", subprocessName);
                        subprocess.put("path", subprocessPath);
                        subprocess.put("id", subprocessId);
                        subprocess.put("version", subprocessVersion);
                        subprocessArray.put(subprocess);
                    }
                }

                if (successorElements.getLength() != 0) {
                    for (int i = 0; i < successorElements.getLength(); i++) {
                        Element successorElement = (Element) successorElements.item(i);
                        String successorName = successorElement.getElementsByTagName("name").item(0).getTextContent();
                        String successorPath = successorElement.getElementsByTagName("path").item(0).getTextContent();
                        String successorId = successorElement.getElementsByTagName("id").item(0).getTextContent();
                        String successorVersion = successorPath.substring(successorPath.lastIndexOf("/") + 1).trim();

                        JSONObject successor = new JSONObject();
                        successor.put("name", successorName);
                        successor.put("path", successorPath);
                        successor.put("id", successorId);
                        successor.put("version", successorVersion);
                        successorArray.put(successor);
                    }
                }

                if (predecessorElements.getLength() != 0) {
                    for (int i = 0; i < predecessorElements.getLength(); i++) {
                        Element predecessorElement = (Element) predecessorElements.item(i);
                        String predecessorName = predecessorElement.getElementsByTagName("name").item(0)
                                .getTextContent();
                        String predecessorPath = predecessorElement.getElementsByTagName("path").item(0)
                                .getTextContent();
                        String predecessorId = predecessorElement.getElementsByTagName("id").item(0).getTextContent();
                        String predecessorVersion = predecessorPath.substring(predecessorPath.lastIndexOf("/") + 1)
                                .trim();

                        JSONObject predecessor = new JSONObject();
                        predecessor.put("name", predecessorName);
                        predecessor.put("path", predecessorPath);
                        predecessor.put("id", predecessorId);
                        predecessor.put("version", predecessorVersion);
                        predecessorArray.put(predecessor);
                    }
                }
                resourceString = conObj.toString();
            }
        } catch (Exception e) {
            log.error("Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        }
        return resourceString;
    }

    public boolean updateOwner(String ownerDetails) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(ownerDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String processOwner = processInfo.getString("value");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                doc.getElementsByTagName("owner").item(0).setTextContent(processOwner);

                String newProcessContent = xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);
            }

        } catch (Exception e) {
            String errMsg = "Failed to update the process owner:" + ownerDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean addSubprocess(String subprocessDetails) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(subprocessDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject subprocess = processInfo.getJSONObject("subprocess");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (subprocess != null) {
                    Element rootElement = doc.getDocumentElement();
                    Element subprocessElement = append(doc, rootElement, "subprocess", mns);
                    appendText(doc, subprocessElement, "name", mns, subprocess.getString("name"));
                    appendText(doc, subprocessElement, "path", mns, subprocess.getString("path"));
                    appendText(doc, subprocessElement, "id", mns, subprocess.getString("id"));

                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to add subprocess: " + subprocessDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean addSuccessor(String successorDetails) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(successorDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject successor = processInfo.getJSONObject("successor");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (successor != null) {
                    Element rootElement = doc.getDocumentElement();
                    Element successorElement = append(doc, rootElement, "successor", mns);
                    appendText(doc, successorElement, "name", mns, successor.getString("name"));
                    appendText(doc, successorElement, "path", mns, successor.getString("path"));
                    appendText(doc, successorElement, "id", mns, successor.getString("id"));

                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to add successor: " + successorDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean addPredecessor(String predecessorDetails) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(predecessorDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject predecessor = processInfo.getJSONObject("predecessor");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (predecessor != null) {
                    Element rootElement = doc.getDocumentElement();
                    Element predecessorElement = append(doc, rootElement, "predecessor", mns);
                    appendText(doc, predecessorElement, "name", mns, predecessor.getString("name"));
                    appendText(doc, predecessorElement, "path", mns, predecessor.getString("path"));
                    appendText(doc, predecessorElement, "id", mns, predecessor.getString("id"));

                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to add predecessor: " + predecessorDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean deleteSubprocess(String deleteSubprocess) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deleteSubprocess);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject subprocess = processInfo.getJSONObject("deleteSubprocess");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (subprocess != null) {
                    NodeList subprocessElements = ((Element) doc.getFirstChild()).getElementsByTagName("subprocess");
                    for (int i = 0; i < subprocessElements.getLength(); i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName = subprocessElement.getElementsByTagName("name").item(0).getTextContent();
                        String subprocessPath = subprocessElement.getElementsByTagName("path").item(0).getTextContent();
                        String subprocessId = subprocessElement.getElementsByTagName("id").item(0).getTextContent();

                        if (subprocessName.equals(subprocess.getString("name")) &&
                                subprocessPath.equals(subprocess.getString("path")) &&
                                subprocessId.equals(subprocess.getString("id"))) {
                            subprocessElement.getParentNode().removeChild(subprocessElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete subprocess: " + deleteSubprocess;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean deleteSuccessor(String deleteSuccessor) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deleteSuccessor);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject successor = processInfo.getJSONObject("deleteSuccessor");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (successor != null) {
                    NodeList successorElements = ((Element) doc.getFirstChild()).getElementsByTagName("successor");
                    for (int i = 0; i < successorElements.getLength(); i++) {
                        Element successorElement = (Element) successorElements.item(i);
                        String successorName = successorElement.getElementsByTagName("name").item(0).getTextContent();
                        String successorPath = successorElement.getElementsByTagName("path").item(0).getTextContent();
                        String successorId = successorElement.getElementsByTagName("id").item(0).getTextContent();

                        if (successorName.equals(successor.getString("name")) &&
                                successorPath.equals(successor.getString("path")) &&
                                successorId.equals(successor.getString("id"))) {
                            successorElement.getParentNode().removeChild(successorElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete successor: " + deleteSuccessor;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean deletePredecessor(String deletePredecessor) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deletePredecessor);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject predecessor = processInfo.getJSONObject("deletePredecessor");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (predecessor != null) {
                    NodeList predecessorElements = ((Element) doc.getFirstChild()).getElementsByTagName("predecessor");
                    for (int i = 0; i < predecessorElements.getLength(); i++) {
                        Element predecessorElement = (Element) predecessorElements.item(i);
                        String predecessorName = predecessorElement.getElementsByTagName("name").item(0)
                                .getTextContent();
                        String predecessorPath = predecessorElement.getElementsByTagName("path").item(0)
                                .getTextContent();
                        String predecessorId = predecessorElement.getElementsByTagName("id").item(0).getTextContent();

                        if (predecessorName.equals(predecessor.getString("name")) &&
                                predecessorPath.equals(predecessor.getString("path")) &&
                                predecessorId.equals(predecessor.getString("id"))) {
                            predecessorElement.getParentNode().removeChild(predecessorElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete predecessor: " + deletePredecessor;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    /**
     * Check the documents availability for a given process
     *
     * @param resourcePath holds the process path
     * @return true if documents are available
     */
    public boolean isDocumentAvailable(String resourcePath) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(resourceContent)));

                NodeList documentElements = ((Element) document.getFirstChild()).getElementsByTagName("document");
                if (documentElements.getLength() != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("No documents available in the path: " + resourcePath, e);
        }
        return false;
    }

    /**
     * Upload a document
     *
     * @param processName    process name
     * @param processVersion process version
     * @param docName        document name
     * @param docSummary     summary of the document
     * @param docUrl         google document url
     * @param docObject      document stream object
     * @param docExtension   document extension
     * @return process id
     */
    public String uploadDocument(String processName, String processVersion, String docName, String docSummary,
            String docUrl, Object docObject, String docExtension) throws ProcessCenterException {
        String processId = "FAILED TO UPLOAD DOCUMENT";
        try {
            StreamHostObject s = (StreamHostObject) docObject;
            InputStream docStream = s.getStream();
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // store doc content as a registry resource
                Resource docContentResource = reg.newResource();
                byte[] docContent = IOUtils.toByteArray(docStream);
                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                          processVersion;
                String docContentPath = null;
                if (docContent.length != 0) {
                    docContentResource.setContent(docContent);
                    if(docExtension.equalsIgnoreCase("pdf")){
                        docContentResource.setMediaType("application/pdf");
                    }
                    else{
                        docContentResource.setMediaType("application/msword");
                    }
                    docContentPath = "doccontent/" + processName + "/" + processVersion + "/" + docName +
                            "." + docExtension;
                    reg.put(docContentPath, docContentResource);
                    reg.addAssociation(docContentPath, processAssetPath,
                                       ProcessContentSearchConstants.ASSOCIATION_TYPE);
                }

                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                Element rootElement = doc.getDocumentElement();
                Element docElement = append(doc, rootElement, "document", mns);
                appendText(doc, docElement, "name", mns, docName);
                appendText(doc, docElement, "summary", mns, docSummary);
                if ((docUrl != null) && (!docUrl.isEmpty())) {
                    appendText(doc, docElement, "url", mns, docUrl);
                } else {
                    appendText(doc, docElement, "url", mns, "NA");
                }
                if (docContentPath != null) {
                    appendText(doc, docElement, "path", mns, docContentPath);
                } else {
                    appendText(doc, docElement, "path", mns, "NA");
                }

                String newProcessContent = xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);

                Resource storedProcessAsset = reg.get(processAssetPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            String errMsg =
                    docName + "." + docExtension + " document upload error for " + processName + ":" + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    /**
     * Get a document which is already uploaded
     *
     * @param resourcePath holds the process path
     * @return document information
     */
    public String getUploadedDocumentDetails(String resourcePath) throws ProcessCenterException {
        String documentString = "NA";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(resourceContent)));

                JSONArray documentArray = new JSONArray();
                NodeList documentElements = ((Element) document.getFirstChild()).getElementsByTagName("document");

                if (documentElements.getLength() != 0) {
                    for (int i = 0; i < documentElements.getLength(); i++) {
                        Element documentElement = (Element) documentElements.item(i);
                        String docName = documentElement.getElementsByTagName("name").item(0).getTextContent();
                        String docSummary = documentElement.getElementsByTagName("summary").item(0).getTextContent();
                        String docUrl = documentElement.getElementsByTagName("url").item(0).getTextContent();
                        String docPath = documentElement.getElementsByTagName("path").item(0).getTextContent();

                        JSONObject processDoc = new JSONObject();
                        processDoc.put("name", docName);
                        processDoc.put("summary", docSummary);
                        processDoc.put("url", docUrl);
                        processDoc.put("path", docPath);
                        documentArray.put(processDoc);
                    }
                }
                documentString = documentArray.toString();
            }
        } catch (Exception e) {
            String errMsg = "Failed to fetch document: " + resourcePath;
            log.error(errMsg);
            throw new ProcessCenterException(errMsg, e);
        }
        return documentString;
    }

    /**
     * Download a document
     *
     * @param resourcePath holds the process path
     * @return document content as  a String
     */
    public String downloadDocument(String resourcePath) throws ProcessCenterException {
        String docString = "FAILED TO GET Document";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource docAsset = reg.get(resourcePath);
                byte[] docContent = (byte[]) docAsset.getContent();
                docString = new sun.misc.BASE64Encoder().encode(docContent);
                if (log.isDebugEnabled()) {
                    log.debug("Document Path:" + resourcePath);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to download document: " + resourcePath;
            log.error(errMsg);
            throw new ProcessCenterException(errMsg, e);
        }
        return docString;
    }

    /**
     * delete a given document
     *
     * @param deleteDocument holds the information of the document that need to be deleted
     * @return true after successful deletion
     */
    public boolean deleteDocument(String deleteDocument) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject documentInfo = new JSONObject(deleteDocument);
                String processName = documentInfo.getString("processName");
                String processVersion = documentInfo.getString("processVersion");
                JSONObject removeDocument = documentInfo.getJSONObject("removeDocument");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (removeDocument != null) {
                    NodeList documentElements = ((Element) doc.getFirstChild()).getElementsByTagName("document");
                    for (int i = 0; i < documentElements.getLength(); i++) {
                        Element documentElement = (Element) documentElements.item(i);
                        String documentName = documentElement.getElementsByTagName("name").item(0).getTextContent();
                        String documentSummary = documentElement.getElementsByTagName("summary").item(0)
                                .getTextContent();
                        String documentUrl = documentElement.getElementsByTagName("url").item(0).getTextContent();
                        String documentPath = documentElement.getElementsByTagName("path").item(0).getTextContent();

                        if (documentName.equals(removeDocument.getString("name")) &&
                                documentSummary.equals(removeDocument.getString("summary")) &&
                                documentPath.equals(removeDocument.getString("path")) &&
                                documentUrl.equals(removeDocument.getString("url"))) {
                            documentElement.getParentNode().removeChild(documentElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);

                    String docContentResourcePath = removeDocument.getString("path");
                    if (!docContentResourcePath.equals("NA")) {
                        if (reg.resourceExists(docContentResourcePath)) {
                            reg.delete(docContentResourcePath);
                        }
                    }
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete a document: " + deleteDocument;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public String getProcessTags() throws ProcessCenterException {

        String textContent = "FAILED TO GET PROCESS TAGS";

        try {
            JSONObject tagsObj = new JSONObject();

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                String[] processPaths = GovernanceUtils
                        .findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);

                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                    String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put("path", processPath);
                    processJSON.put("processid", processResource.getUUID());
                    processJSON.put("processname", processName);
                    processJSON.put("processversion", processVersion);
                    Tag[] tags = reg.getTags(processPath);

                    for (Tag tag : tags) {

                        Iterator<String> keys = tagsObj.keys();

                        if (!keys.hasNext() || keys == null) {
                            JSONArray newTagArray = new JSONArray();
                            newTagArray.put(processJSON);
                            tagsObj.put(tag.getTagName(), newTagArray);
                            continue;
                        }

                        while (keys.hasNext()) {
                            String temp = (keys.next());

                            if (temp == tag.getTagName()) {
                                JSONArray processArray = ((JSONArray) tagsObj.get(temp));
                                processArray = processArray.put(processJSON);
                                tagsObj.put(temp, processArray);
                                break;

                            }
                            if (!keys.hasNext()) {
                                JSONArray newTagArray = new JSONArray();
                                newTagArray.put(processJSON);
                                tagsObj.put(tag.getTagName(), newTagArray);
                            }
                        }
                    }
                }

                textContent = tagsObj.toString();

            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String msg = "Process tags retrieving error";
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
        return textContent;
    }

    /**
     * @param processName
     * @param processVersion
     * @param flowchartJson
     * @return the processId once the flowchart is saved
     */
    public String uploadFlowchart(String processName, String processVersion, String flowchartJson)
                                        throws ProcessCenterException{
        String processId = "NA";
        if (log.isDebugEnabled())
            log.debug("Creating Flowchart...");
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource flowchartContentResource = reg.newResource();
                flowchartContentResource.setContent(flowchartJson);
                flowchartContentResource.setMediaType("application/json");
                String flowchartContentPath = "flowchart/" + processName + "/" + processVersion;
                reg.put(flowchartContentPath, flowchartContentResource);
                String processPath = "processes/" + processName + "/" + processVersion;

                // update process by linking the flowchart asset
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document processXMLContent = stringToXML(processContent);

                //set the flowchart content
                processXMLContent.getElementsByTagName("flowchart").item(0).getFirstChild()
                        .setTextContent(flowchartContentPath);

                String newProcessContent = xmlToString(processXMLContent);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            String errMsg =
                    "Flow-chart uploading error for " + processName + " - " + processVersion + ":" + flowchartJson;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        if (log.isDebugEnabled())
            log.debug("Successfully uploaded the flowchart for process " + processName + "-" + processVersion);
        return processId;
    }

    /**
     * @param flowchartPath
     * @return the flowchart string of a process
     */
    public String getFlowchart(String flowchartPath) throws ProcessCenterException {
        String flowchartString = "NA";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                flowchartPath = flowchartPath.substring("/_system/governance/".length());
                try {
                    Resource flowchartAsset = reg.get(flowchartPath);
                    flowchartString = new String((byte[]) flowchartAsset.getContent());
                } catch (ResourceNotFoundException e) {
                    flowchartString = "NA";
                }
            }
        } catch (Exception e) {
            String values[] = flowchartPath.split("/");
            String errorMessage = "Failed to retrieve the flowchart for process " + values[1] + "-" + values[2];
            log.error(errorMessage, e);
            throw new ProcessCenterException(errorMessage);
        }
        if (log.isDebugEnabled())
            log.debug("Successfully retrieved the flowchart at path " + flowchartPath);
        return flowchartString;
    }

    /**
     * Delete a flowchart from the registry
     *
     * @param name
     * @param version
     */
    public void deleteFlowchart(String name, String version) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            String flowchartContentPath = "flowchart/" + name + "/" + version;
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                reg.delete(flowchartContentPath);

                String processPath = "processes/" + name + "/" + version;
                Resource processResource = reg.get(processPath);

                String processContent = new String((byte[]) processResource.getContent());
                Document processXML = stringToXML(processContent);
                processXML.getElementsByTagName("flowchart").item(0).getFirstChild().setTextContent(
		                "NA");

                String newProcessContent = xmlToString(processXML);
                processResource.setContent(newProcessContent);
                reg.put(processPath, processResource);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to upload the flowchart for process " + name + "-" + version;
            log.error(errorMessage, e);
            throw new ProcessCenterException(errorMessage);
        }
    }

    public String updateDescription(String descriptionDetails) throws ProcessCenterException {
        String processId = "NA";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(descriptionDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String processDescription = processInfo.getString("value");

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if(doc.getElementsByTagName("description").getLength() != 0)
                    doc.getElementsByTagName("description").item(0).setTextContent(processDescription);
                else{

                }

                String newProcessContent = xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);

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
    //    public static void main(String[] args) {
    //        String path = "/home/chathura/temp/t5/TestProcess1.bpmn";
    //        String outpath = "/home/chathura/temp/t5/TestProcess1image.png";
    //        try {
    ////            byte[] image = new ProcessStore().getBPMNImage2(path);
    ////            FileUtils.writeByteArrayToFile(new File(outpath), image);
    //
    //            String imageString = new ProcessStore().getEncodedBPMNImage(path, null);
    //            FileUtils.write(new File(outpath), imageString);
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
}