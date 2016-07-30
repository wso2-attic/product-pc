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
package org.wso2.carbon.pc.analytics.core.generic.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.pc.core.ProcessCenterService;

import static org.wso2.carbon.pc.analytics.core.generic.internal.AnalyticsServerHolder.getInstance;

/**
 * @scr.component name="org.wso2.carbon.pc.analytics.core.kpi.internal.PCAnalticsServiceComponent" immediate="true"
 * @scr.reference name="process.center" interface="org.wso2.carbon.pc.core.ProcessCenterService"
 * cardinality="1..1" policy="dynamic" bind="setProcessCenter" unbind="unsetProcessCenter"
 */
public class AnalyticsServiceComponent {
    private static Log log = LogFactory.getLog(AnalyticsServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        log.info("Initializing the Process Center Analytics component");
    }

    protected void deactivate(ComponentContext ctxt) {
        log.info("Stopping the Process Center Analytics core component");
    }

    protected void setProcessCenter(ProcessCenterService processCenterService) {
        if (log.isDebugEnabled()) {
            log.debug("ProcessCenter bound to Process Center Analytics Publisher component");
        }
        getInstance().setProcessCenter(processCenterService.getProcessCenter());
    }

    protected void unsetProcessCenter(
            ProcessCenterService processCenterService) {
        if (log.isDebugEnabled()) {
            log.debug("ProcessCenter unbound from the Process Center Analytics Publisher component");
        }
        getInstance().setProcessCenter(null);
    }

}
