package org.wso2.carbon.processCenter.core.models;

import org.json.JSONString;

/**
 * Created by dilini on 2/10/16.
 */
public class AdvanceProcessFilter {
    private String filters;
    private String userName;

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
