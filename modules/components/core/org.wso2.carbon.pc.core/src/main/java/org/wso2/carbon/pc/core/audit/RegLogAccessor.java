package org.wso2.carbon.pc.core.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.pc.core.audit.bean.LogBean;
import org.wso2.carbon.pc.core.audit.util.Constants;
import org.wso2.carbon.pc.core.audit.util.LogEntryProcessUtils;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.CollectionImpl;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.indexing.Utils;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.ArrayList;

public class RegLogAccessor {

    private static final Log log = LogFactory.getLog(RegLogAccessor.class);

    private UserRegistry registry;
    private LogBean logBean;

    public RegLogAccessor() {
        try {
            registry = Utils.getRegistryService().getRegistry(CarbonConstants.REGISTRY_SYSTEM_USERNAME);
            logBean = new LogBean();
        } catch (RegistryException e) {
            log.error("Could not initialize registry for indexing", e);
        }
    }

    public String getAllLogEntries() {

        Integer tenantId =  CarbonContext.getThreadLocalCarbonContext().getTenantId();
        UserRegistry registry = getRegistry(tenantId.intValue());
        ArrayList<String> assetPaths = new ArrayList<>();
        JSONArray logResult = new JSONArray();
        JSONObject logObject = new JSONObject();

        try {
            Collection resourceTypes = (Collection) registry.get(Constants.REG.PROCESS_PATH);
            String[] children = resourceTypes.getChildren();

            for (String child : children) {
                Resource resourceWithVersion = registry.get(child);
                String[] versions = ((CollectionImpl) (resourceWithVersion)).getChildren();
                for (String version : versions) {
                    version = version.replace(Constants.REG.PROCESS_PATH, "");
                    assetPaths.add(version);
                }
            }

            for (String path : assetPaths) {
                LogEntryProcessUtils.processLogResult(registry, logBean, logResult, path);
            }

            logObject.put("log", logResult);

        } catch (RegistryException e) {
            log.error("Could not initialize registry for indexing", e);
        } catch (JSONException e) {
            log.error(e);
        }

        return logObject.toString();
    }

    public String getProcessLogEntries(String path) {

        Integer tenantId =  CarbonContext.getThreadLocalCarbonContext().getTenantId();
        UserRegistry registry = getRegistry(tenantId.intValue());
        JSONArray logResult = new JSONArray();
        JSONObject logObject = new JSONObject();

        try {
            path = path.substring(Constants.REG.PROCESS_PATH.length());
            LogEntryProcessUtils.processLogResult(registry, logBean, logResult, path);
            logObject.put("log", logResult);

        } catch (RegistryException e) {
            log.error(e);
        } catch (JSONException e) {
            String msg = "Error processing log entries";
            log.error(msg, e);
        }

        return logObject.toString();
    }

    private UserRegistry getRegistry(int tenantId) {
        if (tenantId == MultitenantConstants.SUPER_TENANT_ID) {
            return registry;
        } else {
            try {
                return Utils.getRegistryService().getRegistry(CarbonConstants.REGISTRY_SYSTEM_USERNAME, tenantId);
            } catch (RegistryException ignore) {
                return null;
            }
        }
    }

}
