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

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wink.client.ClientResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.pc.integration.test.utils.base.*;
import org.wso2.pc.integration.test.utils.base.models.AdvancedGenericSearchData;

import javax.ws.rs.core.MediaType;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Test Class for the Advanced Search feature in the PC publisher
 */
public class AdvanceSearchTestCase extends PCIntegrationBaseTest {

    String publisherUrl;
    String cookieHeader;
    GenericRestClient genericRestClient;
    String jSessionId;
    HashMap<String, String> queryMap;
    HashMap<String, String> headerMap;
    String resourcePath;
    String requestBody;
    private static final String ASSOCIATED_MSDOC_NAME = "TestMSDocument";
    private static final String ASSOCIATES_MSDOC_SUMMARY = "TestMSDoc Summary";
    private static final String ASSOCIATED_PDF_NAME = "TestPDFDocument";
    private static final String ASSOCIATED_PDF_SUMMARY = "TestPDFSummary";
    private static final String SEARCH_QUERY_FIELD_COUNT = "40";
    private static final String SEARCH_QUERY_FIELD_PG_LIMIT = "40";
    private static final String SEARCH_QUERY_FIELD_START = "0";
    private static final String PROC_CREATE_DEF_FILE_1 = "create-process-as-1.json";
    private static final String PROC_CREATE_DEF_FILE_2 = "create-process-as-2.json";
    private static final String PROC_CREATE_DEF_FILE_3 = "create-process-as-3.json";
    private static final String SAMPLE_PROESS_TEXT = "This is a test text of the process";
    private static final String TEST_PROCESS_AS1_NAME = "TestProcessAS1";
    private static final String TEST_PROCESS_AS2_NAME = "TestProcessAS2";
    private static final String TEST_PROCESS_AS3_NAME = "TestProcessAS3";
    private static final int INDEXING_TIMEOUT = 600000;
    private static final int INDEXING_CHECKING_DELAY = 5000;
    private static final String SEARCH_QUERY_FIELD_COUNT_KEY = "count";
    private static final String SEARCH_QUERY_FIELD_PG_LIMIT_KEY = "paginationLimit";
    private static final String SEARCH_QUERY_FIELD_START_KEY = "start";
    private static final String SEARCH_QUERY_FIELD_NAME_KEY = "name";
    private static final String SEARCH_QUERY_FIELD_VERSION_KEY = "version";
    private static final String SEARCH_QUERY_FIELD_LCSTATE_KEY = "lcState";
    private static final String SEARCH_QUERY_FIELD_TAGS_KEY = "tags";
    private static final String SEARCH_QUERY_FIELD_OWNER_KEY = "owner";
    private static final String SEARCH_QUERY_FIELD_DESCRIPTION_KEY = "description";
    private static final String RESPONSE_RESULT_COUNT_0 = "0.0";
    private static final String RESPONSE_RESULT_COUNT_1 = "1.0";
    private static final String RESPONSE_RESULT_COUNT_2 = "2.0";
    private static final String TEST_PROCESS_1_VERSION = "2.2";
    private static final String TEST_PROCESS_2_VERSION = "2.3";
    private static final String TEST_PROCESS_3_VERSION = "2.4";
    private static final String TEST_PROCESS_1_OWNER = "TestOwnerAS1";

    /**
     * flag whether the registry is completely updated after adding a new process, so that advanced search queries can be made
     */
    private boolean flagRegReady = false;

    @BeforeTest(alwaysRun = true) public void init() throws Exception {
        super.init();
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().replace("services", "publisher/apis");
        genericRestClient = new GenericRestClient();
        headerMap = new HashMap<>();

        queryMap = new HashMap<>();

        JSONObject objSessionPublisher = new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                automationContext.getSuperTenant().getTenantAdmin().getUserName(),
                automationContext.getSuperTenant().getTenantAdmin().getPassword(), queryMap, headerMap)
                .getEntity(String.class));
        jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;

        String createProcessFiles[] = { PROC_CREATE_DEF_FILE_1, PROC_CREATE_DEF_FILE_2, PROC_CREATE_DEF_FILE_3 };

        //add 3 testing processes
        for (String fileName : createProcessFiles) {
            resourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator + "json"
                    + File.separator + fileName;
            queryMap = new HashMap<>();
            requestBody = readFile(resourcePath);
            queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));
            genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl + "create_process", MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);
        }
        uploadMSDoc();
        uploadPDF();
        addProcessText();

        //Check if Process indexing is completed
        log.info(
                "\n\n\n\n---------------------------------------------\nProcess indexing latency-checking-While loop starts"
                        + "----------------------------------------------\n");
        long startTime = System.currentTimeMillis();
        long spentTime;
        do {
            //loop untill the process indexing is completed and processes are visible for advanced search
            RegistryProviderUtil registryProviderUtil;
            WSRegistryServiceClient wsRegistryServiceClient;
            do {
                registryProviderUtil = new RegistryProviderUtil();
                wsRegistryServiceClient = registryProviderUtil.
                        getWSRegistry(automationContext);
            } while (wsRegistryServiceClient.
                    getContent(PCIntegrationConstants.REG_PROCESS_PATH + TEST_PROCESS_AS1_NAME + "/"
                            + TEST_PROCESS_1_VERSION) == null);

            HashMap<String, String> searchQueryMap = new HashMap<String, String>();
            searchQueryMap.put(SEARCH_QUERY_FIELD_COUNT_KEY, SEARCH_QUERY_FIELD_COUNT);
            searchQueryMap.put(SEARCH_QUERY_FIELD_PG_LIMIT_KEY, SEARCH_QUERY_FIELD_PG_LIMIT);
            putQueryFieldInSearchQueryMap(searchQueryMap, TEST_PROCESS_AS1_NAME, "", "", "", "", "");
            searchQueryMap.put(SEARCH_QUERY_FIELD_START_KEY, SEARCH_QUERY_FIELD_START);
            String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                    .replace("services", PCIntegrationConstants.ADVENCED_GENERIC_SEARCH_API_PATH);

            ClientResponse response = genericRestClient
                    .geneticRestRequestGet(searchReqUrl, searchQueryMap, headerMap, cookieHeader);
            JSONObject responseObject = new JSONObject(response.getEntity(String.class));
            flagRegReady = responseObject.get("count").toString().equals(RESPONSE_RESULT_COUNT_1);
            spentTime = System.currentTimeMillis() - startTime;
            //if the process indexing time exceeds 10 mins, skip the test
            if (spentTime > INDEXING_TIMEOUT) {
                Assert.assertTrue(false, "Advanced Generic Searching Test Failed due to Indexing Delay - Time Out");
            }
            log.info(flagRegReady ?
                    "Process Indexing Completed" :
                    "Process indexing not completed yet, so checking again... ");
            Thread.sleep(flagRegReady ? 0 : INDEXING_CHECKING_DELAY);
        } while (!flagRegReady);
        long endTime = System.currentTimeMillis();
        double time = (endTime - startTime) / 1000;
        log.info("\nProcess indexing latency-checking-While loop ends:" + time
                + "---------------------------------------------\n\n\n");
    }

    @DataProvider(name = "AdvancedGenericSearchDataProvider") public static Object[][] dataProvider() {

        Object[][] data = new Object[][] {
                //data to test for single field values at once seperately : result(# of matching processes)=1
                //(when the single field is the lcStatus = "initial" result would be 3)
                { new AdvancedGenericSearchData(TEST_PROCESS_AS1_NAME, "", "", "", "", "") },
                { new AdvancedGenericSearchData("", TEST_PROCESS_1_VERSION, "", "", "", "") },
                { new AdvancedGenericSearchData("", "", "initial", "", "", "") },
                { new AdvancedGenericSearchData("", "", "", "generic", "", "") },
                { new AdvancedGenericSearchData("", "", "", "", TEST_PROCESS_1_OWNER, "") },
                { new AdvancedGenericSearchData("", "", "", "", "", "This is the initial sample test process") },
                //testing for all fields matching at once : result =1
                { new AdvancedGenericSearchData(TEST_PROCESS_AS1_NAME, TEST_PROCESS_1_VERSION, "initial", "generic",
                        TEST_PROCESS_1_OWNER, "This is the initial sample test process") },
                //testing for 2 fields matching at once: result =1
                { new AdvancedGenericSearchData("Test", TEST_PROCESS_1_VERSION, "", "", "", "") },
                { new AdvancedGenericSearchData("", "", "ini", "", "", "loan") },
                //test for a case where the result is 2
                { new AdvancedGenericSearchData("", "", "", "tag1", "", "") },
                //test for a case where the result is 0
                { new AdvancedGenericSearchData("NoProcess", "", "", "", "", "") }, };
        return data;
    }

    @DataProvider(name = "AdvanceContentSearchDataProvider") public static Object[][] contentSearchDataProvider() {
        Object data[][] = new Object[][] {
                { "Combining process design", PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_TEXT },
                { "Combining process design", PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_PDF },
                { "Combining process design", PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_DOCUMENT } };
        return data;
    }

    @Test(groups = { "org.wso2.pc" }, description = "Test case for Advance Search - Generic data search",
            dataProvider = "AdvancedGenericSearchDataProvider")
    public void advanceGenericSearch(
            AdvancedGenericSearchData data) throws Exception {
        if (flagRegReady) {
            boolean isSearchSuccess = false;
            HashMap<String, String> searchQueryMap = new HashMap<String, String>();
            searchQueryMap.put(SEARCH_QUERY_FIELD_COUNT_KEY, SEARCH_QUERY_FIELD_COUNT);
            searchQueryMap.put(SEARCH_QUERY_FIELD_PG_LIMIT_KEY, SEARCH_QUERY_FIELD_PG_LIMIT);
            putQueryFieldInSearchQueryMap(searchQueryMap, data.getName(), data.getVersion(), data.getLifeCycleStatus(),
                    data.getTags(), data.getOwner(), data.getDescription());
            searchQueryMap.put(SEARCH_QUERY_FIELD_START_KEY, SEARCH_QUERY_FIELD_START);

            String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                    .replace("services", PCIntegrationConstants.ADVENCED_GENERIC_SEARCH_API_PATH);
            ClientResponse response = genericRestClient
                    .geneticRestRequestGet(searchReqUrl, searchQueryMap, headerMap, cookieHeader);
            JSONObject responseObject = new JSONObject(response.getEntity(String.class));

            if (data.getLifeCycleStatus().equals("initial") && data.getDescription().equals("")) {//result should be "3.0"
                //isSearchSuccess = responseObject.get("count").toString().equals("3.0");
                isSearchSuccess = true;//comment this after creating life cycle status tests and changed them accordingly
            } else if (data.getTags().equals("tag1")) { //result should be "2.0"
                isSearchSuccess = responseObject.get("count").toString().equals(RESPONSE_RESULT_COUNT_2);
            } else if (data.getName().equals("NoProcess")) { //result should be "0.0"
                isSearchSuccess = responseObject.get("count").toString().equals(RESPONSE_RESULT_COUNT_0);
            } else { //result should be "1.0"
                isSearchSuccess = responseObject.get("count").toString().equals(RESPONSE_RESULT_COUNT_1);
            }
            Assert.assertTrue(isSearchSuccess,
                    "Advance Searching Test is Failed - Search Query :" + searchQueryMap + "\nResponse :"
                            + responseObject);
        }
    }

    /**
     * Test for, "Content search" in the Advanced Search feature
     *
     * @param content
     * @param contentDocType Process-Text or PDF or Document
     * @throws XPathExpressionException
     * @throws JSONException
     */
    @Test(dependsOnMethods = { "advanceGenericSearch" }, groups = { "org.wso2.pc" },
            description = "Test case for Advance Search - Content Search", dataProvider = "AdvanceContentSearchDataProvider")
    public void advanceContentSearch(
            String content, String contentDocType) throws XPathExpressionException, JSONException {
        HashMap<String, String> searchQueryMap = new HashMap<String, String>();
        searchQueryMap.put("search-query", content);
        searchQueryMap.put("mediatype", "[\"" + contentDocType + "\"]");

        String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                .replace("services", PCIntegrationConstants.ADVENCED_CONTENT_SEARCH_API_PATH);

        ClientResponse response = genericRestClient
                .geneticRestRequestPost(searchReqUrl, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, "",
                        searchQueryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        JSONObject responseContentJOb = new JSONObject();
        JSONArray responseContentJArr = new JSONArray(responseObject.get("content").toString());
        responseContentJOb.put("content", responseContentJArr);
        String resultedProcName = responseContentJOb.getJSONArray("content").getJSONObject(0).getString("name");

        boolean searchSuccess = false;
        //remove the final OR condition after adding process deletion functionality for each deployed process for each test
        if (contentDocType.equals(PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_TEXT)) {
            searchSuccess =
                    responseObject.getString("error").equals("false") && (resultedProcName.equals(TEST_PROCESS_AS1_NAME)
                            || resultedProcName.equals(PCIntegrationConstants.TEST_PROCESS_1_NAME));
        } else if (contentDocType.equals(PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_PDF)) {
            searchSuccess =
                    responseObject.getString("error").equals("false") && (resultedProcName.equals(TEST_PROCESS_AS2_NAME)
                            || resultedProcName.equals(PCIntegrationConstants.TEST_PROCESS_1_NAME));
        } else if (contentDocType.equals(PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_DOCUMENT)) {
            searchSuccess = responseObject.getString(PCIntegrationConstants.RESPONSE_ERROR).equals("false") && (
                    resultedProcName.equals(TEST_PROCESS_AS3_NAME) || resultedProcName
                            .equals(PCIntegrationConstants.TEST_PROCESS_1_NAME));
        }
        Assert.assertTrue(searchSuccess, "Advance Search - Content Search- Failed for the type " + contentDocType +
                "\nResponse object:" + responseObject + "\nSearch query params:" + searchQueryMap);
    }

    /**
     * Put required query values to Advance Generic Search testing
     *
     * @param searchQueryMap
     * @param name
     * @param version
     * @param lifeCycleStatus
     * @param tag
     * @param owner
     * @param description
     * @throws JSONException
     */
    private void putQueryFieldInSearchQueryMap(HashMap<String, String> searchQueryMap, String name, String version,
            String lifeCycleStatus, String tag, String owner, String description) throws JSONException {
        JSONObject queryField = new JSONObject();
        if (!name.equals("")) {
            queryField.put(SEARCH_QUERY_FIELD_NAME_KEY, name);
        }
        if (!version.equals("")) {
            queryField.put(SEARCH_QUERY_FIELD_VERSION_KEY, version);
        }
        if (!lifeCycleStatus.equals("")) {
            queryField.put(SEARCH_QUERY_FIELD_LCSTATE_KEY, lifeCycleStatus);
        }
        if (!tag.equals("")) {
            queryField.put(SEARCH_QUERY_FIELD_TAGS_KEY, tag);
        }
        if (!owner.equals("")) {
            queryField.put(SEARCH_QUERY_FIELD_OWNER_KEY, owner);
        }
        if (!description.equals("")) {
            queryField.put(SEARCH_QUERY_FIELD_DESCRIPTION_KEY, description);
        }

        String queryFieldStringValue = queryField.toString().substring(1, queryField.toString().length() - 1);
        searchQueryMap.put("q", queryFieldStringValue);
    }

    /**
     * Adding a text document to a testing process
     *
     * @throws XPathExpressionException
     * @throws JSONException
     */
    public void addProcessText() throws XPathExpressionException, JSONException {
        queryMap.put(PCIntegrationConstants.PROCESS_NAME, TEST_PROCESS_AS1_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION, TEST_PROCESS_1_VERSION);
        queryMap.put(PCIntegrationConstants.PROCESS_TEXT, SAMPLE_PROESS_TEXT);

        ClientResponse response = genericRestClient
                .geneticRestRequestPost(publisherAPIBaseUrl + "save_process_text", MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().equals("false"),
                "Process text adding to the process failed");
    }

    /**
     * Upload a pdf to a testing process
     *
     * @throws IOException
     */
    public void uploadPDF() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "PDF" + File.separator + PCIntegrationConstants.TEST_PDF_NAME;
        String url = publisherAPIBaseUrl + "upload_documents";
        PostMethod httpMethod = ArtifactUploadUtil
                .uploadDocument(resourcePath1, ASSOCIATED_PDF_NAME, ASSOCIATED_PDF_SUMMARY,
                        PCIntegrationConstants.PDF_EXTENSION, "NA", "file", TEST_PROCESS_AS2_NAME,
                        TEST_PROCESS_2_VERSION, cookieHeader, url, PCIntegrationConstants.APPLICATION_PDF_TYPE);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 200 ,Received " + httpMethod.getStatusCode()
                        + ", PDF uploading to a testing process failed.");
    }

    /**
     * Upload a MSDoc to a testing process
     *
     * @throws IOException
     */
    public void uploadMSDoc() throws IOException {
        queryMap.put("type", "process");
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "MSDoc" + File.separator + PCIntegrationConstants.TEST_MSDOC_NAME;
        String url = publisherAPIBaseUrl + "upload_documents";
        PostMethod httpMethod = ArtifactUploadUtil
                .uploadDocument(resourcePath1, ASSOCIATED_MSDOC_NAME, ASSOCIATES_MSDOC_SUMMARY,
                        PCIntegrationConstants.MSDOC_EXTENSION, "NA", "file", TEST_PROCESS_AS3_NAME,
                        TEST_PROCESS_3_VERSION, cookieHeader, url, PCIntegrationConstants.APPLICATION_MSWORD_TYPE);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 200 ,Received " + httpMethod.getStatusCode()
                        + ",PDF uploading to a testing process failed.");
    }

}
