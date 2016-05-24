/*
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
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
package org.wso2.carbon.pc.core.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.indexing.service.ContentSearchService;

/**
 * @scr.component name="org.wso2.carbon.pc.core.internal.ProcessCenterServiceComponent" immediate="true"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="registry.indexing.service" interface="org.wso2.carbon.registry.indexing.service.ContentSearchService"
 * cardinality="1..1" policy="dynamic"  bind="setContentSearchService" unbind="unsetContentSearchService"
 * @scr.reference name="registry.common" interface="org.wso2.carbon.registry.common.AttributeSearchService"
 * cardinality="1..1" policy="dynamic"  bind="setAttributeSearchService" unbind="unsetAttributeSearchService"
 */
public class ProcessCenterServiceComponent {

    private static Log log = LogFactory.getLog(ProcessCenterServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        log.info("Initializing the PC core component...");
        try {
            ProcessCenterServerHolder holder = ProcessCenterServerHolder.getInstance();
        }catch (Throwable e) {
            log.error("Failed to initialize the PC core component.", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        log.info("Stopping the PC core component...");
    }

    protected void setRegistryService(RegistryService registrySvc) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService bound to the PC component");
        }
        ProcessCenterServerHolder.getInstance().setRegistryService(registrySvc);
    }

    public void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService unbound from the PC component");
        }
        ProcessCenterServerHolder.getInstance().unsetRegistryService(registryService);
    }
    protected void setContentSearchService(ContentSearchService contentSearchService) {
        if (log.isDebugEnabled()) {
            log.debug("ContentSearchService bound to the PC component");
        }
        ProcessCenterServerHolder.getInstance().setContentSearchService(contentSearchService);
    }

    public void unsetContentSearchService(ContentSearchService contentSearchService) {
        if (log.isDebugEnabled()) {
            log.debug("ContentSearchService unbound from the PC component");
        }
        ProcessCenterServerHolder.getInstance().unsetContentSearchService(contentSearchService);
    }
    protected void setAttributeSearchService(AttributeSearchService attributeSearchService) {
        if (log.isDebugEnabled()) {
            log.debug("AttributeSearchService bound to the PC component");
        }
        ProcessCenterServerHolder.getInstance().setAttributeSearchService(attributeSearchService);
    }

    public void unsetAttributeSearchService(AttributeSearchService attributeSearchService) {
        if (log.isDebugEnabled()) {
            log.debug("AttributeSearchService unbound from the PC component");
        }
        ProcessCenterServerHolder.getInstance().unsetAttributeSearchService(attributeSearchService);
    }

}

