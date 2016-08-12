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
package org.wso2.pc.integration.tests.publisher.packages;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.common.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
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
import java.util.HashMap;

public class AddPackageTestCase extends PCIntegrationBaseTest {

    String publisherUrl, packageID;
    private String cookieHeader;
    private GenericRestClient genericRestClient;
    private HashMap<String, String> queryMap;
    private HashMap<String, String> headerMap;

    /**
     * Get required headers for adding package api
     *
     * @throws Exception
     */
    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", "publisher/apis");
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();

        JSONObject objSessionPublisher =
                new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                        automationContext.getSuperTenant().getTenantAdmin().getUserName(),
                        automationContext.getSuperTenant().getTenantAdmin().getPassword(), queryMap,
                        headerMap).getEntity(String.class));
        String jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;
    }

    /**
     * Test case for adding package
     *
     * @throws IOException
     * @throws XPathExpressionException
     * @throws JSONException
     */
    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding package")
    public void addPackage() throws IOException, XPathExpressionException, JSONException {
        String filePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "BAR" + File.separator + "HelloWorld.bar";

        File file = new File(filePath);
        FilePart fp = new FilePart("package_file", file);
        fp.setContentType(MediaType.TEXT_PLAIN);
        StringPart sp1 = new StringPart("package_file_name", "HelloWorld.bar");
        sp1.setContentType(MediaType.TEXT_PLAIN);
        StringPart sp2 = new StringPart("overview_name", "Test Package");
        sp2.setContentType(MediaType.TEXT_PLAIN);
        StringPart sp3 = new StringPart("overview_versionn", "1.0.0");
        sp2.setContentType(MediaType.TEXT_PLAIN);
        StringPart sp4 = new StringPart("overview_description", "This test package");
        sp2.setContentType(MediaType.TEXT_PLAIN);
        //Set file parts and string parts together
        final Part[] part = {fp, sp1, sp2, sp3, sp4};
        HttpClient httpClient = new HttpClient();
        PostMethod httpMethod = new PostMethod(publisherPackageAPIBaseUrl + "packages?type=package");

        httpMethod.addRequestHeader("Cookie", cookieHeader);
        httpMethod.addRequestHeader("Accept", MediaType.APPLICATION_JSON);
        httpMethod.setRequestEntity(
                new MultipartRequestEntity(part, httpMethod.getParams()));
        // Add package using package api
        httpClient.executeMethod(httpMethod);
        Assert.assertTrue(httpMethod.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_CREATED,
                "Expected 201 CREATED, Received " + httpMethod.getStatusCode());
        JSONObject responseObject = new JSONObject(httpMethod.getResponseBodyAsString());
        packageID = responseObject.get(PCIntegrationConstants.ID).toString();
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error while creating the package");

    }

    @AfterClass(alwaysRun = true, description = "Delete package")
    public void cleanUp() throws Exception {
        queryMap.clear();
        queryMap.put("type", "package");
        ClientResponse response = genericRestClient.
                geneticRestRequestDelete(publisherUrl + "/assets/" + packageID, MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, queryMap, headerMap, cookieHeader);
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode())), "Wrong status code ,Expected 200 " +
                "OK,Received " + response.getStatusCode());
    }

}