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

import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.indexing.service.ContentSearchService;

public class ProcessCenterServerHolder {

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

}
