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

package org.wso2.pc.integration.tests.publisher.processes;

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
import org.wso2.pc.integration.test.utils.base.RegistryProviderUtil;

public class AssociateURLTestCase extends PCIntegrationBaseTest{
    private String cookieHeader;
    private GenericRestClient genericRestClient;
    private HashMap<String, String> queryMap;
    private HashMap<String, String> headerMap;
    private String resourcePath;
    private static final String GDOC_URL = "https://docs.google.com/a/wso2.com/document/d/" +
            "19UbwEpV36EbD2OY6MomrldkFIYjOnmR8drRrx7zp3pA/edit?usp=sharing";
    private static final String ASSOCIATED_GDOC_NAME = "TestGDocOfTheProcess";
    private static final String ASSOCIATES_GDOC_SUMMARY = "This is the summary ofGDoc";
    private static final String PROCESS_NAME = "TestProcess1";
    private static final String PROCESS_VERSION = "1.0";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        String publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", PCIntegrationConstants.DESIGNER_APIS);
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "process" + File.separator+ "create-process.json";
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

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherProcessAPIBaseUrl +
                        "create_process", MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader);
        response.getStatusCode();
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Associating PDF to the process",
            dependsOnMethods = "addProcess")
    public void associateGDoc() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "other" + File.separator + "EmptyFile";
        String url = publisherProcessAPIBaseUrl + "upload_documents";
        PostMethod httpMethod = ArtifactUploadUtil.uploadDocument(resourcePath1, ASSOCIATED_GDOC_NAME,
                ASSOCIATES_GDOC_SUMMARY,"",GDOC_URL,"file", PROCESS_NAME,PROCESS_VERSION,
                cookieHeader,url,PCIntegrationConstants.APPLICATION_OCTET_STREAM);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 200 ,Received " + httpMethod.getStatusCode());
    }

    @Test(groups = {"org.wso2.pc"}, description = "Check associated GDOC document existence",
            dependsOnMethods = "associateGDoc")
    public void checkGDoc() throws Exception {
        RegistryProviderUtil registryProviderUtil = new RegistryProviderUtil();
        WSRegistryServiceClient wsRegistryServiceClient = registryProviderUtil.
                getWSRegistry(automationContext);
        String xml = new String(wsRegistryServiceClient.
                getContent("/_system/governance/processes/TestProcess1/1.0"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        Element root = document.getDocumentElement();
        Assert.assertNotNull(root.getElementsByTagName("document").item(0),"No document found");
        String expectedGDocURL = ((Element)root.getElementsByTagName("document").item(0)).
                getElementsByTagName("url").item(0).getTextContent();
        Assert.assertTrue(expectedGDocURL.equals(GDOC_URL),"Expected GDoc URL not found");
    }

    @Test(groups = {"org.wso2.pc"}, description = "GDoc deleting test case",
            dependsOnMethods = "checkGDoc")
    public void deleteGDOC() throws JSONException, IOException {
        String PDFDeleteRequest = readFile(FrameworkPathUtil.getSystemResourceLocation() +
                "artifacts" + File.separator + "json" + File.separator + "process" + File.separator+ "delete-gdoc-document.json");
        queryMap.put("removeDocumentDetails", URLEncoder.
                encode(PDFDeleteRequest,PCIntegrationConstants.UTF_8));
        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherProcessAPIBaseUrl +
                        "delete_document",MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,null, queryMap,headerMap,cookieHeader);
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"),"Couldn't delete associated MSDoc");
    }
}