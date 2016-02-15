package org.wso2.carbon.processCenter.api;

/**
 * Created by sathya on 2/13/16.
 */
public class ProcessVersion {

    private String processPath;
    private String updatedVersion;

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public String getUpdatedVersion() {
        return updatedVersion;
    }

    public void setUpdatedVersion(String updatedVersion) {
        this.updatedVersion = updatedVersion;
    }
}
