package org.wso2.carbon.processCenter.core;
/*
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
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

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.governance.api.util.GovernanceUtils;

import org.apache.commons.io.IOUtils;
import org.jaggeryjs.hostobjects.stream.StreamHostObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.processCenter.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.InputStream;
import java.io.StringReader;

import java.io.StringWriter;

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by sathya on 2/8/16.
 */

public class ProcessStore {

    private static ProcessStore processStore = new ProcessStore();
    private static final Log log = LogFactory.getLog(ProcessStore.class);
    private static final String mns = "http://www.wso2.org/governance/metadata";
    private static final String OK = "OK";


    public static ProcessStore getInstance(){
        return processStore;
    }


    private Element append(Document doc, Element parent, String childName, String childNS) {
        Element childElement = doc.createElementNS(childNS, childName);
        parent.appendChild(childElement);
        return childElement;
    }

    private Element appendText(Document doc, Element parent, String childName, String childNS,
                               String text) {
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

    public String createProcess(String processDetails) {

        String processId = "FAILED TO ADD PROCESS";

        try {
            JSONObject processInfo = new JSONObject(processDetails);
            String processName = processInfo.getString("processName");
            String processVersion = processInfo.getString("processVersion");
            String processOwner = processInfo.getString("processOwner");
            String processTags = processInfo.getString("processTags");
            JSONArray subprocess = processInfo.getJSONArray("subprocess");
            JSONArray successor = processInfo.getJSONArray("successor");
            JSONArray predecessor = processInfo.getJSONArray("predecessor");

            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
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

                Element propertiesElement = append(doc, rootElement, "properties", mns);
                appendText(doc, propertiesElement, "processtextpath", mns, "NA");

                //                if (processText != null && processText.length() > 0) {
                //                    Resource processTextResource = reg.newResource();
                //                    processTextResource.setContent(processText);
                //                    processTextResource.setMediaType("text/html");
                //                    String processTextResourcePath = "processText/" + processName + "/" + processVersion;
                //                    reg.put(processTextResourcePath, processTextResource);
                //                    appendText(doc, propertiesElement, "processtextpath", mns, processTextResourcePath);
                //                } else {
                //                    String processTextResourcePath = "processText/" + processName + "/" + processVersion;
                //                    reg.delete(processTextResourcePath);
                //                    appendText(doc, propertiesElement, "processtextpath", mns, "NA");
                //                }

                // fill bpmn properties with NA values
                appendText(doc, propertiesElement, "bpmnpath", mns, "NA");
                appendText(doc, propertiesElement, "bpmnid", mns, "NA");

                if (subprocess.length() != 0) {
                    for (int i = 0; i < subprocess.length(); i++) {
                        Element successorElement = append(doc, rootElement, "subprocess", mns);
                        appendText(doc, successorElement, "name", mns,
                                subprocess.getJSONObject(i).getString("name"));
                        appendText(doc, successorElement, "path", mns,
                                subprocess.getJSONObject(i).getString("path"));
                        appendText(doc, successorElement, "id", mns,
                                subprocess.getJSONObject(i).getString("id"));
                    }
                }

                if (successor.length() != 0) {
                    for (int i = 0; i < successor.length(); i++) {
                        Element successorElement = append(doc, rootElement, "successor", mns);
                        appendText(doc, successorElement, "name", mns,
                                successor.getJSONObject(i).getString("name"));
                        appendText(doc, successorElement, "path", mns,
                                successor.getJSONObject(i).getString("path"));
                        appendText(doc, successorElement, "id", mns,
                                successor.getJSONObject(i).getString("id"));
                    }
                }

                if (predecessor.length() != 0) {
                    for (int i = 0; i < predecessor.length(); i++) {
                        Element predecessorElement = append(doc, rootElement, "predecessor", mns);
                        appendText(doc, predecessorElement, "name", mns,
                                predecessor.getJSONObject(i).getString("name"));
                        appendText(doc, predecessorElement, "path", mns,
                                predecessor.getJSONObject(i).getString("path"));
                        appendText(doc, predecessorElement, "id", mns,
                                predecessor.getJSONObject(i).getString("id"));
                    }
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
            }
        } catch (Exception e) {
            log.error(e);
        }

        return processId;
    }

    public boolean saveProcessText(String processName, String processVersion, String processText) {

        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // get process asset content
                String processPath = "processes/" + processName + "/" + processVersion;
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document doc = stringToXML(processContent);

                // store process text as a separate resource
                String processTextResourcePath =
                        "processText/" + processName + "/" + processVersion;
                if (processText != null && processText.length() > 0) {
                    Resource processTextResource = reg.newResource();
                    processTextResource.setContent(processText);
                    processTextResource.setMediaType("text/html");
                    reg.put(processTextResourcePath, processTextResource);
                    doc.getElementsByTagName("processtextpath").item(0)
                            .setTextContent(processTextResourcePath);
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
            log.error(e);
        }

        return true;
    }

    public void processBPMN(String version, String type, StreamHostObject s) {

        InputStream barStream = s.getStream();

        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
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
                            Element bpmnAssetContentElement =
                                    append(bpmnDoc, bpmnRoot, "content", mns);
                            appendText(bpmnDoc, bpmnAssetContentElement, "contentpath", mns,
                                    bpmnResourcePath);
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

    public String createBPMN(String bpmnName, String bpmnVersion, Object o) {
        String processId = "FAILED TO CREATE BPMN";
        log.debug("Creating BPMN resource...");
        try {
            StreamHostObject s = (StreamHostObject) o;
            InputStream bpmnStream = s.getStream();
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
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
            log.error(e);
        }

        return processId;
    }

    public void processBAR(String barName, String barVersion, String description,
                           StreamHostObject s) {

        log.debug("Processing BPMN archive " + barName);
        InputStream barStream = s.getStream();
        if (description == null) {
            description = "";
        }

        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
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
                        String bpmnResourcePath =
                                "bpmn_xml/" + barName + "." + entryName + "/" + barVersion;
                        reg.put(bpmnResourcePath, bpmnResource);

                        // add bpmn asset
                        String bpmnAssetName = barName + "." + entryName;
                        String displayName = entryName;
                        if (displayName.endsWith(".bpmn")) {
                            displayName = displayName.substring(0, (entryName.length() - 5));
                        }
                        String bpmnAssetPath =
                                "bpmn/" + barName + "." + entryName + "/" + barVersion;
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
                        appendText(bpmnDoc, bpmnAssetContentElement, "contentpath", mns,
                                bpmnResourcePath);
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
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                barPath = barPath.substring(ProcessCenterConstants.GREG_PATH.length());
                Resource barAsset = reg.get(barPath);
                String barContent = new String((byte[]) barAsset.getContent());

                //                String barContent = "<metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><name>b4444</name><version>1.0.0</version><description>hfrefgygeofyure</description></overview><content><contentPath>bpmn_archives_binary/b4444/1.0.0</contentPath></content><bpmn><Name>test_name</Name><Path>bpmn/b4444.TestProcess1.bpmn/1.0.0</Path><Id>815fe296-9363-4895-b386-23162b78bec4</Id></bpmn></metadata>";

                JSONObject bar = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(barContent)));
                Element overviewElement =
                        (Element) document.getElementsByTagName("overview").item(0);
                String barName =
                        overviewElement.getElementsByTagName("name").item(0).getTextContent();
                String barVersion =
                        overviewElement.getElementsByTagName("version").item(0).getTextContent();
                String barDescription = overviewElement.getElementsByTagName("description").item(0)
                        .getTextContent();

                bar.put("name", barName);
                bar.put("version", barVersion);
                bar.put("description", barDescription);

                Element contentElement = (Element) document.getElementsByTagName("content").item(0);
                String contentPath =
                        contentElement.getElementsByTagName("contentpath").item(0).getTextContent();
                bar.put("contentpath", contentPath);

                JSONArray jbpmns = new JSONArray();
                bar.put("bpmnModels", jbpmns);

                NodeList bpmnElements = document.getElementsByTagName("bpmn");
                for (int i = 0; i < bpmnElements.getLength(); i++) {
                    Element bpmnElement = (Element) bpmnElements.item(i);
                    String bpmnId = bpmnElement.getElementsByTagName("Id").item(0).getTextContent();
                    String bpmnName =
                            bpmnElement.getElementsByTagName("Name").item(0).getTextContent();
                    String bpmnPath =
                            bpmnElement.getElementsByTagName("Path").item(0).getTextContent();

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

    public String getProcessText(String textPath) {
        String textContent = "FAILED TO GET TEXT CONTENT";
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource textResource = reg.get(textPath);
                textContent = new String((byte[]) textResource.getContent());
            }
        } catch (Exception e) {
            log.error(e);
        }
        return textContent;
    }

    public String getBPMN(String bpmnPath) {

        String bpmnString = "";

        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                bpmnPath = bpmnPath.substring(ProcessCenterConstants.GREG_PATH.length());
                Resource bpmnAsset = reg.get(bpmnPath);
                String bpmnContent = new String((byte[]) bpmnAsset.getContent());
                JSONObject bpmn = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(bpmnContent)));
                Element overviewElement =
                        (Element) document.getElementsByTagName("overview").item(0);
                String bpmnName =
                        overviewElement.getElementsByTagName("name").item(0).getTextContent();
                String bpmnVersion =
                        overviewElement.getElementsByTagName("version").item(0).getTextContent();
                String bpmnDescription = overviewElement.getElementsByTagName("description").item(0)
                        .getTextContent();
                String processPath = overviewElement.getElementsByTagName("processpath").item(0)
                        .getTextContent();

                bpmn.put("name", bpmnName);
                bpmn.put("version", bpmnVersion);
                bpmn.put("description", bpmnDescription);
                bpmn.put("processPath", processPath);

                Element contentElement = (Element) document.getElementsByTagName("content").item(0);
                String contentPath =
                        contentElement.getElementsByTagName("contentpath").item(0).getTextContent();
                bpmn.put("contentpath", contentPath);
                String encodedBPMNImage = getEncodedBPMNImage(contentPath);
                bpmn.put("bpmnImage", encodedBPMNImage);

                bpmnString = bpmn.toString();
            }
        } catch (Exception e) {
            log.error("Failed to fetch BPMN model: " + bpmnPath);
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

            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                String[] processPaths = GovernanceUtils
                        .findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    String processName =
                            processXML.getElementsByTagName("name").item(0).getTextContent();
                    String processVersion =
                            processXML.getElementsByTagName("version").item(0).getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put("path", processPath);
                    processJSON.put("processid", processResource.getUUID());
                    processJSON.put("processname", processName);
                    processJSON.put("processversion", processVersion);
                    result.put(processJSON);
                }

                processDetails = result.toString();

                int a = 1;

            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String msg = "Failed";
            log.error(msg, e);
        }

        return processDetails;
    }

    public String getSuccessorPredecessorSubProcessList(String resourcePath) {
        String resourceString = "";
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessCenterConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());

                JSONObject conObj = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document =
                        builder.parse(new InputSource(new StringReader(resourceContent)));

                JSONArray subprocessArray = new JSONArray();
                JSONArray successorArray = new JSONArray();
                JSONArray predecessorArray = new JSONArray();

                conObj.put("subprocesses", subprocessArray);
                conObj.put("successors", successorArray);
                conObj.put("predecessors", predecessorArray);

                NodeList subprocessElements =
                        ((Element) document.getFirstChild()).getElementsByTagName("subprocess");
                NodeList successorElements =
                        ((Element) document.getFirstChild()).getElementsByTagName("successor");
                NodeList predecessorElements =
                        ((Element) document.getFirstChild()).getElementsByTagName("predecessor");

                if (subprocessElements.getLength() != 0) {
                    for (int i = 0; i < subprocessElements.getLength(); i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName =
                                subprocessElement.getElementsByTagName("name").item(0)
                                        .getTextContent();
                        String subprocessPath =
                                subprocessElement.getElementsByTagName("path").item(0)
                                        .getTextContent();
                        String subprocessId = subprocessElement.getElementsByTagName("id").item(0)
                                .getTextContent();
                        String subprocessVersion =
                                subprocessPath.substring(subprocessPath.lastIndexOf("/") + 1)
                                        .trim();

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
                        String successorName = successorElement.getElementsByTagName("name").item(0)
                                .getTextContent();
                        String successorPath = successorElement.getElementsByTagName("path").item(0)
                                .getTextContent();
                        String successorId = successorElement.getElementsByTagName("id").item(0)
                                .getTextContent();
                        String successorVersion =
                                successorPath.substring(successorPath.lastIndexOf("/") + 1).trim();

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
                        String predecessorName =
                                predecessorElement.getElementsByTagName("name").item(0)
                                        .getTextContent();
                        String predecessorPath =
                                predecessorElement.getElementsByTagName("path").item(0)
                                        .getTextContent();
                        String predecessorId = predecessorElement.getElementsByTagName("id").item(0)
                                .getTextContent();
                        String predecessorVersion =
                                predecessorPath.substring(predecessorPath.lastIndexOf("/") + 1)
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
            log.error("Failed to fetch Successor Predecessor and Subprocess information: " +
                    resourcePath, e);
        }
        return resourceString;
    }

    public boolean updateOwner(String ownerDetails) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(ownerDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String processOwner = processInfo.getString("processOwner");

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
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
            log.error("Failed to update the process owner", e);
        }
        return true;
    }

    public boolean addSubprocess(String subprocessDetails) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(subprocessDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject subprocess = processInfo.getJSONObject("subprocess");

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
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
            log.error("Failed to add a subprocess", e);
        }
        return true;
    }

    public boolean addSuccessor(String successorDetails) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(successorDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject successor = processInfo.getJSONObject("successor");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
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
            log.error("Failed to add a successor", e);
        }
        return true;
    }

    public boolean addPredecessor(String predecessorDetails) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(predecessorDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject predecessor = processInfo.getJSONObject("predecessor");

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
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
            log.error("Failed to add a predecessor", e);
        }
        return true;
    }

    public boolean deleteSubprocess(String deleteSubprocess) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deleteSubprocess);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject subprocess = processInfo.getJSONObject("deleteSubprocess");

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (subprocess != null) {
                    NodeList subprocessElements =
                            ((Element) doc.getFirstChild()).getElementsByTagName("subprocess");
                    for (int i = 0; i < subprocessElements.getLength(); i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName =
                                subprocessElement.getElementsByTagName("name").item(0)
                                        .getTextContent();
                        String subprocessPath =
                                subprocessElement.getElementsByTagName("path").item(0)
                                        .getTextContent();
                        String subprocessId = subprocessElement.getElementsByTagName("id").item(0)
                                .getTextContent();

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
            log.error("Failed to delete a subprocess", e);
        }
        return true;
    }

    public boolean deleteSuccessor(String deleteSuccessor) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deleteSuccessor);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject successor = processInfo.getJSONObject("deleteSuccessor");

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (successor != null) {
                    NodeList successorElements =
                            ((Element) doc.getFirstChild()).getElementsByTagName("successor");
                    for (int i = 0; i < successorElements.getLength(); i++) {
                        Element successorElement = (Element) successorElements.item(i);
                        String successorName = successorElement.getElementsByTagName("name").item(0)
                                .getTextContent();
                        String successorPath = successorElement.getElementsByTagName("path").item(0)
                                .getTextContent();
                        String successorId = successorElement.getElementsByTagName("id").item(0)
                                .getTextContent();

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
            log.error("Failed to delete a successor", e);
        }
        return true;
    }

    public boolean deletePredecessor(String deletePredecessor) {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(deletePredecessor);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject predecessor = processInfo.getJSONObject("deletePredecessor");

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                if (predecessor != null) {
                    NodeList predecessorElements =
                            ((Element) doc.getFirstChild()).getElementsByTagName("predecessor");
                    for (int i = 0; i < predecessorElements.getLength(); i++) {
                        Element predecessorElement = (Element) predecessorElements.item(i);
                        String predecessorName =
                                predecessorElement.getElementsByTagName("name").item(0)
                                        .getTextContent();
                        String predecessorPath =
                                predecessorElement.getElementsByTagName("path").item(0)
                                        .getTextContent();
                        String predecessorId = predecessorElement.getElementsByTagName("id").item(0)
                                .getTextContent();

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
            log.error("Failed to delete a predecessor", e);
        }
        return true;
    }

    public String getProcessDetails(String processPath) {

        String processDetails = "{}";
        JSONArray result = null;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource processResource = reg.get(processPath);
                String processContent = new String((byte[]) processResource.getContent());
                Document processXML = stringToXML(processContent);
                String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();
                String processOwner = processXML.getElementsByTagName("owner").item(0).getTextContent();
                String bpmnPath = processXML.getElementsByTagName("bpmnpath").item(0).getTextContent();
                String bpmnId = processXML.getElementsByTagName("bpmnid").item(0).getTextContent();
                String processTextPath = processXML.getElementsByTagName("processtextpath").item(0).getTextContent();

                JSONObject processJSON = new JSONObject();
                processJSON.put("path", processPath);
                processJSON.put("processid", processResource.getUUID());
                processJSON.put("processname", processName);
                processJSON.put("processversion", processVersion);
                processJSON.put("owner", processOwner);
                processJSON.put("bpmnpath", bpmnPath);
                processJSON.put("bpmnid", bpmnId);
                processJSON.put("processtextpath", processTextPath);

                result.put(processJSON);


                processDetails = result.toString();


            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String msg = "Failed";
            log.error(msg, e);
        }

        return processDetails;


    }


    public String updateVersion(String processPath, String updatedVersion) {

        String processId = "";

        try {
            JSONArray result = new JSONArray();

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();


                Resource processResource = reg.get(processPath);
                String processContent = new String((byte[]) processResource.getContent());
                Document processXML = stringToXML(processContent);
                String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                processXML.getElementsByTagName("version").item(0).setTextContent(updatedVersion);


                String updatedProcess = xmlToString(processXML);


                Resource processAsset = reg.newResource();
                processAsset.setContent(updatedProcess);
                processAsset.setMediaType("application/vnd.wso2-process+xml");
                String processAssetPath = "processes/" + processName + "/" + updatedVersion;
                reg.put(processAssetPath, processAsset);

                // associate lifecycle with the process asset, so that it can be promoted to published state
                GovernanceUtils.associateAspect(processAssetPath, "SampleLifeCycle2", reg);

                // apply tags to the resource


                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();
            }
        } catch (Exception e) {
            log.error(e);
        }

        return processId;


    }

    public JSONArray getProcessInformationByName(String filter, String filterType, String loggedUser){
        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                JSONArray result = new JSONArray();
                UserRegistry reg = null;
                reg = registryService.getGovernanceSystemRegistry();
                String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                    String processOwner = processXML.getElementsByTagName("owner").item(0).getTextContent();

                    String expValue = "", actValue = "";
                    actValue = filter;
                    if(ProcessCenterConstants.NAME.equals(filterType)){
                        expValue = processName;
                    }else if(ProcessCenterConstants.PROCESS_ID.equals(filterType)){
                        expValue = processResource.getUUID();
                    }

                    if(actValue.equals(expValue)){
                        if(loggedUser.equals(processOwner)) {
                            JSONObject processJSON = getJSONObjectFromProcessXML(processXML, processPath, processResource);
                            result.put(processJSON);
                        }
                    }
                }
                return result;
            }

        } catch (RegistryException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getJSONObjectFromProcessXML(Document processXML, String processPath, Resource processResource) throws JSONException {
        NodeList list = processXML.getElementsByTagName(ProcessCenterConstants.BOOKMARK_NODE);
        String bookmarked = "false";
        if(list.getLength() > 0)
            bookmarked = list.item(0).getTextContent();

        String keys[] = {"owner", "name", "version", "createdtime", "bpmnpath", "bpmnid", "processtextpath", "bookmarked"};
        JSONObject processJSON = new JSONObject();
        for (int i = 0; i < keys.length; i++) {
            if(processXML.getElementsByTagName(keys[i]).getLength() > 0) {
                processJSON.put(keys[i], processXML.getElementsByTagName(keys[i]).item(0).getTextContent());
            }else{
                processJSON.put(keys[i], "");
            }
        }
        processJSON.put("processid", processResource.getUUID());
        processJSON.put("path", processPath);
        processJSON.put("bookmarked", bookmarked);

        return processJSON;
    }



    public String getAdvanceSearchResult(org.wso2.carbon.processCenter.core.models.Process process, String loggedUser){

        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                JSONArray result = new JSONArray();
                UserRegistry reg = null;
                reg = registryService.getGovernanceSystemRegistry();
                String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                JSONObject object = getJSONObject(process);

                String keys[] = {"owner", "name", "version", "createdtime", "bpmnpath", "bpmnid", "processtextpath"};
                Hashtable<String, String> list = new Hashtable<>();
                for (int i = 0; i < keys.length; i++) {
                    if(!object.getString(keys[i]).isEmpty()){
                        list.put(keys[i], object.getString(keys[i]));
                    }
                }

                boolean matched = false;

                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    processXML.getElementsByTagName(ProcessCenterConstants.NAME).item(0).getTextContent();
                    String processOwner = processXML.getElementsByTagName(ProcessCenterConstants.OWNER).item(0).getTextContent();
                    if(processOwner.equals(loggedUser)) {
                        Iterator<String> listIterator = list.keySet().iterator();
                        while (listIterator.hasNext()) {
                            String key = listIterator.next();
                            if(!"NA".equals(processXML.getElementsByTagName(key).item(0).getTextContent())){
                                if (list.get(key).equals(processXML.getElementsByTagName(key).item(0).getTextContent())) {
                                    matched = true;
                                } else {
                                    matched = false;
                                }
                            }
                        }

                        if (matched) {
                            JSONObject processJSON = getJSONObjectFromProcessXML(processXML, processPath, processResource);
                            result.put(processJSON);
                        }
                    }
                }
                return result.toString();
            }
        } catch (RegistryException e) {
            log.error(e);
        } catch (JSONException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "{}";
    }

    private JSONObject getJSONObject(org.wso2.carbon.processCenter.core.models.Process process) throws JSONException {
        JSONObject object = new JSONObject();
        String keys[] = {"owner", "name", "version", "createdtime", "bpmnpath", "bpmnid", "processtextpath"};
        object.put(keys[0], process.getOwner());
        object.put(keys[1], process.getName());
        object.put(keys[2], process.getVersion());
        object.put(keys[3], process.getCreatedtime());
        object.put(keys[4], process.getBpmnpath());
        object.put(keys[5], process.getBpmnid());
        object.put(keys[6], process.getProcesstextpath());
        return object;
    }

    public int bookMarkProcess(String processID){
        int result = -1;
        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = null;
                reg = registryService.getGovernanceSystemRegistry();
                String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);

                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    String processid = processResource.getUUID();

                    if(processid.equals(processID)){
                        Element parent = (Element)processXML.getElementsByTagName("properties").item(0);
                        NodeList list = processXML.getElementsByTagName(ProcessCenterConstants.BOOKMARK_NODE);
                        if(list.getLength() == 0){
                            appendText(processXML, parent, ProcessCenterConstants.BOOKMARK_NODE, mns, "true");
                            result = 1;
                        }else{
                            if("false".equals(processXML.getElementsByTagName(ProcessCenterConstants.BOOKMARK_NODE).item(0).getTextContent())){
                                processXML.getElementsByTagName(ProcessCenterConstants.BOOKMARK_NODE).item(0).setTextContent("true");
                                result = 1;
                            }else{
                                processXML.getElementsByTagName(ProcessCenterConstants.BOOKMARK_NODE).item(0).setTextContent("false");
                                result = 0;
                            }
                        }
                        String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                        String processVersion = processXML.getElementsByTagName(ProcessCenterConstants.VERSION).item(0).getTextContent();
                        String processAssetPath =
                                ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                        processVersion;
                        Resource resource = reg.get(processAssetPath);
                        String newProcessContent = xmlToString(processXML);
                        resource.setContent(newProcessContent);
                        reg.put(processAssetPath, resource);
                    }
                }
            }
        } catch (RegistryException e) {
            log.error(e.getMessage(), e);
        } catch (TransformerException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    public String getBookmarkedProcessList(String user) {
        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                JSONArray result = new JSONArray();
                UserRegistry reg = null;
                reg = registryService.getGovernanceSystemRegistry();
                String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);

                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);
                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = stringToXML(processContent);
                    JSONObject processObject = getJSONObjectFromProcessXML(processXML, processPath, processResource);
                    if(user.equals(processObject.getString(ProcessCenterConstants.OWNER))){
                        if(ProcessCenterConstants.TRUE.equals(processObject.getString(ProcessCenterConstants.BOOKMARK_NODE))){
                            result.put(processObject);
                        }
                    }
                }
                return result.toString();
            }
        } catch (RegistryException e) {
            log.error(e.getMessage(), e);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public byte[] getBPMNImage(String path) throws ProcessCenterException {

        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource bpmnXMLResource = reg.get(path);
                byte[] bpmnContent = (byte[]) bpmnXMLResource.getContent();
                InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);

                BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
                BpmnModel bpmnModel =
                        bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, false, false);

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
}

