package org.wso2.carbon.pc.analytics.config.utils;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;

/**
 * Created by samithac on 8/3/16.
 */
public class DASConfigurationUtils {
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
           e.printStackTrace();
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
            e.printStackTrace();
        }
        return  true;
    }
}

/*
 private void writePublishTimeToRegistry(
            List<HistoricProcessInstance> historicProcessInstanceList) {
        if (log.isDebugEnabled()) {
            log.debug("Start writing last completed process instance publish time...");
        }
        Date lastProcessInstancePublishTime =
                historicProcessInstanceList.get(historicProcessInstanceList.size() - 1)
                        .getStartTime();
        try {
            PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            Registry registry = context.getRegistry(RegistryType.SYSTEM_GOVERNANCE);
            Resource resource;
            if (!registry.resourceExists(AnalyticsPublisherConstants.PROCESS_RESOURCE_PATH)) {
                resource = registry.newResource();
                resource.addProperty(AnalyticsPublisherConstants.LAST_PROCESS_INSTANCE_PUBLISH_TIME,
                        String.valueOf(lastProcessInstancePublishTime));
                registry.put(AnalyticsPublisherConstants.PROCESS_RESOURCE_PATH, resource);
            } else {
                resource = registry.get(AnalyticsPublisherConstants.PROCESS_RESOURCE_PATH);
                resource.setProperty(AnalyticsPublisherConstants.LAST_PROCESS_INSTANCE_PUBLISH_TIME,
                        String.valueOf(lastProcessInstancePublishTime));
                registry.put(AnalyticsPublisherConstants.PROCESS_RESOURCE_PATH, resource);
            }
            if (log.isDebugEnabled()) {
                log.debug("End of writing last completed process instance publish time...");
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while writing the process instance publish time.";
            log.error(errMsg, e);
        }
    }
 */