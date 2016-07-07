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

import org.apache.wink.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.pc.integration.test.utils.base.GenericRestClient;
import org.wso2.pc.integration.test.utils.base.PCIntegrationBaseTest;
import org.wso2.pc.integration.test.utils.base.PCIntegrationConstants;
import org.wso2.pc.integration.test.utils.base.TestUtils;

import javax.ws.rs.core.MediaType;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class AddFlowChartTestCase extends PCIntegrationBaseTest{

    @Factory(dataProvider = "userModeProvider")
    public AddFlowChartTestCase(TestUserMode userMode) {
        this.userMode = userMode;
    }

    private String cookieHeader;
    private GenericRestClient genericRestClient;
    private HashMap<String, String> queryMap;
    private HashMap<String, String> headerMap;
    private String resourcePath;
    private static final String PROCESS_NAME = "TestProcess1";
    private static final String PROCESS_VERSION = "1.0";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init(userMode);
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

    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding process")
    public void addProcess() throws IOException, XPathExpressionException, JSONException {

        String requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "create_process" , MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader);
        response.getStatusCode();
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"), "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding flowchart",
            dependsOnMethods = "addProcess")
    public void addFlowChart() throws IOException, JSONException {
        String flowchartBody = readFile(FrameworkPathUtil.getSystemResourceLocation() + "artifacts"
                + File.separator + "json" + File.separator + "TestFlowChart.json");
        queryMap.put(PCIntegrationConstants.PROCESS_NAME,PROCESS_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION,PROCESS_VERSION);
        queryMap.put("flowchartJson",URLEncoder.encode(flowchartBody,PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "upload_flowchart" , MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                null, queryMap, headerMap, cookieHeader);
        response.getStatusCode();
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"), "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Check associated flowchart of the process",
            dependsOnMethods = "addFlowChart")
    public void checkFlowchart() throws JSONException {
        queryMap.put("flowchartPath",String.format("%s%s/%s",
                PCIntegrationConstants.REG_FLOWCHART_PATH, PROCESS_NAME, PROCESS_VERSION));
        ClientResponse response = genericRestClient.geneticRestRequestGet(publisherAPIBaseUrl +
                "get_process_flowchart",queryMap,headerMap,cookieHeader);
        Assert.assertTrue(new JSONObject(response.getEntity(String.class)).
                get(PCIntegrationConstants.RESPONSE_ERROR).toString().equals("false"),
                "Associated Flowchart doesn't exit");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Deleting flowchart testcase",
            dependsOnMethods = "checkFlowchart")
    public void deleteFlowchart() throws JSONException {
        ClientResponse response = genericRestClient.geneticRestRequestGet(publisherAPIBaseUrl +
                "delete_flowchart",queryMap,headerMap,cookieHeader);
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"),"Error while deleting flowchart");
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_CONTENT).toString().
                contains("Successfully deleted the flowchart"),"Couldn't delete flowchart");
    }

    @DataProvider
    private static Object[][] userModeProvider() {
        return new TestUserMode[][]{
                new TestUserMode[]{TestUserMode.SUPER_TENANT_ADMIN},
//                new TestUserMode[]{TestUserMode.SUPER_TENANT_USER},
//                new TestUserMode[]{TestUserMode.TENANT_ADMIN},
//                new TestUserMode[]{TestUserMode.TENANT_USER}
        };
    }
}