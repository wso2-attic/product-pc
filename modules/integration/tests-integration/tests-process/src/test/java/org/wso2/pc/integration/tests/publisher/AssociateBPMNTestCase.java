/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.wso2.pc.integration.tests.publisher;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wink.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.pc.integration.test.utils.base.*;
import org.xml.sax.InputSource;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;

public class AssociateBPMNTestCase extends PCIntegrationBaseTest {

    private GenericRestClient genericRestClient;
    private HashMap<String, String> headerMap;
    private HashMap<String, String> queryMap;
    private String resourcePath;
    private String cookieHeader;
    private RegistryProviderUtil registryProviderUtil = new RegistryProviderUtil();
    private static final String PROCESS_NAME="TestProcess1";
    private static final String PROCESS_VERSION="1.0";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        String publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services","publisher/apis");
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "create-process.json";
        JSONObject objSessionPublisher =
                new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                        automationContext.getSuperTenant().getTenantAdmin().getUserName(),
                        automationContext.getSuperTenant().getTenantAdmin().getPassword(), queryMap,
                        headerMap).getEntity(String.class));
        String jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;
    }

    @Test(groups = {"org.wso2.pc"}, description = "Adding process in to publisher")
    public void addProcess() throws IOException, JSONException {
        String requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "create_process", MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader);
        response.getStatusCode();
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Associating BPMN to the process",
            dependsOnMethods = "addProcess")
    public void uploadBPMN() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "BPMN"
                + File.separator + "userTaskProcess.bpmn20.xml";
        String url = publisherAPIBaseUrl + "upload_bpmn";
        PostMethod httpMethod = ArtifactUploadUtil.uploadBPMN(resourcePath1,
                PROCESS_NAME, PROCESS_VERSION, "BPMN", cookieHeader, url);
        Assert.assertTrue(httpMethod.getStatusCode() == 302,
                "Wrong status code ,Expected 302 ,Received " + httpMethod.getStatusCode());
    }

    @Test(groups = {"org.wso2.pc"}, description = "Checking associated BPMN",
            dependsOnMethods = "uploadBPMN")
    public void checkBPMN() throws Exception {
        Element bpmnElement = getAssociateProcess("name");
        Assert.assertNotNull(bpmnElement,"Associated BPMN doesn't exist");
        Assert.assertTrue(bpmnElement.getTextContent().equals(PROCESS_NAME),
                "TestProcess1 doesn't have associated BPMN");
    }

    @Test(groups = {"org.wso2.pc"}, description = "BPMN deleting test case",
            dependsOnMethods = "checkBPMN")
    public void deleteBPMN() throws JSONException {
        queryMap.put(PCIntegrationConstants.PROCESS_NAME, PROCESS_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION,PROCESS_VERSION);
        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                "delete_bpmn",MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON,null,
                queryMap,headerMap,cookieHeader);
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"),"Couldn't delete BPMN");
    }

    private Element getAssociateProcess(String processType) throws Exception {
        Element associateProcessElement = null;
        WSRegistryServiceClient wsRegistryServiceClient = registryProviderUtil.
                getWSRegistry(automationContext);
        String xml = new String(wsRegistryServiceClient.
                getContent("/_system/governance/bpmn/TestProcess1/1.0"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        Element root = document.getDocumentElement();
        if (root.getElementsByTagName(processType) != null)
            associateProcessElement = (Element) root.getElementsByTagName(processType).item(0);
        return associateProcessElement;
    }
}