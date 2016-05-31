package org.wso2.pc.integration.test.utils.base.models;

/**
 * Created by samithac on 30/5/16.
 */
public class AdvancedGenericSearchData {
    private String name;
    private String version;
    private String lifeCycleStatus;
    private String tags;
    private String owner;
    private String description;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    public String getTags() {
        return tags;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public AdvancedGenericSearchData(String name, String version, String lifeCycleStatus, String tags, String owner, String description) {
        this.name = name;
        this.version = version;
        this.lifeCycleStatus = lifeCycleStatus;
        this.tags = tags;
        this.owner = owner;
        this.description = description;
    }
}
