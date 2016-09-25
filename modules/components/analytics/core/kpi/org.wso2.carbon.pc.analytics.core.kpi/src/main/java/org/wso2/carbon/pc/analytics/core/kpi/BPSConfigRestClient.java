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
 */
package org.wso2.carbon.pc.analytics.core.kpi;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.pc.analytics.core.kpi.utils.DASConfigurationUtils;
import org.wso2.carbon.registry.core.utils.RegistryUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Rest Client to communicate the analytics configuration details to the WSO2 BPS from PC
 */
public class BPSConfigRestClient {
    private static final Log log = LogFactory.getLog(BPSConfigRestClient.class);

    /**
     * Send post request to a BPS BPMN rest web service
     *
     * @param dasConfigDetails is the request message that need to be sent to the web service
     * @param processName
     * @param processVersion
     * @return the result as a String
     */
    public static void post(String dasConfigDetails, String processName, String processVersion)
            throws IOException, XMLStreamException, RuntimeException {

        log.debug("Sending POST request to WSO2 BPS, to communicate the analytics configuration details and configure "
                        + "BPS for analytics");
        String bpsUrl = DASConfigurationUtils.getBPSURL();
        RegistryUtils.setTrustStoreSystemProperties();
        HttpClient httpClient = new HttpClient();
        String requestUrl = bpsUrl + AnalyticsConfigConstants.BPS_PROCESS_VAR_PUBLISH_REST_PATH + processName + "_"
                + processVersion;
        PostMethod postRequest = new PostMethod(requestUrl);
        postRequest.setRequestHeader(HttpHeaders.AUTHORIZATION, DASConfigurationUtils.getAuthorizationHeader());
        StringRequestEntity input = new StringRequestEntity(dasConfigDetails, MediaType.APPLICATION_JSON,
                StandardCharsets.UTF_8.name());
        postRequest.setRequestEntity(input);
        int returnCode = httpClient.executeMethod(postRequest);
        InputStreamReader reader = new InputStreamReader((postRequest.getResponseBodyAsStream()),
                StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        String output;
        StringBuilder totalOutput = new StringBuilder();
        while ((output = br.readLine()) != null) {
            totalOutput.append(output);
        }
        String responseMsg = totalOutput.toString();
        postRequest.releaseConnection();
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                String errMsg = "BPS Config Rest client BufferedReader close exception.";
                log.error(errMsg, e);
            }
        }
        //deal with the response
        if (returnCode == HttpStatus.SC_OK) {
            log.debug("BPS was configured with analytics configuration details.");
        } else {
            String errMsg =
                    "Failed : Sending the REST Post call to the WSO2 BPS to communicate the analytics configuration "
                            + "details to the BPS from PC\n: HTTP Error code : " + returnCode;
            throw new RuntimeException(responseMsg + errMsg);
        }
    }
}
