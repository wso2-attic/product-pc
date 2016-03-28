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

import java.rmi.RemoteException;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.event.receiver.stub.EventReceiverAdminServiceStub;
import org.wso2.carbon.event.receiver.stub.types.BasicInputAdapterPropertyDto;

/**
 * Access DAS EventReceiverAdminService and creates the event receiver.
 */
public class ReceiverAdminServiceClient {
    private final String serviceName = "EventReceiverAdminService";
    private EventReceiverAdminServiceStub eventReceiverAdminServiceStub;
    private String endPoint;
    private String receiverName;
    private String streamId;
    private String eventAdapterType = "wso2event";
    private static final Log log = LogFactory.getLog(ReceiverAdminServiceClient.class);

    public ReceiverAdminServiceClient(String backEndUrl, String sessionCookie, String receiverName, String streamId,
            String wso2event) {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        try {
            eventReceiverAdminServiceStub = new EventReceiverAdminServiceStub(endPoint);

        this.receiverName = receiverName;
        this.streamId = streamId;

        // Authenticate stub from sessionCooke
        ServiceClient serviceClient;
        Options option;

        serviceClient = eventReceiverAdminServiceStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);

        } catch (Exception e) {
            String errMsg = "Error in connecting to DAS EventReceiverAdmin Service";
            log.error(errMsg, e);
        }

    }

    public boolean deployEventReceiverConfiguration() {
        BasicInputAdapterPropertyDto props[] = new BasicInputAdapterPropertyDto[1];
        props[0] = new BasicInputAdapterPropertyDto();
        props[0].setKey("events.duplicated.in.cluster");
        props[0].setValue("false");
        try {
            eventReceiverAdminServiceStub
                    .deployWso2EventReceiverConfiguration(receiverName, streamId, eventAdapterType, null, null, null,
                            props, false, "");
        } catch (RemoteException e) {
            String errMsg = "Error in deploying event receiver";
            log.error(errMsg, e);
            return false;
        }
        return true;
    }
}
