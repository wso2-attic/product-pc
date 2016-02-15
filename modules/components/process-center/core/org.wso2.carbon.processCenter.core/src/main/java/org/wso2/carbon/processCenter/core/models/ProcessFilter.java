package org.wso2.carbon.processCenter.core.models;

/**
 * Created by dilini on 2/9/16.
 */
public class ProcessFilter {

    private String filter;
    private String userName;
    private String filterType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String name) {
        this.filter = name;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
}
