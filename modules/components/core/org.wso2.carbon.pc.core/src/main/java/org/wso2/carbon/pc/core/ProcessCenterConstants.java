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
}
