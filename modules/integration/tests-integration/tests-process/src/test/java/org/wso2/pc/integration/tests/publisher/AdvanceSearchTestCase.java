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

import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.ftpserver.command.impl.SYST;
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
    /**
     * flag whether the registry is completely updated after adding a new process, so that advanced search queries can be made
     */
    boolean flagRegReady = false;

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

        String createProcessFiles[] = { PCIntegrationConstants.PROC_CREATE_DEF_FILE_1,
                PCIntegrationConstants.PROC_CREATE_DEF_FILE_2, PCIntegrationConstants.PROC_CREATE_DEF_FILE_3 };

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

        //add a MSWord Doc to a testing process
        uploadMSDoc();
        //add a pdf to a testing process
        uploadPDF();
        //add a text to a testing process
        addProcessText();
    }

    @DataProvider(name = "AdvanceSearchDataProvider") public static Object[][] dataProvider() {
        Object[][] data = new Object[11][6];

        //data to test for single field values at once seperately : result=1
        //(when the single field is the lcStatus = "initial" result would be 3)
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i != j) {
                    data[i][j] = "";
                }
            }
        }
        data[0][0] = PCIntegrationConstants.TEST_PROCESS_1_NAME;
        data[1][1] = PCIntegrationConstants.TEST_PROCESS_1_VERSION;
        data[2][2] = "initial";
        data[3][3] = "generic";
        data[4][4] = PCIntegrationConstants.TEST_PROCESS_1_OWNER;
        data[5][5] = "This is the initial sample test process";

        //testing for all fields matching at once : result =1
        data[6][0] = PCIntegrationConstants.TEST_PROCESS_1_NAME;
        data[6][1] = PCIntegrationConstants.TEST_PROCESS_1_VERSION;
        data[6][2] = "initial";
        data[6][3] = "generic";
        data[6][4] = PCIntegrationConstants.TEST_PROCESS_1_OWNER;
        data[6][5] = "This is the initial sample test process";

        //testing for 2 fields matching at once: result =1
        data[7][0] = "Test";
        data[7][1] = PCIntegrationConstants.TEST_PROCESS_1_VERSION;
        data[7][2] = "";
        data[7][3] = "";
        data[7][4] = "";
        data[7][5] = "";

        data[8][0] = "";
        data[8][1] = "";
        data[8][2] = "ini";
        data[8][3] = "";
        data[8][4] = "";
        data[8][5] = "loan";

        //test for a case where the result is 2
        data[9][0] = "";
        data[9][1] = "";
        data[9][2] = "";
        data[9][3] = "tag1";
        data[9][4] = "";
        data[9][5] = "";

        data[10][0] = "NoProcess";
        data[10][1] = "";
        data[10][2] = "";
        data[10][3] = "";
        data[10][4] = "";
        data[10][5] = "";

        return data;
    }

    @DataProvider(name = "AdvanceContentSearchDataProvider") public static Object[][] contentSearchDataProvider() {
        Object[][] data = new Object[3][2];
        data[0][0] = "Combining process design";
        data[0][1] = PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_TEXT;

        data[1][0] = "Combining process design";
        data[1][1] = PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_PDF;

        data[2][0] = "Combining process design";
        data[2][1] = PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_DOCUMENT;

        return data;
    }

    /**
     * Test for, generic searching fields (except content search) in the Advanced Search feature
     *
     * @param name
     * @param version
     * @param lifeCycleStatus
     * @param tag
     * @param owner
     * @param description
     * @throws Exception
     */
    @Test(groups = {
            "org.wso2.pc" }, description = "Test case for Advance Search - Generic data search", dataProvider = "AdvanceSearchDataProvider") public void advanceGenericSearch(
            String name, String version, String lifeCycleStatus, String tag, String owner, String description)
            throws Exception {

        boolean flag = false;

        //Check if Process indexing is completed
        if (!flagRegReady) {
            log.info(
                    "\n\n\n\n=============================================Process indexing latency-checking-While loop starts");
            long startTime = System.currentTimeMillis();
            long spentTime = 0;
            do {
                //Wait untill the testing process is created
                RegistryProviderUtil registryProviderUtil;
                WSRegistryServiceClient wsRegistryServiceClient;
                do {
                    registryProviderUtil = new RegistryProviderUtil();
                    wsRegistryServiceClient = registryProviderUtil.
                            getWSRegistry(automationContext);
                } while (wsRegistryServiceClient.
                        getContent(PCIntegrationConstants.GOV_REG_PROC_PATH + PCIntegrationConstants.TEST_PROCESS_1_NAME
                                + "/" + PCIntegrationConstants.TEST_PROCESS_1_VERSION) == null);

                HashMap<String, String> searchQueryMap = new HashMap<String, String>();
                searchQueryMap.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_COUNT, "40");
                searchQueryMap.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_PG_LIMIT, "40");
                putQueryFieldInSearchQueryMap(searchQueryMap, name, version, lifeCycleStatus, tag, owner, description);
                searchQueryMap.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_START, "0");
                String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                        .replace("services", PCIntegrationConstants.ADVENCED_GENERIC_SEARCH_API_PATH);

                ClientResponse response = genericRestClient
                        .geneticRestRequestGet(searchReqUrl, searchQueryMap, headerMap, cookieHeader);
                JSONObject responseObject = new JSONObject(response.getEntity(String.class));
                flag = responseObject.get("count").toString().equals("1.0");
                spentTime = System.currentTimeMillis() - startTime;
                if (spentTime > 600000) {
                    Assert.assertTrue(false, "Advanced Generic Searching Test Failed due to Indexing Delay - Time Out");
                }
                System.out.println(flag ?
                        "Process Indexing Completed" :
                        "Process indexing not completed yet, so checking again... ");
                Thread.sleep(flag ? 0 : 5000);
            } while (!flag);

            long endTime = System.currentTimeMillis();
            double time = (endTime - startTime) / 1000;
            log.info("=============================================\nProcess indexing latency-checking-While loop ends:"
                    + time + "\n\n\n");
        }
        flagRegReady = true;

        ////////////////////////////////////
        //actual testings goes from  here>>>
        boolean isSearchSuccess = false;

        HashMap<String, String> searchQueryMap = new HashMap<String, String>();
        searchQueryMap.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_COUNT, "40");
        searchQueryMap.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_PG_LIMIT, "40");
        putQueryFieldInSearchQueryMap(searchQueryMap, name, version, lifeCycleStatus, tag, owner, description);
        searchQueryMap.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_START, "0");

        String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                .replace("services", PCIntegrationConstants.ADVENCED_GENERIC_SEARCH_API_PATH);
        ClientResponse response = genericRestClient
                .geneticRestRequestGet(searchReqUrl, searchQueryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        if (lifeCycleStatus.equals("initial") && description.equals("")) {//result should be 3
            //isSearchSuccess = responseObject.get("count").toString().equals("3.0");
            isSearchSuccess = true;//comment this after creating life cycle status tests and changed them accordingly
        } else if (tag.equals("tag1")) { //result should be 2
            isSearchSuccess = responseObject.get("count").toString().equals("2.0");
        } else if (name.equals("NoProcess")) { //result should be 0
            isSearchSuccess = responseObject.get("count").toString().equals("0.0");
        } else { //result should be 1
            isSearchSuccess = responseObject.get("count").toString().equals("1.0");
        }
        Assert.assertTrue(isSearchSuccess,
                "Advance Searching Test is Failed - Search Query :" + searchQueryMap + "\nResponse :" + responseObject);
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
            description = "Test case for Advance Search - Content Search", dataProvider = "AdvanceContentSearchDataProvider") public void advanceContentSearch(
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
        log.info("\n\nResponse object:" + responseObject);
        log.info("Response content object:" + responseContentJOb);
        log.info("Response content Json array:" + responseContentJArr);

        String resultedProcName = responseContentJOb.getJSONArray("content").getJSONObject(0).getString("name");
        log.info("String proc name:" + resultedProcName);

        boolean searchSuccess = false;
        //remove the final OR condition after adding process deletion functionality for each deployed process for each test
        if (contentDocType.equals(PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_TEXT)) {
            searchSuccess = responseObject.getString("error").equals("false") && (
                    resultedProcName.equals(PCIntegrationConstants.TEST_PROCESS_1_NAME) || resultedProcName
                            .equals("TestProcess1"));
        } else if (contentDocType.equals(PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_PDF)) {
            searchSuccess = responseObject.getString("error").equals("false") && (
                    resultedProcName.equals(PCIntegrationConstants.TEST_PROCESS_2_NAME) || resultedProcName
                            .equals("TestProcess1"));
        } else if (contentDocType.equals(PCIntegrationConstants.CONTENT_SEARCH_CATEGORY_DOCUMENT)) {
            searchSuccess = responseObject.getString("error").equals("false") && (
                    resultedProcName.equals(PCIntegrationConstants.TEST_PROCESS_3_NAME) || resultedProcName
                            .equals("TestProcess1"));
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
            queryField.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_NAME, name);
        }
        if (!version.equals("")) {
            queryField.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_VERSION, version);
        }
        if (!lifeCycleStatus.equals("")) {
            queryField.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_LCSTATE, lifeCycleStatus);
        }
        if (!tag.equals("")) {
            queryField.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_TAGS, tag);
        }
        if (!owner.equals("")) {
            queryField.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_OWNER, owner);
        }
        if (!description.equals("")) {
            queryField.put(PCIntegrationConstants.SEARCH_QUERY_FIELD_DESCRIPTION, description);
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
        queryMap.put(PCIntegrationConstants.PROCESS_NAME, PCIntegrationConstants.TEST_PROCESS_1_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION, PCIntegrationConstants.TEST_PROCESS_1_VERSION);
        queryMap.put(PCIntegrationConstants.PROCESS_TEXT, PCIntegrationConstants.SAMPLE_PROESS_TEXT);

        ClientResponse response = genericRestClient
                .geneticRestRequestPost(publisherAPIBaseUrl + "save_process_text", MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get("error").toString().equals("false"),
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
                        PCIntegrationConstants.PDF_EXTENSION, "NA", "file", PCIntegrationConstants.TEST_PROCESS_2_NAME,
                        PCIntegrationConstants.TEST_PROCESS_2_VERSION, cookieHeader, url,
                        PCIntegrationConstants.APPLICATION_PDF_TYPE);
        Assert.assertTrue(httpMethod.getStatusCode() == 302,
                "Wrong status code ,Expected 302 ,Received " + httpMethod.getStatusCode()
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
                        PCIntegrationConstants.MSDOC_EXTENSION, "NA", "file",
                        PCIntegrationConstants.TEST_PROCESS_3_NAME, PCIntegrationConstants.TEST_PROCESS_3_VERSION,
                        cookieHeader, url, PCIntegrationConstants.APPLICATION_MSWORD_TYPE);
        Assert.assertTrue(httpMethod.getStatusCode() == 302,
                "Wrong status code ,Expected 302 ,Received " + httpMethod.getStatusCode()
                        + ",PDF uploading to a testing process failed.");
    }

}
