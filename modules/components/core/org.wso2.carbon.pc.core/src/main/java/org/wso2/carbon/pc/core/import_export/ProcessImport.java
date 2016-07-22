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

package org.wso2.carbon.pc.core.import_export;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.audit.util.RegPermissionUtil;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.api.UserStoreException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ProcessImport {
    private static final Log log = LogFactory.getLog(ProcessImport.class);
    File[] listOfProcessDirs;
    RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
    UserRegistry reg;
    String user;

    /**
     *
     * @param processZipInputStream
     * @param user
     * @throws IOException
     * @throws RegistryException
     * @throws ProcessCenterException
     * @throws UserStoreException
     */
    public void importProcesses(InputStream processZipInputStream, String user)
            throws IOException, RegistryException, ProcessCenterException, UserStoreException {

        reg = registryService.getGovernanceUserRegistry(user);
        this.user = user;
        //extract zip file stream to the system disk
        byte[] buffer = new byte[2048];
        ZipInputStream zipInputStream = new ZipInputStream(processZipInputStream);
        new File(ImportExportConstants.IMPORTS_DIR).mkdirs();

        try {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                //counter++;
                String outpath = ImportExportConstants.IMPORTS_DIR + "/" + entry.getName();
                String dirPath = outpath.substring(0, outpath.lastIndexOf("/"));
                new File(dirPath).mkdirs();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(outpath);
                    int len = 0;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                } finally {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
            }
        } finally {
            zipInputStream.close();
        }

        //check if the importing processes are already available
        if (isProcessesAlreadyAvailble()) {
            return;
        }

        if (registryService != null) {

            //else do the process importing for each process
            for (File processDir : listOfProcessDirs) {
                if(processDir.isDirectory()) {
                    String processDirName = processDir.getName();
                    String processDirPath = processDir.getPath();
                    String processRxtPath = processDirPath + "/" + "process_rxt.xml";
                    String processName = processDirName.substring(0, processDirName.lastIndexOf("-"));
                    String processVersion = processDirName.substring(processDirName.lastIndexOf("-") + 1, processDirName.length());
                    putProcessRxt(processName, processVersion, processRxtPath);
                    setImageThumbnail(processName, processVersion,processDirPath);

                    //set process documents
                    //set process tags
                    //Finally remove the Contents of Imports folder
                }
            }
        }
    }

    /**
     *
     * @param processName
     * @param processVersion
     * @param processDirPath
     * @throws RegistryException
     * @throws IOException
     */
    private void setImageThumbnail(String processName, String processVersion, String processDirPath)
            throws RegistryException, IOException {
        String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                processVersion;
        Resource storedProcess = reg.get(processAssetPath);
        String processId = storedProcess.getUUID();

        String imageResourcePath =
                ProcessCenterConstants.PROCESS_ASSET_RESOURCE_REG_PATH + processId + "/images_thumbnail";

        Resource imageContentResource = reg.newResource();

        File imageThumbnailFile = new File(processDirPath+"/"+"process_image_thumbnail");
        byte[] imageContent = Files.readAllBytes(imageThumbnailFile.toPath());
        imageContentResource.setContent(imageContent);
        reg.put(imageResourcePath, imageContentResource);

    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ProcessCenterException
     * @throws RegistryException
     */
    public boolean isProcessesAlreadyAvailble() throws IOException, ProcessCenterException, RegistryException {
        File folder = new File(ImportExportConstants.IMPORTS_DIR);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles[0].isDirectory() && listOfFiles.length == 1) {
            String zipHomeDirectoryName = listOfFiles[0].getPath();
            File packageFolder = new File(zipHomeDirectoryName);
            listOfProcessDirs = packageFolder.listFiles();
            ArrayList<String> processListinPC = getProcessList();

            for (File processDir : listOfProcessDirs) {
                if (processDir.isDirectory()) {
                    String fileName = processDir.getName();
                    if (processListinPC.contains(fileName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @return
     * @throws ProcessCenterException
     * @throws RegistryException
     */
    public ArrayList<String> getProcessList() throws ProcessCenterException, RegistryException {

        ArrayList<String> processList = new ArrayList<String>();
        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            UserRegistry reg = registryService.getGovernanceSystemRegistry();

            String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
            for (String processPath : processPaths) {
                String processName = processPath.split("/")[2];
                String processVersion = processPath.split("/")[3];
                processList.add(processName + "-" + processVersion);
            }
        } else {
            String msg = "Registry service not available for retrieving processes.";
            throw new ProcessCenterException(msg);
        }
        return processList;
    }

    /**
     *
     * @param processName
     * @param processVersion
     * @param processRxtPath
     * @throws RegistryException
     * @throws UserStoreException
     * @throws IOException
     */
    public void putProcessRxt(String processName, String processVersion, String processRxtPath) throws RegistryException, UserStoreException, IOException {
        File rxtFile = new File(processRxtPath);

        RegPermissionUtil.setPutPermission(registryService, user, ProcessCenterConstants.PROCESS_ASSET_ROOT);
        String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                processVersion;
        Resource processRxt = reg.newResource();
        processRxt.setContentStream(FileUtils.openInputStream(rxtFile));
        processRxt.setMediaType("application/vnd.wso2-process+xml");
        reg.put(processAssetPath, processRxt);


    }
}
