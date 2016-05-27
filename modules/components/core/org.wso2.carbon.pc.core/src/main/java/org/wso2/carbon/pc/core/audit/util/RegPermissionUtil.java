/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.pc.core.audit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.registry.core.ActionConstants;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.api.AuthorizationManager;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *  class for check the permissions of a user to access registry paths
 */

public class RegPermissionUtil {

    private static final Log log = LogFactory.getLog(RegPermissionUtil.class);

    /**
     *
     * @param registryService registry service to access registry functions
     * @param user user performs tasks at publisher
     * @param path the path to process asset
     */
    public static void setPutPermission(RegistryService registryService, String user, String path) throws RegistryException, UserStoreException {

        try {
            UserRegistry sysReg = registryService.getGovernanceSystemRegistry();
            AuthorizationManager ac = sysReg.getUserRealm().getAuthorizationManager();

            UserStoreManager um = sysReg.getUserRealm().getUserStoreManager();
            List<String> roleList = new LinkedList<>(Arrays.asList(um.getRoleListOfUser(user)));

            String privateUserRole = ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user.toLowerCase();

            if(ac.isUserAuthorized(user, ProcessCenterConstants.PROCESS_UPDATE_PERMISSION, ProcessCenterConstants.UI_PERMISSION_ACTION)) {
                if (!roleList.contains(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE) && roleList.contains(privateUserRole)) {
                    ac.authorizeRole(privateUserRole, path, ActionConstants.PUT);
                    ac.authorizeRole(privateUserRole, ProcessCenterConstants.AUDIT.AC_PROCESS_PATH, ActionConstants.PUT);
                }
            }
        } catch (RegistryException e) {
            String msg = "Error retrieving system registry";
            log.error("Error retrieving system registry", e);
            throw new RegistryException(msg);
        } catch (UserStoreException e) {
            String msg = "Error authorizing role for "+path;
            log.error(msg, e);
            throw new UserStoreException(msg);
        }

    }

}

