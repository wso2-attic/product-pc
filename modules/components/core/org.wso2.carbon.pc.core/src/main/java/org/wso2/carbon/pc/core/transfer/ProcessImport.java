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
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.api.UserStoreException;
import org.xml.sax.SAXException;

import javax.ws.rs.core.MediaType;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Import a process zip archive to the WSO2 Process Center
 */
public class ProcessImport {
    private static final Log log = LogFactory.getLog(ProcessImport.class);
    File[] listOfProcessDirs;
    private RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
    private UserRegistry reg;
    private String user;
    private static final int BYTE_ARRAY_BUFFER_LENGTH = 2048;

    /**
     * Import the process
     *
     * @param processZipInputStream process ZIP input stream
     * @param user                  username
     * @throws IOException
     * @throws RegistryException
     * @throws ProcessCenterException
     * @throws UserStoreException
     */
    public String importProcesses(InputStream processZipInputStream, String user)
            throws IOException, RegistryException, ProcessCenterException, UserStoreException, JSONException,
            TransformerException, SAXException, ParserConfigurationException {

        JSONObject response = new JSONObject();
        if (registryService != null) {
            reg = registryService.getGovernanceUserRegistry(user);
            this.user = user;
            //extract zip file stream to the system disk
            byte[] buffer = new byte[BYTE_ARRAY_BUFFER_LENGTH];
            ZipInputStream zipInputStream = new ZipInputStream(processZipInputStream);
            //File importsDir = new File(ProcessCenterConstants.IMPORTS_DIR);
            Path importsDirPath = Paths.get(ProcessCenterConstants.IMPORTS_DIR);
            try {
                Files.createDirectories(importsDirPath);
                try {
                    ZipEntry entry;
                    while ((entry = zipInputStream.getNextEntry()) != null) {
                        String outpath = ProcessCenterConstants.IMPORTS_DIR + File.separator + entry.getName();
                        String dirPath = outpath.substring(0, outpath.lastIndexOf(File.separator));
                        Path directoryPath = Paths.get(dirPath);
                        Files.createDirectories(directoryPath);
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

                File folder = new File(ProcessCenterConstants.IMPORTS_DIR);
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null) {
                    if (listOfFiles[0].isDirectory() && listOfFiles.length == 1) {
                        String zipHomeDirectoryName = listOfFiles[0].getPath();
                        File zipFolder = new File(zipHomeDirectoryName);
                        listOfProcessDirs = zipFolder.listFiles();
                    }
                }

                //else do the process importing for each process
                for (File processDir : listOfProcessDirs) {
                    if (processDir.isDirectory()) {
                        String processDirName = processDir.getName();
                        String processDirPath = processDir.getPath();
                        String processRxtPath =
                                processDirPath + File.separator + ProcessCenterConstants.EXPORTED_PROCESS_RXT_FILE;
                        String processName = processDirName.substring(0, processDirName.lastIndexOf("-"));
                        String processVersion = processDirName
                                .substring(processDirName.lastIndexOf("-") + 1, processDirName.length());
                        String processAssetPath =
                                ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + File.separator +
                                        processVersion;

                        if (registryService != null) {
                            UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                            String processPath =
                                    ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + File.separator
                                            + processVersion;
                            // Check whether process already exists with same name and version
                            if (!reg.resourceExists(processPath)) {
                                putProcessRxt(processRxtPath, processAssetPath);
                                GovernanceUtils.associateAspect(processAssetPath, ProcessCenterConstants.
                                        DEFAULT_LIFECYCLE_NAME, reg);
                                setImageThumbnail(processDirPath, processAssetPath);
                                setProcessDocuments(processName, processVersion, processDirPath, processAssetPath);
                                setProcessTags(processDirPath, processAssetPath);
                                setProcessText(processName, processVersion, processDirPath, processAssetPath);
                                setBPMN(processName, processVersion, processDirPath, processAssetPath);
                                setFlowChart(processName, processVersion, processDirPath, processAssetPath);
                                setProcessAssociations(processName, processVersion, processDirPath, processAssetPath);
                            }
                        }
                    }
                }
            } finally {
                //Finally remove the Imports folder
                if (new File(ProcessCenterConstants.IMPORTS_DIR).exists()) {
                    FileUtils.deleteDirectory(new File(ProcessCenterConstants.IMPORTS_DIR));
                }
            }
        } else {
            String errMsg = "Process Importing failed due to unavailability of Registry Service";
            throw new ProcessCenterException(errMsg);
        }
        return response.toString();
    }

    /**
     * Add the process associations (sub processes, successors, predecessors) to the registry
     *
     * @param processName      name of the process
     * @param processVersion   process version
     * @param processDirPath   process directory path
     * @param processAssetPath process path
     * @throws ProcessCenterException
     */
    private void setProcessAssociations(String processName, String processVersion, String processDirPath,
            String processAssetPath) throws ProcessCenterException {
        try {
            Path procAssociationsJSONFilePath = Paths
                    .get(processDirPath + File.separator + ProcessCenterConstants.PROCESS_ASSOCIATIONS_FILE);
            if (Files.exists(procAssociationsJSONFilePath)) {
                String procAssociationsFileContent = "";
                try (BufferedReader reader = Files.newBufferedReader(procAssociationsJSONFilePath, Charsets.US_ASCII)) {
                    String line;
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
        } catch (IOException | RegistryException | JSONException e) {
            String errMsg =
                    "Error in setting process associations for the process :" + processName + "-" + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Add Predecessor Association to registry
     *
     * @param processAssetPath path of the process asset source path
     * @param predecessorPath  path of the predecessor asset path
     * @throws RegistryException
     */
    private void addPredecessorAssociation(String processAssetPath, String predecessorPath) throws RegistryException {
        reg.addAssociation(processAssetPath, predecessorPath, ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
        reg.addAssociation(predecessorPath, processAssetPath, ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
    }

    /**
     * Add Sub Process Association to registry
     *
     * @param processAssetPath path of the process asset source path
     * @param subProcessPath   path of the sub process asset path
     * @throws RegistryException
     */
    private void addSubProcessAssociation(String processAssetPath, String subProcessPath) throws RegistryException {
        reg.addAssociation(processAssetPath, subProcessPath, ProcessCenterConstants.SUBPROCESS_ASSOCIATION);
        reg.addAssociation(subProcessPath, processAssetPath, ProcessCenterConstants.PARENTPROCESS_ASSOCIATION);
    }

    /**
     * Add Successor Association to registry
     *
     * @param processAssetPath path of the process asset source path
     * @param successorPath    path of the successor asset path
     * @throws RegistryException
     */
    private void addSuccessorAssociation(String processAssetPath, String successorPath) throws RegistryException {
        reg.addAssociation(processAssetPath, successorPath, ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
        reg.addAssociation(successorPath, processAssetPath, ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
    }

    /**
     * Add the flow chart of imported process into the registry
     *
     * @param processName      process name
     * @param processVersion   process version
     * @param processDirPath   process directory path
     * @param processAssetPath process path
     * @throws UserStoreException
     * @throws RegistryException
     * @throws IOException
     */
    private void setFlowChart(String processName, String processVersion, String processDirPath, String processAssetPath)
            throws UserStoreException, RegistryException, IOException {
        UserRegistry reg = registryService.getGovernanceUserRegistry(user);
        RegPermissionUtil.setPutPermission(registryService, user, ProcessCenterConstants.AUDIT.PROCESS_FLOW_CHART_PATH);
        Resource flowchartContentResource = reg.newResource();
        Path flowChartJSONFilePath = Paths
                .get(processDirPath + File.separator + ProcessCenterConstants.EXPORTED_FLOW_CHART_FILE);
        if (Files.exists(flowChartJSONFilePath)) {
            String flowChartFileContent = "";
            try (BufferedReader reader = Files.newBufferedReader(flowChartJSONFilePath, Charsets.US_ASCII)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    flowChartFileContent += line;
                }
            } catch (IOException e) {
                String errMsg = "Error in reading process's flowChart.json file";
                throw new IOException(errMsg, e);
            }

            flowchartContentResource.setContent(flowChartFileContent);
            flowchartContentResource.setMediaType(MediaType.APPLICATION_JSON);
            String flowchartContentPath =
                    ProcessCenterConstants.FLOW_CHART + File.separator + processName + File.separator +
                            processVersion;
            reg.put(flowchartContentPath, flowchartContentResource);
            //add reg association
            reg.addAssociation(flowchartContentPath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
        }
    }

    /**
     * Add the BPMN file of the imported process into the registry
     *
     * @param processName      process name
     * @param processVersion   process version
     * @param processDirPath   process directory path
     * @param processAssetPath process path
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws RegistryException
     */
    private void setBPMN(String processName, String processVersion, String processDirPath, String processAssetPath)
            throws IOException, SAXException, ParserConfigurationException, TransformerException, RegistryException {

        String bpmnFilePathStr = processDirPath + File.separator + ProcessCenterConstants.EXPORTED_BPMN_CONTENT_FILE;
        Path bpmnFilePath = Paths.get(bpmnFilePathStr);
        String bpmnMetaDataFilePathStr =
                processDirPath + File.separator + ProcessCenterConstants.EXPORTED_BPMN_META_FILE;
        Path bpmnMetaDataFilePath = Paths.get(bpmnMetaDataFilePathStr);

        if (Files.exists(bpmnFilePath) && Files.exists(bpmnMetaDataFilePath)) {
            //set bpmn content file
            File bpmnXMLFile = new File(bpmnFilePathStr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(bpmnXMLFile);
            String bpmnFileContent = Utils.xmlToString(doc);
            String bpmnContentResourcePath = ProcessCenterConstants.BPMN_CONTENT_PATH + processName +
                    File.separator + processVersion;
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
            String bpmnMetaDataContent = Utils.xmlToString(bpmnMetaDataDoc);
            Resource bpmnMetaDataResource = reg.newResource();
            bpmnMetaDataResource.setContent(bpmnMetaDataContent);
            bpmnMetaDataResource.setMediaType(ProcessCenterConstants.WSO2_BPMN_ASSET_MEDIA_TYPE);
            String bpmnMetaDataResPath =
                    ProcessCenterConstants.BPMN_META_DATA_FILE_PATH + processName + File.separator + processVersion;
            reg.put(bpmnMetaDataResPath, bpmnMetaDataResource);
        }
    }

    /**
     * Add the process text of the imported process into the registry
     *
     * @param processName      process name
     * @param processVersion   process version
     * @param processDirPath   process directory path
     * @param processAssetPath process path
     * @throws IOException
     * @throws RegistryException
     */
    private void setProcessText(String processName, String processVersion, String processDirPath,
            String processAssetPath) throws IOException, RegistryException {
        Path processTextFilePath = Paths
                .get(processDirPath + File.separator + ProcessCenterConstants.EXPORTED_PROCESS_TEXT_FILE);
        if (Files.exists(processTextFilePath)) {
            String processTextFileContent = null;
            try (BufferedReader reader = Files.newBufferedReader(processTextFilePath, Charsets.US_ASCII)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processTextFileContent += line;
                }
            } catch (IOException e) {
                String errMsg = "Error in reading process tags file";
                throw new IOException(errMsg, e);
            }

            // store process text as a separate resource
            String processTextResourcePath =
                    ProcessCenterConstants.PROCESS_TEXT_PATH + processName + File.separator + processVersion;
            if (processTextFileContent != null && processTextFileContent.length() > 0) {
                Resource processTextResource = reg.newResource();
                processTextResource.setContent(processTextFileContent);
                processTextResource.setMediaType(MediaType.TEXT_HTML);
                reg.put(processTextResourcePath, processTextResource);
                reg.addAssociation(processTextResourcePath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
            }
        }
    }

    /**
     * Add the process tags of the imported process to registry
     *
     * @param processDirPath   process directory path
     * @param processAssetPath process path
     * @throws IOException
     * @throws RegistryException
     */
    private void setProcessTags(String processDirPath, String processAssetPath) throws IOException, RegistryException {
        Path processTagsFilePath = Paths
                .get(processDirPath + File.separator + ProcessCenterConstants.PROCESS_TAGS_FILE);
        if (Files.exists(processTagsFilePath)) {
            String tagsFileContent = "";
            try (BufferedReader reader = Files.newBufferedReader(processTagsFilePath, Charsets.US_ASCII)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    tagsFileContent += line;
                }
            } catch (IOException e) {
                String errMsg = "Error in reading process tags file";
                throw new IOException(errMsg, e);
            }

            String[] tags = tagsFileContent.split(ProcessCenterConstants.TAGS_FILE_TAG_SEPARATOR);
            for (String tag : tags) {
                if (tag.length() > 0) {
                    reg.applyTag(processAssetPath, tag);
                }
            }
        }
    }

    /**
     * Add the documents (pdf, ms-word) of the imported process to registry
     *
     * @param processName      process name
     * @param processVersion   process version
     * @param processDirPath   process directory path
     * @param processAssetPath process path
     * @throws ProcessCenterException
     * @throws JSONException
     * @throws RegistryException
     * @throws IOException
     * @throws SecurityException
     */
    private void setProcessDocuments(String processName, String processVersion, String processDirPath,
            String processAssetPath)
            throws ProcessCenterException, JSONException, RegistryException, IOException, SecurityException {

        File docsFolder = new File(processDirPath + File.separator + ProcessCenterConstants.PROCESS_ZIP_DOCUMENTS_DIR);
        if (docsFolder.exists()) {
            File[] docFiles = docsFolder.listFiles();
            for (File docFile : docFiles) {
                if (docFile.isFile()) {
                    String fileName = docFile.getName();

                    String fileExt = FilenameUtils.getExtension(docFile.getPath());
                    String docResourcePath =
                            ProcessCenterConstants.DOC_CONTENT_PATH + processName + File.separator + processVersion
                                    + File.separator +
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
     * Add the process thumbnail image of the imported process into the registry
     *
     * @param processDirPath   process directory path
     * @param processAssetPath process oath
     * @throws RegistryException
     * @throws IOException
     */
    private void setImageThumbnail(String processDirPath, String processAssetPath)
            throws RegistryException, IOException {
        Resource storedProcess = reg.get(processAssetPath);
        String processId = storedProcess.getUUID();

        String imageResourcePath = ProcessCenterConstants.PROCESS_ASSET_RESOURCE_REG_PATH + processId + File.separator
                + ProcessCenterConstants.IMAGE_THUMBNAIL;

        Resource imageContentResource = reg.newResource();

        File imageThumbnailFile = new File(processDirPath + File.separator + ProcessCenterConstants.IMAGE_THUMBNAIL);
        //Avoid trying to read non-existing thumbnail file
        if (imageThumbnailFile.exists() && !imageThumbnailFile.isDirectory()) {
            byte[] imageContent = Files.readAllBytes(imageThumbnailFile.toPath());
            imageContentResource.setContent(imageContent);
            reg.put(imageResourcePath, imageContentResource);
        }
    }

    /**
     * Get a list of processes
     *
     * @return processList List of processes
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
                String processName = processPath.split(File.separator)[2];
                String processVersion = processPath.split(File.separator)[3];
                processList.add(processName + "-" + processVersion);
            }
        } else {
            String msg = "Registry service not available for retrieving processes.";
            throw new ProcessCenterException(msg);
        }
        return processList;
    }

    /**
     * Add the rxt file of imported process into the registry
     *
     * @param processRxtPath   process rxt path
     * @param processAssetPath process path
     * @throws RegistryException
     * @throws UserStoreException
     * @throws IOException
     */
    private void putProcessRxt(String processRxtPath, String processAssetPath)
            throws RegistryException, UserStoreException, IOException {
        File rxtFile = new File(processRxtPath);

        RegPermissionUtil.setPutPermission(registryService, user, ProcessCenterConstants.PROCESS_ASSET_ROOT);
        Resource processRxt = reg.newResource();
        processRxt.setContentStream(FileUtils.openInputStream(rxtFile));
        processRxt.setMediaType(ProcessCenterConstants.PROCESS_MEDIA_TYPE);
        reg.put(processAssetPath, processRxt);
    }
}
