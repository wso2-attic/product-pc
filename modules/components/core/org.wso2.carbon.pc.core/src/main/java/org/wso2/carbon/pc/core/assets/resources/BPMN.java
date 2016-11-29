package org.wso2.carbon.pc.core.assets.resources;


import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaggeryjs.hostobjects.stream.StreamHostObject;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.audit.util.RegPermissionUtil;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.PCInputStreamProvider;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;

import javax.ws.rs.core.MediaType;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class related to BPMN resources
 */
public class BPMN {
    private Document BPMNDocument;
    private Map<String, String> variableMap = new HashMap<>();
    private static final Log log = LogFactory.getLog(BPMN.class);
    private static final String mns = "http://www.wso2.org/governance/metadata";

    public BPMN() {
    }

    public BPMN(Document BPMNDocument) {
        this.BPMNDocument = BPMNDocument;
    }

    /*
   Get process variables list from the BPMN
    */
    public String getProcessVariables() {
        NodeList variableList = BPMNDocument.getElementsByTagName(ProcessCenterConstants.ACTIVITI_FORM_PROPERTY);

        for (int i = 0; i < variableList.getLength(); i++) {
            //We won't find variable attribute always for a variable. We use variable id in such cases
            String variable = (variableList.item(i).getAttributes().getNamedItem(ProcessCenterConstants.VARIABLE)
                    == null) ? variableList.item(i).getAttributes().getNamedItem(ProcessCenterConstants.ID).getNodeValue() :
                    variableList.item(i).getAttributes().getNamedItem(ProcessCenterConstants.VARIABLE).getNodeValue();

            String variableType = variableList.item(i).getAttributes().getNamedItem(ProcessCenterConstants.TYPE).
                    getNodeValue();

            variableMap.put(variable, variableType);
        }
        return new JSONObject(variableMap).toString();
    }

    public void processBPMN(String version, String type, StreamHostObject s) {

        InputStream barStream = s.getStream();

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                if (type.equals("bpmn_file")) {

                } else {

                    // add each bpmn file as a new asset
                    byte[] barContent = IOUtils.toByteArray(barStream);
                    ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(barContent));
                    ZipEntry entry = null;
                    while ((entry = zip.getNextEntry()) != null) {
                        String entryName = entry.getName();
                        if (entryName.endsWith(".bpmn") || entryName.endsWith(".bpmn20.xml")) {

                            // add bpmn xml as a separate resource
                            StringBuilder sb = new StringBuilder();
                            byte[] buffer = new byte[1024];
                            int read = 0;
                            while ((read = zip.read(buffer, 0, 1024)) >= 0) {
                                sb.append(new String(buffer, 0, read));
                            }
                            String bpmnContent = sb.toString();
                            Resource bpmnResource = reg.newResource();
                            bpmnResource.setContent(bpmnContent);
                            bpmnResource.setMediaType("text/xml");
                            String bpmnResourcePath = "bpmn_xml/" + entryName + "/" + version;
                            reg.put(bpmnResourcePath, bpmnResource);

                            // add bpmn asset
                            String displayName = entryName;
                            if (displayName.endsWith(".bpmn")) {
                                displayName = displayName.substring(0, (entryName.length() - 5));
                            }
                            String bpmnAssetPath = "bpmn/" + entryName + "/" + version;
                            Document bpmnDoc = docBuilder.newDocument();
                            Element bpmnRoot = bpmnDoc.createElementNS(mns, "metadata");
                            bpmnDoc.appendChild(bpmnRoot);
                            Element bpmnOverview = Utils.append(bpmnDoc, bpmnRoot, "overview", mns);
                            Utils.appendText(bpmnDoc, bpmnOverview, "name", mns, displayName);
                            Utils.appendText(bpmnDoc, bpmnOverview, "version", mns, version);
                            Utils.appendText(bpmnDoc, bpmnOverview, "processpath", mns, "");
                            Utils.appendText(bpmnDoc, bpmnOverview, "description", mns, "");
                            Element bpmnAssetContentElement = Utils.append(bpmnDoc, bpmnRoot, "content", mns);
                            Utils.appendText(bpmnDoc, bpmnAssetContentElement, "contentpath", mns, bpmnResourcePath);
                            String bpmnAssetContent = Utils.xmlToString(bpmnDoc);

                            Resource bpmnAsset = reg.newResource();
                            bpmnAsset.setContent(bpmnAssetContent);
                            bpmnAsset.setMediaType("application/vnd.wso2-bpmn+xml");
                            reg.put(bpmnAssetPath, bpmnAsset);
                            Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                            String bpmnAssetID = storedBPMNAsset.getUUID();
                        }
                    }
                }

            } else {
                String msg = "Registry service is not available.";
                throw new ProcessCenterException(msg);
            }

        } catch (Exception e) {
            log.error("Failed to store bar archive: " + version, e);
        }
    }

    public String createBPMN(String bpmnName, String bpmnVersion, Object o, String user) throws ProcessCenterException {
        String processId = "FAILED TO CREATE BPMN";
        if (log.isDebugEnabled()) {
            log.debug("Creating BPMN resource:" + bpmnName + " - " + bpmnVersion);
        }
        try {
            StreamHostObject s = (StreamHostObject) o;
            InputStream bpmnStream = s.getStream();
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                RegPermissionUtil.setPutPermission(registryService, user, ProcessCenterConstants.AUDIT.PROCESS_BPMN);

                // store bpmn content as a registry resource
                Resource bpmnContentResource = reg.newResource();
                byte[] bpmnContent = IOUtils.toByteArray(bpmnStream);
                String bpmnText = new String(bpmnContent);
                bpmnContentResource.setContent(bpmnText);
                bpmnContentResource.setMediaType(MediaType.APPLICATION_XML);
                String bpmnContentPath = "bpmncontent/" + bpmnName + "/" + bpmnVersion;
                reg.put(bpmnContentPath, bpmnContentResource);

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // add bpmn asset pointing to above bpmn content
                String mns = "http://www.wso2.org/governance/metadata";
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(mns, "metadata");
                doc.appendChild(rootElement);

                Element overviewElement = Utils.append(doc, rootElement, "overview", mns);
                Utils.appendText(doc, overviewElement, "name", mns, bpmnName);
                Utils.appendText(doc, overviewElement, "version", mns, bpmnVersion);
                Utils.appendText(doc, overviewElement, "description", mns, "");
                String processPath = "processes/" + bpmnName + "/" + bpmnVersion;
                Utils.appendText(doc, overviewElement, "processpath", mns, processPath);

                Element contentElement = Utils.append(doc, rootElement, "content", mns);
                Utils.appendText(doc, contentElement, "contentpath", mns, bpmnContentPath);

                String bpmnAssetContent = Utils.xmlToString(doc);
                Resource bpmnAsset = reg.newResource();
                bpmnAsset.setContent(bpmnAssetContent);
                bpmnAsset.setMediaType("application/vnd.wso2-bpmn+xml");

                String bpmnAssetPath = "bpmn/" + bpmnName + "/" + bpmnVersion;
                reg.put(bpmnAssetPath, bpmnAsset);
                Resource storedBPMNAsset = reg.get(bpmnAssetPath);
                String bpmnAssetID = storedBPMNAsset.getUUID();

                // update process by linking the bpmn asset

                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document pdoc = Utils.stringToXML(processContent);
                pdoc.getElementsByTagName("bpmnpath").item(0).setTextContent(bpmnAssetPath);
                pdoc.getElementsByTagName("bpmnid").item(0).setTextContent(bpmnAssetID);
                String newProcessContent = Utils.xmlToString(pdoc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();

                //add reg association
                reg.addAssociation(bpmnContentPath, processPath, ProcessCenterConstants.ASSOCIATION_TYPE);
            }
        } catch (Exception e) {
            String errMsg = "Create BPMN error:" + bpmnName + " - " + bpmnVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    public String removeBPMNDiagram(String processName, String processVersion, String user)
            throws ProcessCenterException {
        String processId = "";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                String bpmnContentPath = ProcessCenterConstants.BPMN_CONTENT_PATH + processName + "/" + processVersion;

                if (reg.resourceExists(bpmnContentPath)) {
                    reg.delete(bpmnContentPath);
                }
                String bpmnAssetPath = ProcessCenterConstants.BPMN_META_DATA_FILE_PATH + processName + "/" + processVersion;
                if (reg.resourceExists(bpmnAssetPath)) {
                    reg.delete(bpmnAssetPath);
                }
                String processPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
                if (reg.resourceExists(processPath)) {
                    Resource processResource = reg.get(processPath);

                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = Utils.stringToXML(processContent);
                    processXML.getElementsByTagName("bpmnpath").item(0).setTextContent("NA");
                    processXML.getElementsByTagName("bpmnid").item(0).setTextContent("NA");

                    String newProcessContent = Utils.xmlToString(processXML);
                    processResource.setContent(newProcessContent);
                    reg.put(processPath, processResource);

                    Resource storedProcessAsset = reg.get(processPath);
                    processId = storedProcessAsset.getUUID();
                }
            }
        } catch (Exception e) {
            String errMsg = "Error has been occurred while removing BPMN diagram in the process:" + processName + "-"
                    + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }

    public String getBPMN(String bpmnPath) throws ProcessCenterException {

        String bpmnString = "";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                bpmnPath = bpmnPath.substring("/_system/governance/".length());
                Resource bpmnAsset = reg.get(bpmnPath);
                String bpmnContent = new String((byte[]) bpmnAsset.getContent());
                JSONObject bpmn = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(bpmnContent)));
                Element overviewElement = (Element) document.getElementsByTagName("overview").item(0);
                String bpmnName = overviewElement.getElementsByTagName("name").item(0).getTextContent();
                String bpmnVersion = overviewElement.getElementsByTagName("version").item(0).getTextContent();
                String bpmnDescription = overviewElement.getElementsByTagName("description").item(0).getTextContent();
                String processPath = overviewElement.getElementsByTagName("processpath").item(0).getTextContent();

                bpmn.put("name", bpmnName);
                bpmn.put("version", bpmnVersion);
                bpmn.put("description", bpmnDescription);
                bpmn.put("processPath", processPath);

                Element contentElement = (Element) document.getElementsByTagName("content").item(0);
                String contentPath = contentElement.getElementsByTagName("contentpath").item(0).getTextContent();
                bpmn.put("contentpath", contentPath);
                String encodedBPMNImage = getEncodedBPMNImage(contentPath);
                bpmn.put("bpmnImage", encodedBPMNImage);

                bpmnString = bpmn.toString();
            }
        } catch (Exception e) {
            String errMsg = "Failed to fetch BPMN model: " + bpmnPath;
            log.error("Failed to fetch BPMN model: " + bpmnPath);
            throw new ProcessCenterException(errMsg, e);
        }

        return bpmnString;
    }

    public String getEncodedBPMNImage(String path) throws ProcessCenterException {
        byte[] encoded = Base64.encodeBase64(getBPMNImage(path));
        return new String(encoded);
    }

    public byte[] getBPMNImage(String path) throws ProcessCenterException {

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource bpmnXMLResource = reg.get(path);
                byte[] bpmnContent = (byte[]) bpmnXMLResource.getContent();
                InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);

                BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
                BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, false, false);

                ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
                InputStream imageStream = generator.generatePngDiagram(bpmnModel);

                return IOUtils.toByteArray(imageStream);
            } else {
                String msg = "Registry service not available for fetching the BPMN image.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String msg = "Failed to fetch BPMN model: " + path;
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
    }

    public byte[] getBPMNImage2(String path) throws ProcessCenterException {

        try {
            byte[] bpmnContent = FileUtils.readFileToByteArray(new File(path));
            InputStreamProvider inputStreamProvider = new PCInputStreamProvider(bpmnContent);

            BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
            BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(inputStreamProvider, false, false);

            ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generatePngDiagram(bpmnModel);

            return IOUtils.toByteArray(imageStream);
        } catch (Exception e) {
            String msg = "Failed to fetch BPMN model: " + path;
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
    }

}