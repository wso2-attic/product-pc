/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for t
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
var gregPermissionUtil = {};
(function (gregPermissionUtil) {

    var REGISTRY_PERMISSION_CHECK = "registry.permissionCheck";
    gregPermissionUtil.permissions = {};

    gregPermissionUtil.permissions.list = function (am, assetId) {
        return listPermissions(am, assetId);
    };

    gregPermissionUtil.permissions.add = function (am, params) {
        var actionToAuthorize = params.actionToAuthorize;
        var roleToAuthorize = params.roleToAuthorize;
        var pathWithVersion = params.pathWithVersion;
        var permissionType = params.permissionType;
        var permissionCheck = params.permissionCheck;

        var results = addPermissions(am, pathWithVersion,
                                     roleToAuthorize, actionToAuthorize, permissionType, permissionCheck);

        if (params.roleToDeny) {
            results = addPermissions(am, pathWithVersion,
                                     params.roleToDeny, actionToAuthorize, params.permissionTypeDeny, permissionCheck);
        }

        return results;
    };

    gregPermissionUtil.permissions.modify = function (am, params) {
        var permissionObject = params.permissionObject;
        var pathWithVersion = params.pathWithVersion;
        var permissionCheck = params.permissionCheck;
        var newPermissionsString = "";
        var results;
        var readAllow = "ra";
        var writeAllow = "wa";
        var deleteAllow = "da";
        var writeDeny = "wd";
        var deleteDeny = "dd";

        for (var role in permissionObject) {
            if (permissionObject.hasOwnProperty(role)) {
                newPermissionsString = newPermissionsString + "|";
                var roleId = role;
                var rolePermissionType = permissionObject[role];
                var actions = ":";
                if (rolePermissionType == readAllow) {
                    actions = actions + readAllow + "^true:" + writeDeny + "^true:" + deleteDeny + "^true";
                } else if (rolePermissionType == writeAllow) {
                    actions = actions + readAllow + "^true:" + writeAllow + "^true:" + deleteDeny + "^true";
                } else if (rolePermissionType == deleteAllow) {
                    actions = actions + readAllow + "^true:" + writeAllow + "^true:" + deleteAllow + "^true";
                }
                newPermissionsString = newPermissionsString + roleId + actions;
            }
        }
        results = modifyPermissions(am, pathWithVersion, newPermissionsString, permissionCheck);

        return results;
    };

    gregPermissionUtil.permissions.remove = function (am, params) {
        var permissionRemoveString = ":rd^true:wd^true:dd^true";
        var pathWithVersion = params.pathWithVersion;
        var roleToRemove = params.roleToRemove;
        var permissionsString = "|" + roleToRemove + permissionRemoveString;
        return modifyPermissions(am, pathWithVersion, permissionsString);
    };
    /***
     * Calls the registry util class to get the role permissions for a particular resource
     * @param am
     * @param assetId
     * @returns {{}}
     */
    var listPermissions = function (am, assetId) {
        var registryPath = am.get(assetId).path;
        var userRegistry = am.registry;
        var registry = userRegistry.registry;
        var PermissionUtil = Packages.org.wso2.carbon.registry.resource.services.utils.PermissionUtil;
        var results = {};
        var result = [];
        var permissionCheck;
        var resource = registry.get(registryPath);
        try {
            if(resource){
                permissionCheck = resource.getProperty(REGISTRY_PERMISSION_CHECK);
            }

            var permissionsBean = PermissionUtil.getPermissions(userRegistry.registry, registryPath);
            if (permissionsBean) {
                var permissions = permissionsBean.getRolePermissions();
                var authorizedRoles = [];

                for (var i = 0; i < permissions.length; i++) {
                    var permissionOptions = {};
                    var permission = permissions[i];

                    if (!permission.isAuthorizeAllow()) {
                        permissionOptions.userName = permission.getUserName();
                        permissionOptions.formattedUserName = renderRoles(permission.getUserName());
                        permissionOptions.readAllow = permission.isReadAllow();
                        permissionOptions.readDeny = permission.isReadDeny();
                        permissionOptions.writeAllow = permission.isWriteAllow();
                        permissionOptions.writeDeny = permission.isWriteDeny();
                        permissionOptions.deleteAllow = permission.isDeleteAllow();
                        permissionOptions.deleteDeny = permission.isDeleteDeny();
                        permissionOptions.notReadOnly = !(permission.getUserName() == "system/wso2.anonymous.role");

                        result.push(permissionOptions);
                    } else {
                        authorizedRoles.push(permission.getUserName());
                    }
                }

                results.list = result;
                results.roleNames = permissionsBean.getRoleNames();
                results.authorizedRoles = authorizedRoles;
                results.pathWithVersion = permissionsBean.getPathWithVersion();
                results.isAuthorizeAllowed = permissionsBean.isAuthorizeAllowed();
                results.isVersionView = permissionsBean.isVersionView();
                results.permissionCheck = permissionCheck;
                return results;
            } else {
                return null;
            }
        } catch (e) {
            log.error(e);
            throw "Unable to retrieve permissions";
        }
    };

    /***
     *
     * Calls the registry util class to add the role permissions for a particular resource
     * @param am
     * @param pathToAuthorize
     * @param roleToAuthorize
     * @param actionToAuthorize
     * @param permissionType
     * @param permissionCheck
     * @returns {boolean}
     */
    var addPermissions = function (am, pathToAuthorize,
                               roleToAuthorize, actionToAuthorize, permissionType, permissionCheck) {
        var userRegistry = am.registry;
        var AddRolePermissionUtil = Packages.org.wso2.carbon.registry.resource.services.utils.AddRolePermissionUtil;
        var registry = userRegistry.registry;
        var resource = registry.get(pathToAuthorize);
        try {
            if (resource) {
                resource.setProperty(REGISTRY_PERMISSION_CHECK, permissionCheck);
                registry.put(pathToAuthorize, resource);
            }

            AddRolePermissionUtil.addRolePermission(userRegistry.registry,
                                                    pathToAuthorize, roleToAuthorize, actionToAuthorize, permissionType);
            return true;
        } catch (e) {
            log.error(e);
            throw "Unable to add role permissions";
        }
    };

    /***
     * Calls the registry util class to modify the role permissions for a particular resource
     * @param am
     * @param resourcePath
     * @param permissionString
     * @param permissionCheck
     * @returns {boolean}
     */
    var modifyPermissions = function (am, resourcePath, permissionString, permissionCheck) {
        var userRegistry = am.registry;
        var registry = userRegistry.registry;
        var ChangeRolePermissionsUtil =
                Packages.org.wso2.carbon.registry.resource.services.utils.ChangeRolePermissionsUtil;
        var resource = registry.get(resourcePath);
        try {
            if (resource) {
                resource.setProperty(REGISTRY_PERMISSION_CHECK, permissionCheck);
                registry.put(resourcePath, resource);
            }
            ChangeRolePermissionsUtil.changeRolePermissions(registry, resourcePath, permissionString);
            return true;
        } catch (e) {
            log.error(e);
            throw "Unable to change role permissions";
        }
    };

    var renderRoles = function (role) {
        var modifiedRole = "";
        //UM API sometimes returns uppercase letters
        var roleLower = role.toLowerCase();
        if (roleLower == "internal/everyone") {
            modifiedRole = "All tenant users";
        } else if (roleLower == "system/wso2.anonymous.role") {
            modifiedRole = "Public";
        } else if (roleLower.startsWith("internal/")) {
            modifiedRole = capitalize(roleLower.substr(roleLower.indexOf("/")+1));
        } else {
            modifiedRole = capitalize(roleLower);
        }
        return modifiedRole;
    };

    var capitalize = function(role) {
        return role.substr(0, 1).toUpperCase() + role.substr(1);
    };

}(gregPermissionUtil));
