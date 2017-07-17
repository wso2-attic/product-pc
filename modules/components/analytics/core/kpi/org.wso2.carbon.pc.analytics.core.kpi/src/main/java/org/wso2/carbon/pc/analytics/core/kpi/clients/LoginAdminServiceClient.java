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
package org.wso2.carbon.pc.analytics.core.kpi.clients;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.apache.axis2.context.ServiceContext;
import org.wso2.carbon.pc.analytics.core.kpi.AnalyticsConfigConstants;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Connects with DAS's LoginAdminService
 */
public class LoginAdminServiceClient {
    private AuthenticationAdminStub authenticationAdminStub;
    private String endPoint;
    private static final Log log = LogFactory.getLog(LoginAdminServiceClient.class);
    private String dasURL = null;

    /**
     * @param DASUrl Analytics server url
     * @throws AxisFault Throws AxisFault, if an error occurred in accessing AuthenticationAdminStub
     */
    public LoginAdminServiceClient(String DASUrl) throws AxisFault {
        this.dasURL = DASUrl;
        this.endPoint = DASUrl + File.separator + AnalyticsConfigConstants.SERVICES + File.separator
                + AnalyticsConfigConstants.AUTHENTICATION_ADMIN;
        authenticationAdminStub = new AuthenticationAdminStub(endPoint);
    }

    /**
     * Authenticate DAS login
     *
     * @param userName Analytics server login user name
     * @param password Analytics server login password
     * @return sessionCookie This session related cookie
     * @throws RemoteException                       Throws RemoteException, if an error occurred in login to DAS
     *                                               admin services
     * @throws LoginAuthenticationExceptionException Throws LoginAuthenticationExceptionException, if an error
     *                                               occurred in login to DAS admin services
     */
    public String authenticate(String userName, char[] password)
            throws RemoteException, LoginAuthenticationExceptionException {
        String sessionCookie = null;
        String dasHostName = dasURL.substring(dasURL.indexOf(File.separator) + 2)
                .substring(0, dasURL.substring(dasURL.indexOf(File.separator) + 2).indexOf(":"));
        if (authenticationAdminStub.login(userName, String.valueOf(password), dasHostName)) {
            Arrays.fill(password, ' ');
            password = null;
            if (log.isDebugEnabled()) {
                log.debug("Login successful to DAS Admin Services for username :" + userName + "at " + dasHostName);
            }
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        }
        return sessionCookie;
    }

    /**
     * Logout from DAS Admin Services
     *
     * @throws RemoteException                        Throws if an error occurred in login out from DAS Admin Services
     * @throws LogoutAuthenticationExceptionException Throws if an error occurred in login out from DAS Admin Services
     */
    public void logOut() throws RemoteException, LogoutAuthenticationExceptionException {
        authenticationAdminStub.logout();
        log.debug("Logout from DAS Admin Services");
    }
}