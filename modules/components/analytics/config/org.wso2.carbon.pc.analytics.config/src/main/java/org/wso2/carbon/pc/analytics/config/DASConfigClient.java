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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.pc.analytics.config.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.ReceiverAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.StreamAdminServiceClient;

import java.rmi.RemoteException;

public class DASConfigClient {

    String streamName;
    String stremaVersion;
    String streamId;
    String streamDescription;
    String streamNickName;
    String receiverName;
    JSONArray processVariablesJObArr;

    public boolean configDAS(String processVariableDetails) {

        JSONObject processInfo = null;
        try {
            processInfo = new JSONObject(processVariableDetails);
            streamName = processInfo.getString("eventStreamName");
            stremaVersion = processInfo.getString("eventStreamVersion");
            streamId=processInfo.getString("eventStreamId");
            streamDescription=processInfo.getString("eventStreamDescription");
            streamNickName=processInfo.getString("eventStreamNickName");
            receiverName=processInfo.getString("eventReceiverName");
            processVariablesJObArr = processInfo.getJSONArray("processVariables");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    System.setProperty(
                "javax.net.ssl.trustStore",
                "/home/samithac/wso2-products/wso2das-3.0.0-SNAPSHOT/repository/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = "https://localhost:9448";

        // login to DAS as admin
        LoginAdminServiceClient login = null;
        try {
            login = new LoginAdminServiceClient(backEndUrl);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        String session = null;
        try {
            session = login.authenticate("admin", "admin");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        }
        System.out.println(session);

        ////////////////
        /*try {
            EventReceiverAdminServiceStub eventReceiverAdminServiceStub = new EventReceiverAdminServiceStub("https://localhost:9448/services/EventReceiverAdminService");
            EventReceiverAdminServiceStub eventReceiverAdminServiceStub2 = new EventReceiverAdminServiceStub("https://localhost:9448/services/EventReceiverAdminService");

            //eventReceiverAdminServiceStub.
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }*/

        ///////////////
        //create event stream
        StreamAdminServiceClient streamAdminServiceClient = null;
        try {
            streamAdminServiceClient = new StreamAdminServiceClient(backEndUrl, session,streamName,stremaVersion,streamId,streamNickName,streamDescription, processVariablesJObArr);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            return false;
        }
        /*System.out.println("getEventStreamNames::"
				+ streamAdminServiceClient.getGG());*/
		boolean successCreateStream=streamAdminServiceClient.createEventStream();

        if(!successCreateStream){
            try {
                login.logOut();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (LogoutAuthenticationExceptionException e) {
                e.printStackTrace();
            }
            return false;
        }

        //create event receiver
        ReceiverAdminServiceClient receiverAdminServiceClient= null;
        receiverAdminServiceClient = new ReceiverAdminServiceClient(backEndUrl, session,receiverName,streamId,"wso2event");
        boolean receiverConfigSuccess=receiverAdminServiceClient.deployEventReceiverConfiguration();
        //receiverAdminServiceClient.deployEventReceiverConfiguration(eventReceiverName, streamNameWithVersion, eventAdapterType);
        //EventReceiverAdminServiceStub eventReceiverAdminServiceStub=new EventReceiverAdminServiceStub(backEndUrl);


        try {
            login.logOut();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LogoutAuthenticationExceptionException e) {
            e.printStackTrace();
        }
        return receiverConfigSuccess;
    }
}