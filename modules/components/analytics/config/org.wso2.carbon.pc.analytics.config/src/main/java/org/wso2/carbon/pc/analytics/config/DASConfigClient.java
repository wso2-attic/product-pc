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
package org.wso2.carbon.pc.analytics.config;

/**
 * Configure DAS configurations to publish data to DAS an do the analytics
 * (initiator class in the module)
 */

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.pc.analytics.config.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.ReceiverAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.StreamAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.utils.DASConfigurationUtils;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.rmi.RemoteException;

public class DASConfigClient {

    String streamName;
    String stremaVersion;
    String streamId;
    String streamDescription;
    String streamNickName;
    String receiverName;
    JSONArray processVariablesJObArr;
    private static final Log log = LogFactory.getLog(DASConfigClient.class);

    public boolean configDAS(String DASconfigDetails) {

        log.info("Configuring WSO2 DAS for analytics of WSO2 PC...");//log process name,url
        JSONObject processInfo = null;
        try {
            processInfo = new JSONObject(DASconfigDetails);
            streamName = processInfo.getString("eventStreamName");
            stremaVersion = processInfo.getString("eventStreamVersion");
            streamId = processInfo.getString("eventStreamId");
            streamDescription = processInfo.getString("eventStreamDescription");
            streamNickName = processInfo.getString("eventStreamNickName");
            receiverName = processInfo.getString("eventReceiverName");
            processVariablesJObArr = processInfo.getJSONArray("processVariables");
        } catch (JSONException e) {
            String errMsg = "Error in extracting data from JSON string";
            log.error(errMsg, e);
        }

        System.setProperty("javax.net.ssl.trustStore",
                "/home/samithac/wso2-products/wso2das-3.0.0-SNAPSHOT/repository/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = null;
        try {
            backEndUrl = DASConfigurationUtils.getURL();
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (XMLStreamException e) {
            log.error(e.getMessage());
        }

        // login to DAS as admin
        LoginAdminServiceClient login = null;
        try {
            login = new LoginAdminServiceClient(backEndUrl);

            String session = login.authenticate("admin", "admin");
        } catch (RemoteException e) {
            String errMsg = "Remote exception in login to DAS AuthenticationAdmin service";
            log.error(errMsg, e);
        } catch (LoginAuthenticationExceptionException e) {
            String errMsg = "Authentication error in login to DAS AuthenticationAdmin service";
            log.error(errMsg, e);
        }

        //create event stream
        StreamAdminServiceClient streamAdminServiceClient = null;
        try {
            streamAdminServiceClient = new StreamAdminServiceClient(backEndUrl, session, streamName, stremaVersion,
                    streamId, streamNickName, streamDescription, processVariablesJObArr);
        } catch (AxisFault axisFault) {
            log.error(axisFault.getMessage());
            return false;
        }
        boolean successCreateStream = streamAdminServiceClient.createEventStream();
        if (!successCreateStream) {
            try {
                login.logOut();
            } catch (RemoteException e) {
                String errMsg = "Remote exception in login out from DAS AuthenticationAdmin service";
                log.error(errMsg, e);
            } catch (LogoutAuthenticationExceptionException e) {
                String errMsg = "Authentication error in login out from DAS AuthenticationAdmin service";
                log.error(errMsg, e);
            }
            return false;
        }
        log.info("Created the Event Stream: " + streamId + " in WSO2 DAS");

        //create event receiver
        ReceiverAdminServiceClient receiverAdminServiceClient = null;
        receiverAdminServiceClient = new ReceiverAdminServiceClient(backEndUrl, session, receiverName, streamId,
                "wso2event");
        boolean receiverConfigSuccess = receiverAdminServiceClient.deployEventReceiverConfiguration();
        if (receiverConfigSuccess) {
            log.debug("Created the Event Receiver: " + receiverName + "for the " + streamId + " in WSO2 DAS");
        }

        //logging out
        try {
            login.logOut();
        } catch (RemoteException e) {
            String errMsg = "Remote exception in login out from DAS AuthenticationAdmin service";
            log.error(errMsg, e);
        } catch (LogoutAuthenticationExceptionException e) {
            String errMsg = "Authentication error in login out from DAS AuthenticationAdmin service";
            log.error(errMsg, e);
        }
        return receiverConfigSuccess;
    }
}