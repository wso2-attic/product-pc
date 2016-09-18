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

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.event.receiver.stub.EventReceiverAdminServiceStub;
import org.wso2.carbon.event.receiver.stub.types.BasicInputAdapterPropertyDto;
import org.wso2.carbon.pc.analytics.core.kpi.AnalyticsConfigConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;

/**
 * Access DAS EventReceiverAdminService and creates the event receiver.
 */
public class ReceiverAdminServiceClient {
    private static final String SERVICE_NAME = "EventReceiverAdminService";
    private EventReceiverAdminServiceStub eventReceiverAdminServiceStub;
    private String endPoint;
    private static final String EVENT_ADAPTER_TYPE = "wso2event";
    private static final Log log = LogFactory.getLog(ReceiverAdminServiceClient.class);
    private static final String INPUT_PROP_CONFIG_KEY = "events.duplicated.in.cluster";

    /**
     * @param backEndUrl
     * @throws AxisFault
     */
    public ReceiverAdminServiceClient(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + File.separator + AnalyticsConfigConstants.SERVICES + File.separator + SERVICE_NAME;
        eventReceiverAdminServiceStub = new EventReceiverAdminServiceStub(endPoint);
    }

    /**
     * Deploy Event Receiver for the particular process, configuring the previously created Event Stream
     *
     * @param sessionCookie
     * @param receiverName
     * @param streamId
     * @throws ProcessCenterException
     */
    public void deployEventReceiverConfiguration(String sessionCookie, String receiverName, String streamId)
            throws ProcessCenterException {

        // Authenticate stub from sessionCooke
        ServiceClient serviceClient = eventReceiverAdminServiceStub._getServiceClient();
        Options option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);

        BasicInputAdapterPropertyDto props[] = new BasicInputAdapterPropertyDto[1];
        props[0] = new BasicInputAdapterPropertyDto();
        props[0].setKey(INPUT_PROP_CONFIG_KEY);
        props[0].setValue("false");
        try {
            eventReceiverAdminServiceStub
                    .deployWso2EventReceiverConfiguration(receiverName, streamId, EVENT_ADAPTER_TYPE, null, null, null,
                            props, false, "");
        } catch (RemoteException e) {
            String errMsg = "Error in deploying event receiver";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }
}
