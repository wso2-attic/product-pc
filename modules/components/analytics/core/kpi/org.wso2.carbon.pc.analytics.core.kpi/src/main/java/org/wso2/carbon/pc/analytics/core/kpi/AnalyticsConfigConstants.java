/**
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.pc.analytics.core.kpi;

/**
 * Keep all the constants related to analytics configuration
 */
public class AnalyticsConfigConstants {
    public static final String ANALYTICS = "analytics";
    public static final String CONFIG_BASE_URL = "dasBaseUrl";
    public static final String PC_CONFIGURATION_FILE_NAME = "pc.xml";
    public static final String CONFIG_USER_NAME = "bpsUsername";
    public static final String CONFIG_PASSWORD = "bpsPassword";
    public static final String SECRET_ALIAS_BPS_PASSWORD = "PC.Analytics.BPSPassword";
    public static final String SECRET_ALIAS_DAS_PASSWORD = "PC.Analytics.DASPassword";
    public static final String EVENT_STREAM_NAME = "eventStreamName";

    public static final String EVENT_STREAM_VERSION = "eventStreamVersion";
    public static final String EVENT_STREAM_ID = "eventStreamId";
    public static final String EVENT_STREAM_DESCRIPTION = "eventStreamDescription";
    public static final String EVENT_STREAM_NICK_NAME = "eventStreamNickName";
    public static final String EVENT_RECEIVER_NAME = "eventReceiverName";
    public static final String PROCESS_VARIABLES = "processVariables";
    public static final String WSO2_EVENT = "wso2event";
    public static final String SERVICES = "services";
    public static final String AUTHENTICATION_ADMIN="AuthenticationAdmin";
    public static final String REQUEST_HEADER_BASIC= "Basic";
    public static final String IS_DAS_CONFIGED_TAG= "isDasConfigedForAnalytics";
    public static final String DAS_USER_NAME= "dasUsername";
    public static final String DAS_PASSWORD= "dasPassword";
    public static final String EVENT_STREAM_ADMIN_SERVICE_NAME= "EventStreamAdminService";
    public static final String BPS_BASE_URL = "bpsBaseUrl";
    public static final String BPS_PROCESS_VAR_PUBLISH_REST_PATH = "/bpmn/analytics/publish-process-variables/" ;
}
