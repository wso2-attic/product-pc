package org.wso2.carbon.pc.core.audit.bean;


import java.io.Serializable;

public class Process implements Serializable{
    private boolean isLifeCycleAdded = false;
    private boolean isCreated = false;

    public boolean isLifeCycleAdded() {
        return isLifeCycleAdded;
    }

    public void setLifeCycleAdded(boolean lifeCycleAdded) {
        isLifeCycleAdded = lifeCycleAdded;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
