package org.wso2.carbon.processCenter.core;

public class ProcessStoreServiceImpl implements ProcessStoreService{
    private ProcessStore processStore;
    @Override
    public ProcessStore getProcessStore() {
        return processStore;
    }

    public void setProcessStore(ProcessStore processStore){
        this.processStore = processStore;
    }
}
