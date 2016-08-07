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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.core.FolderZiper;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.ProcessStore;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProcessExport {
    private List<String> exportedProcessList;
    private static final Log log = LogFactory.getLog(ProcessExport.class);

    /**
     *
     * @param processName
     * @param processVersion
     * @param exportWithAssociations
     * @param user
     * @return
     * @throws Exception
     */
    public String initiateExportProcess(String processName, String processVersion, String exportWithAssociations,
            String user) throws Exception {
        exportedProcessList = new ArrayList<String>();
        File exportsDir = new File(ProcessCenterConstants.PROCESS_EXPORT_DIR);
        if(exportsDir!= null) {
            exportsDir.mkdirs();
            Boolean exportWithAssociationsBool = Boolean.valueOf(exportWithAssociations);
            try {
                // save details about the exported zip and the core process
                String exportRootPath =
                        ProcessCenterConstants.PROCESS_EXPORT_DIR + "/" + processName + "-" + processVersion +
                                "-PC-Package/";
                new File(exportRootPath).mkdirs();
                exportProcess(exportRootPath, processName, processVersion, exportWithAssociationsBool, user);

                //zip the folder
                FolderZiper folderZiper = new FolderZiper();
                String zipFileName = processName + "-" + processVersion + "-PC-Package.zip";
                folderZiper.zipFolder(exportRootPath, zipFileName);

                //encode zip file
                String encodedZip = encodeFileToBase64Binary(zipFileName);
                //Finally remove the Imports folder
                FileUtils.deleteDirectory(exportsDir);
                return encodedZip;

            } catch (Exception e) {
                String errMsg = "Failed to export process:" + processName + "-" + processVersion;
                log.error(errMsg, e);
                throw new ProcessCenterException(errMsg, e);
            }
        }else {
            String errMsg = "Exports Directory Creation failed..!!! So failed to export process:" + processName + "-" +
                    processVersion;
            throw new ProcessCenterException(errMsg);
        }
    }

    public void exportProcess(String exportRootPath, String processName, String processVersion,
            boolean exportWithAssociations, String user) throws ProcessCenterException {

        if (exportedProcessList.contains(processName + "-" + processVersion)) {
            return;
        }
        exportedProcessList.add(processName + "-" + processVersion);

        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                ProcessStore ps = new ProcessStore();
                String exportProcessPath = exportRootPath + processName + "-" + processVersion + "/";
                new File(exportProcessPath + "documents/").mkdirs();

                //save the process rxt registry entry >> xml
                downloadResource(reg, ProcessCenterConstants.PROCESS_ASSET_ROOT,
                        ProcessCenterConstants.EXPORTED_PROCESS_RXT_FILE, processName, processVersion, "xml",
                        exportProcessPath);
                //save bpmn registry entry >> xml
                downloadResource(reg, ProcessCenterConstants.BPMN_META_DATA_FILE_PATH, ProcessCenterConstants.EXPORTED_BPMN_META_FILE,
                        processName, processVersion, "xml", exportProcessPath);
                //save bpmncontent registry entry >> xml
                downloadResource(reg, ProcessCenterConstants.BPMN_CONTENT_PATH,
                        ProcessCenterConstants.EXPORTED_BPMN_CONTENT_FILE, processName, processVersion, "xml",
                        exportProcessPath);
                //save flowchart registry entry >> json
                downloadResource(reg, ProcessCenterConstants.AUDIT.PROCESS_FLOW_CHART_PATH,
                        ProcessCenterConstants.EXPORTED_FLOW_CHART_FILE, processName, processVersion, "json",
                        exportProcessPath);
                //save processText registry entry
                downloadResource(reg, ProcessCenterConstants.PROCESS_TEXT_PATH,
                        ProcessCenterConstants.EXPORTED_PROCESS_TEXT_FILE, processName, processVersion, "txt",
                        exportProcessPath);

                //save doccontent registry entries >> doc, docx, pdf
                String processResourcePath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                JSONArray jsonArray = new JSONArray(
                        ps.getUploadedDocumentDetails(ProcessCenterConstants.GREG_PATH + processResourcePath));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    if (jsonObj.getString("url").equals("NA")) {
                        String docResourcePath = jsonObj.getString("path");
                        Resource docResource = reg.get(docResourcePath);
                        String[] tempStrArr = docResourcePath.split("/");
                        String docFileName = exportProcessPath + "documents/" + tempStrArr[tempStrArr.length - 1];
                        FileOutputStream docFileOutPutStream = new FileOutputStream(docFileName);
                        IOUtils.copy(docResource.getContentStream(), docFileOutPutStream);
                        docFileOutPutStream.close();
                    }
                }

                //download process image thumbnail
                downloadProcessThumbnailImage(reg, processName, processVersion, exportProcessPath);

                //write process tags in a file
                FileOutputStream tagsFileOutputStream = new FileOutputStream(
                        exportProcessPath + ProcessCenterConstants.PROCESS_TAGS_FILE);
                tagsFileOutputStream.write(ps.getProcessTags(processName, processVersion).getBytes());
                tagsFileOutputStream.close();

                //export process associations
                if (exportWithAssociations) {
                    //write details on process associations (sub-processes/predecessors/successors) in a json file
                    FileOutputStream out = new FileOutputStream(
                            exportProcessPath + ProcessCenterConstants.PROCESS_ASSOCIATIONS_FILE);
                    JSONObject successorPredecessorSubprocessListJSON = new JSONObject(
                            ps.getSucessorPredecessorSubprocessList(
                                    ProcessCenterConstants.GREG_PATH + processResourcePath));
                    out.write(successorPredecessorSubprocessListJSON
                            .toString(ProcessCenterConstants.JSON_FILE_INDENT_FACTOR).getBytes());
                    out.close();

                    exportAssociatedProcesses(successorPredecessorSubprocessListJSON, exportRootPath,
                            exportWithAssociations, "subprocesses", user);
                    exportAssociatedProcesses(successorPredecessorSubprocessListJSON, exportRootPath,
                            exportWithAssociations, "predecessors", user);
                    exportAssociatedProcesses(successorPredecessorSubprocessListJSON, exportRootPath,
                            exportWithAssociations, "successors", user);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to export process:" + processName + "-" + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Export accociated process (sub process/predecessor/successor) - The related files are saved in the respective
     * directory
     *
     * @param successorPredecessorSubprocessListJSON
     * @param exportRootPath
     * @param exportWithAssociations
     * @param assocationType
     * @param user
     * @throws JSONException
     * @throws ProcessCenterException
     */
    public void exportAssociatedProcesses(JSONObject successorPredecessorSubprocessListJSON, String exportRootPath,
            boolean exportWithAssociations, String assocationType, String user)
            throws JSONException, ProcessCenterException {
        JSONArray associatedProcessesJArray = successorPredecessorSubprocessListJSON.getJSONArray(assocationType);

        for (int i = 0; i < associatedProcessesJArray.length(); i++) {
            String processName = associatedProcessesJArray.getJSONObject(i).getString("name");
            String processVersion = associatedProcessesJArray.getJSONObject(i).getString("version");
            exportProcess(exportRootPath, processName, processVersion, exportWithAssociations, user);
        }
    }

    /**
     * Download non-document process related resources (i.e: flow chart, bpmn, process text) for exporting the process
     *
     * @param reg
     * @param resourceRoot
     * @param savingFileName
     * @param processName
     * @param processVersion
     * @param exportedFileType
     * @param exportProcessPath
     * @throws RegistryException
     * @throws IOException
     * @throws JSONException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     */
    public void downloadResource(UserRegistry reg, String resourceRoot, String savingFileName, String processName,
            String processVersion, String exportedFileType, String exportProcessPath)
            throws RegistryException, IOException, JSONException, ParserConfigurationException, SAXException,
            TransformerException {
        String resourcePath = resourceRoot + processName + "/" + processVersion;
        if (reg.resourceExists(resourcePath)) {
            Resource resource = reg.get(resourcePath);
            FileOutputStream fileOutputStream = new FileOutputStream(exportProcessPath + savingFileName);
            if (exportedFileType.equals("json")) {
                String stringContent = IOUtils
                        .toString(resource.getContentStream(), String.valueOf(StandardCharsets.UTF_8));
                //create JSONObject to pretty-print content
                JSONObject contentInJSON = new JSONObject(stringContent);
                fileOutputStream
                        .write(contentInJSON.toString(ProcessCenterConstants.JSON_FILE_INDENT_FACTOR).getBytes());
                fileOutputStream.close();
            } else if (exportedFileType.equals("xml")) {
                IOUtils.copy(resource.getContentStream(), fileOutputStream);
            } else { //.pdf, doc, docx, txt
                IOUtils.copy(resource.getContentStream(), fileOutputStream);
            }
        }
    }

    public String encodeFileToBase64Binary(String fileName) throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * Download process thumbnail image, for process exporting
     * @param reg
     * @param processName
     * @param processVersion
     * @param exportProcessPath
     * @throws RegistryException
     * @throws IOException
     */
    public void downloadProcessThumbnailImage(UserRegistry reg, String processName, String processVersion,
            String exportProcessPath) throws RegistryException, IOException {
        String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                processVersion;
        Resource storedProcess = reg.get(processAssetPath);
        String processId = storedProcess.getUUID();
        String imageResourcePath =
                ProcessCenterConstants.PROCESS_ASSET_RESOURCE_REG_PATH + processId + "/"+ProcessCenterConstants
                        .IMAGE_THUMBNAIL;

        if (reg.resourceExists(imageResourcePath)) {
            Resource resource = reg.get(imageResourcePath);
            FileOutputStream fileOutputStream = new FileOutputStream(exportProcessPath +
                    ProcessCenterConstants.IMAGE_THUMBNAIL);
            IOUtils.copy(resource.getContentStream(), fileOutputStream);
            fileOutputStream.close();
        }
    }
}
