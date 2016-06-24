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
import org.json.JSONException;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
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
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ProcessStoreConstants.YES);
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }

    private Document stringToXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        return document;
    }

    public String createProcess(String processDetails) {

        String processId = "FAILED TO ADD PROCESS";

        try {
            JSONObject processInfo = new JSONObject(processDetails);
            String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
            String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
            String processOwner = processInfo.getString(ProcessStoreConstants.PROCESS_OWNER);
            String processTags = processInfo.getString(ProcessStoreConstants.PROCESS_TAGS);
            JSONArray subprocess = processInfo.getJSONArray(ProcessStoreConstants.SUBPROCESS);
            JSONArray successor = processInfo.getJSONArray(ProcessStoreConstants.SUCCESSOR);
            JSONArray predecessor = processInfo.getJSONArray(ProcessStoreConstants.PREDECESSOR);

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(ProcessStoreConstants.MNS, ProcessStoreConstants.METADATA);
                doc.appendChild(rootElement);

                Element overviewElement = append(doc, rootElement, ProcessStoreConstants.OVERVIEW, ProcessStoreConstants.MNS);
                appendText(doc, overviewElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, processName);
                appendText(doc, overviewElement, ProcessStoreConstants.VERSION, ProcessStoreConstants.MNS, processVersion);
                appendText(doc, overviewElement, ProcessStoreConstants.OWNER, ProcessStoreConstants.MNS, processOwner);

                Element propertiesElement = append(doc, rootElement, ProcessStoreConstants.PROPERTIES, ProcessStoreConstants.MNS);
                appendText(doc, propertiesElement, ProcessStoreConstants.PROCESS_TEXT_PATH, ProcessStoreConstants.MNS, "NA");

                // fill bpmn properties with NA values
                appendText(doc, propertiesElement, ProcessStoreConstants.BPMN_PATH, ProcessStoreConstants.MNS, "NA");
                appendText(doc, propertiesElement, ProcessStoreConstants.BPMN_ID, ProcessStoreConstants.MNS, "NA");

                if (subprocess.length() != 0) {
                    for (int i = 0; i < subprocess.length(); i++) {
                        Element successorElement = append(doc, rootElement, ProcessStoreConstants.SUBPROCESS, ProcessStoreConstants.MNS);
                        appendText(doc, successorElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, subprocess.getJSONObject(i).getString(ProcessStoreConstants.NAME));
                        appendText(doc, successorElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, subprocess.getJSONObject(i).getString(ProcessStoreConstants.PATH));
                        appendText(doc, successorElement, ProcessStoreConstants.ID, ProcessStoreConstants.MNS, subprocess.getJSONObject(i).getString(ProcessStoreConstants.ID));
                    }
                }

                if (successor.length() != 0) {
                    for (int i = 0; i < successor.length(); i++) {
                        Element successorElement = append(doc, rootElement, ProcessStoreConstants.SUCCESSOR, ProcessStoreConstants.MNS);
                        appendText(doc, successorElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, successor.getJSONObject(i).getString(ProcessStoreConstants.NAME));
                        appendText(doc, successorElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, successor.getJSONObject(i).getString(ProcessStoreConstants.PATH));
                        appendText(doc, successorElement, ProcessStoreConstants.ID, ProcessStoreConstants.MNS, successor.getJSONObject(i).getString(ProcessStoreConstants.ID));
                    }
                }

                if (predecessor.length() != 0) {
                    for (int i = 0; i < predecessor.length(); i++) {
                        Element predecessorElement = append(doc, rootElement, ProcessStoreConstants.PREDECESSOR, ProcessStoreConstants.MNS);
                        appendText(doc, predecessorElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS,
                                predecessor.getJSONObject(i).getString(ProcessStoreConstants.NAME));
                        appendText(doc, predecessorElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS,
                                predecessor.getJSONObject(i).getString(ProcessStoreConstants.PATH));
                        appendText(doc, predecessorElement, ProcessStoreConstants.ID, ProcessStoreConstants.MNS, predecessor.getJSONObject(i).getString(ProcessStoreConstants.ID));
                    }
                }

                Element pdfElement = append(doc, rootElement, ProcessStoreConstants.PDF, ProcessStoreConstants.MNS);
                appendText(doc, pdfElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, "NA");

                Element bpmnDesignElement = append(doc, rootElement, ProcessStoreConstants.BPMN_DESIGN, ProcessStoreConstants.MNS);
                appendText(doc, bpmnDesignElement, ProcessStoreConstants.BPMN_DESIGN_PATH, ProcessStoreConstants.MNS, "NA");

                Element flowchartElement = append(doc, rootElement, ProcessStoreConstants.FLOWCHART, ProcessStoreConstants.MNS);
                appendText(doc, flowchartElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, "NA");

                Element documentElement = append(doc, rootElement, ProcessStoreConstants.DOCUMENT, ProcessStoreConstants.MNS);
                appendText(doc, documentElement, ProcessStoreConstants.DOCUMENT_NAME, ProcessStoreConstants.MNS, "NA");
                appendText(doc, documentElement, ProcessStoreConstants.SUMMARY, ProcessStoreConstants.MNS, "NA");
                appendText(doc, documentElement, ProcessStoreConstants.URL, ProcessStoreConstants.MNS, "NA");
                appendText(doc, documentElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, "NA");

                String processAssetContent = xmlToString(doc);
                Resource processAsset = reg.newResource();
                processAsset.setContent(processAssetContent);
                processAsset.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND);
                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
                reg.put(processAssetPath, processAsset);

                // associate lifecycle with the process asset, so that it can be promoted to published state
                GovernanceUtils.associateAspect(processAssetPath, ProcessStoreConstants.SAMPLE_LIFE_CYCLE2, reg);

                // apply tags to the resource
                String[] tags = processTags.split(",");
                for (String tag : tags) {
                    tag = tag.trim();
                    reg.applyTag(processAssetPath, tag);
                }

                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();
            }
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when creating the process", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when creating the process", e);
        } catch (JSONException e) {
            log.error("JSON Exception when creating the process", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when creating the process", e);
        }

        return processId;
    }

    public boolean saveProcessText(String processName, String processVersion, String processText) {

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // get process asset content
                String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document doc = stringToXML(processContent);

                // store process text as a separate resource
                String processTextResourcePath =  ProcessStoreConstants.PROCESS_TEXT + "/" + processName + "/" + processVersion;
                if (processText != null && processText.length() > 0) {
                    Resource processTextResource = reg.newResource();
                    processTextResource.setContent(processText);
                    processTextResource.setMediaType(ProcessStoreConstants.TEXT_HTML);
                    reg.put(processTextResourcePath, processTextResource);
                    doc.getElementsByTagName(ProcessStoreConstants.PROCESS_TEXT_PATH).item(0).setTextContent(processTextResourcePath);
                } else {
                    reg.delete(processTextResourcePath);
                    doc.getElementsByTagName(ProcessStoreConstants.PROCESS_TEXT_PATH).item(0).setTextContent("NA");
                }

                // update process asset
                String newProcessContent = xmlToString(doc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);
            }
        } catch (SAXException e) {
            log.error("SAX Exception when saving the process text", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when saving the process text", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when saving the process text", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when saving the process text", e);
        } catch (IOException e) {
            log.error("I/O Exception when saving the process text", e);
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

                if (type.equals(ProcessStoreConstants.BPMN_FILE)) {

                } else {

                    // add each bpmn file as a new asset
                    byte[] barContent = IOUtils.toByteArray(barStream);
                    ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(barContent));
                    ZipEntry entry = null;
                    while ((entry = zip.getNextEntry()) != null) {
                        String entryName = entry.getName();
                        if (entryName.endsWith(ProcessStoreConstants.BPMN_EXTENSION) || entryName.endsWith(ProcessStoreConstants.BPMN_FILE_EXTENSION)) {

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
                            bpmnResource.setMediaType(ProcessStoreConstants.TEXT_XML);
                            String bpmnResourcePath = "bpmn_xml/" + entryName + "/" + version;
                            reg.put(bpmnResourcePath, bpmnResource);

                            // add bpmn asset
                            String bpmnAssetName = entryName;
                            String displayName = entryName;
                            if (displayName.endsWith(ProcessStoreConstants.BPMN_EXTENSION)) {
                                displayName = displayName.substring(0, (entryName.length() - 5));
                            }
                            String bpmnAssetPath =  ProcessStoreConstants.BPMN + "/" + entryName + "/" + version;
                            Document bpmnDoc = docBuilder.newDocument();
                            Element bpmnRoot = bpmnDoc.createElementNS(ProcessStoreConstants.MNS, ProcessStoreConstants.METADATA);
                            bpmnDoc.appendChild(bpmnRoot);
                            Element bpmnOverview = append(bpmnDoc, bpmnRoot, ProcessStoreConstants.OVERVIEW, ProcessStoreConstants.MNS);
                            appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, displayName);
                            appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.VERSION, ProcessStoreConstants.MNS, version);
                            appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.PROCESS_PATH, ProcessStoreConstants.MNS, "");
                            appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.DESCRIPTION, ProcessStoreConstants.MNS, "");
                            Element bpmnAssetContentElement = append(bpmnDoc, bpmnRoot, ProcessStoreConstants.CONTENT, ProcessStoreConstants.MNS);
                            appendText(bpmnDoc, bpmnAssetContentElement, ProcessStoreConstants.CONTENT_PATH, ProcessStoreConstants.MNS, bpmnResourcePath);
                            String bpmnAssetContent = xmlToString(bpmnDoc);

                            Resource bpmnAsset = reg.newResource();
                            bpmnAsset.setContent(bpmnAssetContent);
                            bpmnAsset.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND);
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
        } catch (ProcessCenterException e) {
            log.error("Process Center Exception when processing the process", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when processing the process", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when processing the process", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when processing the process", e);
        } catch (IOException e) {
            log.error("I/O Exception when processing the process", e);
        }
    }

    public String createBPMN(String bpmnName, String bpmnVersion, Object o) {
        String processId = "FAILED TO CREATE BPMN";
        log.debug("Creating BPMN resource...");
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
                bpmnContentResource.setMediaType(ProcessStoreConstants.APPLICATION_XML);
                String bpmnContentPath = ProcessStoreConstants.BPMN_CONTENT + "/" + bpmnName + "/" + bpmnVersion;
                reg.put(bpmnContentPath, bpmnContentResource);

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // add bpmn asset pointing to above bpmn content
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(ProcessStoreConstants.MNS, ProcessStoreConstants.METADATA);
                doc.appendChild(rootElement);

                Element overviewElement = append(doc, rootElement, ProcessStoreConstants.OVERVIEW, ProcessStoreConstants.MNS);
                appendText(doc, overviewElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, bpmnName);
                appendText(doc, overviewElement, ProcessStoreConstants.VERSION, ProcessStoreConstants.MNS, bpmnVersion);
                appendText(doc, overviewElement, ProcessStoreConstants.DESCRIPTION, ProcessStoreConstants.MNS, "");
                String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + bpmnName + "/" + bpmnVersion;
                appendText(doc, overviewElement, ProcessStoreConstants.PROCESS_PATH, ProcessStoreConstants.MNS, processPath);

                Element contentElement = append(doc, rootElement, ProcessStoreConstants.CONTENT, ProcessStoreConstants.MNS);
                appendText(doc, contentElement, ProcessStoreConstants.CONTENT_PATH, ProcessStoreConstants.MNS, bpmnContentPath);

                String bpmnAssetContent = xmlToString(doc);
                Resource bpmnAsset = reg.newResource();
                bpmnAsset.setContent(bpmnAssetContent);
                bpmnAsset.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND);

                String bpmnAssetPath = ProcessStoreConstants.BPMN + "/" + bpmnName + "/" + bpmnVersion;
                reg.put(bpmnAssetPath, bpmnAsset);
                Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                String bpmnAssetID = storedBPMNAsset.getUUID();

                // update process by linking the bpmn asset

                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document pdoc = stringToXML(processContent);
                pdoc.getElementsByTagName(ProcessStoreConstants.BPMN_PATH).item(0).setTextContent(bpmnAssetPath);
                pdoc.getElementsByTagName(ProcessStoreConstants.BPMN_ID).item(0).setTextContent(bpmnAssetID);
                String newProcessContent = xmlToString(pdoc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (SAXException e) {
            log.error("SAX Exception when creating the bpmn", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when creating the bpmn", e);
        } catch (IOException e) {
            log.error("I/O Exception when creating the bpmn", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when creating the bpmn", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when creating the bpmn", e);
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
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(ProcessStoreConstants.MNS, ProcessStoreConstants.METADATA);
                doc.appendChild(rootElement);

                Element overviewElement = append(doc, rootElement, ProcessStoreConstants.OVERVIEW, ProcessStoreConstants.MNS);
                appendText(doc, overviewElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, barName);
                appendText(doc, overviewElement, ProcessStoreConstants.VERSION, ProcessStoreConstants.MNS, barVersion);
                appendText(doc, overviewElement, ProcessStoreConstants.DESCRIPTION, ProcessStoreConstants.MNS, description);

                // add binary data of the bar asset as a separate resource
                Resource barResource = reg.newResource();
                byte[] barContent = IOUtils.toByteArray(barStream);
                barResource.setContent(barContent);
                barResource.setMediaType(ProcessStoreConstants.APPLICATION_BAR);
                String barResourcePath =  ProcessStoreConstants.BPMN_ARCHIVE_BINARY + "/" + barName + "/" + barVersion;
                reg.put(barResourcePath, barResource);

                Element contentElement = append(doc, rootElement, ProcessStoreConstants.CONTENT, ProcessStoreConstants.MNS);
                appendText(doc, contentElement, ProcessStoreConstants.CONTENT_PATH, ProcessStoreConstants.MNS, barResourcePath);

                //                reg.addAssociation(barAssetPath, barResourcePath, "has_content");

                // add each bpmn file as a new asset
                ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(barContent));
                ZipEntry entry = null;
                while ((entry = zip.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.endsWith(ProcessStoreConstants.BPMN_EXTENSION) || entryName.endsWith(ProcessStoreConstants.BPMN_FILE_EXTENSION)) {

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
                        bpmnResource.setMediaType(ProcessStoreConstants.TEXT_XML);
                        String bpmnResourcePath =  ProcessStoreConstants.BPMN_XML + "/" + barName + "." + entryName + "/" + barVersion;
                        reg.put(bpmnResourcePath, bpmnResource);

                        // add bpmn asset
                        String bpmnAssetName = barName + "." + entryName;
                        String displayName = entryName;
                        if (displayName.endsWith(ProcessStoreConstants.BPMN_EXTENSION)) {
                            displayName = displayName.substring(0, (entryName.length() - 5));
                        }
                        String bpmnAssetPath = ProcessStoreConstants.BPMN + "/" + barName + "." + entryName + "/" + barVersion;
                        //                        String bpmnAssetContent = "<metadata xmlns='http://www.wso2.org/governance/metadata'>" +
                        //                                "<overview><name>" + bpmnAssetName + "</name><version>" + barVersion + "</version>" +
                        //                                "<description>This is a BPMN asset</description></overview></metadata>";

                        Document bpmnDoc = docBuilder.newDocument();
                        Element bpmnRoot = bpmnDoc.createElementNS(ProcessStoreConstants.MNS, ProcessStoreConstants.METADATA);
                        bpmnDoc.appendChild(bpmnRoot);
                        Element bpmnOverview = append(bpmnDoc, bpmnRoot, ProcessStoreConstants.OVERVIEW, ProcessStoreConstants.MNS);
                        //                        appendText(bpmnDoc, bpmnOverview, "name", mns, bpmnAssetName);
                        appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, displayName);
                        appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.VERSION, ProcessStoreConstants.MNS, barVersion);
                        appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.PACKAGE, ProcessStoreConstants.MNS, barName);
                        appendText(bpmnDoc, bpmnOverview, ProcessStoreConstants.DESCRIPTION, ProcessStoreConstants.MNS, "");
                        Element bpmnAssetContentElement = append(bpmnDoc, bpmnRoot, ProcessStoreConstants.CONTENT, ProcessStoreConstants.MNS);
                        appendText(bpmnDoc, bpmnAssetContentElement, ProcessStoreConstants.CONTENT_PATH, ProcessStoreConstants.MNS, bpmnResourcePath);
                        String bpmnAssetContent = xmlToString(bpmnDoc);

                        Resource bpmnAsset = reg.newResource();
                        bpmnAsset.setContent(bpmnAssetContent);
                        bpmnAsset.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND);
                        reg.put(bpmnAssetPath, bpmnAsset);
                        Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                        String bpmnAssetID = storedBPMNAsset.getUUID();

                        Element bpmnElement = append(doc, rootElement, ProcessStoreConstants.BPMN, ProcessStoreConstants.MNS);
                        appendText(doc, bpmnElement, ProcessStoreConstants.NAME_UPPERCASE, ProcessStoreConstants.MNS, bpmnAssetName);
                        appendText(doc, bpmnElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, bpmnAssetPath);
                        appendText(doc, bpmnElement, ProcessStoreConstants.ID_UPPERCASE, ProcessStoreConstants.MNS, bpmnAssetID);

                        //                        reg.addAssociation(barAssetPath, bpmnAssetPath, "contains");
                        //                        reg.addAssociation(bpmnAssetPath, bpmnResourcePath, "has_content");
                    }
                }

                String barAssetContent = xmlToString(doc);
                Resource barAsset = reg.newResource();
                barAsset.setContent(barAssetContent);
                barAsset.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND);
                String barAssetPath = ProcessStoreConstants.BAR_ASSET_PATH + barName + "/" + barVersion;
                reg.put(barAssetPath, barAsset);

            } else {
                String msg = "Registry service is not available.";
                throw new ProcessCenterException(msg);
            }

        } catch (ProcessCenterException e) {
            log.error("Process Center Exception when processing the bar file", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when processing the bar file", e);
        } catch (IOException e) {
            log.error("I/O Exception when processing the bar file", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when processing the bar file", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when processing the bar file", e);
        }
    }

    public String getBAR(String barPath) {

        String barString = "";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                barPath = barPath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource barAsset = reg.get(barPath);
                String barContent = new String((byte[]) barAsset.getContent());

                //                String barContent = "<metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><name>b4444</name><version>1.0.0</version><description>hfrefgygeofyure</description></overview><content><contentPath>bpmn_archives_binary/b4444/1.0.0</contentPath></content><bpmn><Name>test_name</Name><Path>bpmn/b4444.TestProcess1.bpmn/1.0.0</Path><Id>815fe296-9363-4895-b386-23162b78bec4</Id></bpmn></metadata>";

                JSONObject bar = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(barContent)));
                Element overviewElement = (Element) document.getElementsByTagName(ProcessStoreConstants.OVERVIEW).item(0);
                String barName = overviewElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                String barVersion = overviewElement.getElementsByTagName(ProcessStoreConstants.VERSION).item(0).getTextContent();
                String barDescription = overviewElement.getElementsByTagName(ProcessStoreConstants.DESCRIPTION).item(0).getTextContent();

                bar.put(ProcessStoreConstants.NAME, barName);
                bar.put(ProcessStoreConstants.VERSION, barVersion);
                bar.put(ProcessStoreConstants.DESCRIPTION, barDescription);

                Element contentElement = (Element) document.getElementsByTagName(ProcessStoreConstants.CONTENT).item(0);
                String contentPath = contentElement.getElementsByTagName(ProcessStoreConstants.CONTENT_PATH).item(0).getTextContent();
                bar.put(ProcessStoreConstants.CONTENT_PATH, contentPath);

                JSONArray jbpmns = new JSONArray();
                bar.put(ProcessStoreConstants.BPMN_MODELS, jbpmns);

                NodeList bpmnElements = document.getElementsByTagName(ProcessStoreConstants.BPMN);
                for (int i = 0; i < bpmnElements.getLength(); i++) {
                    Element bpmnElement = (Element) bpmnElements.item(i);
                    String bpmnId = bpmnElement.getElementsByTagName(ProcessStoreConstants.ID_UPPERCASE).item(0).getTextContent();
                    String bpmnName = bpmnElement.getElementsByTagName(ProcessStoreConstants.NAME_UPPERCASE).item(0).getTextContent();
                    String bpmnPath = bpmnElement.getElementsByTagName(ProcessStoreConstants.URL).item(0).getTextContent();

                    JSONObject jbpmn = new JSONObject();
                    jbpmn.put(ProcessStoreConstants.BPMN_ID, bpmnId);
                    jbpmn.put(ProcessStoreConstants.BPMN_NAME, bpmnName);
                    jbpmn.put(ProcessStoreConstants.BPMN_PATH, bpmnPath);
                    jbpmns.put(jbpmn);
                }

                barString = bar.toString();
            }
        } catch (SAXException e) {
            log.error("SAX Exception when retrieving the bar file", e);
        } catch (JSONException e) {
            log.error("JSON Exception when retrieving the bar file", e);
        } catch (IOException e) {
            log.error("I/O Exception when retrieving the bar file", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when retrieving the bar file", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when retrieving the bar file", e);
        }

        return barString;
    }

    public String getProcessText(String textPath) {
        String textContent = "FAILED TO GET TEXT CONTENT";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource textResource = reg.get(textPath);
                textContent = new String((byte[]) textResource.getContent());
            }
        } catch (RegistryException e) {
            log.error("Registry Exception when processing the process text", e);
        }
        return textContent;
    }

    public String getBPMN(String bpmnPath) {

        String bpmnString = "";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                bpmnPath = bpmnPath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource bpmnAsset = reg.get(bpmnPath);
                String bpmnContent = new String((byte[]) bpmnAsset.getContent());
                JSONObject bpmn = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(bpmnContent)));
                Element overviewElement = (Element) document.getElementsByTagName(ProcessStoreConstants.OVERVIEW).item(0);
                String bpmnName = overviewElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                String bpmnVersion = overviewElement.getElementsByTagName(ProcessStoreConstants.VERSION).item(0).getTextContent();
                String bpmnDescription = overviewElement.getElementsByTagName(ProcessStoreConstants.DESCRIPTION).item(0).getTextContent();
                String processPath = overviewElement.getElementsByTagName(ProcessStoreConstants.PROCESS_PATH).item(0).getTextContent();

                bpmn.put(ProcessStoreConstants.NAME, bpmnName);
                bpmn.put(ProcessStoreConstants.VERSION, bpmnVersion);
                bpmn.put(ProcessStoreConstants.DESCRIPTION, bpmnDescription);
                bpmn.put(ProcessStoreConstants.PROCESS_PATH, processPath);

                Element contentElement = (Element) document.getElementsByTagName(ProcessStoreConstants.CONTENT).item(0);
                String contentPath = contentElement.getElementsByTagName(ProcessStoreConstants.CONTENT_PATH).item(0).getTextContent();
                bpmn.put(ProcessStoreConstants.CONTENT_PATH, contentPath);
                String encodedBPMNImage = getEncodedBPMNImage(contentPath);
                bpmn.put(ProcessStoreConstants.BPMN_IMAGE, encodedBPMNImage);

                bpmnString = bpmn.toString();
            }
        } catch (IOException e) {
            log.error("I/O Exception when retrieving the bpmn", e);
        } catch (SAXException e) {
            log.error("SAX Exception when retrieving the bpmn", e);
        } catch (JSONException e) {
            log.error("JSON Exception when retrieving the bpmn", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when retrieving the bpmn", e);
        } catch (ProcessCenterException e) {
            log.error("Process Center Exception when retrieving the bpmn", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when retrieving the bpmn", e);
        }

        return bpmnString;
    }

    public String getEncodedBPMNImage(String path) throws ProcessCenterException {
        byte[] encoded = Base64.encodeBase64(getBPMNImage(path));
        String imageString = new String(encoded);
        return imageString;
    }

    public String getProcesses() {

        String processDetails = "{}";

        try {
            JSONArray result = new JSONArray();

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                String[] processPaths = GovernanceUtils
                        .findGovernanceArtifacts(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND, reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    String processName = processXML.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                    String processVersion = processXML.getElementsByTagName(ProcessStoreConstants.VERSION).item(0).getTextContent();
                    String pdfPath = processXML.getElementsByTagName(ProcessStoreConstants.PDF).item(0).getFirstChild().getTextContent();
                    String flowchartPath = processXML.getElementsByTagName(ProcessStoreConstants.FLOWCHART).item(0).getFirstChild().getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put(ProcessStoreConstants.PATH, processPath);
                    processJSON.put(ProcessStoreConstants.PROCESS_ID, processResource.getUUID());
                    processJSON.put(ProcessStoreConstants.PROCESS_NAME, processName);
                    processJSON.put(ProcessStoreConstants.PROCESS_VERSION, processVersion);
                    processJSON.put(ProcessStoreConstants.PDS_PATH, pdfPath);
                    processJSON.put(ProcessStoreConstants.FLOWCHART_PATH, flowchartPath);
                    result.put(processJSON);
                }

                processDetails = result.toString();

                int a = 1;

            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (IOException e) {
            log.error("I/O Exception when retrieving the processes", e);
        } catch (SAXException e) {
            log.error("SAX Exception when retrieving the processes", e);
        } catch (JSONException e) {
            log.error("JSON Exception when retrieving the processes", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when retrieving the processes", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when retrieving the processes", e);
        } catch (ProcessCenterException e) {
            String msg = "Failed";
            log.error("Process Center Exception when retrieving the processes : " + msg, e);
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
        } catch (RegistryException | IOException e) {
            String msg = "Failed to fetch bpmn model : " + path;
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
        } catch (IOException e) {
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

                conObj.put(ProcessStoreConstants.SUBPROCESSES, subprocessArray);
                conObj.put(ProcessStoreConstants.SUCCESSORS, successorArray);
                conObj.put(ProcessStoreConstants.PREDECESSORS, predecessorArray);

                NodeList subprocessElements = ((Element) document.getFirstChild()).getElementsByTagName(
                        ProcessStoreConstants.SUBPROCESS);
                NodeList successorElements = ((Element) document.getFirstChild()).getElementsByTagName(ProcessStoreConstants.SUCCESSOR);
                NodeList predecessorElements = ((Element) document.getFirstChild()).getElementsByTagName(
                        ProcessStoreConstants.PREDECESSOR);

                if (subprocessElements.getLength() != 0) {
                    for (int i = 0; i < subprocessElements.getLength(); i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName = subprocessElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                        String subprocessPath = subprocessElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0).getTextContent();
                        String subprocessId = subprocessElement.getElementsByTagName(ProcessStoreConstants.ID).item(0).getTextContent();
                        String subprocessVersion = subprocessPath.substring(subprocessPath.lastIndexOf("/") + 1).trim();

                        JSONObject subprocess = new JSONObject();
                        subprocess.put(ProcessStoreConstants.NAME, subprocessName);
                        subprocess.put(ProcessStoreConstants.PATH, subprocessPath);
                        subprocess.put(ProcessStoreConstants.ID, subprocessId);
                        subprocess.put(ProcessStoreConstants.VERSION, subprocessVersion);
                        subprocessArray.put(subprocess);
                    }
                }

                if (successorElements.getLength() != 0) {
                    for (int i = 0; i < successorElements.getLength(); i++) {
                        Element successorElement = (Element) successorElements.item(i);
                        String successorName = successorElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                        String successorPath = successorElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0).getTextContent();
                        String successorId = successorElement.getElementsByTagName(ProcessStoreConstants.ID).item(0).getTextContent();
                        String successorVersion = successorPath.substring(successorPath.lastIndexOf("/") + 1).trim();

                        JSONObject successor = new JSONObject();
                        successor.put(ProcessStoreConstants.NAME, successorName);
                        successor.put(ProcessStoreConstants.PATH, successorPath);
                        successor.put(ProcessStoreConstants.ID, successorId);
                        successor.put(ProcessStoreConstants.VERSION, successorVersion);
                        successorArray.put(successor);
                    }
                }

                if (predecessorElements.getLength() != 0) {
                    for (int i = 0; i < predecessorElements.getLength(); i++) {
                        Element predecessorElement = (Element) predecessorElements.item(i);
                        String predecessorName = predecessorElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0)
                                .getTextContent();
                        String predecessorPath = predecessorElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0)
                                .getTextContent();
                        String predecessorId = predecessorElement.getElementsByTagName(ProcessStoreConstants.ID).item(0).getTextContent();
                        String predecessorVersion = predecessorPath.substring(predecessorPath.lastIndexOf("/") + 1)
                                .trim();

                        JSONObject predecessor = new JSONObject();
                        predecessor.put(ProcessStoreConstants.NAME, predecessorName);
                        predecessor.put(ProcessStoreConstants.PATH, predecessorPath);
                        predecessor.put(ProcessStoreConstants.ID, predecessorId);
                        predecessor.put(ProcessStoreConstants.VERSION, predecessorVersion);
                        predecessorArray.put(predecessor);
                    }
                }
                resourceString = conObj.toString();
            }
        } catch (IOException e) {
            log.error("I/O Exception. Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        }
        return resourceString;
    }

    public boolean updateOwner(String ownerDetails) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(ownerDetails);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                String processOwner = processInfo.getString(ProcessStoreConstants.PROCESS_OWNER);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                doc.getElementsByTagName(ProcessStoreConstants.OWNER).item(0).setTextContent(processOwner);

                String newProcessContent = xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);
            }

        } catch (IOException e) {
            log.error("I/O Exception. Failed to update the process owner", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to update the process owner", e);
        } catch (SAXException e) {
            log.error("SAX Exception.Failed to update the process owner", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to update the process owner", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to update the process owner", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to update the process owner", e);
        }
        return true;
    }

    public boolean addSubprocess(String subprocessDetails) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(subprocessDetails);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                JSONObject subprocess = processInfo.getJSONObject(ProcessStoreConstants.SUBPROCESS);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (subprocess != null) {
                    Element rootElement = doc.getDocumentElement();
                    Element subprocessElement = append(doc, rootElement, ProcessStoreConstants.SUBPROCESS, ProcessStoreConstants.MNS);
                    appendText(doc, subprocessElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, subprocess.getString(ProcessStoreConstants.NAME));
                    appendText(doc, subprocessElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, subprocess.getString(ProcessStoreConstants.PATH));
                    appendText(doc, subprocessElement, ProcessStoreConstants.ID, ProcessStoreConstants.MNS, subprocess.getString(ProcessStoreConstants.ID));

                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }

        } catch (IOException e) {
            log.error("I/O Exception. Failed to add a subprocess", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to add a subprocess", e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to add a subprocess", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to add a subprocess", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to add a subprocess", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to add a subprocess", e);
        }
        return true;
    }

    public boolean addSuccessor(String successorDetails) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(successorDetails);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                JSONObject successor = processInfo.getJSONObject(ProcessStoreConstants.SUCCESSOR);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (successor != null) {
                    Element rootElement = doc.getDocumentElement();
                    Element successorElement = append(doc, rootElement, ProcessStoreConstants.SUCCESSOR, ProcessStoreConstants.MNS);
                    appendText(doc, successorElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, successor.getString(ProcessStoreConstants.NAME));
                    appendText(doc, successorElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, successor.getString(ProcessStoreConstants.PATH));
                    appendText(doc, successorElement, ProcessStoreConstants.ID, ProcessStoreConstants.MNS, successor.getString(ProcessStoreConstants.ID));

                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }

        } catch (IOException e) {
            log.error("I/O Exception. Failed to add a successor", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to add a successor", e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to add a successor", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to add a successor", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to add a successor", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to add a successor", e);
        }
        return true;
    }

    public boolean addPredecessor(String predecessorDetails) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(predecessorDetails);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                JSONObject predecessor = processInfo.getJSONObject(ProcessStoreConstants.PREDECESSOR);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (predecessor != null) {
                    Element rootElement = doc.getDocumentElement();
                    Element predecessorElement = append(doc, rootElement, ProcessStoreConstants.PREDECESSOR, ProcessStoreConstants.MNS);
                    appendText(doc, predecessorElement, ProcessStoreConstants.NAME, ProcessStoreConstants.MNS, predecessor.getString(ProcessStoreConstants.NAME));
                    appendText(doc, predecessorElement, ProcessStoreConstants.PATH, ProcessStoreConstants.MNS, predecessor.getString(ProcessStoreConstants.PATH));
                    appendText(doc, predecessorElement, ProcessStoreConstants.ID, ProcessStoreConstants.MNS, predecessor.getString(ProcessStoreConstants.ID));

                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }

        } catch (IOException e) {
            log.error("I/O Exception. Failed to add a predecessor", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to add a predecessor", e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to add a predecessor", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to add a predecessor", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to add a predecessor", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to add a predecessor", e);
        }
        return true;
    }

    public boolean deleteSubprocess(String deleteSubprocess) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deleteSubprocess);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                JSONObject subprocess = processInfo.getJSONObject(ProcessStoreConstants.DELETE_SUBPROCESS);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (subprocess != null) {
                    NodeList subprocessElements = ((Element) doc.getFirstChild()).getElementsByTagName(ProcessStoreConstants.SUBPROCESS);
                    for (int i = 0; i < subprocessElements.getLength(); i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName = subprocessElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                        String subprocessPath = subprocessElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0).getTextContent();
                        String subprocessId = subprocessElement.getElementsByTagName(ProcessStoreConstants.ID).item(0).getTextContent();

                        if (subprocessName.equals(subprocess.getString(ProcessStoreConstants.NAME)) &&
                                subprocessPath.equals(subprocess.getString(ProcessStoreConstants.PATH)) &&
                                subprocessId.equals(subprocess.getString(ProcessStoreConstants.ID))) {
                            subprocessElement.getParentNode().removeChild(subprocessElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }
        } catch (IOException e) {
            log.error("I/O Exception. Failed to delete a subprocess", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to delete a subprocess", e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to delete a subprocess", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to delete a subprocess", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to delete a subprocess", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to delete a subprocess", e);
        }
        return true;
    }

    public boolean deleteSuccessor(String deleteSuccessor) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deleteSuccessor);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                JSONObject successor = processInfo.getJSONObject(ProcessStoreConstants.DELETE_SUCCESSOR);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (successor != null) {
                    NodeList successorElements = ((Element) doc.getFirstChild()).getElementsByTagName(ProcessStoreConstants.SUCCESSOR);
                    for (int i = 0; i < successorElements.getLength(); i++) {
                        Element successorElement = (Element) successorElements.item(i);
                        String successorName = successorElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                        String successorPath = successorElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0).getTextContent();
                        String successorId = successorElement.getElementsByTagName(ProcessStoreConstants.ID).item(0).getTextContent();

                        if (successorName.equals(successor.getString(ProcessStoreConstants.NAME)) &&
                                successorPath.equals(successor.getString(ProcessStoreConstants.PATH)) &&
                                successorId.equals(successor.getString(ProcessStoreConstants.ID))) {
                            successorElement.getParentNode().removeChild(successorElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }
        } catch (IOException e) {
            log.error("I/O Exception. Failed to delete a successor", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to delete a successor", e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to delete a successor", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to delete a successor", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to delete a successor", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to delete a successor", e);
        }
        return true;
    }

    public boolean deletePredecessor(String deletePredecessor) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deletePredecessor);
                String processName = processInfo.getString(ProcessStoreConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessStoreConstants.PROCESS_VERSION);
                JSONObject predecessor = processInfo.getJSONObject(ProcessStoreConstants.DELETE_PREDECESSOR);

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + ProcessStoreConstants.PROCESS_NAME + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (predecessor != null) {
                    NodeList predecessorElements = ((Element) doc.getFirstChild()).getElementsByTagName(ProcessStoreConstants.PREDECESSOR);
                    for (int i = 0; i < predecessorElements.getLength(); i++) {
                        Element predecessorElement = (Element) predecessorElements.item(i);
                        String predecessorName = predecessorElement.getElementsByTagName(ProcessStoreConstants.NAME).item(0)
                                .getTextContent();
                        String predecessorPath = predecessorElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0)
                                .getTextContent();
                        String predecessorId = predecessorElement.getElementsByTagName(ProcessStoreConstants.ID).item(0).getTextContent();

                        if (predecessorName.equals(predecessor.getString(ProcessStoreConstants.NAME)) &&
                                predecessorPath.equals(predecessor.getString(ProcessStoreConstants.PATH)) &&
                                predecessorId.equals(predecessor.getString(ProcessStoreConstants.ID))) {
                            predecessorElement.getParentNode().removeChild(predecessorElement);
                            break;
                        }
                    }
                    String newProcessContent = xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
            }
        } catch (IOException e) {
            log.error("I/O Exception. Failed to delete a predecessor", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception. Failed to delete a predecessor", e);
        } catch (SAXException e) {
            log.error("SAX Exception. Failed to delete a predecessor", e);
        } catch (JSONException e) {
            log.error("JSON Exception. Failed to delete a predecessor", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception. Failed to delete a predecessor", e);
        } catch (RegistryException e) {
            log.error("Registry Exception. Failed to delete a predecessor", e);
        }
        return true;
    }

    public String uploadDocument(String processName, String processVersion, String docName, String docSummary,
            String docUrl, Object docObject, String docExtension) {
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
                String docContentPath = null;
                if (docContent.length != 0) {
                    docContentResource.setContent(docContent);
                    docContentPath = ProcessStoreConstants.DOC_CONTENT + processName + "/" + processVersion + "/" + docName +
                            "." + docExtension;
                    reg.put(docContentPath, docContentResource);
                }

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                int numOfDocAttrs = doc.getElementsByTagName(ProcessStoreConstants.DOCUMENT).item(0).getChildNodes().getLength();
                NodeList nodeList = doc.getElementsByTagName(ProcessStoreConstants.DOCUMENT).item(0).getChildNodes();

                if(numOfDocAttrs != 0) {
                    for(int i = 0; i < numOfDocAttrs; i++) {
                        if(nodeList.item(i).getNodeName().equals(ProcessStoreConstants.DOCUMENT_NAME)) {
                            nodeList.item(i).setTextContent(docName);
                        }else if(nodeList.item(i).getNodeName().equals(ProcessStoreConstants.SUMMARY)){
                            nodeList.item(i).setTextContent(docSummary);
                        }else if(nodeList.item(i).getNodeName().equals(ProcessStoreConstants.URL)){
                            if ((docUrl != null) && (!docUrl.isEmpty())) {
                                nodeList.item(i).setTextContent(docUrl);
                            }
                        }else if(nodeList.item(i).getNodeName().equals(ProcessStoreConstants.PATH)){
                            if (docContentPath != null) {
                                nodeList.item(i).setTextContent(docContentPath);
                            }
                        }
                    }
                }
                String newProcessContent = xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);

                Resource storedProcessAsset = reg.get(processAssetPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (IOException e) {
            log.error("I/O Exception when uploading a documentation", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when uploading a documentation", e);
        } catch (SAXException e) {
            log.error("SAX Exception when uploading a documentation", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when uploading a documentation", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when uploading a documentation", e);
        }
        return processId;
    }

    public String getUploadedDocumentDetails(String resourcePath) {
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
                NodeList documentElements = ((Element) document.getFirstChild()).getElementsByTagName(ProcessStoreConstants.DOCUMENT);

                if (documentElements.getLength() != 0) {
                    for (int i = 0; i < documentElements.getLength(); i++) {

                        Element documentElement = (Element) documentElements.item(i);
                        String docName = documentElement.getElementsByTagName(ProcessStoreConstants.DOCUMENT_NAME).item(0).getTextContent();
                        String docSummary = documentElement.getElementsByTagName(ProcessStoreConstants.SUMMARY).item(0).getTextContent();
                        String docUrl = documentElement.getElementsByTagName(ProcessStoreConstants.URL).item(0).getTextContent();
                        String docPath = documentElement.getElementsByTagName(ProcessStoreConstants.PATH).item(0).getTextContent();

                        JSONObject processDoc = new JSONObject();
                        processDoc.put(ProcessStoreConstants.DOCUMENT_NAME, docName);
                        processDoc.put(ProcessStoreConstants.SUMMARY, docSummary);
                        processDoc.put(ProcessStoreConstants.URL, docUrl);
                        processDoc.put(ProcessStoreConstants.PATH, docPath);
                        documentArray.put(processDoc);
                    }
                }
                documentString = documentArray.toString();
            }
        } catch (IOException e) {
            log.error("I/O Exception when fetching a document: " + resourcePath, e);
        } catch (SAXException e) {
            log.error("SAX Exception when fetching a document: " + resourcePath, e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when fetching a document: " + resourcePath, e);
        } catch (RegistryException e) {
            log.error("Registry Exception when fetching a document: " + resourcePath, e);
        } catch (JSONException e) {
            log.error("JSON Exception when fetching a document: " + resourcePath, e);
        }
        return documentString;
    }

    public String downloadDocument(String resourcePath) {
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
        } catch (RegistryException e) {
            log.error("Registry Exception when fetching a document: " + resourcePath, e);
        }

        return docString;
    }

    public String getProcessTags() throws ProcessCenterException {
        try {
            JSONObject tagsObj = new JSONObject();

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                String[] processPaths = GovernanceUtils
                        .findGovernanceArtifacts(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_VND, reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);

                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    String processName = processXML.getElementsByTagName(ProcessStoreConstants.NAME).item(0).getTextContent();
                    String processVersion = processXML.getElementsByTagName(ProcessStoreConstants.VERSION).item(0).getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put(ProcessStoreConstants.PATH, processPath);
                    processJSON.put(ProcessStoreConstants.PROCESS_ID, processResource.getUUID());
                    processJSON.put(ProcessStoreConstants.PROCESS_NAME, processName);
                    processJSON.put(ProcessStoreConstants.PROCESS_VERSION, processVersion);
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

                return tagsObj.toString();

            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (RegistryException | ParserConfigurationException | SAXException | JSONException | IOException e) {
            String msg = "Unable to process process tags";
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
    }

    public String associatePDF(String processName, String processVersion, Object object) {

        String processId = "FAILED TO ADD PDF";
        log.debug("Creating PDF resource...");
        try {
            StreamHostObject s = (StreamHostObject) object;
            InputStream pdfStream = s.getStream();
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // store pdf content as a registry resource
                Resource pdfContentResource = reg.newResource();
                byte[] pdfContent = IOUtils.toByteArray(pdfStream);

                pdfContentResource.setContent(pdfContent);
                pdfContentResource.setMediaType(ProcessStoreConstants.APPLICATION_PDF);
                String pdfContentPath = ProcessStoreConstants.PDF + "/" + processName + "/" + processVersion;
                reg.put(pdfContentPath, pdfContentResource);
                String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;

                // update process by linking the pdf asset

                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document pdoc = stringToXML(processContent);

                pdoc.getElementsByTagName(ProcessStoreConstants.PDF).item(0).getFirstChild().setTextContent(pdfContentPath);
                String newProcessContent = xmlToString(pdoc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (IOException e) {
            log.error("I/O Exception when associating a pdf", e);
        } catch (SAXException e) {
            log.error("SAX Exception when associating a pdf", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when associating a pdf ", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when associating a pdf ", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when associating a pdf", e);
        }

        log.info("successfully added pdf asset");
        return processId;
    }

    public String getPDF(String pdfPath) {

        String pdfString = "FAILED TO GET PDF";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                pdfPath = pdfPath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource pdfAsset = reg.get(pdfPath);
                byte[] pdfContent = (byte[]) pdfAsset.getContent();
                pdfString = new sun.misc.BASE64Encoder().encode(pdfContent);
                if (log.isDebugEnabled()) {
                    log.debug("PDF PATH:" + pdfPath);
                }
            }
        } catch (RegistryException e) {
            log.error("Registry Exception when fetching a pdf" + pdfPath, e);
        }

        return pdfString;
    }

    /**
     * @param processName
     * @param processVersion
     * @param flowchartJson
     * @return the processId once the flowchart is saved
     */
    public String uploadFlowchart(String processName, String processVersion, String flowchartJson) {
        String processId = "NA";
        log.debug("Creating Flowchart...");
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource flowchartContentResource = reg.newResource();
                flowchartContentResource.setContent(flowchartJson);
                flowchartContentResource.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_JSON);
                String flowchartContentPath = ProcessStoreConstants.FLOWCHART + "/" + processName + "/" + processVersion;
                reg.put(flowchartContentPath, flowchartContentResource);
                String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;

                // update process by linking the pdf asset
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document processXMLContent = stringToXML(processContent);

                processXMLContent.getElementsByTagName(ProcessStoreConstants.FLOWCHART).item(0).getFirstChild().setTextContent(flowchartContentPath);

                String newProcessContent = xmlToString(processXMLContent);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (IOException e) {
            log.error("I/O Exception when uploading a flowchart", e);
        } catch (SAXException e) {
            log.error("SAX Exception when uploading a flowchart", e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when uploading a flowchart", e);
        } catch (RegistryException e) {
            log.error("Registry Exception when uploading a flowchart", e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when uploading a flowchart", e);
        }
        return processId;
    }

    /**
     * @param flowchartPath
     * @return the flowchart string of a process
     */
    public String getFlowchart(String flowchartPath) {
        String flowchartString = "NA";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                flowchartPath = flowchartPath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource flowchartAsset = reg.get(flowchartPath);
                flowchartString = new String((byte[]) flowchartAsset.getContent());
            }
        } catch (RegistryException e) {
            log.error("Registry Exception when fetching a flowchart", e);
        }

        return flowchartString;
    }

    /**
     * Uploads the json file/string to the registry against the process id
     * @param processName name of the process
     * @param processVersion version of the process
     * @param bpmndesignJson json to be included in the registry
     * @return the bpmn diagram json string for a process
     */
    public String uploadBpmnDesign(String processName, String processVersion, String bpmndesignJson) {
        String processId = "NA";
        if (log.isDebugEnabled()) {
            log.debug("Creating bpmn design...");
        }
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource bpmnDesignContentResource = reg.newResource();
                bpmnDesignContentResource.setContent(bpmndesignJson);
                bpmnDesignContentResource.setMediaType(ProcessStoreConstants.MEDIA_TYPE_APPLICATION_JSON);
                String bpmnDesignContentPath = ProcessStoreConstants.BPMN_DESIGN + "/" + processName + "/" + processVersion;
                reg.put(bpmnDesignContentPath, bpmnDesignContentResource);
                String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;


                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document processXMLContent = stringToXML(processContent);
                processXMLContent.getElementsByTagName(ProcessStoreConstants.BPMN_DESIGN).item(0).getFirstChild().setTextContent(bpmnDesignContentPath);

                String newProcessContent = xmlToString(processXMLContent);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (RegistryException e) {
            log.error("Registry Exception when uploading the bpmn design" ,e);
        } catch (TransformerException e) {
            log.error("Transfomer Exception when uploading the bpmn design" ,e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when uploading the bpmn design", e);
        } catch (SAXException e) {
            log.error("SAX Exception when uploading the bpmn design", e);
        } catch (IOException e) {
            log.error("I/O Exception when uploading the bpmn design", e);
        }
        return processId;
    }

    /**
     *
     * @param bpmnDiagramPath
     * @return the bpmn digram path of a process
     */
    public String getbpmnDesign(String bpmnDiagramPath) {
        String bpmnDiagramString = "NA";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                bpmnDiagramPath = bpmnDiagramPath.substring(ProcessStoreConstants.GREG_PATH.length());
                Resource bpmnDiagramAsset = reg.get(bpmnDiagramPath);
                bpmnDiagramString = new String((byte[]) bpmnDiagramAsset.getContent());
            }
        } catch (RegistryException e) {
            log.error("Registry Exception when reteriving the bpmn design" ,e);
        }

        return bpmnDiagramString;
    }

    /**
     * Delete a bpmn process diagram from the registry
     * @param name
     * @param version
     */
    public void deletebpmnDiagram(String name, String version){
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            String flowchartContentPath = ProcessStoreConstants.BPMN_PATH + "/" + name + "/" +version;
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                reg.delete(flowchartContentPath);

                String processPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + name + "/" + version;
                Resource processResource = reg.get(processPath);

                String processContent = new String((byte[]) processResource.getContent());
                Document processXML = stringToXML(processContent);
                processXML.getElementsByTagName(ProcessStoreConstants.BPMN_DESIGN).item(0).getFirstChild().setTextContent("NA");

                String newProcessContent = xmlToString(processXML);
                processResource.setContent(newProcessContent);
                reg.put(processPath, processResource);
            }
        } catch (RegistryException e) {
            log.error("Registry Exception when deleting a bpmn process diagram : " + name + "-" + version, e);
        } catch (IOException e) {
            log.error("I/O Exception when deleting a bpmn process diagram : " + name + "-" + version, e);
        } catch (SAXException e) {
            log.error("SAX Exception when deleting a bpmn process diagram  : " + name + "-" + version, e);
        } catch (ParserConfigurationException e) {
            log.error("Parser Configuration Exception when deleting a bpmn process diagram  : " + name + "-" + version, e);
        } catch (TransformerException e) {
            log.error("Transformer Exception when deleting a bpmn process diagram  : " + name + "-" + version, e);
        }
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