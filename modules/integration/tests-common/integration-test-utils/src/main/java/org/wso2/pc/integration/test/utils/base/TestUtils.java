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
package org.wso2.pc.integration.test.utils.base;

import org.apache.wink.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    /**
     * Authenticate and return jSessionId
     *
     * @param url
     * @param genericRestClient
     * @param username
     * @param password
     * @return ClientResponse
     * @throws JSONException
     */
    public static ClientResponse authenticate(String url,
                                       GenericRestClient genericRestClient,
                                       String username,
                                       String password,
                                       Map<String, String> queryParamMap,
                                       Map<String, String> headerMap) throws JSONException {
        ClientResponse response =
                genericRestClient.geneticRestRequestPost(url + "/authenticate/",
                                                         MediaType.APPLICATION_FORM_URLENCODED,
                                                         MediaType.APPLICATION_JSON,
                                                         "username=" + username + "&password=" +
                                                                 password, queryParamMap, headerMap,
                                                          null);
        return response;
    }

    public static String addProcess(String requestBody,
                                    String cookieHeader,
                                    String publisherAPIBaseUrl)
            throws UnsupportedEncodingException, JSONException {
        GenericRestClient genericRestClient = new GenericRestClient();
        HashMap<String, String> queryMap = new HashMap<>();
        HashMap<String, String> headerMap = new HashMap<>();
        queryMap.put("processInfo", URLEncoder.encode(requestBody, PCIntegrationConstants.UTF_8));

        ClientResponse response = genericRestClient.geneticRestRequestPost(publisherAPIBaseUrl +
                        "create_process" , MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, null,
                queryMap, headerMap, cookieHeader);
        JSONObject responseObject = new JSONObject(response.getEntity(String.class));
        return responseObject.get("content").toString();
    }
}
