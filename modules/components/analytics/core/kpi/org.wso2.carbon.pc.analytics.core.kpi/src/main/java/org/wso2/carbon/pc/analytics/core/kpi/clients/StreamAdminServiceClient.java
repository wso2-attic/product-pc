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
package org.wso2.carbon.pc.analytics.core.kpi.clients;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.analytics.core.kpi.AnalyticsConfigConstants;
import org.wso2.carbon.event.stream.stub.EventStreamAdminServiceStub;
import org.wso2.carbon.pc.core.ProcessCenterException;

import java.io.File;
import java.rmi.RemoteException;

/**
 * Access DAS EventStreamAdminService and creates the event stream.
 */
public class StreamAdminServiceClient {

    private EventStreamAdminServiceStub serviceAdminStub;
    private static final Log log = LogFactory.getLog(StreamAdminServiceClient.class);
    private static final String STREAM_ID_KEY = "streamId";
    private static final String STREAM_NAME_KEY = "name";
    private static final String STREAM_VERSION_KEY = "version";
    private static final String STREAM_NICK_NAME_KEY = "nickName";
    private static final String STREAM_DESCRIPTION_KEY = "description";
    private static final String PAYLOAD_DATA = "payloadData";

    /**
     * Create EventStreamAdminServiceStub object
     *
     * @param backEndUrl
     * @throws AxisFault
     */
    public StreamAdminServiceClient(String backEndUrl) throws AxisFault {
        String endPoint = backEndUrl + File.separator + AnalyticsConfigConstants.SERVICES + File.separator
                + AnalyticsConfigConstants.EVENT_STREAM_ADMIN_SERVICE_NAME;
        serviceAdminStub = new EventStreamAdminServiceStub(endPoint);
    }

    /**
     * @param session           session string
     * @param streamName        event stream name
     * @param streamVersion     event stream version
     * @param streamId          event stream ID
     * @param streamNickName    event stream nick name
     * @param streamDescription event stream description
     * @param processVariables Event stream payload fields including process variables.<p> Ex:
     *              [{"isAnalyzeData":false,"name":"processInstanceId","isDrillDownData":"false", "type":"string"},
     *              {"isAnalyzeData":false,"name":"valuesAvailability","isDrillDownData":"false","type":"string"},
     *              {"isAnalyzeData":false,"name":"custid","isDrillDownData":false,"type":"string"},
     *              {"isAnalyzeData":false,"name":"amount","isDrillDownData":false,"type":"long"},
     *              {"isAnalyzeData":false,"name":"confirm","isDrillDownData":false,"type":"bool"}]
     * @throws ProcessCenterException
     */
    public void createEventStream(String session, String streamName, String streamVersion, String streamId,
            String streamNickName, String streamDescription, JSONArray processVariables) throws ProcessCenterException {
        JSONObject streamDefinitionJsonOb = new JSONObject();
        try {
            // Authenticate stub from sessionCooke
            ServiceClient serviceClient = serviceAdminStub._getServiceClient();
            Options option = serviceClient.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, session);

            streamDefinitionJsonOb.put(STREAM_ID_KEY, streamId);
            streamDefinitionJsonOb.put(STREAM_NAME_KEY, streamName);
            streamDefinitionJsonOb.put(STREAM_VERSION_KEY, streamVersion);
            streamDefinitionJsonOb.put(STREAM_NICK_NAME_KEY, streamNickName);
            streamDefinitionJsonOb.put(STREAM_DESCRIPTION_KEY, streamDescription);

            //setting process variables as payloadData into the eventStream definition
            streamDefinitionJsonOb.put(PAYLOAD_DATA, processVariables);
            if (log.isDebugEnabled()) {
                log.debug("Stream Definition Json Object:" + streamDefinitionJsonOb.toString());
            }
            serviceAdminStub.addEventStreamDefinitionAsString(streamDefinitionJsonOb.toString());
        } catch (JSONException e) {
            String errMsg = "Error in creating event stream Definition Json object with data: " + "," + session + ","
                    + streamName + "," + streamVersion + "," + streamId + "," + streamNickName + "," + streamDescription
                    + "," + processVariables.toString();
            throw new ProcessCenterException(errMsg, e);
        } catch (RemoteException e) {
            String errMsg =
                    "Error in creating event stream in DAS with the StreamStringDefinition :" + streamDefinitionJsonOb
                            .toString();
            throw new ProcessCenterException(errMsg, e);
        }
    }
}