package org.wso2.carbon.pc.core.audit.bean;

import org.wso2.carbon.registry.core.LogEntry;

import java.io.Serializable;

public class LogBean implements Serializable{

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
