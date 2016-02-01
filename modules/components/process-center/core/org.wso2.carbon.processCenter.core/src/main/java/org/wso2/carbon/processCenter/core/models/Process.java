package org.wso2.carbon.processCenter.core.models;

/**
 * Created by dilini on 1/22/16.
 */
public class Process {
    private String name;
    private String version;

    public Process(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
