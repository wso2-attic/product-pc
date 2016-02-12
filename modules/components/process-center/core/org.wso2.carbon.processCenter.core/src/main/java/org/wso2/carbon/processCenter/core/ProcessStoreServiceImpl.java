package org.wso2.carbon.processCenter.core;

/**
 * Created by dilini on 2/9/16.
 */
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
