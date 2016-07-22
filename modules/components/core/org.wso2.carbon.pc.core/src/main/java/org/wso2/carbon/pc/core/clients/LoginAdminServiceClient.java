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
package org.wso2.carbon.pc.core.clients;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.pc.core.ProcessCenterConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
/**
 * Connects with WSO2 Server LoginAdminService
 */
public class LoginAdminServiceClient {
    private static final Log log = LogFactory.getLog(LoginAdminServiceClient.class);
    private AuthenticationAdminStub authenticationAdminStub;
    private String endPoint;
    private String serverUrl = null;

    /**
     * @param serverUrl
     * @throws AxisFault
     */
    public LoginAdminServiceClient(String serverUrl) throws AxisFault {
        this.serverUrl = serverUrl;
        this.endPoint = serverUrl + "/" + ProcessCenterConstants.SERVICES + "/" + ProcessCenterConstants
                .AUTHENTICATION_ADMIN;
        authenticationAdminStub = new AuthenticationAdminStub(endPoint);
    }

    /**
     * Authenticate Server login
     *
     * @param userName
     * @param password
     * @return
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     */
    public String authenticate(String userName, char[] password)
            throws RemoteException, LoginAuthenticationExceptionException, MalformedURLException {

        String sessionCookie = null;
        String serverHostName = new URL(serverUrl).getHost();
        if (authenticationAdminStub.login(userName, String.valueOf(password), serverHostName)) {
            Arrays.fill(password, ' ');
            password = null;
            if (log.isDebugEnabled()) {
                log.debug("Login successful to admin service for server url " + serverUrl);
            }
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        }
        return sessionCookie;
    }

    /**
     * Logout from Server Admin Services
     *
     * @throws RemoteException
     * @throws LogoutAuthenticationExceptionException
     */
    public void logOut() throws RemoteException, LogoutAuthenticationExceptionException {
        authenticationAdminStub.logout();
        if (log.isDebugEnabled()) {
            log.debug("Sucessefully logout from admin service for server url " + serverUrl);
        }
    }
}
