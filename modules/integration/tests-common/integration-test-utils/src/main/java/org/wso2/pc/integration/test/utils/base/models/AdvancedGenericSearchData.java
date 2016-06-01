/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.wso2.pc.integration.test.utils.base.models;

/**
 * Model class using for Advanced generic search query data
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
