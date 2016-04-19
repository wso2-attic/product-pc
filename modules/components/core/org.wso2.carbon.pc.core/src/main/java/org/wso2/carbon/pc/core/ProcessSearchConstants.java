package org.wso2.carbon.pc.core;

/**
 * Class to store content search related constants
 */
public final class ProcessSearchConstants {

    // Make the constructor private, since it is a utility class
    private ProcessSearchConstants() {
    }

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

    //association type for processes in pdf, documents and process text assets
    public static final String ASSOCIATION_TYPE = "process_association";




}
