/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.apache.wink.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
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

public class AddProcessTestCase extends PCIntegrationBaseTest {

    private String cookieHeader;
    private GenericRestClient genericRestClient;
    private HashMap<String, String> queryMap;
    private HashMap<String, String> headerMap;
    private String resourcePath;

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        String publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", "publisher/apis");
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "process"  + File.separator + "create-process.json";
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

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherProcessAPIBaseUrl +
                "create_process" , MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader);

        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error while creating the process");
    }
}