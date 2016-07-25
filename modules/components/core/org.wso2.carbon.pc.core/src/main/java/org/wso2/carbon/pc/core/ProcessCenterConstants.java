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
package org.wso2.carbon.pc.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Holds the constants of ProcessStore
 */
public class ProcessCenterConstants {
    /**
     * Asset Registry Properties
     **/
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String PROCESS = "process";
    public static final String VERSION = "version";
    public static final String PROVIDER = "provider";
    public static final String OVERVIEW = "overview";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_TIME = "createdtime";
    public static final String IMAGES = "images";
    public static final String THUMBNAIL = "thumbnail";
    public static final String PROCESS_ID = "processID";
    public static final String DEPLOYMENT_ID = "deploymentId";
    public static final String METADATA = "metadata";
    public static final String METADATA_NAMESPACE = "http://www.wso2.org/governance/metadata";
    public static final String NOT_APPLICABLE = "NA";
    public static final String IMAGE_THUMBNAIL_VALUE = "images_thumbnail";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    /**
     * Package asset
     */
    public static final String PACKAGE_BPMN_ARCHIVE_FILE_NAME = "packageBpmnFileName";
    public static final String BPMN_RESOURCES_COUNT = "bpmn_resources_count";
    public static final String PACKAGE_PROCESS_ASSOCIATION = "package_process";
    /**
     * Service Clients
     */
    public static final String SERVICES = "services";
    public static final String AUTHENTICATION_ADMIN = "AuthenticationAdmin";
    public static final String BPMN_UPLOADER_SERVICE = "BPMNUploaderService";
    public static final String BPMN_DEPLOYMENT_SERVICE = "BPMNDeploymentService";
    /**
     * Assets Storage
     **/
    public static final String GREG_PATH = "/_system/governance/";
    public static final String PROCESS_CENTER_RESOURCES_PATH = "process_center/asset_resources/";
    public static final String PROCESS_ASSET_ROOT = "processes/";
    public static final String PACKAGE_ASSET_ROOT = "package/";
    public static final String PACKAGES_ASSET_ROOT = "packages/";
    public static final String BPMN_CONTENT_PATH = "bpmncontent/";
    public static final String BPMN_PATH = "bpmn/";
    public static final String IMAGE_PATH = "store/asset_resources/process/";
    public static final String IMAGE_PATH_PACKAGE = "store/asset_resources/package/";
    public static final String GREG_PATH_PROCESS = "/_system/governance/processes/";
    public static final String SAMPLE_LIFECYCLE2_NAME = "SampleLifeCycle2";
    //association type for processes in pdf, documents and process text assets
    public static final String ASSOCIATION_TYPE = "process_association";
    public static final String SUBPROCESS_ASSOCIATION = "subprocess";
    public static final String PARENTPROCESS_ASSOCIATION = "parent";
    public static final String SUCCESSOR_ASSOCIATION = "successor";
    public static final String PREDECESSOR_ASSOCIATION = "predecessor";
    public static final String PROCESS_TEXT_PATH = "processText/";
    public static final String DOC_CONTENT_PATH = "doccontent/";
    public static final String UI_PERMISSION_ACTION = "ui.execute";
    public static final String PROCESS_UPDATE_PERMISSION =
            "/permission/admin/enterprisestore/apps/publisher/features/assets/process/update";
    public static final String PROCESS_NAME = "processName";
    public static final String PROCESS_ASSET_NAME = "processAssetName";
    public static final String PROCESS_ASSET_VERSION = "processAssetVersion";
    /**
     * Process Center Configurations
     **/
    public static final String PROCESS_CENTER_CONFIGURATION_FILE_NAME = "process-center.xml";
    public static final String ANALYTICS_SERVER_PASSWORD_SECRET_ALIAS = "PC.Analytics.Password";
    public static final String RUNTIME_ENVIRONMENT_PASSWORD_SECRET_ALIAS = "PC.Runtime.Environment.Password";
    /**
     * BPMN Deployment
     **/
    public static final List<String> BPMN_RESOURCE_SUFFIXES = Collections.unmodifiableList(Arrays.asList("bpmn20.xml",
            "bpmn"));
    /**
     * Process Center Asset Permission
     **/
    public static final String READ = "2";
    public static final String WRITE = "3";
    public static final String AUTHORIZE = "5";
    public static final String ALLOW = "1";

    public static final String PROCESS_EXPORT_DIR = "Exports/";
    public static final String EXPORTED_PROCESS_RXT_FILE = "process_rxt.xml";
    public static final String EXPORTED_BPMN_FILE = "bpmn.xml";
    public static final String EXPORTED_BPMN_CONTENT_FILE = "bpmn_content.xml";
    public static final String EXPORTED_FLOW_CHART_FILE = "flowChart.json";
    public static final String EXPORTED_PROCESS_TEXT_FILE = "process_text.xml";
    public static final int JSON_FILE_INDENT_FACTOR = 5;
    public static final String PROCESS_ASSET_RESOURCE_REG_PATH = "store/asset_resources/process/";
    public static final String PROCESS_TAGS_FILE = "process_tags.txt";
    public static final String PROCESS_ASSOCIATIONS_FILE = "process_associations.json";
    // Make the constructor private, since it is a utility class
    private ProcessCenterConstants() {
    }

    public static class AUDIT {

        public static final String PROCESS = "process";
        public static final String PROCESS_TEXT = "processText";
        public static final String PDF = "pdf";
        public static final String FLOW_CHART = "flowchart";
        public static final String DOCUMENT = "document";
        public static final String BPMN = "bpmn";
        public static final String LIFE_CYCLE = "lifecycle";
        public static final String DOC_CONTENT = "doccontent";

        public static final String PROCESS_PATH = "/processes/";
        public static final String PROCESS_TEXT_PATH = "/processText/";
        public static final String PROCESS_PDF_PATH = "/pdf/";
        public static final String PROCESS_FLOW_CHART_PATH = "/flowchart/";
        public static final String PROCESS_DOC_PATH = "/doccontent/";
        public static final String PROCESS_BPMN = "/bpmn/";
        public static final String PROCESS_LIFECYCLE_HISTORY = "/_system/governance/repository/components/org.wso2" +
                ".carbon.governance/lifecycles/history/__system_governance_processes_";
        public static final String AC_PROCESS_PATH = "/_system/governance/processes/";

        public static final String ADMIN_ROLE = "admin";
        public static final String PUBLISHER_ROLE = "Internal/publisher";
        public static final String PRIVATE_USER_ROLE = "Internal/private_";
        public static final String ASSET_TYPE = "asset";
        public static final String ACTION = "action";
        public static final String USER = "user";
        public static final String ACTION_TYPE = "type";
        public static final String TIME_STAMP = "timestamp";
    }

    public static final String PROCESS_VERSION = "processVersion";
    public static final String MNS = "http://www.wso2.org/governance/metadata";

    public static final String EVENT_STREAM_NAME = "eventStreamName" ;
    public static final String EVENT_STREAM_VERSION = "eventStreamVersion";
    public static final String EVENT_STREAM_DESCRIPTION = "eventStreamDescription";
    public static final String EVENT_STREAM_NICK_NAME = "eventStreamNickName";
    public static final String EVENT_RECEIVER_NAME = "eventReceiverName";
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";

    public static class PROCESS_CONTENT_SEARCH {

        //UI constants
        public static final String PDF = "PDF";
        public static final String DOCUMENT = "Document";
        public static final String PROCESS_TEXT = "Process-Text";
        public static final String PROCESS = "Process";

        //media types for for indexing
        public static final String PDF_MEDIATYPE = "application/pdf";
        public static final String DOCUMENT_MEDIATYPE = "application/msword";
        public static final String PROCESS_TEXT_MEDIATYPE = "text/html";
        public static final String PROCESS_MEDIATYPE = "application/vnd.wso2-process+xml";
        public static final String PACKAGE_MEDIATYPE = "application/vnd.wso2-package+xml";
    }
}
