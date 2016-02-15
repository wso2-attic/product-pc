package org.wso2.carbon.processCenter.api;

/**
 * Created by sathya on 2/9/16.
 */

public class Process {

    //private String processPath;
    private String processName;
    private String processVersion;
    private String processText;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessText() {
        return processText;
    }

    public void setProcessText(String processText) {
        this.processText = processText;
    }


    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

}
