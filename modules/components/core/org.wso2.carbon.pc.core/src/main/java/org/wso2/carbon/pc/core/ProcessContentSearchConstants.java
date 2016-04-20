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

/**
 * Class to store content search related constants
 */
public final class ProcessContentSearchConstants {

    // Make the constructor private, since it is a utility class
    private ProcessContentSearchConstants() {
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
