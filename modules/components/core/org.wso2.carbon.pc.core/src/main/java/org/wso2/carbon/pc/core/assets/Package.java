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

import org.apache.commons.collections.CollectionUtils;
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
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.assets.common.AssetResource;
import org.wso2.carbon.pc.core.assets.common.BPMNResource;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
        return packageAssetPath + "/" + ProcessCenterConstants.BPMN_META_DATA_FILE_PATH;
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
     * @param packageName       Name of the Package
     * @param packageVersion    - Version of the Package
     * @param username          - Provider of the Package
     * @param description       - Package description
     * @param createdTime       - Create time of the Package
     * @param tags              -Tags associated with Package
     * @param imageFileStream   - Image file stream
     * @param packageFileStream - Deploy-able archive file stream
     * @return
     * @throws ProcessCenterException
     */
    public String create(String packageName, String packageVersion, String username, String description, String
            createdTime, String tags, String packageFileName, InputStream imageFileStream, InputStream
                                 packageFileStream) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        String packageRegistryPath = getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNRegistryPath = getPackageBPMNRegistryPath(getPackageAssetRegistryPath(packageName,
                packageVersion));
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath
                (packageBPMNRegistryPath);
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
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
                    /* Add bpmn archive to the registry
                    Extract the bpmn archive and add bpmn files to registry
                    */
                    Resource packageZipContentResource = userRegistry.newResource();
                    packageZipContentResource.setContentStream(packageFileStream);
                    packageZipContentResource.setMediaType(ProcessCenterConstants.ZIP_MEDIA_TYPE);
                    userRegistry.put(packageBPMNRegistryPath + "/" + packageFileName,
                            packageZipContentResource);
                    InputStream inputStream = packageZipContentResource.getContentStream();
                    List<String> newBpmnResources = extractPackageAndStoreBPMNFiles(userRegistry,
                            packageBPMNContentRegistryPath,inputStream,packageName,packageVersion);

                    // If bar does not contain any bpmn files we will send invalidate message
                    if (newBpmnResources.size() == 0) {
                        if (userRegistry.resourceExists(packageBPMNRegistryPath)) {
                            userRegistry.delete(packageBPMNRegistryPath);
                        }
                        response.put(ProcessCenterConstants.ERROR, true);
                        response.put(ProcessCenterConstants.MESSAGE, "Package file doesn't contain any bpmn files");
                        return response.toString();
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
                            .METADATA_NAMESPACE, username);
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

                    Element imageElement = append(doc, rootElement, ProcessCenterConstants.IMAGES,
                            ProcessCenterConstants.METADATA_NAMESPACE);

                    if (imageFileStream != null) {
                        appendText(doc, imageElement, ProcessCenterConstants.THUMBNAIL, ProcessCenterConstants
                                .METADATA_NAMESPACE, ProcessCenterConstants
                                .IMAGE_THUMBNAIL);
                    } else {
                        appendText(doc, imageElement, ProcessCenterConstants.THUMBNAIL, ProcessCenterConstants
                                .METADATA_NAMESPACE, ProcessCenterConstants
                                .NO_FILE_SPECIFIED);
                    }


                    String packageAssetContent = xmlToString(doc);
                    String packageFileChecksum = Utils.getMD5Checksum(packageZipContentResource.getContentStream());

                    Resource packageAsset = userRegistry.newResource();
                    packageAsset.setContent(packageAssetContent);
                    packageAsset.setMediaType(ProcessCenterConstants.PACKAGE_MEDIA_TYPE);
                    packageAsset.setProperty(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME, packageFileName);
                    packageAsset.setProperty(ProcessCenterConstants.BPMN_RESOURCES_COUNT, String.valueOf
                            (newBpmnResources.size()));
                    packageAsset.setProperty(ProcessCenterConstants.CHECKSUM,packageFileChecksum);
                    userRegistry.put(packageRegistryPath, packageAsset);

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
                            ProcessCenterConstants.IMAGE_THUMBNAIL;

                    // Add image thumbnail file
                    if (imageFileStream != null) {
                        Resource imageContentResource = userRegistry.newResource();
                        imageContentResource.setContent(imageFileStream);
                        userRegistry.put(imageRegPath, imageContentResource);
                    }

                    // set package Permission
                    setPermission(ProcessCenterConstants.GREG_PATH + ProcessCenterConstants.PACKAGES_ASSET_ROOT,
                            username,
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
        } catch (NoSuchAlgorithmException | IOException e) {
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
     * Create new Package
     *
     * @param packageName        Name of the Package
     * @param packageVersion     - Version of the Package
     * @param username           - Provider of the Package
     * @param description        - Package description
     * @param newPackageFileName - Package File name
     * @param imageFileStream    - Image file stream
     * @param packageFileStream  - Deploy-able archive file stream
     * @return
     * @throws ProcessCenterException
     */
    public String update(String packageName, String packageVersion, String username, String description, String
            newPackageFileName, InputStream imageFileStream, InputStream
                                 packageFileStream) throws
            ProcessCenterException {

        JSONObject response = new JSONObject();
        String packageRegistryPath = getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNRegistryPath = getPackageBPMNRegistryPath(getPackageAssetRegistryPath(packageName,
                packageVersion));
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath
                (packageBPMNRegistryPath);

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {
                    // Check whether package exists with same name and version
                    if (!userRegistry.resourceExists(packageRegistryPath)) {
                        response.put(ProcessCenterConstants.ERROR, true);
                        response.put(ProcessCenterConstants.MESSAGE, "Package does not exists with name " +
                                packageName + " version:" + packageVersion);
                        return response.toString();
                    }
                    Resource packageAsset = userRegistry.get(packageRegistryPath);
                     /*
                     Update bpmn archive to the registry
                     Extract the bpmn archive and add bpmn files to registry
                    */
                    if (newPackageFileName != null && packageFileStream != null) {

                        String packageFileName = packageAsset.getProperty(ProcessCenterConstants
                                .PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                        String bpmnResourceCount = packageAsset.getProperty(ProcessCenterConstants
                                .BPMN_RESOURCES_COUNT);
                        // Store existing bpmn resource paths
                        String[] existingBpmnResourcePaths = null;
                        // Store new bpmn resource paths

                        if (bpmnResourceCount != null) {
                            // Get bpmn file registry registry collection
                            Collection bpmnResourceCollection = userRegistry.get(packageBPMNContentRegistryPath, 0,
                                    Integer.parseInt(bpmnResourceCount));
                            if (bpmnResourceCollection != null) {
                                existingBpmnResourcePaths = (String[]) bpmnResourceCollection.getContent();
                            }
                        }

                        byte[] buffer = new byte[1024];
                        InputStream packageInputStream, packageInputStreamCopy;
                        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
                            int len;
                            while ((len = packageFileStream.read(buffer)) > -1) {
                                arrayOutputStream.write(buffer, 0, len);
                            }
                            arrayOutputStream.flush();
                            packageInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
                            packageInputStreamCopy = new ByteArrayInputStream(arrayOutputStream.toByteArray());
                        }

                        List<String> bpmnResources = extractPackageAndStoreBPMNFiles(userRegistry,
                                packageBPMNContentRegistryPath, packageInputStream,packageName, packageVersion);

                        // If bar does not contain any bpmn files we will send invalidate message
                        if (bpmnResources.size() == 0) {
                            response.put(ProcessCenterConstants.ERROR, true);
                            response.put(ProcessCenterConstants.MESSAGE, "Package file doesn't contain any bpmn files");
                            return response.toString();
                        }
                        // Delete old bpmn files extracted using old bpmn zip.
                        // We will not remove the same bpmn files to keep old associations to processes.
                        if (existingBpmnResourcePaths != null) {
                            java.util.Collection oldBpmnResourcePaths = CollectionUtils.removeAll(Arrays.asList
                                            (existingBpmnResourcePaths),
                                    bpmnResources);
                            for (Object oldBpmnResourcePath : oldBpmnResourcePaths) {
                                if (userRegistry.resourceExists(oldBpmnResourcePath.toString())) {
                                    userRegistry.delete(oldBpmnResourcePath.toString());
                                }
                            }
                        }

                        // Add zip to Registry
                        Resource packageZipContentResource = userRegistry.newResource();
                        packageZipContentResource.setContentStream(packageInputStreamCopy);

                        packageZipContentResource.setMediaType(ProcessCenterConstants.ZIP_MEDIA_TYPE);
                        userRegistry.put(packageBPMNRegistryPath + "/" + newPackageFileName,
                                packageZipContentResource);

                        // Add new package file to package asset
                        String packageFileChecksum = Utils.getMD5Checksum(packageFileStream);
                        packageAsset.setProperty(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME,
                                newPackageFileName);
                        packageAsset.setProperty(ProcessCenterConstants.BPMN_RESOURCES_COUNT, String.valueOf
                                (bpmnResources.size()));
                        packageAsset.setProperty(ProcessCenterConstants.CHECKSUM, packageFileChecksum);
                        // Remove old package file
                        if (!packageFileName.equals(newPackageFileName)) {
                            userRegistry.delete(packageBPMNRegistryPath + "/" + packageFileName);
                        }
                    }
                    String packageContent = new String((byte[]) packageAsset.getContent(),
                            StandardCharsets.UTF_8);
                    Document packageDocument = stringToXML(packageContent);
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList metadataElement = ((NodeList) xPath.evaluate("/metadata",
                            packageDocument.getDocumentElement(), XPathConstants.NODESET));


                    if (metadataElement != null && metadataElement.getLength() > 0) {
                        Element metadataRootElement = (Element) (metadataElement.item(0));

                        // Update the description
                        if (description != null) {
                            NodeList overviewNode = metadataRootElement.getElementsByTagName(ProcessCenterConstants
                                    .OVERVIEW);
                            if (overviewNode != null && overviewNode.getLength() > 0) {
                                Element overviewRootElement = (Element) (overviewNode.item(0));
                                NodeList descriptionNode = metadataRootElement.getElementsByTagName
                                        (ProcessCenterConstants
                                                .DESCRIPTION);
                                if (descriptionNode != null && descriptionNode.getLength() > 0) {
                                    descriptionNode.item(0).setTextContent(description);
                                } else {
                                    appendText(packageDocument, overviewRootElement, ProcessCenterConstants.DESCRIPTION,
                                            ProcessCenterConstants.METADATA_NAMESPACE, ProcessCenterConstants
                                                    .NOT_APPLICABLE);
                                }
                            }
                        }
                        //Update image stream
                        if (imageFileStream != null) {
                            NodeList imagesElement = metadataRootElement.getElementsByTagName(ProcessCenterConstants
                                    .IMAGES);
                            if (imagesElement != null && imagesElement.getLength() > 0) {
                                Element imageRootElement = (Element) (imagesElement.item(0));
                                NodeList imageNode = imageRootElement.getElementsByTagName(ProcessCenterConstants
                                        .THUMBNAIL);
                                // Update the description
                                if (imageNode != null && imageNode.getLength() > 0) {
                                    imageNode.item(0).setTextContent(ProcessCenterConstants
                                            .IMAGE_THUMBNAIL);
                                } else {
                                    Element imageElement = append(packageDocument, imageRootElement,
                                            ProcessCenterConstants.IMAGES,
                                            ProcessCenterConstants.METADATA_NAMESPACE);
                                    appendText(packageDocument, imageElement, ProcessCenterConstants.THUMBNAIL,
                                            ProcessCenterConstants.METADATA_NAMESPACE, ProcessCenterConstants
                                                    .IMAGE_THUMBNAIL);
                                }
                            }
                        }
                    }

                    // Update asset with new rxt content
                    packageAsset.setContent(xmlToString(packageDocument));
                    packageAsset.setMediaType(ProcessCenterConstants.PACKAGE_MEDIA_TYPE);
                    userRegistry.put(packageRegistryPath, packageAsset);

                    Resource storedPackage = userRegistry.get(packageRegistryPath);
                    if (storedPackage == null) {
                        throw new ProcessCenterException("Couldn't find the package in the registry for path " +
                                packageRegistryPath);
                    }
                    String packageId = storedPackage.getUUID();
                    String imageRegPath = ProcessCenterConstants.IMAGE_PATH_PACKAGE + packageId + "/" +
                            ProcessCenterConstants.IMAGE_THUMBNAIL;

                    // Update image thumbnail file
                    if (imageFileStream != null) {
                        Resource imageContentResource = userRegistry.newResource();
                        imageContentResource.setContent(imageFileStream);
                        userRegistry.put(imageRegPath, imageContentResource);
                    }
                    response.put(ProcessCenterConstants.ERROR, false);
                    response.put(ProcessCenterConstants.ID, packageId);
                    response.put(ProcessCenterConstants.NAME, packageName);
                }
            }

        } catch (ProcessCenterException e) {
            String errMsg = "Update package error: " + packageName + " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (SAXException | TransformerException | ParserConfigurationException | XPathExpressionException e) {
            String errMsg = "Error occurred while processing xml to string to update new package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (RegistryException e) {
            String errMsg = "Error occurred while accessing registry for updating package " + packageName +
                    " version " + packageVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (NoSuchAlgorithmException | IOException e) {
            String errMsg = "Error occurred while accessing package archive file for updating package " + packageName +
                    " file " + newPackageFileName;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (JSONException e) {
            String errMsg = "JSON Error occurred while updating package  " + packageName + " version " +
                    packageVersion;
            log.error(errMsg, e);
        }
        return response.toString();
    }

    /**
     * Delete Package
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @return
     * @throws ProcessCenterException
     */
    public void delete(String packageName, String packageVersion, String username) throws
            ProcessCenterException {

        String packageAssetRegistryPath = getPackageAssetRegistryPath(packageName,
                packageVersion);
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {

                    // Remove the bpmn bar file directory if exist
                    if (userRegistry.resourceExists(packageAssetRegistryPath)) {
                        userRegistry.delete(packageAssetRegistryPath);
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Package : " + packageName + " version : " + packageVersion
                        + " has been successfully deleted.");
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
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - provider
     * @return bpmn resources as json string
     * @throws ProcessCenterException
     */
    public String getBpmnResources(String packageName, String packageVersion, String username) throws
            ProcessCenterException {

        JSONArray bpmnResources = new JSONArray();
        String packageRegistryPath = getPackageRegistryPath(packageName, packageVersion);
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath
                (getPackageBPMNRegistryPath(getPackageAssetRegistryPath(packageName, packageVersion)));
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
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
                                    String processDeploymentID = bpmnRegistryResource.getProperty
                                            (ProcessCenterConstants.PROCESS_DEPLOYMENT_ID);
                                    if (bpmnProcessName != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_NAME, bpmnProcessName);
                                    }
                                    if (processDeploymentID != null) {
                                        bpmnResource.put(ProcessCenterConstants.PROCESS_DEPLOYMENT_ID,
                                                processDeploymentID);
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
     * @param processName      Name of the Process
     * @param processVersion   - Version of the Process
     * @param packageName      Name of the Package
     * @param packageVersion   - Version of the Package
     * @param bpmnResourcePath - bpmn file path
     * @param username         - Provider of the Package
     * @throws ProcessCenterException
     */
    public void associateProcess(String processName, String processVersion, String packageName, String
            packageVersion, String bpmnResourcePath, String username)
            throws ProcessCenterException {

        String processRegistryPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
        String packageBPMNContentRegistryPath = getPackageBPMNContentRegistryPath(getPackageBPMNRegistryPath
                (getPackageAssetRegistryPath(packageName, packageVersion))) + bpmnResourcePath;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
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
     * @param packageName      Name of the Package
     * @param packageVersion   - Version of the Package
     * @param bpmnResourcePath BPMN resource path
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
     * Get deployment file name
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @throws ProcessCenterException
     */
    public String getDeploymentFileName(String packageName, String packageVersion, String username) throws
            ProcessCenterException {
        try {
            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {
                    if (userRegistry.resourceExists(packageRegistryPath)) {
                        Resource packageAsset = userRegistry.get(packageRegistryPath);
                        return packageAsset.getProperty(ProcessCenterConstants.PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while getting deployment file name for the package " + packageName +
                    " version " + packageVersion + " in the server";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return null;
    }

    /**
     * Download the bpmn bar file
     *
     * @param packageName    Name of the Package
     * @param packageVersion - Version of the Package
     * @param username       - Provider of the Package
     * @return bpmn bar file content as  a String
     */
    public String downloadPackageFile(String packageName, String packageVersion, String username) throws
            ProcessCenterException {
        try {
            String packageRegistryPath = Package.getPackageRegistryPath(packageName, packageVersion);
            String packageBPMNRegistryPath = Package.getPackageBPMNRegistryPath(Package.getPackageAssetRegistryPath
                    (packageName,
                            packageVersion));
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceUserRegistry(username);
                if (userRegistry != null) {
                    if (userRegistry.resourceExists(packageRegistryPath)) {
                        Resource packageAsset = userRegistry.get(packageRegistryPath);
                        String packageFileName = packageAsset.getProperty(ProcessCenterConstants
                                .PACKAGE_BPMN_ARCHIVE_FILE_NAME);
                        if (userRegistry.resourceExists(packageBPMNRegistryPath + packageFileName)) {
                            Resource packageArchive = userRegistry.get(packageBPMNRegistryPath + packageFileName);
                            byte[] docContent = (byte[]) packageArchive.getContent();
                            return new sun.misc.BASE64Encoder().encode(docContent);
                        }
                    }
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Registry error while downloading deployment file for the package " + packageName +
                    " version " + packageVersion + " in the server";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return null;
    }

    /**
     * Check whether the given xml is BPMN resource
     *
     * @param resourceName - bpmn resource name
     * @return
     */
    private boolean isBpmnResource(String resourceName) {
        for (String suffix : ProcessCenterConstants.BPMN_RESOURCE_SUFFIXES) {
            if (resourceName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param userRegistry                   User registry where we need to store the bpmn files
     * @param packageBPMNContentRegistryPath Extracted bpmn files archive path
     * @param packageFileInputStream         Package file stream
     * @param packageName                    Name of the package
     * @param packageVersion                 Version of the package
     * @return bpmn file count
     * @throws RegistryException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ProcessCenterException
     */
    private List<String> extractPackageAndStoreBPMNFiles(UserRegistry userRegistry,String packageBPMNContentRegistryPath, InputStream
            packageFileInputStream,  String packageName, String packageVersion) throws
            RegistryException,
            IOException, ParserConfigurationException, ProcessCenterException {
        List<String> bpmnResources = new LinkedList<String>();
        byte[] buffer = new byte[1024];
        if (packageFileInputStream != null) {
            try (final ZipInputStream packageZIPInoutStream = new ZipInputStream(packageFileInputStream)) {
                ZipEntry zipEntry = packageZIPInoutStream.getNextEntry();
                while (zipEntry != null) {
                    if (!zipEntry.isDirectory() && isBpmnResource(zipEntry.getName())) {
                        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream
                                ()) {
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
                                packageBPMNContentResource.setProperty(ProcessCenterConstants
                                                .PROCESS_NAME,
                                        bpmnProcessID);
                                packageBPMNContentResource.setContent(byteArrayOutputStream
                                        .toByteArray());
                                userRegistry.put(packageBPMNContentRegistryPath + zipEntry
                                                .getName(),
                                        packageBPMNContentResource);
                                bpmnResources.add("/" + packageBPMNContentRegistryPath + zipEntry
                                        .getName());
                            } else if (log.isDebugEnabled()) {
                                log.debug("Process ID cannot be found for bpmn resource " + zipEntry
                                        .getName() + "to create new package " +
                                        packageName + " version " + packageVersion);
                            }
                        } catch (SAXException e) {
                            throw new ProcessCenterException("Error while getting process id from " +
                                    "bpmn file"
                                    + zipEntry.getName(), e);
                        }
                    }
                    zipEntry = packageZIPInoutStream.getNextEntry();
                }
                packageZIPInoutStream.closeEntry();
            }
        }
        return bpmnResources;
    }
}
