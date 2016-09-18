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

import java.io.File;
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
    public static final String USERNAME = "username";
    public static final String OVERVIEW = "overview";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_TIME = "createdtime";
    public static final String IMAGES = "images";
    public static final String THUMBNAIL = "thumbnail";
    public static final String PROCESS_DEPLOYMENT_ID = "processDeploymentID";
    public static final String DEPLOYMENT_ID = "deploymentID";
    public static final String METADATA = "metadata";
    public static final String METADATA_NAMESPACE = "http://www.wso2.org/governance/metadata";
    public static final String NOT_APPLICABLE = "NA";
    public static final String IMAGE_THUMBNAIL = "images_thumbnail";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public static final String NO_FILE_SPECIFIED = "no-file-specified";
    public static final String BPMN_RESOURCES = "bpmnResources";
    public static final String DEPLOYMENTS = "deployments";
    /**
     * Package asset
     */
    public static final String PACKAGE_BPMN_ARCHIVE_FILE_NAME = "packageBpmnFileName";
    public static final String BPMN_RESOURCES_COUNT = "bpmn_resources_count";
    public static final String PACKAGE_PROCESS_ASSOCIATION = "package_process";
    public static final String RUNTIME_ENVIRONMENT = "runtimeEnvironment";
    public static final String CHECKSUM = "checksum";
    public static final String LATEST = "latest";
    public static final String LATEST_CHECKSUM = "latestChecksum";
    public static final String LAST_UPDATED_TIME = "lastUpdatedTime";
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
    public static final String GREG_PATH = File.separator + "_system" + File.separator + "governance" + File.separator;
    public static final String PROCESS_CENTER_RESOURCES_PATH =
            "process_center" + File.separator + "asset_resources" + File.separator;
    public static final String PROCESS_ASSET_ROOT = "processes" + File.separator;
    public static final String PACKAGE_ASSET_ROOT = "package" + File.separator;
    public static final String PACKAGES_ASSET_ROOT = "packages" + File.separator;
    public static final String BPMN_CONTENT_PATH = "bpmncontent" + File.separator;
    public static final String BPMN_META_DATA_FILE_PATH = "bpmn" + File.separator;
    public static final String IMAGE_PATH =
            "store" + File.separator + "asset_resources" + File.separator + "process" + File.separator;
    public static final String IMAGE_PATH_PACKAGE =
            "store" + File.separator + "asset_resources" + File.separator + "package" + File.separator;
    public static final String GREG_PATH_PROCESS =
            File.separator + "_system" + File.separator + "governance" + File.separator + "processes" + File.separator;
    public static final String PROCESS_LIFECYCLE_NAME = "DefaultProcessLifeCycle";
    //association type for processes in pdf, documents and process text assets
    public static final String ASSOCIATION_TYPE = "process_association";
    public static final String SUBPROCESS_ASSOCIATION = "subprocess";
    public static final String PARENTPROCESS_ASSOCIATION = "parent";
    public static final String SUCCESSOR_ASSOCIATION = "successor";
    public static final String PREDECESSOR_ASSOCIATION = "predecessor";
    public static final String PROCESS_TEXT_PATH = "processText" + File.separator;
    public static final String DOC_CONTENT_PATH = "doccontent" + File.separator;
    public static final String UI_PERMISSION_ACTION = "ui.execute";
    public static final String PROCESS_UPDATE_PERMISSION =
            File.separator + "permission" + File.separator + "admin" + File.separator + "enterprisestore"
                    + File.separator + "apps" + File.separator + "publisher" + File.separator + "features"
                    + File.separator + "assets" + File.separator + "process" + File.separator + "update";
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
    public static final String BPMN20XML = "bpmn20.xml";
    public static final String BPMN = "bpmn";
    public static final List<String> BPMN_RESOURCE_SUFFIXES = Collections
            .unmodifiableList(Arrays.asList(BPMN20XML, BPMN));
    /**
     * Process Center Asset Permission
     **/
    public static final String READ = "2";
    public static final String WRITE = "3";
    public static final String DELETE = "4";
    public static final String AUTHORIZE = "5";
    public static final String ALLOW = "1";
    public static final String DENY = "-1";
    // Media types
    public static final String PDF_MEDIA_TYPE = "application/pdf";
    public static final String MS_WORD_DOC_MEDIA_TYPE = "application/msword";
    public static final String PROCESS_TEXT_MEDIA_TYPE = "text/html";
    public static final String PROCESS_MEDIA_TYPE = "application/vnd.wso2-process+xml";
    public static final String PACKAGE_MEDIA_TYPE = "application/vnd.wso2-package+xml";
    public static final String ZIP_MEDIA_TYPE = "application/zip";
    public static final String BAR_EXTENSION = ".bar";
    // UI constants
    public static final String PDF = "PDF";
    public static final String DOCUMENT = "Document";
    public static final String PROCESS_TEXT = "Process-Text";
    public static final String PROCESS_TYPE = "Process";
    public static final String PROCESS_EXPORT_DIR = "Exports" + File.separator;
    public static final String EXPORTED_PROCESS_RXT_FILE = "process_rxt.xml";
    public static final String EXPORTED_BPMN_META_FILE = "bpmn.xml";
    public static final String EXPORTED_BPMN_CONTENT_FILE = "bpmn_content.xml";
    public static final String EXPORTED_FLOW_CHART_FILE = "flowChart.json";
    public static final String EXPORTED_PROCESS_TEXT_FILE = "process_text.xml";
    public static final int JSON_FILE_INDENT_FACTOR = 5;
    public static final String PROCESS_ASSET_RESOURCE_REG_PATH =
            "store" + File.separator + "asset_resources" + File.separator + "process" + File.separator;
    public static final String PROCESS_TAGS_FILE = "process_tags.txt";
    public static final String PROCESS_ASSOCIATIONS_FILE = "process_associations.json";
    public static final String PROCESS_VERSION = "processVersion";
    public static final String MNS = "http://www.wso2.org/governance/metadata";
    public static final String EVENT_STREAM_NAME = "eventStreamName";
    public static final String EVENT_STREAM_VERSION = "eventStreamVersion";
    public static final String EVENT_STREAM_DESCRIPTION = "eventStreamDescription";
    public static final String EVENT_STREAM_NICK_NAME = "eventStreamNickName";
    public static final String EVENT_RECEIVER_NAME = "eventReceiverName";
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    //BPMN related constants
    public static final String TYPE = "type";
    public static final String ACTIVITI_FORM_PROPERTY = "activiti:formProperty";
    public static final String VARIABLE = "variable";
    public static final String WSO2_BPMN_ASSET_MEDIA_TYPE = "application/vnd.wso2-bpmn+xml";
    public static final String PROCESS_ZIP_DOCUMENTS_DIR = "documents";
    public static final String TAGS_FILE_TAG_SEPARATOR = ",";
    public static final String FLOW_CHART = "flowchart";
    public static final String SUCCESSORS = "successors";
    public static final String SUBPROCESSES = "subprocesses";
    public static final String PREDECESSORS = "predecessors";
    public static final String IMPORTS_DIR = "Imports";
    public static final String EXPORTS_DIR_SUFFIX = "-PC-Package";
    public static final String EXPORTS_ZIP_SUFFIX = "-PC-Package.zip";
    public static final String DEFAULT_LIFECYCLE_NAME = "DefaultProcessLifeCycle";
    //analytics related constants
    public static final String PROCESS_VARIABLES = "processVariables";
    public static final String PROCESS_VARIABLE = "process_variable";
    public static final String IS_ANALYZE_DATA = "isAnalyzeData";
    public static final String IS_DRILL_DOWN_VARIABLE = "isDrillDownVariable";
    public static final String IS_DRILL_DOWN_DATA = "isDrillDownData";
    public static final String PROCESS_VARIABLE_LIST = "processVariableList";
    public static final String ANALYTICS_CONFIG_INFO = "analytics_config_info";
    public static final String VARIABLE_NAME = "variableName";
    public static final String VARIABLE_TYPE = "variableType";
    //other
    public static final String URL = "url";
    public static final String NA = "NA";

    // Make the constructor private, since it is a utility class
    private ProcessCenterConstants() {
    }

    public enum PACKAGE_STATUS {
        UPLOADED, ASSOCIATED
    }

    public static class AUDIT {

        public static final String PROCESS = "process";
        public static final String PROCESS_TEXT = "processText";
        public static final String PDF = "pdf";
        public static final String FLOW_CHART = "flowchart";
        public static final String DOCUMENT = "document";
        public static final String LIFE_CYCLE = "lifecycle";
        public static final String DOC_CONTENT = "doccontent";

        public static final String PROCESS_PATH = File.separator + "processes" + File.separator;
        public static final String PROCESS_TEXT_PATH = File.separator + "processText" + File.separator;
        public static final String PROCESS_PDF_PATH = File.separator + "pdf" + File.separator;
        public static final String PROCESS_FLOW_CHART_PATH = File.separator + "flowchart" + File.separator;
        public static final String PROCESS_DOC_PATH = File.separator + "doccontent" + File.separator;
        public static final String PROCESS_BPMN = File.separator + "bpmn" + File.separator;
        public static final String PROCESS_LIFECYCLE_HISTORY =
                File.separator + "_system" + File.separator + "governance" + File.separator + "repository"
                        + File.separator + "components" + File.separator + "org.wso2" + ".carbon.governance"
                        + File.separator + "lifecycles" + File.separator + "history" + File.separator
                        + "__system_governance_processes_";
        public static final String AC_PROCESS_PATH =
                File.separator + "_system" + File.separator + "governance" + File.separator + "processes"
                        + File.separator;

        public static final String ADMIN_ROLE = "admin";
        public static final String PUBLISHER_ROLE = "Internal/publisher";
        public static final String PRIVATE_USER_ROLE = "Internal/private_";
        public static final String ASSET_TYPE = "asset";
        public static final String ACTION = "action";
        public static final String USER = "user";
        public static final String ACTION_TYPE = "type";
        public static final String TIME_STAMP = "timestamp";
    }
}
