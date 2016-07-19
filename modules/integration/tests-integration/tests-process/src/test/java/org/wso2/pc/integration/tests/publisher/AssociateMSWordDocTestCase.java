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
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.pc.integration.test.utils.base.*;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class AssociateMSWordDocTestCase extends PCIntegrationBaseTest{

    private String cookieHeader;
    private GenericRestClient genericRestClient;
    private HashMap<String, String> queryMap;
    private HashMap<String, String> headerMap;
    private String resourcePath;
    private static final String ASSOCIATED_MSDOC_NAME = "TestMSDocument";
    private static final String ASSOCIATES_MSDOC_SUMMARY = "TestMSDoc Summary";
    private static final String PROCESS_NAME = "TestProcess1";
    private static final String PROCESS_VERSION = "1.0";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        String publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", "publisher/apis");
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

    @Test(groups = {"org.wso2.pc"}, description = "Associating PDF to the process",
            dependsOnMethods = "addProcess")
    public void uploadMSDoc() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "MSDoc" + File.separator + "TestMSDoc.doc";
        String url = publisherAPIBaseUrl + "upload_documents";
        PostMethod httpMethod = ArtifactUploadUtil.uploadDocument(resourcePath1, ASSOCIATED_MSDOC_NAME,
                ASSOCIATES_MSDOC_SUMMARY,PCIntegrationConstants.MSDOC_EXTENSION,"NA","file",
                "TestProcess1","1.0",cookieHeader,url,PCIntegrationConstants.APPLICATION_MSWORD_TYPE);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 200 ,Received " + httpMethod.getStatusCode());
    }

    @Test(groups = {"org.wso2.pc"}, description = "Download associated PDF document",
            dependsOnMethods = "uploadMSDoc")
    public void checkMSDoc() throws JSONException {

        queryMap.put("process_doc_path",String.format("%s/%s/%s/%s.%s",
                PCIntegrationConstants.DOC_CONTENT,
                PROCESS_NAME,PROCESS_VERSION,
                ASSOCIATED_MSDOC_NAME,
                PCIntegrationConstants.MSDOC_EXTENSION));
        ClientResponse response = genericRestClient.geneticRestRequestGet(publisherAPIBaseUrl +
                "download_document",queryMap,headerMap,cookieHeader);
        Assert.assertTrue(new JSONObject(response.getEntity(String.class)).get("error").toString().
                equals("false"),"Associated MSDoc doesn't exit");
    }

    @Test(groups = {"org.wso2.pc"}, description = "MSDoc deleting test case",
            dependsOnMethods = "checkMSDoc")
    public void deleteMSDOC() throws JSONException, IOException {
        String PDFDeleteRequest = readFile(FrameworkPathUtil.getSystemResourceLocation() +
                "artifacts" + File.separator + "json" + File.separator + "delete-msdoc-document.json");
        queryMap.put("removeDocumentDetails", URLEncoder.
                encode(PDFDeleteRequest,PCIntegrationConstants.UTF_8));
        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "delete_document",MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,null, queryMap,headerMap,cookieHeader);
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"),"Couldn't delete associated MSDoc");
    }
}