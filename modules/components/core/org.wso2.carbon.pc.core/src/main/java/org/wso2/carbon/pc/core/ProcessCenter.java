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
package org.wso2.carbon.pc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.pc.core.config.ProcessCenterConfiguration;
import org.wso2.carbon.pc.core.runtime.ProcessServer;
import org.wso2.carbon.pc.core.runtime.ProcessServerImpl;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;

/**
 * This Class represents the Process Center and initialize configuration
 * and runtime environments
 * This will describe the OSGi service interface where others can used to interact with
 * Process Center implementation.
 */
public class ProcessCenter {

    private static final Log log = LogFactory.getLog(ProcessCenter.class);

    /**
     * The process center configurations
     */
    private ProcessCenterConfiguration processCenterConfiguration;

    /**
     * The process Server
     */
    private ProcessServer processServer;

    /**
     * Initialize Process Center
     *
     * @throws ProcessCenterException
     */
    public void init() throws ProcessCenterException {
        loadProcessCenterConfiguration();
        loadRuntimeEnvironment();
    }

    /**
     * Read the process center configuration file and load it to memory. If configuration file is not there default
     * configuration will be created.
     */
    private void loadProcessCenterConfiguration() {
        if (log.isDebugEnabled()) {
            log.debug("Loading Process Center Configuration.");
        }
        if (isProcessCenterConfigurationFileAvailable()) {
            File processCenterConfigFile = new File(getProcessCenterConfigurationFilePath());
            processCenterConfiguration = new ProcessCenterConfiguration(processCenterConfigFile);
        } else {
            log.warn("Process Center configuration file: " + ProcessCenterConstants
                    .PROCESS_CENTER_CONFIGURATION_FILE_NAME +
                    " not found. Loading default configurations.");
        }
    }

    /**
     * Load Process Runtime Servers
     */
    private void loadRuntimeEnvironment() throws ProcessCenterException {
        if (log.isDebugEnabled()) {
            log.debug("Loading Process Runtime Servers.");
        }
        // load the runtime server configs from configuration
        if (processCenterConfiguration != null && processCenterConfiguration.isRuntimeEnvironmentEnabled()) {
            processServer = new ProcessServerImpl(processCenterConfiguration.getRuntimeEnvironmentURL(),
                    processCenterConfiguration.getRuntimeEnvironmentUsername(), processCenterConfiguration
                    .getRuntimeEnvironmentPassword());
        }
        // TODO: support multiple runtime enviroments
    }

    /**
     * @return : true is the configuration file is in the file system false otherwise.
     */
    private boolean isProcessCenterConfigurationFileAvailable() {
        File processCenterConfigurationFile = new File(getProcessCenterConfigurationFilePath());
        return processCenterConfigurationFile.exists();
    }

    /**
     * @return Process Center configuration path.
     */
    private String getProcessCenterConfigurationFilePath() {
        return CarbonUtils.getCarbonConfigDirPath() + File.separator +
                ProcessCenterConstants.PROCESS_CENTER_CONFIGURATION_FILE_NAME;
    }

    /**
     * Get process center configuration
     *
     * @return process center configuration
     */
    public ProcessCenterConfiguration getProcessCenterConfiguration() {
        return processCenterConfiguration;
    }

    /**
     * Get process server
     *
     * @return process server
     */
    public ProcessServer getProcessServer() {
        return processServer;
    }
}
