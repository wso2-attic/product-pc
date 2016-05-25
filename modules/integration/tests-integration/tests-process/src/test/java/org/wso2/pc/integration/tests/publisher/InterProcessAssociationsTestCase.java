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

import com.google.gson.Gson;
import org.apache.wink.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.pc.integration.test.utils.base.*;
import org.wso2.pc.integration.test.utils.base.beans.ProcessBean;
import org.xml.sax.InputSource;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class    InterProcessAssociationsTestCase extends PCIntegrationBaseTest {

    private String processResourcePath;
    private String cookieHeader;
    private String subProcessID;
    private String predecessorProcessID;
    private String successorProcessID;
    private static final String PREDECESSOR_PROCESS_NAME = "PredecessorProcess";
    private static final String SUBPROCESS_NAME = "SubProcess";
    private static final String SUCESSOR_PROCESS_NAME = "SuccessorProcess";
    private RegistryProviderUtil registryProviderUtil = new RegistryProviderUtil();
    private GenericRestClient genericRestClient = new GenericRestClient();
    private HashMap<String, String> headerMap = new HashMap<>();
    private HashMap<String, String> queryMap = new HashMap<>();
    private static final String PROCESS_NAME = "TestProcess1";
    private static final String PROCESS_VERSION = "1.0";
    private ProcessBean predecessorProcess;
    private ProcessBean subProcess;
    private ProcessBean successorProcess;

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        String publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", "publisher/apis");
        GenericRestClient genericRestClient = new GenericRestClient();

        //logging in to publisher and obtain session cookie
        JSONObject objSessionPublisher =
                new JSONObject(TestUtils.authenticate(publisherUrl, genericRestClient,
                        automationContext.getSuperTenant().getTenantAdmin().getUserName(),
                        automationContext.getSuperTenant().getTenantAdmin().getPassword(), queryMap,
                        headerMap).getEntity(String.class));
        String jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;

        processResourcePath = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "json" + File.separator + "create-process.json";
        String subProcessResourcePath = FrameworkPathUtil.getSystemResourceLocation() +
                "artifacts" + File.separator + "json" + File.separator + "SubProcess.json";
        String predecessorProcessResourcePath = FrameworkPathUtil.getSystemResourceLocation() +
                "artifacts" + File.separator
                + "json" + File.separator + "PredecessorProcess.json";
        String successorProcessResourcePath = FrameworkPathUtil.getSystemResourceLocation() +
                "artifacts" + File.separator + "json" + File.separator + "SuccessorProcess.json";

        //Adding sub process
        subProcessID = TestUtils.addProcess(readFile(subProcessResourcePath), cookieHeader,
                publisherAPIBaseUrl);

        //Adding predecessor process
        predecessorProcessID = TestUtils.addProcess(readFile(predecessorProcessResourcePath),
                cookieHeader, publisherAPIBaseUrl);

        //Adding successor process
        successorProcessID = TestUtils.addProcess(readFile(successorProcessResourcePath),
                cookieHeader, publisherAPIBaseUrl);

    }

    @Test(groups = {"org.wso2.pc"}, description = "Adding a process with subprocess, predecessor " +
            "process and successorprocess")
    public void addProcess() throws Exception {

        predecessorProcess = createProcessBean(PREDECESSOR_PROCESS_NAME,
                "/processes/PredecessorProcess/1.0", predecessorProcessID);
        subProcess = createProcessBean(SUBPROCESS_NAME,
                "/processes/SubProcess/1.0", subProcessID);
        successorProcess = createProcessBean(SUCESSOR_PROCESS_NAME,
                "/processes/SuccessorProcess/1.0", successorProcessID);

        Gson gson = new Gson();
        String requestBody = readFile(processResourcePath);
        JSONObject jsonObject = new JSONObject(requestBody);

        jsonObject.getJSONArray("predecessor").put(new JSONObject(gson.toJson(predecessorProcess)));
        jsonObject.getJSONArray("subprocess").put(new JSONObject(gson.toJson(subProcess)));
        jsonObject.getJSONArray("successor").put(new JSONObject(gson.toJson(successorProcess)));

        String processID = TestUtils.addProcess(jsonObject.toString()
                , cookieHeader, publisherAPIBaseUrl);
        Assert.assertFalse(processID.contains("error"),
                "Error while creating process with associate processes");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Subprocess test case",
            dependsOnMethods = "addProcess")
    public void subProcessTest() throws Exception {
        Element subProcessElement = getAssociateProcess("subprocess");
        Assert.assertNotNull(subProcessElement, "Subprocess doesn't exist");
        Assert.assertEquals(subProcessElement.getElementsByTagName("id").item(0).
                getTextContent(), subProcessID, "Subprocess error");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Successor Process test case" )
    public void successorProcessTest() throws Exception {
        Element successorProcessElement = getAssociateProcess("successor");
        Assert.assertNotNull(successorProcessElement,"Successor Process doesn't exist");
        Assert.assertEquals(successorProcessElement.getElementsByTagName("id").item(0).
                getTextContent(), successorProcessID, "SuccessorProcess error");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Predecessor Process test case")
    public void predecessorTest() throws Exception {
        Element predecessorProcessElement = getAssociateProcess("predecessor");
        Assert.assertNotNull(predecessorProcessElement,"PredecessorProcess doesn't exist");
        Assert.assertEquals(predecessorProcessElement.getElementsByTagName("id").item(0).
                getTextContent(), predecessorProcessID, "predecessor error");
    }

    @Test(groups = {"org.wso2.pc"}, description = "Delete subprocess test",
            dependsOnMethods = "subProcessTest")
    public void deleteSubprocess() throws JSONException, UnsupportedEncodingException {
        queryMap.put("deleteSubprocessDetails",
                URLEncoder.encode(associateProcessDeleteRequest("deleteSubprocess",
                        PROCESS_NAME,PROCESS_VERSION,subProcess),PCIntegrationConstants.UTF_8));
        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "delete_subprocess", MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,null, queryMap,headerMap,cookieHeader);
        Assert.assertTrue(new JSONObject(response.getEntity(String.class)).get("error").toString().
                equals("false"));
    }

    @Test(groups = {"org.wso2.pc"}, description = "Delete Predecessor process test",
            dependsOnMethods = "predecessorTest")
    public void deletePredecessor() throws JSONException, UnsupportedEncodingException {
        queryMap.put("deletePredecessorDetails",
                URLEncoder.encode(associateProcessDeleteRequest("deletePredecessor",
                        PROCESS_NAME,PROCESS_VERSION,predecessorProcess),PCIntegrationConstants.UTF_8));
        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "delete_Predecessor", MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,null, queryMap,headerMap,cookieHeader);
        Assert.assertTrue(new JSONObject(response.getEntity(String.class)).get("error").toString().
                equals("false"));
    }

    @Test(groups = {"org.wso2.pc"}, description = "Delete Successor process test",
            dependsOnMethods = "successorProcessTest")
    public void deleteSuccessor() throws JSONException, UnsupportedEncodingException {
        queryMap.put("deleteSuccessorDetails",
                URLEncoder.encode(associateProcessDeleteRequest("deleteSuccessor",
                        PROCESS_NAME,PROCESS_VERSION,successorProcess),PCIntegrationConstants.UTF_8));
        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "delete_successor", MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,null, queryMap,headerMap,cookieHeader);
        Assert.assertTrue(new JSONObject(response.getEntity(String.class)).get("error").toString().
                equals("false"));
    }

    private Element getAssociateProcess(String processType) throws Exception {
        Element associateProcessElement = null;
        WSRegistryServiceClient wsRegistryServiceClient = registryProviderUtil.
                getWSRegistry(automationContext);
        String xml = new String(wsRegistryServiceClient.
                getContent("/_system/governance/processes/TestProcess1/1.0"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        Element root = document.getDocumentElement();
        if (root.getElementsByTagName(processType) != null)
            associateProcessElement = (Element) root.getElementsByTagName(processType).item(0);
        return associateProcessElement;
    }

    private ProcessBean createProcessBean(String name, String path, String ID) {
        ProcessBean process = new ProcessBean();
        process.setName(name);
        process.setId(ID);
        process.setPath(path);
        return process;
    }

    private String associateProcessDeleteRequest(String action,
                                                 String processName,
                                                 String processVersion,
                                                 ProcessBean associateProcess)
            throws JSONException {
        JSONObject deleteProcessObject = new JSONObject();
        Gson gson = new Gson();
        deleteProcessObject.put(PCIntegrationConstants.PROCESS_NAME,processName);
        deleteProcessObject.put(PCIntegrationConstants.PROCESS_VERSION,processVersion);
        deleteProcessObject.put(action,new JSONObject(gson.toJson(associateProcess)));
        return deleteProcessObject.toString();
    }
}