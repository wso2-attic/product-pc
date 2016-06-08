package org.wso2.carbon.pc.core;

/**
 * Holds the constants of ProcessStore
 */
public class ProcessCenterConstants {
    public static final String GREG_PATH = "/_system/governance/";
    public static final String PROCESS_ASSET_ROOT = "processes/";
    public static final String BPMN_CONTENT_PATH = "bpmncontent/";
    public static final String BPMN_PATH = "bpmn/";
    public static final String IMAGE_PATH = "store/asset_resources/process/";
    public static final String GREG_PATH_PROCESS = "/_system/governance/processes/";

    public static final String TAGS = "tags";
    public static final String LC_STATE = "lcState";
    public static final String PROCESS_MEDIA_TYPE = "application/vnd.wso2-process+xml";

    public static final String LC_NAME = "SampleLifeCycle2";

    //association type for processes in pdf, documents and process text assets
    public static final String ASSOCIATION_TYPE = "process_association";
    public static final String SUBPROCESS_ASSOCIATION = "subprocess";
    public static final String PARENTPROCESS_ASSOCIATION = "parent";
    public static final String SUCCESSOR_ASSOCIATION = "successor";
    public static final String PREDECESSOR_ASSOCIATION = "predecessor";

    public static final String UI_PERMISSION_ACTION = "ui.execute";

    public static final String PROCESS_UPDATE_PERMISSION = "/permission/admin/enterprisestore/apps/publisher/features/assets/process/update";

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
        public static final String PROCESS_LIFECYCLE_HISTORY = "/_system/governance/repository/components/org.wso2.carbon.governance/lifecycles/history/__system_governance_processes_";
        public static final String AC_PROCESS_PATH = "/_system/governance/processes/";

        public static final String PUBLISHER_ROLE = "Internal/publisher";
        public static final String PRIVATE_USER_ROLE = "Internal/private_";
        public static final String ASSET_TYPE = "asset";
        public static final String ACTION = "action";
        public static final String USER = "user";
        public static final String ACTION_TYPE = "type";
        public static final String TIME_STAMP = "timestamp";
        public static final String ASSET_ID = "id";
    }

    public static final String READ = "2";
    public static final String WRITE = "3";
    public static final String AUTHORIZE = "5";
    public static final String ALLOW = "1";

}
