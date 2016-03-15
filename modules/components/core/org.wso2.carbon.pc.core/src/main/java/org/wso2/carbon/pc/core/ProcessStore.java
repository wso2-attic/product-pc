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

//import com.sun.javafx.scene.web.Debugger;
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
import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ProcessStore {
	private static final Log log = LogFactory.getLog(ProcessStore.class);

	private static final String mns = "http://www.wso2.org/governance/metadata";
	private static final String OK = "OK";

	public String getProcessVariablesList(String resourcePath) {
		String resourceString = "";
		//Element variableElementsArray[] = null;
		try {
			RegistryService registryService =
					ProcessCenterServerHolder.getInstance().getRegistryService();
			if (registryService != null) {
				UserRegistry reg = registryService.getGovernanceSystemRegistry();
				resourcePath = resourcePath.substring(ProcessStoreConstants.GREG_PATH.length());
				Resource resourceAsset = reg.get(resourcePath);
				String resourceContent = new String((byte[]) resourceAsset.getContent());

				JSONObject conObj = new JSONObject();
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				Document document =
						builder.parse(new InputSource(new StringReader(resourceContent)));

				JSONArray variableArray = new JSONArray();

				conObj.put("processVariables", variableArray);

				NodeList processVariableElements =
						((Element) document.getFirstChild()).getElementsByTagName("process_variable");

				if (processVariableElements.getLength() != 0) {
					for (int i = 0; i < processVariableElements.getLength(); i++) {
						Element processVariableElement = (Element) processVariableElements.item(i);
						String processVariableName =
								processVariableElement.getElementsByTagName("name").item(0)
										.getTextContent();
						String processVariableType =
								processVariableElement.getElementsByTagName("type").item(0)
										.getTextContent();

						JSONObject processVariable = new JSONObject();
						processVariable.put("name", processVariableName);
						processVariable.put("type", processVariableType);
						variableArray.put(processVariable);
					}
				}
				resourceString = conObj.toString();

				/*//conObj.put("variable", variablesArray);

				NodeList variableElements =
						((Element) document.getFirstChild()).getElementsByTagName("process_variable");
				//variableElementsArray= new Element[variableElements.getLength()];

				if (variableElements.getLength() != 0) {
					for (int i = 0; i < variableElements.getLength(); i++) {
						Element variableElement = (Element) variableElements.item(i);
						String variableName=variableElement.getFirstChild().getNodeValue(); //getAttribute("name");
						String variableType=variableElement.getLastChild().getNodeValue();//getAttribute("type");

						JSONObject processVariabeJob = new JSONObject();
						processVariabeJob.put("name", variableName);
						processVariabeJob.put("type", variableType);
						//processVariabeJob.put("id", subprocessId);
						//processVariabeJob.put("version", subprocessVersion);
						variablesJobArray.put(processVariabeJob);

						//variableElementsArray[i]= (Element) variableElements.item(i);
					}
				}*/

			}
		} catch (Exception e) {
			log.error("Failed to get the process variables list");
		}
		return resourceString;
	}



	public boolean saveProcessVariables(String processVariableDetails){
		try {
			RegistryService registryService =
					ProcessCenterServerHolder.getInstance().getRegistryService();

			if (registryService != null) {
				UserRegistry reg = registryService.getGovernanceSystemRegistry();

				JSONObject processInfo = new JSONObject(processVariableDetails);
				String processName = processInfo.getString("processName");
				String processVersion = processInfo.getString("processVersion");
				String processAssetPath =
						ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
								processVersion;
				Resource resource = reg.get(processAssetPath);
				String processContent = new String((byte[]) resource.getContent());
				Document doc = stringToXML(processContent);

				JSONObject processVariablesJOb = processInfo.getJSONObject("processVariables");

				//Map<String, Object> map = (Map<String,Object>)processVariablesJOb;
				 Iterator<?> keys = processVariablesJOb.keys();

				/*//saving pracess variable name,type as element attributes
				while (	keys.hasNext()){
					String variableName = (String)keys.next();
					//if (Debugger.isEnabled())
						log.debug(variableName);
					String variableType=processVariablesJOb.get(variableName).toString();
					//JSONObject processVariableJOb= (JSONObject) processVariablesArray.get(i);
					Element rootElement = doc.getDocumentElement();

					Element variableElement=append(doc,rootElement,"processVariable",mns);
					variableElement.setAttribute("name",variableName);
					variableElement.setAttribute("type",variableType);
					//variableElement.setNodeValue("My value");
					//variableElement.setTextContent("My value 2");

					//appendText(doc,rootElement,"name",mns,variableName);
					//appendText(doc,rootElement,"type",mns,processVariablesJOb.get(variableName).toString());

					String newProcessContent = xmlToString(doc);
					resource.setContent(newProcessContent);
					reg.put(processAssetPath, resource);
				}*/
				//saving pracess variable name,type as sub elements
				while (	keys.hasNext()){
					String variableName = (String)keys.next();
					//if (Debugger.isEnabled())
					log.debug(variableName);
					String variableType=processVariablesJOb.get(variableName).toString();
					//JSONObject processVariableJOb= (JSONObject) processVariablesArray.get(i);
					Element rootElement = doc.getDocumentElement();
					Element variableElement=append(doc,rootElement,"process_variable",mns);
					appendText(doc,variableElement,"name",mns,variableName);
					appendText(doc,variableElement,"type",mns,variableType);

					String newProcessContent = xmlToString(doc);
					resource.setContent(newProcessContent);
					reg.put(processAssetPath, resource);
				}

			}
		}catch (Exception e){
			log.error("Failed to save processVariables", e);
		}
		return true;
	}

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

                Element pdfElement = append(doc, rootElement, "pdf", mns);
                appendText(doc, pdfElement, "path", mns, "NA");

                Element bpmnDesignElement = append(doc, rootElement, "bpmnDesign", mns);
                appendText(doc, bpmnDesignElement, "bpmnDesignPath", mns, "NA");

                Element flowchartElement = append(doc, rootElement, "flowchart", mns);
                appendText(doc, flowchartElement, "path", mns, "NA");

                Element documentElement = append(doc, rootElement, "document", mns);
                appendText(doc, documentElement, "documentname", mns, "NA");
                appendText(doc, documentElement, "summary", mns, "NA");
                appendText(doc, documentElement, "url", mns, "NA");
                appendText(doc, documentElement, "path", mns, "NA");

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
                if (processText != null && processText.length() > 0) {
                    Resource processTextResource = reg.newResource();
                    processTextResource.setContent(processText);
                    processTextResource.setMediaType("text/html");
                    reg.put(processTextResourcePath, processTextResource);
                    doc.getElementsByTagName("processtextpath").item(0).setTextContent(processTextResourcePath);
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

    public String getProcessText(String textPath) {
        String textContent = "FAILED TO GET TEXT CONTENT";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
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
            log.error("Failed to fetch BPMN model: " + bpmnPath);
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
                    String pdfPath = processXML.getElementsByTagName("pdf").item(0).getFirstChild().getTextContent();
                    String flowchartPath = processXML.getElementsByTagName("flowchart").item(0).getFirstChild().getTextContent();

                    JSONObject processJSON = new JSONObject();
                    processJSON.put("path", processPath);
                    processJSON.put("processid", processResource.getUUID());
                    processJSON.put("processname", processName);
                    processJSON.put("processversion", processVersion);
                    processJSON.put("pdfpath", pdfPath);
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
            String msg = "Failed";
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
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

                NodeList subprocessElements = ((Element) document.getFirstChild()).getElementsByTagName(
                        "subprocess");
                NodeList successorElements = ((Element) document.getFirstChild()).getElementsByTagName("successor");
                NodeList predecessorElements = ((Element) document.getFirstChild()).getElementsByTagName(
                        "predecessor");

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

    public boolean updateOwner(String ownerDetails) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(ownerDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String processOwner = processInfo.getString("processOwner");

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
            log.error("Failed to update the process owner", e);
        }
        return true;
    }

    public boolean addSubprocess(String subprocessDetails) {
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
            log.error("Failed to add a subprocess", e);
        }
        return true;
    }

    public boolean addSuccessor(String successorDetails) {
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
            log.error("Failed to add a successor", e);
        }
        return true;
    }

    public boolean addPredecessor(String predecessorDetails) {
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
            log.error("Failed to add a predecessor", e);
        }
        return true;
    }

    public boolean deleteSubprocess(String deleteSubprocess) {
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
            log.error("Failed to delete a subprocess", e);
        }
        return true;
    }

    public boolean deleteSuccessor(String deleteSuccessor) {
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
            log.error("Failed to delete a successor", e);
        }
        return true;
    }

    public boolean deletePredecessor(String deletePredecessor) {
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
            log.error("Failed to delete a predecessor", e);
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
                    docContentPath = "doccontent/" + processName + "/" + processVersion + "/" + docName +
                            "." + docExtension;
                    reg.put(docContentPath, docContentResource);
                }

                String processAssetPath = ProcessStoreConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = stringToXML(processContent);

                int numOfDocAttrs = doc.getElementsByTagName("document").item(0).getChildNodes().getLength();
                NodeList nodeList = doc.getElementsByTagName("document").item(0).getChildNodes();

                if(numOfDocAttrs != 0) {
                    for(int i = 0; i < numOfDocAttrs; i++) {
                        if(nodeList.item(i).getNodeName().equals("documentname")) {
                            nodeList.item(i).setTextContent(docName);
                        }else if(nodeList.item(i).getNodeName().equals("summary")){
                            nodeList.item(i).setTextContent(docSummary);
                        }else if(nodeList.item(i).getNodeName().equals("url")){
                            if ((docUrl != null) && (!docUrl.isEmpty())) {
                                nodeList.item(i).setTextContent(docUrl);
                            }
                        }else if(nodeList.item(i).getNodeName().equals("path")){
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
        } catch (Exception e) {
            log.error("Upload documentation error.");
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
                NodeList documentElements = ((Element) document.getFirstChild()).getElementsByTagName("document");

                if (documentElements.getLength() != 0) {
                    for (int i = 0; i < documentElements.getLength(); i++) {
                        Element documentElement = (Element) documentElements.item(i);
                        String docName = documentElement.getElementsByTagName("documentname").item(0).getTextContent();
                        String docSummary = documentElement.getElementsByTagName("summary").item(0).getTextContent();
                        String docUrl = documentElement.getElementsByTagName("url").item(0).getTextContent();
                        String docPath = documentElement.getElementsByTagName("path").item(0).getTextContent();

                        JSONObject processDoc = new JSONObject();
                        processDoc.put("documentname", docName);
                        processDoc.put("summary", docSummary);
                        processDoc.put("url", docUrl);
                        processDoc.put("path", docPath);
                        documentArray.put(processDoc);
                    }
                }
                documentString = documentArray.toString();
            }
        }catch (Exception e){
            log.error("Failed to fetch document: " + resourcePath);
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
        } catch (Exception e) {
            log.error("Failed to fetch document: " + resourcePath);
        }
        return docString;
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
            String msg = "Failed";
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }

        return textContent;

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
                pdfContentResource.setMediaType("application/pdf");
                String pdfContentPath = "pdf/" + processName + "/" + processVersion;
                reg.put(pdfContentPath, pdfContentResource);
                String processPath = "processes/" + processName + "/" + processVersion;

                // update process by linking the pdf asset

                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document pdoc = stringToXML(processContent);

                pdoc.getElementsByTagName("pdf").item(0).getFirstChild().setTextContent(pdfContentPath);
                String newProcessContent = xmlToString(pdoc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            log.error(e);
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
                pdfPath = pdfPath.substring("/_system/governance/".length());
                Resource pdfAsset = reg.get(pdfPath);
                byte[] pdfContent = (byte[]) pdfAsset.getContent();
                pdfString = new sun.misc.BASE64Encoder().encode(pdfContent);
                if (log.isDebugEnabled()) {
                    log.debug("PDF PATH:" + pdfPath);
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch pdf: " + pdfPath);
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
                flowchartContentResource.setMediaType("application/json");
                String flowchartContentPath = "flowchart/" + processName + "/" + processVersion;
                reg.put(flowchartContentPath, flowchartContentResource);
                String processPath = "processes/" + processName + "/" + processVersion;

                // update process by linking the pdf asset
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document processXMLContent = stringToXML(processContent);

                processXMLContent.getElementsByTagName("flowchart").item(0).getFirstChild().setTextContent(flowchartContentPath);

                String newProcessContent = xmlToString(processXMLContent);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
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
                flowchartPath = flowchartPath.substring("/_system/governance/".length());
                Resource flowchartAsset = reg.get(flowchartPath);
                flowchartString = new String((byte[]) flowchartAsset.getContent());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return flowchartString;
    }

    /**
     *
     * @param processName
     * @param processVersion
     * @param bpmndesignJson
     * @return the bpmn diagram json string for a process
     */
    public String uploadBpmnDesign(String processName, String processVersion, String bpmndesignJson) {
        String processId = "NA";
        log.debug("Creating bpmn design...");
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource bpmnDesignContentResource = reg.newResource();
                bpmnDesignContentResource.setContent(bpmndesignJson);
                bpmnDesignContentResource.setMediaType("application/json");
                String bpmnDesignContentPath = "bpmnDesign/" + processName + "/" + processVersion;
                reg.put(bpmnDesignContentPath, bpmnDesignContentResource);
                String processPath = "processes/" + processName + "/" + processVersion;

                // update process by linking the pdf asset
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document processXMLContent = stringToXML(processContent);
                processXMLContent.getElementsByTagName("bpmnDesign").item(0).getFirstChild().setTextContent(bpmnDesignContentPath);

                String newProcessContent = xmlToString(processXMLContent);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
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
                bpmnDiagramPath = bpmnDiagramPath.substring("/_system/governance/".length());
                Resource bpmnDiagramAsset = reg.get(bpmnDiagramPath);
                bpmnDiagramString = new String((byte[]) bpmnDiagramAsset.getContent());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return bpmnDiagramString;
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