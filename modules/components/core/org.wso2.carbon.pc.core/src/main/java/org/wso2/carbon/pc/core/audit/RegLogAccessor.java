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

package org.wso2.carbon.pc.core.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.audit.bean.LogBean;
import org.wso2.carbon.pc.core.audit.util.LogEntryProcessUtils;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.CollectionImpl;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.util.ArrayList;

/**
 * class to process activity logs and returns to the front end
 */
public class RegLogAccessor {

    private static final Log log = LogFactory.getLog(RegLogAccessor.class);

    private UserRegistry registry;
    private LogBean logBean;
    private LogEntryProcessUtils logEntryProcessUtils;

    public RegLogAccessor() {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            registry = registryService.getGovernanceSystemRegistry();
            logBean = new LogBean();
            logEntryProcessUtils = new LogEntryProcessUtils();
        } catch (RegistryException e) {
            log.error("Could not initialize registry for indexing", e);
        }
    }

    /**
     * Gets activity logs for the all the process assets at process center
     *
     * @return the resultant json of the activity logs
     */
    public String getAllLogEntries() {

        ArrayList<String> assetPaths = new ArrayList<>();
        JSONArray logResult = new JSONArray();
        JSONObject logObject = new JSONObject();

        try {
            Collection resourceTypes = (Collection) registry.get(ProcessCenterConstants.AUDIT.PROCESS_PATH);
            String[] children = resourceTypes.getChildren();

            for (String child : children) {
                Resource resourceWithVersion = registry.get(child);
                String[] versions = ((CollectionImpl) (resourceWithVersion)).getChildren();
                for (String version : versions) {
                    version = version.replace(ProcessCenterConstants.AUDIT.PROCESS_PATH, "");
                    assetPaths.add(version);
                }
            }

            for (String path : assetPaths) {
                logEntryProcessUtils.processLogResult(registry, logBean, logResult, path);
            }
            logObject.put("log", logResult);

        } catch (RegistryException e) {
            String msg = "Could not get resources for the process assets";
            log.error(msg, e);
        } catch (JSONException e) {
            String msg = "Error processing resultant audit log json";
            log.error(msg, e);
        }
        return logObject.toString();
    }

    /**
     * Gets activity logs for a selected process asset
     *
     * @param path the path to process asset
     * @return the resultant json of the activity logs
     */
    public String getProcessLogEntries(String path) {

        JSONArray logResult = new JSONArray();
        JSONObject logObject = new JSONObject();

        try {
            if (registry != null) {
                path = path.substring(ProcessCenterConstants.AUDIT.AC_PROCESS_PATH.length());
                logEntryProcessUtils.processLogResult(registry, logBean, logResult, path);
                logObject.put("log", logResult);
            }
        } catch (RegistryException e) {
            String msg = "Could not get resources from the path" + path;
            log.error(msg, e);
        } catch (JSONException e) {
            String msg = "Error processing log entries resultant json";
            log.error(msg, e);
        }

        return logObject.toString();
    }

}
