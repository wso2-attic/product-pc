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

package org.wso2.carbon.pc.core.audit.bean;

import org.wso2.carbon.registry.core.LogEntry;

import java.io.Serializable;

/**
 * bean class for store a log entry
 */
public class LogBean implements Serializable {

    private LogEntry[] processEntries;
    private LogEntry[] processTxtEntries;
    private LogEntry[] pdfEntries;
    private LogEntry[] flowchartEntries;
    private LogEntry[] docEntries;
    private LogEntry[] bpmnEntries;
    private LogEntry[] lifecycleEntries;

    public LogEntry[] getProcessEntries() {
        return processEntries;
    }

    public void setProcessEntries(LogEntry[] processEntries) {
        this.processEntries = processEntries;
    }

    public LogEntry[] getProcessTxtEntries() {
        return processTxtEntries;
    }

    public void setProcessTxtEntries(LogEntry[] processTxtEntries) {
        this.processTxtEntries = processTxtEntries;
    }

    public LogEntry[] getPdfEntries() {
        return pdfEntries;
    }

    public void setPdfEntries(LogEntry[] pdfEntries) {
        this.pdfEntries = pdfEntries;
    }

    public LogEntry[] getFlowchartEntries() {
        return flowchartEntries;
    }

    public void setFlowchartEntries(LogEntry[] flowchartEntries) {
        this.flowchartEntries = flowchartEntries;
    }

    public LogEntry[] getDocEntries() {
        return docEntries;
    }

    public void setDocEntries(LogEntry[] docEntries) {
        this.docEntries = docEntries;
    }

    public LogEntry[] getBpmnEntries() {
        return bpmnEntries;
    }

    public void setBpmnEntries(LogEntry[] bpmnEntries) {
        this.bpmnEntries = bpmnEntries;
    }

    public LogEntry[] getLifecycleEntries() {
        return lifecycleEntries;
    }

    public void setLifecycleEntries(LogEntry[] lifecycleEntries) {
        this.lifecycleEntries = lifecycleEntries;
    }

}
