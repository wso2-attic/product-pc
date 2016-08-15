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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.common.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.pc.integration.test.utils.base.*;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ExportProcessTestCase extends PCIntegrationBaseTest {

    private String cookieHeader;
    private GenericRestClient genericRestClient;
    private HashMap<String, String> queryMap;
    private HashMap<String, String> headerMap;
    private  String publisherUrl, resourcePath ,processId;
    private static final String ASSOCIATED_PDF_NAME = "TestPDFDocument";
    private static final String ASSOCIATED_PDF_SUMMARY = "TestPDFSummary";
    private static final String PROCESS_NAME = "TestProcess1";
    private static final String PROCESS_VERSION = "1.0";
    private static final String TEST_PDF_FILE = "TestFile.pdf";
    private static final String TEST_BPMN_FILE = "userTaskProcess.bpmn20.xml";
    private static final String TEST_FLOW_CHART_FILE = "TestFlowChart.json";
    private static final String TEST_CREATE_PROCESS_JSON_FILE = "create-process.json";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", "publisher/apis");
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "process" + File.separator+ TEST_CREATE_PROCESS_JSON_FILE;
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
        processId = responseObject.get(PCIntegrationConstants.ID).toString();
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Associating PDF to the process",
            dependsOnMethods = "addProcess")
    public void uploadPDF() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "PDF" + File.separator + TEST_PDF_FILE;
        String url = publisherProcessAPIBaseUrl + "upload_documents";
        PostMethod httpMethod = ArtifactUploadUtil.uploadDocument(resourcePath1, ASSOCIATED_PDF_NAME,
                ASSOCIATED_PDF_SUMMARY,PCIntegrationConstants.PDF_EXTENSION,"NA","file",
                PROCESS_NAME,PROCESS_VERSION,cookieHeader,url,PCIntegrationConstants.APPLICATION_PDF_TYPE);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 200 ,Received " + httpMethod.getStatusCode());
    }

    @Test(groups = {"org.wso2.pc"}, description = "Associating BPMN to the process",
            dependsOnMethods = "addProcess")
    public void uploadBPMN() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "BPMN"
                + File.separator + TEST_BPMN_FILE;
        String url = publisherProcessAPIBaseUrl + "upload_bpmn";
        PostMethod httpMethod = ArtifactUploadUtil.uploadBPMN(resourcePath1,
                PROCESS_NAME, PROCESS_VERSION, "BPMN", cookieHeader, url);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 302 ,Received " + httpMethod.getStatusCode());
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding flowchart",
            dependsOnMethods = "addProcess")
    public void addFlowChart() throws IOException, JSONException {
        String flowchartBody = readFile(FrameworkPathUtil.getSystemResourceLocation() + "artifacts"
                + File.separator + "json" + File.separator + "process" + File.separator+ TEST_FLOW_CHART_FILE);
        queryMap.put(PCIntegrationConstants.PROCESS_NAME,PROCESS_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION,PROCESS_VERSION);
        queryMap.put("flowchartJson",URLEncoder.encode(flowchartBody,PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherProcessAPIBaseUrl +
                        "upload_flowchart" , MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                null, queryMap, headerMap, cookieHeader);
        response.getStatusCode();
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"), "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test case for exporting created process",
            dependsOnMethods = "addProcess")
    public void exportProcess() throws IOException, JSONException {
        queryMap.put(PCIntegrationConstants.PROCESS_NAME,PROCESS_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION,PROCESS_VERSION);

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherProcessAPIBaseUrl +
                        "upload_flowchart" , MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                null, queryMap, headerMap, cookieHeader);
        response.getStatusCode();
        byte[] zipData = Base64.decodeBase64(response.getEntity(String.class));
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertNotNull(zipData, "Error while exporting the process");
    }


    @AfterClass(alwaysRun = true, description = "Delete process")
    public void cleanUp() throws Exception {
        queryMap.clear();
        queryMap.put("type", "process");
        ClientResponse response = genericRestClient.
                geneticRestRequestDelete(publisherUrl + "/assets/" + processId, MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, queryMap, headerMap, cookieHeader);
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode())), "Wrong status code ,Expected 200 " +
                "OK,Received " + response.getStatusCode());

    }
}
