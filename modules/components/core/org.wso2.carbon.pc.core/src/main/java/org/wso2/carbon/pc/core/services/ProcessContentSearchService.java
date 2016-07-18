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

package org.wso2.carbon.pc.core.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.common.ResourceData;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.indexing.IndexingConstants;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for content based search on Process-Center assets.
 */
public class ProcessContentSearchService {

    private static final Log log = LogFactory.getLog(ProcessContentSearchService.class);
    private static final Map<String, String> mediatypes;

    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put(ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PDF, ProcessCenterConstants.PROCESS_CONTENT_SEARCH
                .PDF_MEDIATYPE);
        aMap.put(ProcessCenterConstants.PROCESS_CONTENT_SEARCH.DOCUMENT, ProcessCenterConstants.PROCESS_CONTENT_SEARCH.DOCUMENT_MEDIATYPE);
        aMap.put(ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PROCESS_TEXT, ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PROCESS_TEXT_MEDIATYPE);
        aMap.put(ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PROCESS, ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PROCESS_MEDIATYPE);
        mediatypes = Collections.unmodifiableMap(aMap);
    }

    /**
     * @param searchQuery
     * @param mediaType
     * @param username
     * @return
     * @throws ProcessCenterException
     */
    public String search(String searchQuery, String mediaType, String username) throws ProcessCenterException {

        String processString = "FAILED TO GET PROCESS LIST";
        String mediaTypeStr;

        AttributeSearchService attributeSearchService =  ProcessCenterServerHolder.getInstance()
                .getAttributeSearchService();
        try {
            //get current logged in user.
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(username);
            JSONArray mediaTypeArr = new JSONArray(mediaType);

            //creating the mediatype value string
            mediaTypeStr = mediaTypeArr.length() > 0 ? mediatypes.get(mediaTypeArr.get(0)) : "";
            for (int i = 1; i < mediaTypeArr.length(); i++) {
                mediaTypeStr += " OR " + mediatypes.get(mediaTypeArr.get(i));
            }

            //mediatype value format for multiple mediatypes : (mediatype1 OR mediatype2)
            mediaTypeStr = mediaTypeArr.length() > 1 ? "(" + mediaTypeStr + ")" : mediaTypeStr;

            Map<String, String> input = new HashMap<>();
            input.put(IndexingConstants.FIELD_MEDIA_TYPE, mediaTypeStr);
            input.put(IndexingConstants.FIELD_CONTENT, searchQuery);
            ResourceData[] resources = attributeSearchService.search(input);

            if (resources != null && resources.length > 0) {

                //keeping the process resources in a hashmap to avoid duplicates
                HashMap<String, JSONObject> processMap = new HashMap<>();

                RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
                if (registryService != null) {
                    UserRegistry reg = registryService.getGovernanceSystemRegistry();

                    for (ResourceData resource : resources) {

                        String resourcePath = resource.getResourcePath();
                        //fetch the associations of the process_association type for the resources
                        Association[] associations = reg
                                .getAssociations(resourcePath.substring("/_system/governance/".length()),
                                        ProcessCenterConstants.ASSOCIATION_TYPE);

                        //get the process resource for each association
                        for (Association association : associations) {
                            String destinationPath = association.getDestinationPath();
                            Resource processResource = reg.get(destinationPath);
                            String lifecycleState = processResource
                                    .getProperty("registry.lifecycle.SampleLifeCycle2.state");

                            String processContent = new String((byte[]) processResource.getContent());
                            Document processXML = stringToXML(processContent);
                            String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                            String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();

                            JSONObject processJSON = new JSONObject();
                            processJSON.put("id", processResource.getUUID());
                            processJSON.put("type", "process");
                            processJSON.put("path", destinationPath);
                            processJSON.put("lifecycle", "SampleLifeCycle2");
                            processJSON.put("mediaType", ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PROCESS_MEDIATYPE);
                            processJSON.put("name", processName);
                            processJSON.put("version", processVersion);
                            processJSON.put("lifecycleState", lifecycleState);

                            JSONObject attributes = new JSONObject();
                            attributes.put("overview_name", processName);
                            attributes.put("overview_version", processVersion);

                            processJSON.put("attributes", attributes);
                            processJSON.put("_default", false);
                            processJSON.put("showType", true);

                            processMap.put(processResource.getUUID(), processJSON);

                        }
                    }
                    JSONArray results = new JSONArray(processMap.values());
                    processString = results.toString();
                }
            }
        } catch (Exception ex) {
            String message = "Registry service not available for retrieving processes.";
            log.error(message, ex);
            throw new ProcessCenterException(message, ex);
        }

        return processString;

    }

    private Document stringToXML(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        return document;
    }

}
