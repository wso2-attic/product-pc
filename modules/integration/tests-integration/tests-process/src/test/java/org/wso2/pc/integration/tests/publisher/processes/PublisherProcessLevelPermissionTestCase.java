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
 *
 */
package org.wso2.pc.integration.tests.publisher.processes;


import org.apache.wink.client.ClientResponse;
import org.apache.wink.common.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.AutomationContext;
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

public class PublisherProcessLevelPermissionTestCase extends PCIntegrationBaseTest {

    GenericRestClient genericRestClient;
    String jSessionId;
    HashMap<String, String> queryMap;
    HashMap<String, String> headerMap;
    String resourcePath;
    private AutomationContext automationContextUser1;
    private String cookieHeader1;
    private String publisherUrl, APIUrl;
    private String requestBody, processId, Path;

    @DataProvider
    private static Object[][] userModeProvider() {
        return new TestUserMode[][]{
                new TestUserMode[]{TestUserMode.SUPER_TENANT_ADMIN}
        };
    }

    @BeforeClass(alwaysRun = true)
    public void createProcess() throws Exception {
        super.init();

        automationContextUser1 = new AutomationContext("PC", "publisher", "superTenant", "user1");
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services",
                "publisher/apis");
        cookieHeader1 = login(automationContextUser1);

        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "process" + File.separator + "process.json";
        requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, "UTF-8"));
        APIUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services",
                "publisher/assets/process/apis/create_process");
        ClientResponse response = genericRestClient.geneticRestRequestPost(APIUrl,
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, requestBody, queryMap,
                headerMap, cookieHeader1);
        log.info(response.getStatusCode() + " Process created successfully");
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        processId = responseObject.get(PCIntegrationConstants.ID).toString();
        Path = "/_system/governance/processes/yasima/1.0";

    }

    /**
     * This test is to check whether the process creater has permission to assign different permission for different
     * users.
     *
     * @throws Exception
     */
    @Test(groups = {"org.wso2.pc"}, description = "Test authorize permission of the process creater")
    public void checkAuthorizePermission() throws Exception {

        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        queryMap.put("assetType", "process");
        queryMap.put("id", processId);

        ClientResponse response = genericRestClient.
                geneticRestRequestGet(publisherUrl + "/permissions", queryMap, headerMap, cookieHeader1);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode()) || (response.getStatusCode()
                        == HttpStatus.CREATED.getCode())),
                "Wrong status code ,Expected 200 OK or 201 OK ,Received " + response.getStatusCode());

        JSONObject data = (JSONObject) responseObject.get("data");
        JSONArray authorizedRoles = data.getJSONArray("authorizedRoles");
        Boolean status = false;
        for (int i = 0; i < authorizedRoles.length(); i++) {
            if (authorizedRoles.get(i).equals("role1")) {
                status = true;
                break;
            }
        }
        Boolean isAuthorizeAllowed = (Boolean) data.get("isAuthorizeAllowed");

        Assert.assertTrue(status && isAuthorizeAllowed, "Permission setting error");
    }

    /**
     * When a process is created only the process creater can edit and give permission for the process. This test case
     * is to test allowing write permission for another role.
     *
     * @throws IOException
     * @throws JSONException
     * @throws XPathExpressionException
     */
    @Test(groups = {"org.wso2.pc"}, description = "Adding write permission", dependsOnMethods =
            {"checkAuthorizePermission"})
    public void addPermission() throws IOException, JSONException, XPathExpressionException {

        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        requestBody = String.
                format(readFile(FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                                + "json" + File.separator + "process" + File.separator + "publisherPermissionAdd.json"),
                        processId, Path);
        ClientResponse response = genericRestClient.
                geneticRestRequestPost(publisherUrl + "/permissions", MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader1);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        String responseStatus = responseObject.get("status").toString();
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode()) ||
                        (response.getStatusCode() == HttpStatus.CREATED.getCode())),
                "Wrong status code ,Expected 200 OK or 201 OK ,Received " + response.getStatusCode());
        Assert.assertTrue(responseStatus.equals("true"), "Could not add permission to resource");
        Assert.assertTrue(isPermittedResource("role2", "writeAllow", cookieHeader1),
                "Could not add write permission");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Removing write permission", dependsOnMethods = {"addPermission"})
    public void removePermission() throws IOException, JSONException, XPathExpressionException {

        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        queryMap.put("assetType", "process");
        queryMap.put("assetId", processId);
        queryMap.put("roleToRemove", "role2");
        queryMap.put("pathWithVersion", Path);

        ClientResponse response = genericRestClient.
                geneticRestRequestDelete(publisherUrl + "/permissions", MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, queryMap, headerMap, cookieHeader1);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        String responseStatus = responseObject.get("status").toString();
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode()) ||
                        (response.getStatusCode() == HttpStatus.CREATED.getCode())),
                "Wrong status code ,Expected 200 OK or 201 OK ,Received " + response.getStatusCode());
        Assert.assertTrue(responseStatus.equals("true"), "Could not remove permission to resource");
        Assert.assertTrue(isPermittedResource("role2", "writeDeny", cookieHeader1),
                "Could not remove permission of resource");

    }

    /**
     * The requirement is any user can view the any process. For that internal/everyone should have read permission.
     *
     * @throws Exception
     */

    @Test(groups = {"org.wso2.pc"}, description = "Test Read permission")
    public void viewProcess() throws Exception {

        Assert.assertTrue(isPermittedResource("INTERNAL/everyone", "readAllow", cookieHeader1), "Read Permission " +
                "error");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test edit process", dependsOnMethods = {"addPermission"})
    public void EditProcess() throws Exception {

        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        AutomationContext automationContextUser2 = new AutomationContext("PC", "publisher", "superTenant", "user2");
        String cookieHeader2 = login(automationContextUser2);
        APIUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services",
                "publisher/assets/process/apis/update_description");

        requestBody = String.
                format(readFile(FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                                + "json" + File.separator + "process" + File.separator + "edit-process.json"),
                        processId, Path);
        queryMap.put("descriptionDetails", URLEncoder.encode(requestBody, "UTF-8"));
        ClientResponse response = genericRestClient.geneticRestRequestPost(APIUrl,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader2);
        response.getStatusCode();
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode())),
                "Wrong status code ,Expected 200 OK or 201 OK ,Received " + response.getStatusCode());
        Assert.assertFalse(responseObject.get("error").equals("false"), "Could not update resource");

    }

    @Test(groups = {"org.wso2.pc"}, description = "Test edit process without write permission",
            dependsOnMethods = {"removePermission"})
    public void checkEditProcessWithoutReadPermission() throws Exception {

        boolean status = isPermittedResource("role2", "writeDeny", cookieHeader1);
        Assert.assertTrue(status, "Write permission error");

    }

    @AfterClass(alwaysRun = true)
    public void cleanUp(){
        automationContextUser1=null;
        //delete process

    }

    public String login(AutomationContext automationCxt) throws Exception {

        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        JSONObject objSessionPublisher =
                new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                        automationCxt.getContextTenant().getContextUser().getUserName(),
                        automationCxt.getContextTenant().getContextUser().getPassword(), queryMap,
                        headerMap).getEntity(String.class));
        jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");

        return "JSESSIONID=" + jSessionId;
    }

    private Boolean isPermittedResource(String role, String permissionType, String cookieHeader)
            throws JSONException, IOException {

        queryMap.put("assetType", "process");
        queryMap.put("id", processId);

        ClientResponse response = genericRestClient.
                geneticRestRequestGet(publisherUrl + "/permissions", queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode()) || (response.getStatusCode()
                        == HttpStatus.CREATED.getCode())),
                "Wrong status code ,Expected 200 OK or 201 OK ,Received " + response.getStatusCode());

        JSONObject data = (JSONObject) responseObject.get("data");
        JSONArray permissionArray = data.getJSONArray("list");
        Boolean status = false;
        for (int i = 0; i < permissionArray.length(); i++) {
            JSONObject roleObject = permissionArray.getJSONObject(i);
            if (roleObject.get("userName").equals(role) && roleObject.getBoolean(permissionType)) {
                status = true;
                break;
            }
        }

        return status;
    }
}