package org.wso2.pc.integration.tests.publisher;

import org.apache.wink.client.ClientResponse;
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
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by samithac on 16/5/16.
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

   // String processName = "TestProcess1";
   // String processVersion = "1.0";
    //String processText = "This is a test text of the process";
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

        String createProcessFiles [] = {"create-process.json","create-process-2.json","create-process-3.json"};

        for (String fileName:createProcessFiles) {
            resourcePath =
                    FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator + "json" + File.separator
                            + fileName;
            queryMap = new HashMap<>();
            requestBody = readFile(resourcePath);
            queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));
            genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl + "create_process", MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);
        }
        /*resourcePath =
                FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator + "json" + File.separator
                        + "create-process.json";
        queryMap = new HashMap<>();
        requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));
        genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl + "create_process", MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);

        //add 2nd test process
        resourcePath =
                FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator + "json" + File.separator
                        + "create-process-2.json";
        queryMap = new HashMap<>();
        requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));
        genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl + "create_process", MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);

        //add 3rd test process
        resourcePath =
                FrameworkPathUtil.getSystemResourceLocation() + "artifacts" + File.separator + "json" + File.separator
                        + "create-process-3.json";
        queryMap = new HashMap<>();
        requestBody = readFile(resourcePath);
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));
        genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl + "create_process", MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_JSON, requestBody, queryMap, headerMap, cookieHeader);*/
    }

    @DataProvider(name = "AdvanceSearchDataProvicer") public static Object[][] dataProvider() {
        Object[][] data = new Object[11][6];

        //testing for single field values seperately : result=1
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i != j) {
                    data[i][j] = "";
                }
            }
        }
        //when the single field is the lcStatus = "initial" result would be 3

        data[0][0] = "TestProcess1";
        data[1][1] = "1.0";
        data[2][2] = "initial";
        data[3][3] = "generic";
        data[4][4] = "TestOwner1";
        data[5][5] = "This is the initial sample test process";

        //testing for all fields matching at once : result =1
        data[6][0] = "TestProcess1";
        data[6][1] = "1.0";
        data[6][2] = "initial";
        data[6][3] = "generic";
        data[6][4] = "TestOwner1";
        data[6][5] = "This is the initial sample test process";

        //testing for 2 fields matching at once: result =1
        data[7][0] = "Test";
        data[7][1] = "1.0";
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

        //test for a situation where the result is 2
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

       /*
        > tag= generic
        > with all fields
        > tag= tag1

        */

        return data;
    }

    @Test(groups = {
            "org.wso2.pc" }, description = "Test case for advance search", dataProvider = "AdvanceSearchDataProvicer")
    public void advanceSearch(
            String name, String version, String lifeCycleStatus, String tag, String owner, String description)
            throws Exception {

        boolean flag = false;

        if (!flagRegReady) {
            do {
                //Wait untill the testing process is created
                RegistryProviderUtil registryProviderUtil;
                WSRegistryServiceClient wsRegistryServiceClient;
                do {
                    registryProviderUtil = new RegistryProviderUtil();
                    wsRegistryServiceClient = registryProviderUtil.
                            getWSRegistry(automationContext);
                } while (wsRegistryServiceClient.
                        getContent("/_system/governance/processes/TestProcess1/1.0") == null);

                HashMap<String, String> searchQueryMap = new HashMap<String, String>();
                searchQueryMap.put("count", "40");
                searchQueryMap.put("paginationLimit", "40");
                putQueryFieldInSearchQueryMap(searchQueryMap, name, version, lifeCycleStatus, tag, owner, description);
                searchQueryMap.put("start", "0");

                log.info("\nsearchQueryMap: " + searchQueryMap);

                String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                        .replace("services", "publisher/apis/assets");

                //searchQueryMap.put(PCIntegrationConstants.PROCESS_TEXT, processText);

                ClientResponse response = genericRestClient
                        .geneticRestRequestGet(searchReqUrl, searchQueryMap, headerMap, cookieHeader);
                JSONObject responseObject = new JSONObject(response.getEntity(String.class));

                flag = responseObject.get("count").toString().equals("1.0");
                // if(lifeCycleStatus.equals("")) {

                //  }else {
                //     Assert.assertTrue(responseObject.get("count").toString().equals("1.0"),
                //             "Advance Searching is unsuccessful - Response :" + responseObject);
                // }
            } while (!flag);
        }
        flagRegReady = true;
        /*Assert.assertTrue(!flag,
                "Advance Searching is unsuccessful");*/

        ////////////////////////
        //actual testings goes from  here

        boolean isSearchSuccess = false;
        RegistryProviderUtil registryProviderUtil = new RegistryProviderUtil();
        WSRegistryServiceClient wsRegistryServiceClient = registryProviderUtil.
                getWSRegistry(automationContext);

        HashMap<String, String> searchQueryMap = new HashMap<String, String>();
        searchQueryMap.put("count", "40");
        searchQueryMap.put("paginationLimit", "40");
        putQueryFieldInSearchQueryMap(searchQueryMap, name, version, lifeCycleStatus, tag, owner, description);
        searchQueryMap.put("start", "0");

        //log.info("\nsearchQueryMap: " + searchQueryMap);

        String searchReqUrl = automationContext.getContextUrls().getSecureServiceUrl()
                .replace("services", "publisher/apis/assets");
        ClientResponse response = genericRestClient
                .geneticRestRequestGet(searchReqUrl, searchQueryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));

        if (lifeCycleStatus.equals("initial") && description.equals("")) {//result should be 3
            isSearchSuccess = responseObject.get("count").toString().equals("3.0");
            Assert.assertTrue(isSearchSuccess, "Advance Searching Test is Failed - Search Query :"+searchQueryMap +"\nResponse :" + responseObject);
        } else if (tag.equals("tag1")) { //result should be 2
            isSearchSuccess = responseObject.get("count").toString().equals("2.0");
            Assert.assertTrue(isSearchSuccess, "Advance Searching Test is Failed - Search Query :"+searchQueryMap +"\nResponse :" + responseObject);
        } else if (name.equals("NoProcess")) { //result should be 0
            isSearchSuccess = responseObject.get("count").toString().equals("0.0");
            Assert.assertTrue(isSearchSuccess, "Advance Searching Test is Failed - Search Query :"+searchQueryMap +"\nResponse :" + responseObject);
        } else { //result should be 1
            isSearchSuccess = responseObject.get("count").toString().equals("1.0");
            Assert.assertTrue(isSearchSuccess, "Advance Searching Test is Failed - Search Query :"+searchQueryMap +"\nResponse :" + responseObject);
        }
    }

    private void putQueryFieldInSearchQueryMap(HashMap<String, String> searchQueryMap, String name, String version,
            String lifeCycleStatus, String tag, String owner, String description) throws JSONException {
            /*searchQueryMap.put("q", String.format(
                        "\"name\":\"%s\",\"version\":\"%s\",\"lcState\":\"%s\",\"tags\":\"%s\",\"owner\":\"%s\",\"description\":\"%s\"",
                        name, version, lifeCycleStatus, tag, owner, description));*/
        //	"name":"test","version":"45","lcState":"created","tags":"sam","owner":"admin","description":"this is "
            JSONObject queryField = new JSONObject();
        if (!name.equals("")){
            queryField.put("name",name);
        }
        if (!version.equals("")){
            queryField.put("version",version);
        }
        if (!lifeCycleStatus.equals("")){
            queryField.put("lcState",lifeCycleStatus);
        }
        if (!tag.equals("")){
            queryField.put("tags",tag);
        }
        if (!owner.equals("")){
            queryField.put("owner",owner);
        }
        if (!description.equals("")){
            queryField.put("description",description);
        }
        String queryFieldStringValue= queryField.toString().substring(1,queryField.toString().length()-1);
        searchQueryMap.put("q",queryFieldStringValue);

    }

}
