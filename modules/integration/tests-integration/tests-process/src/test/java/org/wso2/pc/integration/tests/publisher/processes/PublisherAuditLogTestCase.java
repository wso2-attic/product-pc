package org.wso2.pc.integration.tests.publisher.processes;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.common.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
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

public class PublisherAuditLogTestCase extends PCIntegrationBaseTest {

    String cookieHeader;
    String publisherUrl;
    GenericRestClient genericRestClient;
    String jSessionId;
    HashMap<String, String> queryMap;
    HashMap<String, String> headerMap;
    String resourcePath, processId;
    String testProcessName = "TestProcess1/1.0";

    @DataProvider
    private static Object[][] userModeProvider() {
        return new TestUserMode[][]{
                new TestUserMode[]{TestUserMode.SUPER_TENANT_ADMIN}
        };
    }

    @DataProvider(name = "logPaths")
    private static Object[][] logPathProvider() {
        return new Object[][]{
                {"NA"}, {"/_system/governance/processes/TestProcess1/1.0"}
        };
    }

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services",
                PCIntegrationConstants.DESIGNER_APIS);
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();
        queryMap = new HashMap<>();
        resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator
                + "json" + File.separator + "process" + File.separator + "create-process.json";
        JSONObject objSessionPublisher =
                new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                        automationContext.getSuperTenant().getTenantAdmin().getUserName(),
                        automationContext.getSuperTenant().getTenantAdmin().getPassword(), queryMap,
                        headerMap).getEntity(String.class));
        jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;
    }

    @Test(groups = {"org.wso2.pc"}, description = "Test case for adding process")
    public void addProcess() throws IOException, XPathExpressionException, JSONException {

        String requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherProcessAPIBaseUrl +
                        "create_process", MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                requestBody, queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        processId = responseObject.get(PCIntegrationConstants.ID).toString();
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error while creating the process");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Getting audit logs for process", dependsOnMethods = "addProcess",
            dataProvider = "logPaths")
    public void getLogs(String path) throws Exception {
        String auditLogUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services",
                PCIntegrationConstants.DESIGNER_ASSETS + "/process/apis/audit_log");
        queryMap.put("path", URLEncoder.encode(path, PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(auditLogUrl,
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, null, queryMap,
                headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        JSONObject logObject = new JSONObject(responseObject.get("content").toString());
        JSONArray logArr = logObject.getJSONArray("log");

        while (logArr.length() == 0) {
            response = genericRestClient.geneticRestRequestPost(auditLogUrl,
                    MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, null, queryMap,
                    headerMap, cookieHeader);
            responseObject = new JSONObject(response.getEntity(String.class));
            logObject = new JSONObject(responseObject.get("content").toString());
            logArr = logObject.getJSONArray("log");
        }

        String processName = logArr.getJSONObject(logArr.length() - 1).get("asset").toString();
        Assert.assertNotNull(logArr, "There are no log entries to be found");
//        Assert.assertTrue(processName.equals(testProcessName), "Error in retrieving process asset name");
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
                "Error retrieving logs");
    }

    @AfterClass(alwaysRun = true, description = "Delete process")
    public void cleanUp() throws Exception {
        queryMap.clear();
        queryMap.put("type", "process");
        ClientResponse response = genericRestClient.
                geneticRestRequestDelete(publisherUrl + "/assets/" + processId, MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(((response.getStatusCode() == HttpStatus.OK.getCode())), "Wrong status code ,Expected 200 " +
                "OK,Received " + response.getStatusCode());
    }
}
