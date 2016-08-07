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

package org.wso2.carbon.pc.core.transfer;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
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
import org.xml.sax.SAXException;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.wso2.carbon.pc.core.ProcessStore;

public class ProcessImport {
    private static final Log log = LogFactory.getLog(ProcessImport.class);
    File[] listOfProcessDirs;
    RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
    UserRegistry reg;
    String user;

    /**
     * @param processZipInputStream
     * @param user
     * @throws IOException
     * @throws RegistryException
     * @throws ProcessCenterException
     * @throws UserStoreException
     */
    public void importProcesses(InputStream processZipInputStream, String user)
            throws IOException, RegistryException, ProcessCenterException, UserStoreException, JSONException,
            TransformerException, SAXException, ParserConfigurationException {

        if (registryService != null) {
            reg = registryService.getGovernanceUserRegistry(user);
            this.user = user;
            //extract zip file stream to the system disk
            byte[] buffer = new byte[2048];
            ZipInputStream zipInputStream = new ZipInputStream(processZipInputStream);
            File importsDir = new File(ProcessCenterConstants.IMPORTS_DIR);
            if (importsDir != null) {
                importsDir.mkdirs();

                try {
                    ZipEntry entry;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        //counter++;
                        String outpath = ProcessCenterConstants.IMPORTS_DIR + "/" + entry.getName();
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
                boolean isProcessesAvailableAlready = isProcessesAlreadyAvailble();
                if (isProcessesAvailableAlready) {
                    log.error("One or more process in the importing archive is already cavailable in the Process Center");
                    throw new ProcessCenterException("ALREADY_AVAILABLE");
                }

                //else do the process importing for each process
                for (File processDir : listOfProcessDirs) {
                    if (processDir.isDirectory()) {
                        String processDirName = processDir.getName();
                        String processDirPath = processDir.getPath();
                        String processRxtPath = processDirPath + "/" + ProcessCenterConstants.EXPORTED_PROCESS_RXT_FILE;
                        String processName = processDirName.substring(0, processDirName.lastIndexOf("-"));
                        String processVersion = processDirName
                                .substring(processDirName.lastIndexOf("-") + 1, processDirName.length());
                        String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                processVersion;

                        putProcessRxt(processRxtPath, processAssetPath);
                        GovernanceUtils
                                .associateAspect(processAssetPath, ProcessCenterConstants.SAMPLE_LIFECYCLE2_NAME, reg);
                        setImageThumbnail(processDirPath, processAssetPath);
                        setProcessDocuments(processName, processVersion, processDirPath, processAssetPath);
                        setProcessTags(processDirPath, processAssetPath);
                        setProcessText(processName, processVersion, processDirPath, processAssetPath);
                        setBPMN(processName, processVersion, processDirPath, processAssetPath);
                        setFlowChart(processName, processVersion, processDirPath, processAssetPath);
                        setProcessAssociations(processName, processVersion, processDirPath, processAssetPath);
                    }
                }

                //Finally remove the Imports folder
                FileUtils.deleteDirectory(importsDir);
            }else{
                String errMsg = "Process Importing failed due to failure in creating Imports directory";
                throw new ProcessCenterException(errMsg);

            }
        }else {
            String errMsg = "Process Importing failed due to unavailability of Registry Service";
            throw new ProcessCenterException(errMsg);
        }
    }

    private void setProcessAssociations(String processName, String processVersion, String processDirPath, String
            processAssetPath) throws ProcessCenterException {
        try {
            Path procAssociationsJSONFilePath = Paths
                    .get(processDirPath + "/" + ProcessCenterConstants.PROCESS_ASSOCIATIONS_FILE);
            if (Files.exists(procAssociationsJSONFilePath)) {
                String procAssociationsFileContent = "";
                try (BufferedReader reader = Files.newBufferedReader(procAssociationsJSONFilePath, Charsets.US_ASCII)) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        procAssociationsFileContent += line;
                    }
                } catch (IOException e) {
                    String errMsg = "Error in reading process's process_associations.json file";
                    throw new IOException(errMsg, e);
                }

                JSONObject procAssociations = new JSONObject(procAssociationsFileContent);

                //set sub process
                JSONArray subProcesses = procAssociations.getJSONArray(ProcessCenterConstants.SUBPROCESSES);
                for (int i = 0; i < subProcesses.length(); i++) {
                    JSONObject subProcess = subProcesses.getJSONObject(i);
                    String subProcessPath = subProcess.getString("path");
                    addSubProcessAssociation(processAssetPath, subProcessPath);
                }
                //set successor process
                JSONArray successors = procAssociations.getJSONArray(ProcessCenterConstants.SUCCESSORS);
                for (int i = 0; i < successors.length(); i++) {
                    JSONObject successor = successors.getJSONObject(i);
                    String successorPath = successor.getString("path");
                    addSuccessorAssociation(processAssetPath, successorPath);
                }

                //set predecessor process associations
                JSONArray predecessors = procAssociations.getJSONArray(ProcessCenterConstants.PREDECESSORS);
                for (int i = 0; i < predecessors.length(); i++) {
                    JSONObject predecessor = predecessors.getJSONObject(i);
                    String predecessorPath = predecessor.getString("path");
                    addPredecessorAssociation(processAssetPath, predecessorPath);
                }

            }
        } catch (IOException e) {
            String errMsg = "Error in setting process associations for the process :"+processName+"-"+processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg,e);
        } catch (JSONException e) {
            String errMsg = "Error in setting process associations for the process :"+processName+"-"+processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg,e);
        } catch (RegistryException e) {
            String errMsg = "Error in setting process associations for the process :"+processName+"-"+processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg,e);
        }

    }

    private void addPredecessorAssociation(String processAssetPath, String predecessorPath) throws RegistryException {
        reg.addAssociation(processAssetPath, predecessorPath, ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
        reg.addAssociation(predecessorPath, processAssetPath, ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
    }

    private void addSubProcessAssociation(String processAssetPath, String subProcessPath) throws RegistryException {
        reg.addAssociation(processAssetPath, subProcessPath, ProcessCenterConstants.SUBPROCESS_ASSOCIATION);
        reg.addAssociation(subProcessPath, processAssetPath, ProcessCenterConstants.PARENTPROCESS_ASSOCIATION);
    }

    private void addSuccessorAssociation(String processAssetPath, String successorPath) throws RegistryException {
        reg.addAssociation(processAssetPath, successorPath, ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
        reg.addAssociation(successorPath, processAssetPath, ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
    }

    private void setFlowChart(String processName, String processVersion, String processDirPath, String processAssetPath)
            throws UserStoreException, RegistryException, IOException {
        UserRegistry reg = registryService.getGovernanceUserRegistry(user);
        RegPermissionUtil.setPutPermission(registryService, user, ProcessCenterConstants.AUDIT.PROCESS_FLOW_CHART_PATH);

        Resource flowchartContentResource = reg.newResource();

        Path flowChartJSONFilePath = Paths.get(processDirPath + "/" + ProcessCenterConstants.EXPORTED_FLOW_CHART_FILE);
        if (Files.exists(flowChartJSONFilePath)) {
            String flowChartFileContent = "";
            //Charset charset = Charset.forName(Charsets.US_ASCII"US-ASCII");
            try (BufferedReader reader = Files.newBufferedReader(flowChartJSONFilePath, Charsets.US_ASCII)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    flowChartFileContent += line;
                }
            } catch (IOException e) {
                String errMsg = "Error in reading process's flowChart.json file";
                throw new IOException(errMsg, e);
            }

            flowchartContentResource.setContent(flowChartFileContent);
            flowchartContentResource.setMediaType(MediaType.APPLICATION_JSON);
            String flowchartContentPath = ProcessCenterConstants.FLOW_CHART + "/" + processName + "/" +
                    processVersion;
            reg.put(flowchartContentPath, flowchartContentResource);
            //add reg association
            reg.addAssociation(flowchartContentPath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
        }
    }

    private void setBPMN(String processName, String processVersion, String processDirPath, String processAssetPath)
            throws IOException, SAXException, ParserConfigurationException, TransformerException, RegistryException {

        String bpmnFilePathStr = processDirPath + "/" + ProcessCenterConstants.EXPORTED_BPMN_CONTENT_FILE;
        Path bpmnFilePath = Paths.get(bpmnFilePathStr);
        String bpmnMetaDataFilePathStr = processDirPath + "/" + ProcessCenterConstants.EXPORTED_BPMN_META_FILE;
        Path bpmnMetaDataFilePath = Paths.get(bpmnMetaDataFilePathStr);

        if (Files.exists(bpmnFilePath) && Files.exists(bpmnMetaDataFilePath)) {
            //set bpmn content file
            File bpmnXMLFile = new File(bpmnFilePathStr);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(bpmnXMLFile);
            String bpmnFileContent = ProcessStore.xmlToString(doc);
            String bpmnContentResourcePath = ProcessCenterConstants.BPMN_CONTENT_PATH + processName +
                    "/" + processVersion;
            if (bpmnFileContent != null && bpmnFileContent.length() > 0) {
                Resource bpmnFileResource = reg.newResource();
                bpmnFileResource.setContent(bpmnFileContent);
                bpmnFileResource.setMediaType(MediaType.APPLICATION_XML);
                reg.put(bpmnContentResourcePath, bpmnFileResource);
                reg.addAssociation(bpmnContentResourcePath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
            }

            //set bpmn meta data file (contains path of the target bpmn file)
            File bpmnMetaDataXMLFile = new File(bpmnMetaDataFilePathStr);
            Document bpmnMetaDataDoc = dBuilder.parse(bpmnMetaDataXMLFile);
            String bpmnMetaDataContent = ProcessStore.xmlToString(bpmnMetaDataDoc);
            Resource bpmnMetaDataResource = reg.newResource();
            bpmnMetaDataResource.setContent(bpmnMetaDataContent);
            bpmnMetaDataResource.setMediaType(ProcessCenterConstants.WSO2_BPMN_ASSET_MEDIA_TYPE);
            String bpmnMetaDataResPath =
                    ProcessCenterConstants.BPMN_META_DATA_FILE_PATH + processName + "/" + processVersion;
            reg.put(bpmnMetaDataResPath, bpmnMetaDataResource);
        }
    }

    private void setProcessText(String processName, String processVersion, String processDirPath,
            String processAssetPath) throws IOException, RegistryException {
        Path processTextFilePath = Paths.get(processDirPath + "/" + ProcessCenterConstants.EXPORTED_PROCESS_TEXT_FILE);
        if (Files.exists(processTextFilePath)) {
            String processTextFileContent = "";
            //Charset charset = Charset.forName(Charsets.US_ASCII"US-ASCII");
            try (BufferedReader reader = Files.newBufferedReader(processTextFilePath, Charsets.US_ASCII)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    processTextFileContent += line;
                }
            } catch (IOException e) {
                String errMsg = "Error in reading process tags file";
                throw new IOException(errMsg, e);
            }

            // store process text as a separate resource
            String processTextResourcePath =
                    ProcessCenterConstants.PROCESS_TEXT_PATH + processName + "/" + processVersion;
            if (processTextFileContent != null && processTextFileContent.length() > 0) {
                Resource processTextResource = reg.newResource();
                processTextResource.setContent(processTextFileContent);
                processTextResource.setMediaType(MediaType.TEXT_HTML);
                reg.put(processTextResourcePath, processTextResource);
                reg.addAssociation(processTextResourcePath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
            }
        }
    }

    private void setProcessTags(String processDirPath, String processAssetPath) throws IOException, RegistryException {
        Path processTagsFilePath = Paths.get(processDirPath + "/" + ProcessCenterConstants.PROCESS_TAGS_FILE);
        if (Files.exists(processTagsFilePath)) {
            String tagsFileContent = "";
            //Charset charset = Charset.forName("US-ASCII");
            try (BufferedReader reader = Files.newBufferedReader(processTagsFilePath, Charsets.US_ASCII)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    tagsFileContent += line;
                }
            } catch (IOException e) {
                String errMsg = "Error in reading process tags file";
                throw new IOException(errMsg, e);
            }

            String[] tags = tagsFileContent.split(ProcessCenterConstants.TAGS_FILE_TAG_SEPARATOR);
            for (String tag : tags) {
                //tag = tag.trim();
                if (tag.length() > 0) {
                    reg.applyTag(processAssetPath, tag);
                }
            }
        }
    }

    private void setProcessDocuments(String processName, String processVersion, String processDirPath,
            String processAssetPath)
            throws ProcessCenterException, JSONException, RegistryException, IOException, SecurityException {

        File docsFolder = new File(processDirPath + "/" + ProcessCenterConstants.PROCESS_ZIP_DOCUMENTS_DIR);
        if (docsFolder.exists()) {
            File[] docFiles = docsFolder.listFiles();
            for (File docFile : docFiles) {
                if (docFile.isFile()) {
                    String fileName = docFile.getName();

                    String fileExt = FilenameUtils.getExtension(docFile.getPath());
                    String docResourcePath =
                            ProcessCenterConstants.DOC_CONTENT_PATH + processName + "/" + processVersion + "/" +
                                    fileName;
                    Resource docResource = reg.newResource();
                    FileInputStream docFileInputStream = new FileInputStream(docFile);
                    docResource.setContentStream(docFileInputStream);

                    if (fileExt.equalsIgnoreCase(ProcessCenterConstants.PDF)) {
                        docResource.setMediaType(ProcessCenterConstants.PDF_MEDIA_TYPE);
                    } else {
                        docResource.setMediaType(ProcessCenterConstants.MS_WORD_DOC_MEDIA_TYPE);
                    }
                    reg.put(docResourcePath, docResource);
                    reg.addAssociation(docResourcePath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
                    docFileInputStream.close();
                }
            }
        }
    }

    /**
     * @param processDirPath
     * @param processAssetPath
     * @throws RegistryException
     * @throws IOException
     */
    private void setImageThumbnail(String processDirPath, String processAssetPath)
            throws RegistryException, IOException {
        Resource storedProcess = reg.get(processAssetPath);
        String processId = storedProcess.getUUID();

        String imageResourcePath = ProcessCenterConstants.PROCESS_ASSET_RESOURCE_REG_PATH + processId + "/"
                + ProcessCenterConstants.IMAGE_THUMBNAIL;

        Resource imageContentResource = reg.newResource();

        File imageThumbnailFile = new File(processDirPath + "/" + ProcessCenterConstants.IMAGE_THUMBNAIL);
        byte[] imageContent = Files.readAllBytes(imageThumbnailFile.toPath());
        imageContentResource.setContent(imageContent);
        reg.put(imageResourcePath, imageContentResource);

    }

    /**
     * @return
     * @throws IOException
     * @throws ProcessCenterException
     * @throws RegistryException
     */
    public boolean isProcessesAlreadyAvailble() throws IOException, ProcessCenterException, RegistryException {
        File folder = new File(ProcessCenterConstants.IMPORTS_DIR);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            if (listOfFiles[0].isDirectory() && listOfFiles.length == 1) {
                String zipHomeDirectoryName = listOfFiles[0].getPath();
                File zipFolder = new File(zipHomeDirectoryName);
                listOfProcessDirs = zipFolder.listFiles();
                ArrayList<String> processListinPC = getProcessList();

                for (File processDir : listOfProcessDirs) {
                    if (processDir.isDirectory()) {
                        String fileName = processDir.getName();
                        if (processListinPC.contains(fileName)) {
                            log.error("Cannot proceed the importing..! Process :" + fileName + "already available in "
                                    + "Process Center");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return
     * @throws ProcessCenterException
     * @throws RegistryException
     */
    public ArrayList<String> getProcessList() throws ProcessCenterException, RegistryException {

        ArrayList<String> processList = new ArrayList<String>();
        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            UserRegistry reg = registryService.getGovernanceSystemRegistry();

            String[] processPaths = GovernanceUtils
                    .findGovernanceArtifacts(ProcessCenterConstants.PROCESS_MEDIA_TYPE, reg);
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
     * @param processRxtPath
     * @param processAssetPath
     * @throws RegistryException
     * @throws UserStoreException
     * @throws IOException
     */
    public void putProcessRxt(String processRxtPath, String processAssetPath)
            throws RegistryException, UserStoreException, IOException {
        File rxtFile = new File(processRxtPath);

        RegPermissionUtil.setPutPermission(registryService, user, ProcessCenterConstants.PROCESS_ASSET_ROOT);
        Resource processRxt = reg.newResource();
        processRxt.setContentStream(FileUtils.openInputStream(rxtFile));
        processRxt.setMediaType(ProcessCenterConstants.PROCESS_MEDIA_TYPE);
        reg.put(processAssetPath, processRxt);

    }
}
