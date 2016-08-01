/**
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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

    public static final String SERVICES = "services";
    public static final String AUTHENTICATION_ADMIN = "AuthenticationAdmin";
    public static final String REQUEST_HEADER_BASIC = "Basic";
    public static final String EVENT_STREAM_ADMIN_SERVICE_NAME = "EventStreamAdminService";
    public static final String BPS_PROCESS_VAR_PUBLISH_REST_PATH = "/bpmn/analytics/publish-process-variables/";
    public static final String IS_DAS_CONFIGED_TAG= "isDasConfigedForAnalytics";

    // Make the constructor private, since it is a utility class
    private AnalyticsConfigConstants() {
    }
}
