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
import org.wso2.carbon.pc.core.ProcessCenterService;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.service.RegistryService;

import static org.wso2.carbon.pc.analytics.core.kpi.internal.PCAnalyticsServerHolder.getInstance;

/**
 * @scr.component name="org.wso2.carbon.pc.analytics.core.kpi.internal.PCAnalticsServiceComponent" immediate="true"
 * @scr.reference name="process.center" interface="org.wso2.carbon.pc.core.ProcessCenterService"
 * cardinality="1..1" policy="dynamic" bind="setProcessCenter" unbind="unsetProcessCenter"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 */
public class PCAnalticsServiceComponent {
    private static Log log = LogFactory.getLog(PCAnalticsServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        log.info("Initializing the Process Center KPI Analytics component...");
    }

    protected void deactivate(ComponentContext ctxt) {
        log.info("Stopping the PC core component...");
    }

    protected void setProcessCenter(ProcessCenterService processCenterService) {
        if (log.isDebugEnabled()) {
            log.debug("ProcessCenter bound to Process Center KPI Analytics Publisher component");
        }
        getInstance().setProcessCenter(processCenterService.getProcessCenter());
        String analyticsUrl = getInstance().getProcessCenter().getProcessCenterConfiguration().getAnalyticsServerURL();
        if (analyticsUrl != null) {
            try {
                getInstance().setLoginAdminServiceClient(new LoginAdminServiceClient(analyticsUrl));
                getInstance().setStreamAdminServiceClient(new StreamAdminServiceClient(analyticsUrl));
                getInstance().setReceiverAdminServiceClient(new ReceiverAdminServiceClient(analyticsUrl));
            } catch (Throwable e) {
                log.error("Failed to set Process Center servcie..", e);
            }
        }
    }

    protected void unsetProcessCenter(
            ProcessCenterService processCenterService) {
        if (log.isDebugEnabled()) {
            log.debug("ProcessCenter unbound from the Process Center KPI Analytics Publisher component");
        }
        getInstance().setProcessCenter(null);
    }

    protected void setRegistryService(RegistryService registrySvc) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService bound to the Process Center KPI Analytics component");
        }
        getInstance().setRegistryService(registrySvc);
    }

    public void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService unbound from the Process Center KPI Analytics component");
        }
        ProcessCenterServerHolder.getInstance().unsetRegistryService(registryService);
    }


}
