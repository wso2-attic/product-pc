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
package org.wso2.carbon.pc.core.assets;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.common.AssetResource;
import org.wso2.carbon.pc.core.assets.resources.BPMNResource;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class represents Package Asset
 */
public class Package extends AssetResource {

    private static final Log log = LogFactory.getLog(Package.class);

    /**
     * Get Package Registry Path .
     * ex. /_system/governance/packages/PackageName/
     *
     * @param packageName
     * @return
     */
    public static String getPackageRegistryPath(String packageName, String packageVersion) {
        return ProcessCenterConstants.PACKAGES_ASSET_ROOT + packageName + "/" + packageVersion;
    }

    /**
     * Get Package Registry Path gor BPMN .
     * ex. /_system/governance/packages/PackageName/bpmn/PackageVersion/test.bar
     *
     * @param packageName
     * @param packageVersion
     * @return
     */
    public static String getPackageAssetRegistryPath(String packageName, String packageVersion) {
        return ProcessCenterConstants.PROCESS_CENTER_RESOURCES_PATH + ProcessCenterConstants.PACKAGE_ASSET_ROOT +
                packageName + "/" + packageVersion;
    }

    /**
     * Get Package Registry Path gor BPMN .
     * ex. /_system/governance/packages/PackageName/bpmn/PackageVersion/test.bar
     *
     * @param packageAssetPath
     * @return
     */
    public static String getPackageBPMNRegistryPath(String packageAssetPath) {
        return packageAssetPath + "/" + ProcessCenterConstants.BPMN_PATH;
    }

    /**
     * Get Package Registry Path gor BPMN Content
     * ex. /_system/governance/packages/PackageName/bpmn/PackageVersion/bpmncontent/test.bpmn
     *
     * @param packageBPMNRegistryPath
     * @return
     */
    public static String getPackageBPMNContentRegistryPath(String packageBPMNRegistryPath) {
        return packageBPMNRegistryPath + ProcessCenterConstants.BPMN_CONTENT_PATH;
    }

    /**
     * Create new Package
     *
     * @param packageName
     * @param packageVersion
     * @param provider
     * @param description
     * @param createdTime
     * @param tags
     * @param imageFileStream
     * @param packageFileStream
     * @return
     * @throws ProcessCenterException
     */
    public String create(String packageName, String packageVersion, String provider, String description, String
            createdTime, String tags, String packageFileName, InputStream imageFileStream, InputStream
                                 packageFileStream) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        String packageRegistryPath = getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNRegistryPath = getPackageBPMNRegistryPath(getPackageAssetRegistryPath(packageName,
                packageVersion));
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath
                (packageBPMNRegistryPath);
        int bpmnResourcesCount = 0;
        byte[] buffer = new byte[1024];
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(provider);
                if (userRegistry != null) {
                    // Check whether package already exists with same name and version
                    if (userRegistry.resourceExists(packageRegistryPath)) {
                        response.put(ProcessCenterConstants.ERROR, true);
                        response.put(ProcessCenterConstants.MESSAGE, "Package already exists with name " + packageName +
                                " version:" + packageVersion);
                        return response.toString();
                    }
                    // Add bpmn package archive file
                    // Remove the bpmn bar file directory if exist
                    if (userRegistry.resourceExists(packageBPMNRegistryPath)) {
                        userRegistry.delete(packageBPMNRegistryPath);
                    }
                    bpmnResourcesCount = 0;
                    Resource packageZipContentResource = userRegistry.newResource();
                    packageZipContentResource.setContentStream(packageFileStream);
                    userRegistry.put(packageBPMNRegistryPath + "/" + packageFileName,
                            packageZipContentResource);
                    InputStream inputStream = packageZipContentResource.getContentStream();
                    if (inputStream != null) {
                        try (final ZipInputStream packageZIPInoutStream = new ZipInputStream(inputStream)) {
                            ZipEntry zipEntry = packageZIPInoutStream.getNextEntry();
                            while (zipEntry != null) {
                                if (!zipEntry.isDirectory() && isBpmnResource(zipEntry.getName())) {
                                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                                        int len;
                                        while ((len = packageZIPInoutStream.read(buffer)) > 0) {
                                            byteArrayOutputStream.write(buffer, 0, len);
                                        }
                                        String bpmnProcessID = null;
                                        Document bpmnResource = getXMLDocument(byteArrayOutputStream
                                                .toByteArray());
                                        if (bpmnResource != null) {
                                            NodeList processElement = bpmnResource.getElementsByTagName
                                                    (ProcessCenterConstants.PROCESS);
                                            if (processElement.getLength() > 0) {
                                                Node processNameAttribute = processElement.item(0).getAttributes()
                                                        .getNamedItem(ProcessCenterConstants.NAME);
                                                if (processNameAttribute != null) {
                                                    bpmnProcessID = processNameAttribute.getNodeValue();
                                                }
                                            }
                                        }
                                        if (bpmnProcessID != null) {
                                            Resource packageBPMNContentResource = userRegistry.newResource();
                                            packageBPMNContentResource.setProperty(ProcessCenterConstants.PROCESS_NAME,
                                                    bpmnProcessID);
                                            packageBPMNContentResource.setContent(byteArrayOutputStream.toByteArray());
                                            userRegistry.put(packageBPMNContentRegistryPath + "/" + zipEntry.getName(),
                                                    packageBPMNContentResource);
                                            bpmnResourcesCount++;
                                        } else if (log.isDebugEnabled()) {
                                            log.debug("Process ID cannot be found for bpmn resource " + zipEntry
                                                    .getName() + "to create new package " +
                                                    packageName + " version " + packageVersion);
                                        }
                                    } catch (SAXException e) {
                                        throw new ProcessCenterException("Error while getting process id from bpmn file"
                                                + zipEntry.getName(), e);
                                    }
                                }
                                zipEntry = packageZIPInoutStream.getNextEntry();
                            }
                            packageZIPInoutStream.closeEntry();
                        }
                        if (bpmnResourcesCount == 0) {
                            // If bar does not contain any bpmn files we will send invalidate message
                            response.put("error", true);
                            response.put("message", "Package file doesn't contain any bpmn files");
                            return response.toString();
                        }

                    }

                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                    // root elements
                    Document doc = docBuilder.newDocument();

                    Element rootElement = doc.createElementNS(ProcessCenterConstants.METADATA_NAMESPACE,
                            ProcessCenterConstants.METADATA);
                    doc.appendChild(rootElement);

                    Element overviewElement = append(doc, rootElement, ProcessCenterConstants.OVERVIEW,
                            ProcessCenterConstants.METADATA_NAMESPACE);
                    appendText(doc, overviewElement, ProcessCenterConstants.NAME, ProcessCenterConstants
                            .METADATA_NAMESPACE, packageName);
                    appendText(doc, overviewElement, ProcessCenterConstants.VERSION, ProcessCenterConstants
                            .METADATA_NAMESPACE, packageVersion);
                    appendText(doc, overviewElement, ProcessCenterConstants.PROVIDER, ProcessCenterConstants
                            .METADATA_NAMESPACE, provider);
                    appendText(doc, overviewElement, ProcessCenterConstants.CREATED_TIME, ProcessCenterConstants
                            .METADATA_NAMESPACE, createdTime);

                    if ((description != null) && (!description.isEmpty())) {
                        appendText(doc, overviewElement, ProcessCenterConstants.DESCRIPTION, ProcessCenterConstants
                                .METADATA_NAMESPACE, description);
                    } else {
                        appendText(doc, overviewElement, ProcessCenterConstants.DESCRIPTION, ProcessCenterConstants
                                .METADATA_NAMESPACE, ProcessCenterConstants
                                .NOT_APPLICABLE);
                    }
                    if (imageFileStream != null) {
                        Element imageElement = append(doc, rootElement, ProcessCenterConstants.IMAGES,
                                ProcessCenterConstants.METADATA_NAMESPACE);
                        appendText(doc, imageElement, ProcessCenterConstants.THUMBNAIL, ProcessCenterConstants
                                .METADATA_NAMESPACE, ProcessCenterConstants
                                .IMAGE_THUMBNAIL_VALUE);
                    }
                    String packageAssetContent = xmlToString(doc);
                    Resource packageAsset = userRegistry.newResource();
                    packageAsset.setContent(packageAssetContent);
                    packageAsset.setMediaType(ProcessCenterConstants.PROCESS_CONTENT_SEARCH.PACKAGE_MEDIATYPE);
                    packageAsset.setProperty(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME, packageFileName);
                    packageAsset.setProperty(ProcessCenterConstants.BPMN_RESOURCES_COUNT, String.valueOf
                            (bpmnResourcesCount));
                    userRegistry.put(packageRegistryPath, packageAsset);
                    // associate lifecycle with the package asset, so that it can be promoted to published state
                    GovernanceUtils.associateAspect(packageRegistryPath, ProcessCenterConstants
                            .SAMPLE_LIFECYCLE2_NAME, userRegistry);

                    // apply tags to the resource
                    String[] tagsList = tags.split(",");
                    for (String tag : tagsList) {
                        tag = tag.trim();
                        userRegistry.applyTag(packageRegistryPath, tag);
                    }
                    Resource storedPackage = userRegistry.get(packageRegistryPath);
                    if (storedPackage == null) {
                        throw new ProcessCenterException("Couldn't find the package in the registry for path " +
                                packageRegistryPath);
                    }
                    String packageId = storedPackage.getUUID();
                    String imageRegPath = ProcessCenterConstants.IMAGE_PATH_PACKAGE + packageId + "/" +
                            ProcessCenterConstants.IMAGE_THUMBNAIL_VALUE;

                    // Add image thumbnail file
                    if (imageFileStream != null) {
                        Resource imageContentResource = userRegistry.newResource();
                        imageContentResource.setContent(imageFileStream);
                        userRegistry.put(imageRegPath, imageContentResource);
                    }

                    // set package Permission
                    setPermission(ProcessCenterConstants.GREG_PATH + ProcessCenterConstants.PACKAGES_ASSET_ROOT,
                            provider,
                            packageName, packageVersion);
                    response.put(ProcessCenterConstants.ERROR, false);
                    response.put(ProcessCenterConstants.ID, packageId);
                    response.put(ProcessCenterConstants.NAME, packageName);
                }
            }
        } catch (ProcessCenterException e) {
            String errMsg = "Create package error: " + packageName + " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (TransformerException e) {
            String errMsg = "Error occurred while parsing xml to string to create new package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (ParserConfigurationException e) {
            String errMsg = "Error occurred while generating asset document to create new package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (RegistryException e) {
            String errMsg = "Error occurred while accessing registry for creating package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException e) {
            String errMsg = "Error occurred while accessing package archive file for creating package " + packageName +
                    " file " + packageFileName;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException e) {
            String errMsg = "JSON Error occurred while creating package  " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
        }
        return response.toString();
    }

    /**
     * Delete Package
     *
     * @param packageName    Name of the package
     * @param packageVersion Version of the package
     * @param provider       Provider of the package
     * @return
     * @throws ProcessCenterException
     */
    public void delete(String packageName, String packageVersion, String provider) throws
            ProcessCenterException {

        String packageAssetRegistryPath = getPackageAssetRegistryPath(packageName,
                packageVersion);
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(provider);
                if (userRegistry != null) {

                    // Remove the bpmn bar file directory if exist
                    if (userRegistry.resourceExists(packageAssetRegistryPath)) {
                        userRegistry.delete(packageAssetRegistryPath);
                        log.info("Deleting bpmn path");
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Error occurred while accessing registry for deleting package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Get list of  BPMN resources related to the Package
     *
     * @param packageName
     * @param packageVersion
     * @param userName
     * @return
     * @throws ProcessCenterException
     */
    public String getBpmnResources(String packageName, String packageVersion, String userName) throws
            ProcessCenterException {

        JSONArray bpmnResources = new JSONArray();
        String packageRegistryPath = getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath
                (getPackageBPMNRegistryPath(getPackageAssetRegistryPath(packageName, packageVersion)));
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(userName);
                Resource packageAsset = userRegistry.get(packageRegistryPath);
                if (packageAsset != null) {
                    String bpmnResourceCount = packageAsset.getProperty(ProcessCenterConstants
                            .BPMN_RESOURCES_COUNT);
                    if (bpmnResourceCount != null) {
                        // Get bpmn file registry registry collection
                        Collection bpmnResourceCollection = userRegistry.get(packageBPMNContentRegistryPath, 0,
                                Integer.parseInt(bpmnResourceCount));
                        if (bpmnResourceCollection != null) {
                            String[] bpmnResourcePaths = (String[]) bpmnResourceCollection.getContent();
                            // Read each bpmn file
                            for (String bpmnResourcePath : bpmnResourcePaths) {
                                Resource bpmnRegistryResource = userRegistry.get(bpmnResourcePath);
                                if (bpmnRegistryResource != null) {
                                    JSONObject bpmnResource = new JSONObject();
                                    Association[] processAssociations = userRegistry.getAssociations(bpmnResourcePath,
                                            ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
                                    if (processAssociations != null && processAssociations.length > 0)
                                        if (userRegistry.resourceExists(processAssociations[0]
                                                .getDestinationPath())) {
                                            Resource processAsset = userRegistry.get(processAssociations[0]
                                                    .getDestinationPath());
                                            String processContent = new String((byte[]) processAsset.getContent(),
                                                    StandardCharsets.UTF_8);
                                            Document processDocument = stringToXML(processContent);
                                            if (processDocument != null) {
                                                NodeList processAssetName = processDocument.getElementsByTagName
                                                        (ProcessCenterConstants.NAME);
                                                if (processAssetName.getLength() > 0) {
                                                    bpmnResource.put(ProcessCenterConstants.PROCESS_ASSET_NAME,
                                                            processAssetName
                                                                    .item(0).getTextContent());
                                                }
                                                NodeList processAssetVersion = processDocument.getElementsByTagName
                                                        (ProcessCenterConstants.VERSION);
                                                if (processAssetVersion.getLength() > 0) {
                                                    bpmnResource.put(ProcessCenterConstants.PROCESS_ASSET_VERSION,
                                                            processAssetVersion
                                                                    .item(0).getTextContent());
                                                }
                                            }
                                        }
                                    String bpmnProcessName = bpmnRegistryResource.getProperty
                                            (ProcessCenterConstants.PROCESS_NAME);
                                    if (bpmnProcessName != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_NAME, bpmnProcessName);
                                    }
                                    bpmnResource.put(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME,
                                            bpmnRegistryResource.getPath()
                                                    .replaceFirst
                                                            ("/" + packageBPMNContentRegistryPath, ""));
                                    bpmnResources.put(bpmnResource);
                                }
                            }
                        }
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry Error occurred while getting bpmn resources for package: " + packageName + " " +
                    "version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException e) {
            String errMsg = "Error with JSON operation while getting bpmn resources for package: " + packageName + " " +
                    "version" + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            String errMsg = "Error occurred while getting bpmn resources for package " + packageName +
                    " file " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }

        return bpmnResources.toString();
    }

    /**
     * Add process association to given process
     *
     * @param processName
     * @param processVersion
     * @param packageName
     * @param packageVersion
     * @param bpmnResourcePath
     * @param userName
     * @throws ProcessCenterException
     */
    public void associateProcess(String processName, String processVersion, String packageName, String
            packageVersion, String bpmnResourcePath, String userName)
            throws ProcessCenterException {

        String processRegistryPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath(getPackageBPMNRegistryPath
                (getPackageAssetRegistryPath(packageName, packageVersion))) + bpmnResourcePath;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(userName);
                // Remove existing prcoess associations in bpmn resource. We should have only one association for bpmn
                // resource with a process.
                Association[] bpmnAssociations = userRegistry.getAssociations(packageBPMNContentRegistryPath,
                        ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
                if (bpmnAssociations != null && bpmnAssociations.length > 0) {
                    for (Association bpmnAssociation : bpmnAssociations) {
                        userRegistry.removeAssociation(bpmnAssociation.getSourcePath(), bpmnAssociation
                                .getDestinationPath(), bpmnAssociation.getAssociationType());
                    }
                }
                // Remove existing package associations in given process. We should have only one association for
                // process with a bpmn resource in a package.
                Association[] processAssociations = userRegistry.getAssociations(processRegistryPath,
                        ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
                if (processAssociations != null && processAssociations.length > 0) {
                    for (Association processAssociation : processAssociations) {
                        userRegistry.removeAssociation(processAssociation.getSourcePath(), processAssociation
                                .getDestinationPath(), processAssociation.getAssociationType());
                    }
                }

                // Add association to bpmn resource with given process and process to bpmn resource.
                userRegistry.addAssociation(packageBPMNContentRegistryPath, processRegistryPath,
                        ProcessCenterConstants.PACKAGE_PROCESS_ASSOCIATION);
            }
        } catch (RegistryException e) {
            String errMsg = "Error occurred while associating process " + processName + " version " + processVersion +
                    " to package: " + packageName + " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Get BPMN Image
     *
     * @param packageName
     * @param packageVersion
     * @param bpmnResourcePath
     * @throws ProcessCenterException
     */
    public String getBpmnImage(String packageName, String packageVersion, String bpmnResourcePath) throws
            ProcessCenterException {

        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath
                (getPackageBPMNRegistryPath(getPackageAssetRegistryPath(packageName, packageVersion))) +
                bpmnResourcePath;
        BPMNResource bpmnResource = new BPMNResource();
        try {
            return bpmnResource.getEncodedBPMNImage(packageBPMNContentRegistryPath);
        } catch (ProcessCenterException e) {
            String errMsg = "Error occurred while getting bpmn image for package: " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Check whether the given xml is BPMN resource
     *
     * @param resourceName
     * @return
     */
    protected boolean isBpmnResource(String resourceName) {
        for (String suffix : ProcessCenterConstants.BPMN_RESOURCE_SUFFIXES) {
            if (resourceName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
