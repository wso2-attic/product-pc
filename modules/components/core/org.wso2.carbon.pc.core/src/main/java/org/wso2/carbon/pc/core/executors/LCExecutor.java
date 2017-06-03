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
package org.wso2.carbon.pc.core.executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.registry.extensions.interfaces.Execution;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;
import org.wso2.carbon.registry.core.session.CurrentSession;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.resource.services.utils.AddRolePermissionUtil;

import java.util.Map;

/**
 * This executor can be used to change role permissions of a process when changing the states of the lifecycle.
 */
public class LCExecutor implements Execution {

    private static final Log log = LogFactory.getLog(LCExecutor.class);
    private String user = null;
    private String path = null;
    private UserRegistry userRegistry = null;

    public void init(Map map) {
    }

    /**
     * Implementing the execute method
     *
     * @param currentState Current lifecycle status
     * @param targetState Target lifecycle status
     * @return Whether permission updated
     */
    public boolean execute(RequestContext context, String currentState, String targetState) {
        boolean updated;
        try {
            user = context.getResource().getAuthorUserName();
            String[] roles = CurrentSession.getUserRealm().getUserStoreManager().getRoleNames();
            path = context.getResourcePath().toString();
            userRegistry = (UserRegistry) context.getSystemRegistry();

            setPublisherPermission(currentState, targetState);
            for (String role : roles) {
                if (role.equals(ProcessCenterConstants.AUDIT.ADMIN_ROLE) || role
                        .equals(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE) || role
                        .equals(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user)) {
                    continue;
                } else {
                    setOtherRolePermission(role, currentState, targetState);
                }
            }
            if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                    .equals(ProcessCenterConstants.PUBLISHED)) {
                setAnonymousRolePermission();
            }
            setPrivateRolePermission(user, currentState, targetState);
            updated = true;
        } catch (Exception e) {
            String errMsg = "Failed to update Permission";
            log.error(errMsg, e);
            throw new Error(errMsg, e);
        }
        return updated;
    }

    /**
     * This method is used to identify the permissions to be assigned and denied from the permission string and add or
     * remove the permission.
     *
     * @param role User role
     * @param permissionString Permission list
     * @throws Exception If failed to create permission list of the user role
     */
    private void assignPermission(String role, String permissionString) throws Exception {
        String[] permissionList = permissionString.split(",");
        String permissionType;
        String actionToAuthorize = "";
        String action;
        for (String permission : permissionList) {
            if (permission.charAt(0) == '+') {
                permissionType = ProcessCenterConstants.ALLOW;
            } else {
                permissionType = ProcessCenterConstants.DENY;
            }
            action = permission.substring(1, permission.length());
            switch (action) {
            case "get":
                actionToAuthorize = ProcessCenterConstants.READ;
                break;
            case "add":
                actionToAuthorize = ProcessCenterConstants.WRITE;
                break;
            case "delete":
                actionToAuthorize = ProcessCenterConstants.DELETE;
                break;
            case "authorize":
                actionToAuthorize = ProcessCenterConstants.AUTHORIZE;
                break;
            }
            AddRolePermissionUtil.addRolePermission(userRegistry, path, role, actionToAuthorize, permissionType);
        }
    }

    /**
     * Assign permission for the publisher role according to the lifecycle state.
     *
     * @param currentState Current lifecycle status
     * @param targetState Target lifecycle status
     * @throws Exception If failed to assign permission to publisher role
     */
    private void setPublisherPermission(String currentState, String targetState) throws Exception {
        if (currentState.equals(ProcessCenterConstants.DEVELOPMENT)) {
            assignPermission(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE, "+get,+add,+delete,+authorize");
        } else if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                .equals(ProcessCenterConstants.DEVELOPMENT)) {
            assignPermission(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE, "+get,+add,+delete,+authorize");
        } else if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                .equals(ProcessCenterConstants.PUBLISHED)) {
            assignPermission(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE, "+get,+add,-delete,+authorize");
        } else if (currentState.equals(ProcessCenterConstants.PUBLISHED) && targetState
                .equals(ProcessCenterConstants.DEVELOPMENT)) {
            assignPermission(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE, "+get,+add,+delete,+authorize");
        } else if (currentState.equals(ProcessCenterConstants.PUBLISHED) && targetState
                .equals(ProcessCenterConstants.RETIRED)) {
            assignPermission(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE, "+get,+add,+delete,-authorize");
        } else if (currentState.equals(ProcessCenterConstants.RETIRED)) {
            assignPermission(ProcessCenterConstants.AUDIT.PUBLISHER_ROLE, "+get,+add,+delete,+authorize");
        }
    }

    /**
     * Assign permission for the process owner.
     *
     * @param user User name
     * @param currentState Current lifecycle status
     * @param targetState Target lifecycle status
     * @throws Exception If failed to assign permission to user
     */
    private void setPrivateRolePermission(String user, String currentState, String targetState) throws Exception {
        if (!user.equals(ProcessCenterConstants.AUDIT.ADMIN_ROLE)) {
            if (currentState.equals(ProcessCenterConstants.DEVELOPMENT)) {
                assignPermission(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user, "+get,-add,-delete,-authorize");
            } else if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                    .equals(ProcessCenterConstants.DEVELOPMENT)) {
                assignPermission(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user, "+get,+add,+delete,+authorize");
            } else if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                    .equals(ProcessCenterConstants.PUBLISHED)) {
                assignPermission(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user, "+get,-add,-delete,-authorize");
            } else if (currentState.equals(ProcessCenterConstants.PUBLISHED) && targetState
                    .equals(ProcessCenterConstants.DEVELOPMENT)) {
                assignPermission(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user, "+get,+add,+delete,+authorize");
            } else if (currentState.equals(ProcessCenterConstants.PUBLISHED) && targetState
                    .equals(ProcessCenterConstants.RETIRED)) {
                assignPermission(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user, "+get,-add,-delete,-authorize");
            } else if (currentState.equals(ProcessCenterConstants.RETIRED)) {
                assignPermission(ProcessCenterConstants.AUDIT.PRIVATE_USER_ROLE + user, "+get,+add,+delete,+authorize");
            }
        }
    }

    /**
     * Assign permission for the other roles.
     *
     * @param role User role
     * @param currentState Current lifecycle status
     * @param targetState Target lifecycle status
     * @throws Exception If failed to assign permission to role
     */
    private void setOtherRolePermission(String role, String currentState, String targetState) throws Exception {
        if (currentState.equals(ProcessCenterConstants.DEVELOPMENT)) {
            assignPermission(role, "+get,-add,-delete,-authorize");
        } else if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                .equals(ProcessCenterConstants.DEVELOPMENT)) {
            assignPermission(role, "+get,+add,-delete,-authorize");
        } else if (currentState.equals(ProcessCenterConstants.IN_REVIEW) && targetState
                .equals(ProcessCenterConstants.PUBLISHED)) {
            assignPermission(role, "+get,-add,-delete,-authorize");
        } else if (currentState.equals(ProcessCenterConstants.PUBLISHED) && targetState
                .equals(ProcessCenterConstants.DEVELOPMENT)) {
            assignPermission(role, "+get,+add,-delete,-authorize");
        } else if (currentState.equals(ProcessCenterConstants.PUBLISHED) && targetState
                .equals(ProcessCenterConstants.RETIRED)) {
            assignPermission(role, "-get,-add,-delete,-authorize");
        } else if (currentState.equals(ProcessCenterConstants.RETIRED)) {
            assignPermission(role, "+get,+add,-delete,-authorize");
        }
    }

    /**
     * Assign permission for anonymous role
     *
     * @throws Exception If failed to assign permission to anonymous role
     */
    private void setAnonymousRolePermission() throws Exception {
        assignPermission(ProcessCenterConstants.AUDIT.SYSTEM_ANONYMOUS_ROLE, "+get,-add,-delete,-authorize");
    }
}
