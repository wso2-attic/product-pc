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
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.indexing.service.ContentSearchService;
import org.wso2.carbon.registry.resource.beans.PermissionBean;
import org.wso2.carbon.registry.resource.beans.PermissionEntry;
import org.wso2.carbon.registry.resource.services.utils.AddRolePermissionUtil;
import org.wso2.carbon.registry.resource.services.utils.PermissionUtil;
import org.wso2.carbon.user.core.UserStoreException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProcessCenterServerHolder {

    private static final Log log = LogFactory.getLog(ProcessCenterServerHolder.class);

    private static ProcessCenterServerHolder instance = new ProcessCenterServerHolder();

    private RegistryService registryService;

    private ContentSearchService contentSearchService;

    private AttributeSearchService attributeSearchService;

    private ProcessCenterServerHolder() {}

    public static ProcessCenterServerHolder getInstance() {
        return instance;
    }

    public void setRegistryService(RegistryService registrySvc) {
        this.registryService = registrySvc;
        updateRegistryRolesofUser();
    }

    public void unsetRegistryService(RegistryService registryService) {
        this.registryService = null;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void unsetContentSearchService(ContentSearchService contentSearchService) {
        this.contentSearchService = null;
    }

    public ContentSearchService getContentSearchService() {
        return contentSearchService;
    }

    public void setContentSearchService(ContentSearchService contentSearchService) {
        this.contentSearchService = contentSearchService;
    }

    public void unsetAttributeSearchService(AttributeSearchService attributeSearchService){
        this.attributeSearchService = null;
    }

    public AttributeSearchService getAttributeSearchService(){ return attributeSearchService;}

    public void setAttributeSearchService(AttributeSearchService attributeSearchService){
        this.attributeSearchService = attributeSearchService;
    }


    private void updateRegistryRolesofUser() {
        try {
            String[] artifactPaths = {"/_system/governance/flowchart", "/_system/governance/doccontent","/_system/governance/processText"};

            UserRegistry registry = registryService.getGovernanceSystemRegistry();
            initProcessArtifacts(registry);

            PermissionEntry entry = new PermissionEntry();
            entry.setUserName("internal/publisher");
            entry.setWriteAllow(true);

            for (String path: artifactPaths) {
                PermissionBean permissions = PermissionUtil.getPermissions(registry,path);
                List<PermissionEntry> entryRoles = new LinkedList<>(Arrays.asList(permissions.getRolePermissions()));

                if(!entryRoles.contains(entry)) {
                    AddRolePermissionUtil.addRolePermission(registry,path,"Internal/publisher","3","1");
                }
            }

        } catch (RegistryException e) {
            log.error(e);
        } catch (UserStoreException e){
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void initProcessArtifacts(UserRegistry registry) throws RegistryException {

        registry.put(ProcessCenterConstants.AUDIT.PROCESS_TEXT,registry.newCollection());
        registry.put(ProcessCenterConstants.AUDIT.DOC_CONTENT,registry.newCollection());
        registry.put(ProcessCenterConstants.AUDIT.FLOW_CHART,registry.newCollection());
    }

}
