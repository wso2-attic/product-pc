package org.wso2.carbon.processCenter.core.models;

/**
 * Created by dilini on 1/22/16.
 */

public class Process {
    private String owner;
    private String name;
    private String version;
    private String createdtime;
    private String bpmnpath;
    private String bpmnid;
    private String processtextpath;
    private String userName;

    public Process() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getBpmnpath() {
        return bpmnpath;
    }

    public void setBpmnpath(String bpmnpath) {
        this.bpmnpath = bpmnpath;
    }

    public String getBpmnid() {
        return bpmnid;
    }

    public void setBpmnid(String bpmnid) {
        this.bpmnid = bpmnid;
    }

    public String getProcesstextpath() {
        return processtextpath;
    }

    public void setProcesstextpath(String processtextpath) {
        this.processtextpath = processtextpath;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
