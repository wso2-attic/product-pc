package org.wso2.carbon.pc.core.assets.resources;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaggeryjs.hostobjects.stream.StreamHostObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.audit.util.RegPermissionUtil;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Class related to process document resources
 */
public class ProcessDocument {
    private static final Log log = LogFactory.getLog(ProcessDocument.class);

    /**
     * Check the documents availability for a given process
     *
     * @param resourcePath holds the process path
     * @return true if documents are available
     */
    public boolean isDocumentAvailable(String resourcePath) {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessCenterConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(resourceContent)));

                NodeList documentElements = ((Element) document.getFirstChild()).getElementsByTagName("document");
                if (documentElements.getLength() != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("No documents available in the path: " + resourcePath, e);
        }
        return false;
    }

    /**
     * Upload a document
     *
     * @param processName    process name
     * @param processVersion process version
     * @param docName        document name
     * @param docSummary     summary of the document
     * @param docUrl         google document url
     * @param docObject      document stream object
     * @param docExtension   document extension
     * @return process id
     */
    public String uploadDocument(String processName, String processVersion, String docName, String docSummary,
            String docUrl, Object docObject, String docExtension, String user)
            throws ProcessCenterException, JSONException {

        String processId = "FAILED TO UPLOAD DOCUMENT";
        InputStream docStream = null;
        byte[] docContent = new byte[0];
        //Getting associated document list of the process and creating json format of it
        String processDocList = this.getUploadedDocumentDetails(
                ProcessCenterConstants.GREG_PATH_PROCESS + processName + "/" + processVersion);
        JSONArray processDocs = new JSONArray(processDocList);

        //checking for a uploaded document with same name in the list, in positive case throws an error and exit
        for (int iteratorValue = 0; iteratorValue < processDocs.length(); iteratorValue++) {
            String path = ((JSONObject) processDocs.get(iteratorValue)).get("path").toString();
            if ((getAssociatedDocFileName(path).equals(docName + "." + docExtension))) {
                throw new ProcessCenterException(
                        "Associated document " + getAssociatedDocFileName(path) + " exits in " + processName
                                + " version" + processVersion);
            }
        }
        try {
            if(docUrl.equals("NA")) {
                StreamHostObject s = (StreamHostObject) docObject;
                docStream = s.getStream();
                docContent = IOUtils.toByteArray(docStream);
            }
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                //                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                RegPermissionUtil
                        .setPutPermission(registryService, user, ProcessCenterConstants.AUDIT.PROCESS_DOC_PATH);

                // store doc content as a registry resource
                Resource docContentResource = reg.newResource();
                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                String docContentPath = null;
                if (docContent.length != 0) {
                    docContentResource.setContent(docContent);
                    if (docExtension.equalsIgnoreCase("pdf")) {
                        docContentResource.setMediaType("application/pdf");
                    } else {
                        docContentResource.setMediaType("application/msword");
                    }
                    docContentPath = "doccontent/" + processName + "/" + processVersion + "/" + docName +
                            "." + docExtension;
                    reg.put(docContentPath, docContentResource);
                    reg.addAssociation(docContentPath, processAssetPath, ProcessCenterConstants.ASSOCIATION_TYPE);
                }

                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                Element rootElement = doc.getDocumentElement();
                Element docElement = Utils.append(doc, rootElement, "document", ProcessCenterConstants.MNS);
                Utils.appendText(doc, docElement, "name", ProcessCenterConstants.MNS, docName);
                Utils.appendText(doc, docElement, "summary", ProcessCenterConstants.MNS, docSummary);
                if ((docUrl != null) && (!docUrl.isEmpty())) {
                    Utils.appendText(doc, docElement, "url", ProcessCenterConstants.MNS, docUrl);
                } else {
                    Utils.appendText(doc, docElement, "url", ProcessCenterConstants.MNS, "NA");
                }
                if (docContentPath != null) {
                    Utils.appendText(doc, docElement, "path", ProcessCenterConstants.MNS, docContentPath);
                } else {
                    Utils.appendText(doc, docElement, "path", ProcessCenterConstants.MNS, "NA");
                }

                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);

                Resource storedProcessAsset = reg.get(processAssetPath);
                processId = storedProcessAsset.getUUID();
            }
        } catch (Exception e) {
            String errMsg =
                    docName + "." + docExtension + " document upload error for " + processName + ":" + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    /**
     * Delete the resource collection (directory) created in the governance registry for the process to keep its
     * documents
     *
     * @param processName    name of the process
     * @param processVersion version of the process
     * @throws ProcessCenterException
     */
    public void deleteDocumentResourceCollection(String processName, String processVersion)
            throws ProcessCenterException, RegistryException {
        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        String documentsResourceCollectionPath =
                ProcessCenterConstants.DOC_CONTENT_PATH + processName + "/" + processVersion;
        if (registryService != null) {
            UserRegistry reg = registryService.getGovernanceSystemRegistry();
            reg.delete(documentsResourceCollectionPath);
        }
    }

    /**
     * Utility method for extracting file name from the registry path
     *
     * @param filepath
     * @return the name of the associated doc file
     */
    private static String getAssociatedDocFileName(String filepath) {
        return filepath.substring(filepath.lastIndexOf("/") + 1);
    }

    /**
     * Get details of all the documents (including Google docs) added to a certain process
     *
     * @param resourcePath holds the process rxt path
     * @return document information
     */
    public String getUploadedDocumentDetails(String resourcePath) throws ProcessCenterException {
        String documentString = "NA";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessCenterConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(
                        new InputSource(new StringReader(resourceContent)));

                JSONArray documentArray = new JSONArray();
                NodeList documentElements = ((Element) document.getFirstChild()).getElementsByTagName("document");

                if (documentElements.getLength() != 0) {
                    for (int i = 0; i < documentElements.getLength(); i++) {
                        Element documentElement = (Element) documentElements.item(i);
                        String docName = documentElement.getElementsByTagName("name").item(0).getTextContent();
                        String docSummary = documentElement.getElementsByTagName("summary").item(0).getTextContent();
                        String docUrl = documentElement.getElementsByTagName("url").item(0).getTextContent();
                        String docPath = documentElement.getElementsByTagName("path").item(0).getTextContent();

                        JSONObject processDoc = new JSONObject();
                        processDoc.put("name", docName);
                        processDoc.put("summary", docSummary);
                        processDoc.put("url", docUrl);
                        processDoc.put("path", docPath);
                        documentArray.put(processDoc);
                    }
                }
                documentString = documentArray.toString();
            }
        } catch (Exception e) {
            String errMsg = "Failed to fetch document: " + resourcePath;
            log.error(errMsg);
            throw new ProcessCenterException(errMsg, e);
        }
        return documentString;
    }

    /**
     * Download a document
     *
     * @param resourcePath holds the process path
     * @return document content as  a String
     */
    public String downloadDocument(String resourcePath) throws ProcessCenterException {
        String docString = "FAILED TO GET Document";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource docAsset = reg.get(resourcePath);
                byte[] docContent = (byte[]) docAsset.getContent();
                docString = new sun.misc.BASE64Encoder().encode(docContent);
                if (log.isDebugEnabled()) {
                    log.debug("Document Path:" + resourcePath);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to download document: " + resourcePath;
            log.error(errMsg);
            throw new ProcessCenterException(errMsg, e);
        }
        return docString;
    }

    /**
     * delete a given document
     *
     * @param deleteDocument holds the information of the document that need to be deleted
     * @return true after successful deletion
     */
    public boolean deleteDocument(String deleteDocument, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject documentInfo = new JSONObject(deleteDocument);
                String processName = documentInfo.getString("processName");
                String processVersion = documentInfo.getString("processVersion");
                JSONObject removeDocument = documentInfo.getJSONObject("removeDocument");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                if (removeDocument != null) {
                    NodeList documentElements = ((Element) doc.getFirstChild()).getElementsByTagName("document");
                    for (int i = 0; i < documentElements.getLength(); i++) {
                        Element documentElement = (Element) documentElements.item(i);
                        String documentName = documentElement.getElementsByTagName("name").item(0).getTextContent();
                        String documentSummary = documentElement.getElementsByTagName("summary").item(0)
                                .getTextContent();
                        String documentUrl = documentElement.getElementsByTagName("url").item(0).getTextContent();
                        String documentPath = documentElement.getElementsByTagName("path").item(0).getTextContent();

                        if (documentName.equals(removeDocument.getString("name")) &&
                                documentSummary.equals(removeDocument.getString("summary")) &&
                                documentPath.equals(removeDocument.getString("path")) &&
                                documentUrl.equals(removeDocument.getString("url"))) {
                            documentElement.getParentNode().removeChild(documentElement);
                            break;
                        }
                    }
                    String newProcessContent = Utils.xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);

                    String docContentResourcePath = removeDocument.getString("path");
                    if (!docContentResourcePath.equals("NA")) {
                        if (reg.resourceExists(docContentResourcePath)) {
                            reg.delete(docContentResourcePath);
                        }
                    }
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete a document: " + deleteDocument;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    /**
     * Updates document details including name and summary
     *
     * @param documentDetails Details of the document to be updated
     * @param user current user name
     * @throws ProcessCenterException
     */
    public String updateDocumentDetails(String documentDetails, String user) throws ProcessCenterException {

        String processId = "NA";

        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        try {
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                JSONObject processInfo = new JSONObject(documentDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String docSummary = processInfo.getString("summary");
                String docIndex = processInfo.getString("name").replace("doc", "");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());

                Document doc = Utils.stringToXML(processContent);

                NodeList documentElements = ((Element) doc.getFirstChild()).getElementsByTagName("document");
                if (documentElements.getLength() != 0) {
                    Element documentElement = (Element) documentElements.item(Integer.valueOf(docIndex));
                    documentElement.getElementsByTagName("summary").item(0).setTextContent(docSummary);
                }
                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);

                Resource storedProcess = reg.get(processAssetPath);
                processId = storedProcess.getUUID();
            }
        } catch (Exception e) {
            String msg = "Failed to update document details of " + documentDetails;
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
        return processId;
    }
}
