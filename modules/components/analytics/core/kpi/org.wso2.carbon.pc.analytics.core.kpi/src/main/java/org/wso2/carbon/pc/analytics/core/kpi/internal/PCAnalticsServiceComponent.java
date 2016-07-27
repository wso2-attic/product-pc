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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.pc.analytics.core.kpi.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.core.kpi.clients.ReceiverAdminServiceClient;
import org.wso2.carbon.pc.analytics.core.kpi.clients.StreamAdminServiceClient;
import org.wso2.carbon.pc.analytics.core.kpi.utils.DASConfigurationUtils;
import org.wso2.carbon.pc.core.ProcessCenterService;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.service.RegistryService;

/**
 * @scr.component name="org.wso2.carbon.pc.analytics.core.kpi.internal.PCAnalticsServiceComponent" immediate="true"
 * @scr.reference name="process.center" interface="org.wso2.carbon.pc.core.ProcessCenterService"
 * cardinality="1..1" policy="dynamic" bind="setProcessCenter" unbind="unsetProcessCenter"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 */
public class PCAnalticsServiceComponent {
    private static Log log = LogFactory.getLog(PCAnalticsServiceComponent.class);
    private String DASUrl;

    protected void activate(ComponentContext ctxt) {
        log.info("Initializing the PC Analytics component...");
        try {
            DASUrl = DASConfigurationUtils.getDASURL();
            PCAnalyticsServerHolder holder = PCAnalyticsServerHolder.getInstance();
            holder.setLoginAdminServiceClient(new LoginAdminServiceClient(DASUrl));
            holder.setStreamAdminServiceClient(new StreamAdminServiceClient(DASUrl));
            holder.setReceiverAdminServiceClient(new ReceiverAdminServiceClient(DASUrl));
        } catch (Throwable e) {
            log.error("Failed to initialize the PC Analytics component..", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        log.info("Stopping the PC core component...");
    }


    protected void setProcessCenter(ProcessCenterService processCenterService) {
        if (log.isDebugEnabled()) {
            log.debug("ProcessCenter bound to PC Analytics Publisher component");
        }
        PCAnalyticsServerHolder.getInstance().setProcessCenter(processCenterService.getProcessCenter());
    }

    protected void unsetProcessCenter(
            ProcessCenterService processCenterService) {
        if (log.isDebugEnabled()) {
            log.debug("ProcessCenter unbound from the PC Analytics Publisher component");
        }
        PCAnalyticsServerHolder.getInstance().setProcessCenter(null);
    }

    protected void setRegistryService(RegistryService registrySvc) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService bound to the PC component");
        }
        PCAnalyticsServerHolder.getInstance().setRegistryService(registrySvc);
    }

    public void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService unbound from the PC component");
        }
        ProcessCenterServerHolder.getInstance().unsetRegistryService(registryService);
    }


}
