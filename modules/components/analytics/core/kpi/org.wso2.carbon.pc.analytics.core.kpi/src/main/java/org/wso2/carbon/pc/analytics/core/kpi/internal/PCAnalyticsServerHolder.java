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
package org.wso2.carbon.pc.analytics.core.kpi.internal;

import org.wso2.carbon.pc.analytics.core.kpi.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.core.kpi.clients.ReceiverAdminServiceClient;
import org.wso2.carbon.pc.analytics.core.kpi.clients.StreamAdminServiceClient;
import org.wso2.carbon.pc.core.ProcessCenter;
import org.wso2.carbon.registry.core.service.RegistryService;

public class PCAnalyticsServerHolder {
    private static PCAnalyticsServerHolder instance = new PCAnalyticsServerHolder();
    private LoginAdminServiceClient loginAdminServiceClient;
    private StreamAdminServiceClient streamAdminServiceClient;
    private ReceiverAdminServiceClient receiverAdminServiceClient;
    private RegistryService registryService;
    private ProcessCenter processCenter;

    public static PCAnalyticsServerHolder getInstance() {
        return instance;
    }

    public void setLoginAdminServiceClient(LoginAdminServiceClient loginAdminServiceClient) {
        this.loginAdminServiceClient = loginAdminServiceClient;
    }

    public void setStreamAdminServiceClient(StreamAdminServiceClient streamAdminServiceClient) {
        this.streamAdminServiceClient = streamAdminServiceClient;
    }

    public void setReceiverAdminServiceClient(ReceiverAdminServiceClient receiverAdminServiceClient) {
        this.receiverAdminServiceClient = receiverAdminServiceClient;
    }

    public LoginAdminServiceClient getLoginAdminServiceClient() {
        return loginAdminServiceClient;
    }

    public StreamAdminServiceClient getStreamAdminServiceClient() {
        return streamAdminServiceClient;
    }

    public ReceiverAdminServiceClient getReceiverAdminServiceClient() {
        return receiverAdminServiceClient;
    }

    public ProcessCenter getProcessCenter() {
        return processCenter;
    }

    public void setProcessCenter(ProcessCenter processCenter) {
        this.processCenter = processCenter;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public void unsetRegistryService(RegistryService registryService) {
        this.registryService = null;
    }
}
