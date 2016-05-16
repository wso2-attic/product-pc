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

package org.wso2.pc.integration.tests.publisher;


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
import java.net.URLEncoder;
import java.util.HashMap;

public class ProcessTextTestCase extends PCIntegrationBaseTest {

    String publisherUrl;
    String cookieHeader;
    GenericRestClient genericRestClient;
    String jSessionId;
    HashMap<String, String> queryMap;
    HashMap<String, String> headerMap;
    String resourcePath;
    String requestBody;

    String processName = "TestProcess1";
    String processVersion = "1.0";
    String processText = "This is a test text of the process";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services",
                "publisher/apis");
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "create-process.json";
        requestBody = readFile(resourcePath);
        JSONObject objSessionPublisher =
                new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                        automationContext.getSuperTenant().getTenantAdmin().getUserName(),
                        automationContext.getSuperTenant().getTenantAdmin().getPassword(), queryMap,
                        headerMap).getEntity(String.class));
        jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));

        genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl + "create_process",
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, requestBody, queryMap,
                headerMap, cookieHeader);
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding process text")
    public void addProcessText() throws XPathExpressionException, JSONException {
        queryMap.put(PCIntegrationConstants.PROCESS_NAME, processName);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION, processVersion);
        queryMap.put(PCIntegrationConstants.PROCESS_TEXT, processText);

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                "save_process_text", MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Process text is not added");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test case for retrieving the process text",
            dependsOnMethods = {"addProcessText"})
    public void getProcessText() throws XPathExpressionException, JSONException {
        queryMap.put("process_text_path", "/processText/" + processName + "/" + processVersion);

        ClientResponse response = genericRestClient.geneticRestRequestGet(publisherAPIBaseUrl +
                "get_process_text", queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get("content").toString().
                equals(processText), "Error while retrieving text");
    }
}
