/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.pc.core.config;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlException;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.securevault.SecretResolver;
import org.wso2.securevault.SecretResolverFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * The memory model of the process center configuration - process-center.xml.
 */
public class ProcessCenterConfiguration {


    private static final Log log = LogFactory.getLog(ProcessCenterConfiguration.class);
    private ProcessCenterDocument processCenterDocument;

    /* Configurations related to process analytics */
    private String analyticsServerURL;
    private String analyticsServerUsername;
    private String analyticsServerPassword;

    /* Configurations Related to Process Servers */
    private String runtimeEnvironmentURL;
    private String runtimeEnvironmentUsername;
    private String runtimeEnvironmentPassword;

    /**
     * Create Process Center Configuration from a configuration file. If error occurred while parsing configuration
     * file, default configuration will be created.
     *
     * @param processCenterConfig XMLBeans object of human task server configuration file
     */
    public ProcessCenterConfiguration(File processCenterConfig) {
        processCenterDocument = readConfigurationFromFile(processCenterConfig);

        if (processCenterDocument == null) {
            return;
        }
        initConfigurationFromFile(processCenterConfig);
    }

    /**
     * Parse Process center configuration file - process-center.xml and read configurations
     *
     * @param processCenterConfigurationFile
     * @return
     */
    private ProcessCenterDocument readConfigurationFromFile(File processCenterConfigurationFile) {
        try {
            return ProcessCenterDocument.Factory.parse(new FileInputStream(processCenterConfigurationFile));
        } catch (XmlException e) {
            log.error("Error parsing process center configuration.", e);
        } catch (FileNotFoundException e) {
            log.info("Cannot find the process center configuration in specified location "
                    + processCenterConfigurationFile.getPath() + " . Loads the default configuration.");
        } catch (IOException e) {
            log.error("Error reading process center configuration file" + processCenterConfigurationFile.getPath() +
                    " .");
        }

        return null;
    }

    /**
     * Initialize the configuration object from the properties in the human task server config xml file.
     */
    private void initConfigurationFromFile(File processCenterConfigurationFile) {


        SecretResolver secretResolver = null;
        try (InputStream in = new FileInputStream(processCenterConfigurationFile);) {
            StAXOMBuilder builder = new StAXOMBuilder(in);
            secretResolver = SecretResolverFactory.create(builder.getDocumentElement(), true);
        } catch (Exception e) {
            log.warn("Error occurred while retrieving secured Process Center configuration.", e);
        }
        TProcessCenter tProcessCenter = processCenterDocument.getProcessCenter();
        if (tProcessCenter == null) {
            return;
        }
        if (tProcessCenter.getAnalytics() != null) {
            initAnalytics(secretResolver, tProcessCenter.getAnalytics());
        }
        if (tProcessCenter.getRuntimeEnvironment() != null) {
            initEnvironments(secretResolver, tProcessCenter.getRuntimeEnvironment());
        }
    }

    /**
     * Initialize process analytics configurations
     *
     * @param tAnalytics
     */
    private void initAnalytics(SecretResolver secretResolver, TAnalytics tAnalytics) {

            // Get URL
            this.analyticsServerURL = tAnalytics.getDASServerUrl();
            // Get Username
            this.analyticsServerUsername = tAnalytics.getDASUsername();
            // Get Password
            if (secretResolver != null && secretResolver.isInitialized()
                    && secretResolver.isTokenProtected(ProcessCenterConstants.ANALYTICS_SERVER_PASSWORD_SECRET_ALIAS)) {
                this.analyticsServerPassword = secretResolver.resolve(ProcessCenterConstants
                        .ANALYTICS_SERVER_PASSWORD_SECRET_ALIAS);
                if (log.isDebugEnabled()) {
                    log.debug("Loaded  analytics  password from secure vault");
                }
            } else {
                if (tAnalytics.getDASPassword() != null) {
                    this.analyticsServerUsername = tAnalytics.getDASPassword();
                }
            }
        }


    /**
     * Initialize process server configurations
     *
     * @param secretResolver
     * @param tRuntimeEnvironment
     */
    private void initEnvironments(SecretResolver secretResolver, TRuntimeEnvironment tRuntimeEnvironment) {

            // Get runtime Server URL
            this.runtimeEnvironmentURL = tRuntimeEnvironment.getServerUrl();
            // Get Username
            this.runtimeEnvironmentUsername = tRuntimeEnvironment.getUsername();
            // Get Password
            if (secretResolver != null && secretResolver.isInitialized()
                    && secretResolver.isTokenProtected(ProcessCenterConstants.RUNTIME_ENVIRONMENT_PASSWORD_SECRET_ALIAS)) {
                this.runtimeEnvironmentPassword = secretResolver.resolve(ProcessCenterConstants
                        .RUNTIME_ENVIRONMENT_PASSWORD_SECRET_ALIAS);
                if (log.isDebugEnabled()) {
                    log.debug("Loaded  runtime environment password from secure vault");
                }
            } else {
                if (tRuntimeEnvironment.getPassword() != null) {
                    this.runtimeEnvironmentPassword = tRuntimeEnvironment.getPassword();
                }
            }
    }

    // Getters  retrieve process center configuration elements
    public String getAnalyticsServerURL() {
        return analyticsServerURL;
    }

    public String getAnalyticsServerUsername() {
        return analyticsServerUsername;
    }

    public String getAnalyticsServerPassword() {
        return analyticsServerPassword;
    }

    public String getRuntimeEnvironmentURL() {
        return runtimeEnvironmentURL;
    }

    public String getRuntimeEnvironmentUsername() {
        return runtimeEnvironmentUsername;
    }

    public String getRuntimeEnvironmentPassword() {
        return runtimeEnvironmentPassword;
    }
}
