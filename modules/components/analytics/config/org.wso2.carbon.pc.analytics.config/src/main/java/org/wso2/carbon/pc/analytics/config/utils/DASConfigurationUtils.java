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

package org.wso2.carbon.pc.analytics.config.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;

/**
 * Utils file for DAS configuration related properties
 */
public class DASConfigurationUtils {
    private static final Log log = LogFactory.getLog(DASConfigurationUtils.class);

    public static void setPropertyDASAnalyticsConfigured(String processName,String processVersion){
       PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
       Registry registry = context.getRegistry(RegistryType.SYSTEM_GOVERNANCE);
       Resource resource;
       String processAssetPath = "processes/" + processName + "/" + processVersion;

       try {
           if(registry.resourceExists(processAssetPath)){
               resource=registry.get(processAssetPath);
               if(resource.getProperty("isDASConfiguredForAnalytics")==null) {
                   resource.addProperty("isDASConfiguredForAnalytics", "true");
                   registry.put(processAssetPath, resource);
               }
           }
       } catch (RegistryException e) {
           log.error("Error working with SYSTEM_GOVERNANCE registry property -isDASConfiguredForAnalytics");
       }
   }

    public static boolean isDASAnalyticsConfigured(String processName,String processVersion){
        PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        Registry registry = context.getRegistry(RegistryType.SYSTEM_GOVERNANCE);
        Resource resource;
        String processAssetPath = "processes/" + processName + "/" + processVersion;

        try {
            if(registry.resourceExists(processAssetPath)){
                resource=registry.get(processAssetPath);
                System.out.println(Boolean.parseBoolean(resource.getProperty("sss")));
                return Boolean.parseBoolean(resource.getProperty("isDASConfiguredForAnalytics"));

            }else{
                return false;
            }
        } catch (RegistryException e) {
            log.error("Error in getting SYSTEM_GOVERNANCE registry property- isDASConfiguredForAnalytics ");
        }
        return  true;
    }
}