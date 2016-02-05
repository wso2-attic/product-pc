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
package org.wso2.carbon.processCenter.core;
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
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dilini on 1/14/16.
 */
public class ProcessInformationViewer {

    private Log log = LogFactory.getLog(ProcessInformationViewer.class);

    public ProcessInformationViewer(){
        System.out.println("in");
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

    public String getProcessInformationByName(String name, String loggedUser){
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
                    JSONObject object = new JSONObject(name);
                    if(object.getString(ProcessCenterConstants.NAME).equals(processName)){
                        if(loggedUser.equals(processOwner)) {
                            String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();
                            String bpmnpath = processXML.getElementsByTagName(ProcessCenterConstants.BPMN_PATH).item(0).getTextContent();
                            String bpmnid = processXML.getElementsByTagName(ProcessCenterConstants.BPMN_ID).item(0).getTextContent();
                            String processtext = processXML.getElementsByTagName(ProcessCenterConstants.PROCESS_TEXT_PATH).item(0).getTextContent();
                            JSONObject processJSON = new JSONObject();
                            processJSON.put("path", processPath);
                            processJSON.put("processid", processResource.getUUID());
                            processJSON.put("processname", processName);
                            processJSON.put("processversion", processVersion);
                            processJSON.put("processowner", processOwner);
                            processJSON.put("bpmnpath", bpmnpath);
                            processJSON.put("bpmnid", bpmnid);
                            processJSON.put("processtextpath", processtext);
                            result.put(processJSON);
                        }
                    }
                }
                return result.toString();
            }

        } catch (RegistryException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public String getAdvanceSearchResults(String filters, String loggedUser){
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
                    JSONObject object = new JSONObject(filters);
                    String attribute = null;
                    if(object.getString(ProcessCenterConstants.ATTRIBUTE).startsWith(ProcessCenterConstants.OVERVIEW))
                        attribute = object.getString(ProcessCenterConstants.ATTRIBUTE).substring(9);
                    else if(object.getString(ProcessCenterConstants.ATTRIBUTE).startsWith(ProcessCenterConstants.PROPERTY))
                        attribute = object.getString(ProcessCenterConstants.ATTRIBUTE).substring(11);

                    if(attribute != null) {
                        String attrValue = processXML.getElementsByTagName(attribute).item(0).getTextContent();
                        if (object.getString(ProcessCenterConstants.VALUE).equals(attrValue)) {
                            String processOwner = processXML.getElementsByTagName(ProcessCenterConstants.OWNER).item(0).getTextContent();
                            if (loggedUser.equals(processOwner)) {
                                String processName = processXML.getElementsByTagName(ProcessCenterConstants.NAME).item(0).getTextContent();
                                String processVersion = processXML.getElementsByTagName(ProcessCenterConstants.VERSION).item(0).getTextContent();
                                String bpmnpath = processXML.getElementsByTagName(ProcessCenterConstants.BPMN_PATH).item(0).getTextContent();
                                String bpmnid = processXML.getElementsByTagName(ProcessCenterConstants.BPMN_ID).item(0).getTextContent();
                                String processtext = processXML.getElementsByTagName(ProcessCenterConstants.PROCESS_TEXT_PATH).item(0).getTextContent();
                                JSONObject processJSON = new JSONObject();
                                processJSON.put("path", processPath);
                                processJSON.put("processid", processResource.getUUID());
                                processJSON.put("processname", processName);
                                processJSON.put("processversion", processVersion);
                                processJSON.put("processowner", processOwner);
                                processJSON.put("bpmnpath", bpmnpath);
                                processJSON.put("bpmnid", bpmnid);
                                processJSON.put("processtext", processtext);
                                result.put(processJSON);
                            }
                        }
                    }
                }
                return result.toString();
            }
        } catch (RegistryException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "{}";
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

    public String getAdvanceSearchResult(String filters, String loggedUser){
        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                JSONArray result = new JSONArray();
                UserRegistry reg = null;
                reg = registryService.getGovernanceSystemRegistry();
                String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                JSONObject object = new JSONObject(filters);
                int count = 0;
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
                        //check whether all the filters match this process.
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
                            JSONObject processJSON = new JSONObject();
                            for (int i = 0; i < keys.length; i++) {
                                if(!"NA".equals(processXML.getElementsByTagName(keys[i]).item(0).getTextContent())) {
                                    processJSON.put(keys[i], processXML.getElementsByTagName(keys[i]).item(0).getTextContent());
                                }else{
                                    processJSON.put(keys[i], "");
                                }
                            }
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
}
