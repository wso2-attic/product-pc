/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.wso2.pc.integration.test.utils.base;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

public class ArtifactUploadUtil {

        /**
         * This method uploads a BPMN
         *
         * @param filePath       The absolute path of the file
         * @param processName    Process name
         * @param processVersion Process version
         * @param shortName      Asset shortname mentioned in the RXT
         * @param cookieHeader   Session cookie
         * @throws IOException
         */
        public static PostMethod uploadBPMN(String filePath,
                String processName,
                String processVersion,
                String shortName,
                String cookieHeader,
                String apiUrl)
                throws IOException {

                File file = new File(filePath);
                FilePart fp = new FilePart(shortName + "_file", file);
                fp.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp1 = new StringPart("bpmnProcessName", processName);
                sp1.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp2 = new StringPart("bpmnProcessVersion", processVersion);
                sp2.setContentType(MediaType.TEXT_PLAIN);
                //Set file parts and string parts together
                final Part[] part = {fp, sp1, sp2};

                HttpClient httpClient = new HttpClient();
                PostMethod httpMethod = new PostMethod(apiUrl);

                httpMethod.addRequestHeader("Cookie", cookieHeader);
                httpMethod.addRequestHeader("Accept", MediaType.APPLICATION_JSON);
                httpMethod.setRequestEntity(
                        new MultipartRequestEntity(part, httpMethod.getParams()));
                httpClient.executeMethod(httpMethod);
                return httpMethod;
        }

        /**
         * This method uploads a document
         *
         * @param filePath       The absolute path of the file
         * @param documentName   document name
         * @param documentSummary summary of the document
         * @param documentExtension extension of the document file
         * @param documentURL    document URL
         * @param processName    Process name
         * @param processVersion Process version
         * @param cookieHeader   Session cookie
         * @throws IOException
         */
        public static PostMethod uploadDocument(String filePath,
                String documentName,
                String documentSummary,
                String documentExtension,
                String documentURL,
                String optionsRadios1,
                String processName,
                String processVersion,
                String cookieHeader,
                String apiUrl,
                String contentType)
                throws IOException {

                File file = new File(filePath);
                FilePart fp = new FilePart("PDF" + "_file", file);
                fp.setContentType(contentType);
                StringPart sp1 = new StringPart("docName", documentName);
                sp1.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp2 = new StringPart("summaryDoc", documentSummary);
                sp2.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp3 = new StringPart("docProcessName", processName);
                sp3.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp4 = new StringPart("docProcessVersion", processVersion);
                sp4.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp5 = new StringPart("docExtension", documentExtension);
                sp5.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp6 = new StringPart("docUrl", documentURL);
                sp6.setContentType(MediaType.TEXT_PLAIN);
                StringPart sp7 = new StringPart("optionsRadios1", optionsRadios1);
                sp7.setContentType(MediaType.TEXT_PLAIN);

                //Set file parts and string parts together
                final Part[] part = {fp, sp1, sp2,sp3,sp4,sp5,sp6,sp7};

                HttpClient httpClient = new HttpClient();
                PostMethod httpMethod = new PostMethod(apiUrl);

                httpMethod.addRequestHeader("Cookie", cookieHeader);
                httpMethod.addRequestHeader("Accept", MediaType.APPLICATION_JSON);
                httpMethod.setRequestEntity(
                        new MultipartRequestEntity(part, httpMethod.getParams()));
                httpClient.executeMethod(httpMethod);
                return httpMethod;
        }


        /**
         * This method uploads a process ZIP file
         *
         * @param filePath       The absolute path of the file
         * @param cookieHeader   Session cookie
         * @throws IOException
         */
        public static PostMethod uploadProcess(String filePath,
                String cookieHeader,
                String apiUrl)
                throws IOException {

                File file = new File(filePath);
                FilePart fp = new FilePart("processZip", file);

                //Set file parts and string parts together
                final Part[] part = {fp};
                HttpClient httpClient = new HttpClient();
                PostMethod httpMethod = new PostMethod(apiUrl);
                httpMethod.addRequestHeader("Cookie", cookieHeader);
                httpMethod.addRequestHeader("Accept", MediaType.APPLICATION_JSON);
                httpMethod.setRequestEntity(
                        new MultipartRequestEntity(part, httpMethod.getParams()));
                httpClient.executeMethod(httpMethod);
                return httpMethod;
        }
}