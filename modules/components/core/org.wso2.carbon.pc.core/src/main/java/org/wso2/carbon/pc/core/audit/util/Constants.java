package org.wso2.carbon.pc.core.audit.util;

public class Constants {

    public static final String PROCESS = "process";
    public static final String PROCESS_TEXT = "processText";
    public static final String PDF = "pdf";
    public static final String FLOW_CHART = "flowchart";
    public static final String DOCUMENT = "document";
    public static final String BPMN = "bpmn";
    public static final String LIFE_CYCLE = "lifecycle";
    public static final String DOC_CONTENT = "doccontent";

    public static class REG {
        public static final String PROCESS_PATH = "/_system/governance/processes/";
        public static final String PROCESS_TEXT_PATH = "/_system/governance/processText/";
        public static final String PROCESS_PDF_PATH = "/_system/governance/pdf/";
        public static final String PROCESS_FLOW_CHART_PATH = "/_system/governance/flowchart/";
        public static final String PROCESS_DOC_PATH = "/_system/governance/doccontent/";
        public static final String PROCESS_BPMN = "/_system/governance/bpmn/";
        public static final String PROCESS_LIFECYCLE_HISTORY = "/_system/governance/_system/governance/repository/components/org.wso2.carbon.governance/lifecycles/history/__system_governance_processes_";
    }

    public static class LOGENTRY {
        public static final String ASSET_TYPE = "asset";
        public static final String ACTION = "action";
        public static final String USER = "user";
        public static final String ACTION_TYPE = "type";
        public static final String TIME_STAMP = "timestamp";
        public static final String ASSET_ID = "id";
    }
}
