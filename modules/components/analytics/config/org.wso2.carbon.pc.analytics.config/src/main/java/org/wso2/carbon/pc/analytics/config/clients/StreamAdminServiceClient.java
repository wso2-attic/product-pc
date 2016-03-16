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
package org.wso2.carbon.pc.analytics.config.clients;

/**
 * Access DAS EventStreamAdminService and creates the event stream.
 */

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse;

import java.rmi.RemoteException;

public class StreamAdminServiceClient {
    private final String serviceName = "EventStreamAdminService";
    private EventStreamAdminServiceStub serviceAdminStub;
    private String endPoint;
    ServiceClient serviceClient;
    Options option;
    private static final Log log = LogFactory.getLog(StreamAdminServiceClient.class);
    JSONObject streamDefinitionJsonOb;


    public StreamAdminServiceClient(String backEndUrl, String session, String streamName, String streamVersion, String streamId, String streamNickName, String streamDescription, JSONArray processVariablesJObArr)
            throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        serviceAdminStub = new EventStreamAdminServiceStub(endPoint);

        // Authenticate stub from sessionCooke
        serviceClient = serviceAdminStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(
                org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
                session);

        streamDefinitionJsonOb = new JSONObject();
        try {
            streamDefinitionJsonOb.put("streamId", streamId);
            streamDefinitionJsonOb.put("name", streamName);
            streamDefinitionJsonOb.put("version", streamVersion);
            streamDefinitionJsonOb.put("nickName", streamNickName);
            streamDefinitionJsonOb.put("description", streamDescription);
            JSONArray processVariablesJArr = new JSONArray();

            //setting process variables as payloadData into the eventStream definition
            streamDefinitionJsonOb.put("payloadData", processVariablesJObArr);
            log.debug(streamDefinitionJsonOb.toString());
        } catch (JSONException e) {
            String errMsg = "Error in creating event stream Definition Json object";
            log.error(errMsg, e);
        }
    }

    public boolean createEventStream() {

        AddEventStreamDefinitionAsString addEventStreamDefinitionAsString = new AddEventStreamDefinitionAsString();
        AddEventStreamDefinitionAsStringResponse addEventStreamDefinitionAsStringResponse = null;
        //addEventStreamDefinitionAsString
        //		.setStreamStringDefinition("{ \"streamId\": \"streamId\",  			  \"name\": \"org.wso2.test1\",    			  \"version\": \"1.0.0\",    			  \"nickName\": \"TestStream\",    			  \"description\": \"Test Stream\", \"metaData\": [    {      \"name\": \"ip\",      \"type\": \"STRING\"    }  ],  \"correlationData\": [    {      \"name\": \"id\",      \"type\": \"LONG\"    }  ],  \"payloadData\": [    {      \"name\": \"testMessage\",    \"type\": \"STRING\"   }  ]}");
        addEventStreamDefinitionAsString.setStreamStringDefinition(streamDefinitionJsonOb.toString());
        try {
            addEventStreamDefinitionAsStringResponse = serviceAdminStub
                    .addEventStreamDefinitionAsString(addEventStreamDefinitionAsString);
        } catch (RemoteException e) {
            String errMsg = "Error in creating event stream in DAS";
            log.error(errMsg, e);
            return false;
        }
        /*try {
            log.info("Created the Event Stream: "+streamDefinitionJsonOb.getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
        return true;
    }

}