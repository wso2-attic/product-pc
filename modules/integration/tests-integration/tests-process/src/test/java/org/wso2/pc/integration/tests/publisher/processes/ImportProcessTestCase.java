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
package org.wso2.pc.integration.tests.publisher.processes;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wink.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.pc.integration.test.utils.base.*;
import org.xml.sax.InputSource;

import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class ImportProcessTestCase extends PCIntegrationBaseTest {

    private GenericRestClient genericRestClient;
    private HashMap<String, String> headerMap;
    private HashMap<String, String> queryMap;
    private String resourcePath,publisherUrl,cookieHeader;
    private RegistryProviderUtil registryProviderUtil = new RegistryProviderUtil();
    private static final String PROCESS_NAME = "Process1";
    private static final String PROCESS_VERSION = "1.0";

    @BeforeTest(alwaysRun = true)
    public void init() throws Exception {
        super.init();
        publisherUrl = automationContext.getContextUrls().getSecureServiceUrl().
                replace("services", PCIntegrationConstants.DESIGNER_APIS);
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
        String jSessionId = objSessionPublisher.getJSONObject("data").getString("sessionId");
        cookieHeader = "JSESSIONID=" + jSessionId;
    }

    @Test(groups = {"org.wso2.pc"}, description = "Importing process")
    public void importProcess() throws IOException {
        String resourcePath1 = FrameworkPathUtil.getSystemResourceLocation() + "artifacts" +
                File.separator + "exportedProcess" + File.separator + "Process1-1.0-PC-Package.zip";
        String url = publisherProcessAPIBaseUrl + "import_process";
        PostMethod httpMethod = ArtifactUploadUtil.uploadProcess(resourcePath1, cookieHeader, url);
        Assert.assertTrue(httpMethod.getStatusCode() == 200,
                "Wrong status code ,Expected 302 ,Received " + httpMethod.getStatusCode());
    }

    @Test(groups = {"org.wso2.pc"}, description = "Checking associated BPMN",
            dependsOnMethods = "importProcess")
    public void checkBPMN() throws Exception {
        Element bpmnElement = getAssociateProcess("name");
        Assert.assertNotNull(bpmnElement,"Associated BPMN doesn't exist");
        Assert.assertTrue(bpmnElement.getTextContent().equals(PROCESS_NAME),
                "Process1 doesn't have associated BPMN");
    }

    private Element getAssociateProcess(String processType) throws Exception {
        Element associateProcessElement = null;
        WSRegistryServiceClient wsRegistryServiceClient = registryProviderUtil.
                getWSRegistry(automationContext);
        String xml = new String(wsRegistryServiceClient.
                getContent("/_system/governance/bpmn/Process1/1.0"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        Element root = document.getDocumentElement();
        if (root.getElementsByTagName(processType) != null)
            associateProcessElement = (Element) root.getElementsByTagName(processType).item(0);
        return associateProcessElement;
    }

    @Test(groups = {"org.wso2.pc"}, description = "Deleting flowchart",
            dependsOnMethods = "importProcess")
    public void deleteFlowchart() throws JSONException {
        queryMap.put(PCIntegrationConstants.PROCESS_NAME,PROCESS_NAME);
        queryMap.put(PCIntegrationConstants.PROCESS_VERSION,PROCESS_VERSION);
        queryMap.put("flowchartPath",String.format("%s%s/%s",
                PCIntegrationConstants.REG_FLOWCHART_PATH, PROCESS_NAME, PROCESS_VERSION));
        ClientResponse response = genericRestClient.geneticRestRequestGet(publisherProcessAPIBaseUrl +
                "delete_flowchart",queryMap,headerMap,cookieHeader);
        Assert.assertTrue(response.getStatusCode() == PCIntegrationConstants.RESPONSE_CODE_OK,
                "Expected 200 OK, Received " + response.getStatusCode());
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_ERROR).toString().
                equals("false"),"Error while deleting flowchart");
        Assert.assertTrue(responseObject.get(PCIntegrationConstants.RESPONSE_CONTENT).toString().
                contains("Successfully deleted the flowchart"),"Couldn't delete flowchart");
    }
}