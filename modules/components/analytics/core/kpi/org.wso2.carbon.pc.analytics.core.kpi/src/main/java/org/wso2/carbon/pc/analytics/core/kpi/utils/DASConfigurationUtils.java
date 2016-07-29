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

package org.wso2.carbon.pc.analytics.core.kpi.utils;

import org.wso2.carbon.pc.analytics.core.kpi.AnalyticsConfigConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

import static org.wso2.carbon.pc.analytics.core.kpi.internal.PCAnalyticsServerHolder.getInstance;

/**
 * Utils file for Analytics configuration related properties
 */
public class DASConfigurationUtils {

    // Make the constructor private, since it is a utility class
    private DASConfigurationUtils() {
    }

    /**
     * Check whether analytics configurations (with DAS) is made for the respective process
     *
     * @return isDASAnalyticsConfigured
     */
    public static boolean isDASAnalyticsConfigured()
            throws ProcessCenterException {
        return getInstance().getProcessCenter().getProcessCenterConfiguration().isAnalyticsEnabled();
    }

    /**
     * Get Analytics Server URL
     *
     * @return DASUrl
     */
    public static String getDASURL() {
        return getInstance().getProcessCenter().getProcessCenterConfiguration().getAnalyticsServerURL();
    }

    /**
     * Get Runtime Server URL
     *
     * @return BPS runtime url
     */
    public static String getBPSURL() {
        return getInstance().getProcessCenter().getProcessCenterConfiguration().getRuntimeEnvironmentURL();
    }

    /**
     * Get authorization header for access bps admin service
     *
     * @return AuthorizationHeader
     */
    public static String getAuthorizationHeader() {
        String userName = getInstance().getProcessCenter().getProcessCenterConfiguration()
                .getRuntimeEnvironmentUsername();
        String password = getInstance().getProcessCenter().getProcessCenterConfiguration()
                .getRuntimeEnvironmentPassword();
        if (userName != null && password != null) {
            return AnalyticsConfigConstants.REQUEST_HEADER_BASIC + " " + DatatypeConverter.printBase64Binary(
                    (userName + ":" + password).getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    /**
     * Get DAS user name
     *
     * @return dasUserName
     */
    public static String getDASUserName() {
        return getInstance().getProcessCenter().getProcessCenterConfiguration().getAnalyticsServerUsername();
    }

    /**
     * Get DAS Password
     *
     * @return dasPassword
     */
    public static String getDASPassword() {
        return getInstance().getProcessCenter().getProcessCenterConfiguration().getAnalyticsServerPassword();
    }
}