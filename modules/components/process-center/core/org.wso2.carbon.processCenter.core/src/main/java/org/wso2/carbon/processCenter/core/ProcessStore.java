package org.wso2.carbon.processCenter.core;

/**
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.governance.api.util.GovernanceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.processCenter.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.processCenter.core.models.*;
import org.wso2.carbon.processCenter.core.models.Process;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
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
import java.io.IOException;
import java.io.StringReader;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by dilini on 1/14/16.
 */

public class ProcessStore {

    private Log log = LogFactory.getLog(ProcessStore.class);
    private static final String mns = "http://www.wso2.org/governance/metadata";

    public ProcessStore(){

    }

    private Document stringToXML(String xmlString){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

        String keys[] = {"owner", "name", "version", "createdtime", "bpmnpath", "bpmnid", "processtextpath"};
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
            log.error(e.getMessage());
        }
        return textContent;
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
        }
        return "{}";
    }

    private JSONObject getJSONObject(Process process) throws JSONException {
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
        }
        return result;
    }
    public String getSuccessorPredecessorSubProcessList(String resourcePath) {
        String resourceString = "";
        try{
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessCenterConstants.GREG_PATH.length());
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

                NodeList subprocessElements = ((Element)document.getFirstChild()).getElementsByTagName("subprocess");
                NodeList successorElements = ((Element)document.getFirstChild()).getElementsByTagName("successor");
                NodeList predecessorElements = ((Element)document.getFirstChild()).getElementsByTagName("predecessor");

                if(subprocessElements.getLength() != 0) {
                    for (int i = 0 ; i < subprocessElements.getLength() ; i++) {
                        Element subprocessElement = (Element) subprocessElements.item(i);
                        String subprocessName = subprocessElement.getElementsByTagName("name").item(0).getTextContent();
                        String subprocessPath =
                                subprocessElement.getElementsByTagName("path").item(0).getTextContent();
                        String subprocessId =
                                subprocessElement.getElementsByTagName("id").item(0).getTextContent();
                        String subprocessVersion = subprocessPath.substring(subprocessPath.lastIndexOf("/") + 1).trim();

                        JSONObject subprocess = new JSONObject();
                        subprocess.put("name", subprocessName);
                        subprocess.put("path", subprocessPath);
                        subprocess.put("id", subprocessId);
                        subprocess.put("version", subprocessVersion);
                        subprocessArray.put(subprocess);
                    }
                }

                if(successorElements.getLength() != 0) {
                    for(int i = 0 ; i < successorElements.getLength() ; i++) {
                        Element successorElement = (Element) successorElements.item(i);
                        String successorName = successorElement.getElementsByTagName("name").item(0).getTextContent();
                        String successorPath =
                                successorElement.getElementsByTagName("path").item(0).getTextContent();
                        String successorId =
                                successorElement.getElementsByTagName("id").item(0).getTextContent();
                        String successorVersion = successorPath.substring(successorPath.lastIndexOf("/") + 1).trim();

                        JSONObject successor = new JSONObject();
                        successor.put("name", successorName);
                        successor.put("path", successorPath);
                        successor.put("id", successorId);
                        successor.put("version", successorVersion);
                        successorArray.put(successor);
                    }
                }

                if(predecessorElements.getLength() != 0) {
                    for(int i = 0 ; i < predecessorElements.getLength() ; i++) {
                        Element predecessorElement = (Element) predecessorElements.item(i);
                        String predecessorName = predecessorElement.getElementsByTagName("name").item(0).getTextContent();
                        String predecessorPath =
                                predecessorElement.getElementsByTagName("path").item(0).getTextContent();
                        String predecessorId =
                                predecessorElement.getElementsByTagName("id").item(0).getTextContent();
                        String predecessorVersion = predecessorPath.substring(predecessorPath.lastIndexOf("/") + 1).trim();

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
        }catch (Exception e) {
            log.error("Failed to fetch Successor Predecessor and Subprocess information: " + resourcePath, e);
        }
        return resourceString;
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
        }
        return null;
    }
}
