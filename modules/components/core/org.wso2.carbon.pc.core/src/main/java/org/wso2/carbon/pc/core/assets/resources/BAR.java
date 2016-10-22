package org.wso2.carbon.pc.core.assets.resources;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaggeryjs.hostobjects.stream.StreamHostObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class related to BAR
 */
public class BAR {
    private static final Log log = LogFactory.getLog(BAR.class);

    public void processBAR(String barName, String barVersion, String description, StreamHostObject s) {

        log.debug("Processing BPMN archive " + barName);
        InputStream barStream = s.getStream();
        if (description == null) {
            description = "";
        }

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                // add the bar asset
                //                String barAssetContent = "<metadata xmlns='http://www.wso2
                // .org/governance/metadata'>" +
                //                        "<overview><name>" + barName + "</name><version>" + barVersion + "</version>
                // <description>This is a test process2</description></overview></metadata>";

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // root elements
                String mns = "http://www.wso2.org/governance/metadata";
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElementNS(mns, "metadata");
                doc.appendChild(rootElement);

                Element overviewElement = Utils.append(doc, rootElement, "overview", mns);
                Utils.appendText(doc, overviewElement, "name", mns, barName);
                Utils.appendText(doc, overviewElement, "version", mns, barVersion);
                Utils.appendText(doc, overviewElement, "description", mns, description);

                // add binary data of the bar asset as a separate resource
                Resource barResource = reg.newResource();
                byte[] barContent = IOUtils.toByteArray(barStream);
                barResource.setContent(barContent);
                barResource.setMediaType("application/bar");
                String barResourcePath = "bpmn_archives_binary/" + barName + "/" + barVersion;
                reg.put(barResourcePath, barResource);

                Element contentElement = Utils.append(doc, rootElement, "content", mns);
                Utils.appendText(doc, contentElement, "contentpath", mns, barResourcePath);

                //                reg.addAssociation(barAssetPath, barResourcePath, "has_content");

                // add each bpmn file as a new asset
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
                        String bpmnResourcePath = "bpmn_xml/" + barName + "." + entryName + "/" + barVersion;
                        reg.put(bpmnResourcePath, bpmnResource);

                        // add bpmn asset
                        String bpmnAssetName = barName + "." + entryName;
                        String displayName = entryName;
                        if (displayName.endsWith(".bpmn")) {
                            displayName = displayName.substring(0, (entryName.length() - 5));
                        }
                        String bpmnAssetPath = "bpmn/" + barName + "." + entryName + "/" + barVersion;

                        Document bpmnDoc = docBuilder.newDocument();
                        Element bpmnRoot = bpmnDoc.createElementNS(mns, "metadata");
                        bpmnDoc.appendChild(bpmnRoot);
                        Element bpmnOverview = Utils.append(bpmnDoc, bpmnRoot, "overview", mns);
                        Utils.appendText(bpmnDoc, bpmnOverview, "name", mns, displayName);
                        Utils.appendText(bpmnDoc, bpmnOverview, "version", mns, barVersion);
                        Utils.appendText(bpmnDoc, bpmnOverview, "package", mns, barName);
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

                        Element bpmnElement = Utils.append(doc, rootElement, "bpmn", mns);
                        Utils.appendText(doc, bpmnElement, "Name", mns, bpmnAssetName);
                        Utils.appendText(doc, bpmnElement, "Path", mns, bpmnAssetPath);
                        Utils.appendText(doc, bpmnElement, "Id", mns, bpmnAssetID);
                    }
                }

                String barAssetContent = Utils.xmlToString(doc);
                Resource barAsset = reg.newResource();
                barAsset.setContent(barAssetContent);
                barAsset.setMediaType("application/vnd.wso2-bar+xml");
                String barAssetPath = "bar/admin/" + barName + "/" + barVersion;
                reg.put(barAssetPath, barAsset);

            } else {
                String msg = "Registry service is not available.";
                throw new ProcessCenterException(msg);
            }

        } catch (Exception e) {
            log.error("Failed to store bar archive: " + barName + " - " + barVersion, e);
        }
    }

    public String getBAR(String barPath) {

        String barString = "";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                barPath = barPath.substring("/_system/governance/".length());
                Resource barAsset = reg.get(barPath);
                String barContent = new String((byte[]) barAsset.getContent());

                JSONObject bar = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(barContent)));
                Element overviewElement = (Element) document.getElementsByTagName("overview").item(0);
                String barName = overviewElement.getElementsByTagName("name").item(0).getTextContent();
                String barVersion = overviewElement.getElementsByTagName("version").item(0).getTextContent();
                String barDescription = overviewElement.getElementsByTagName("description").item(0).getTextContent();

                bar.put("name", barName);
                bar.put("version", barVersion);
                bar.put("description", barDescription);

                Element contentElement = (Element) document.getElementsByTagName("content").item(0);
                String contentPath = contentElement.getElementsByTagName("contentpath").item(0).getTextContent();
                bar.put("contentpath", contentPath);

                JSONArray jbpmns = new JSONArray();
                bar.put("bpmnModels", jbpmns);

                NodeList bpmnElements = document.getElementsByTagName("bpmn");
                for (int i = 0; i < bpmnElements.getLength(); i++) {
                    Element bpmnElement = (Element) bpmnElements.item(i);
                    String bpmnId = bpmnElement.getElementsByTagName("Id").item(0).getTextContent();
                    String bpmnName = bpmnElement.getElementsByTagName("Name").item(0).getTextContent();
                    String bpmnPath = bpmnElement.getElementsByTagName("Path").item(0).getTextContent();

                    JSONObject jbpmn = new JSONObject();
                    jbpmn.put("bpmnId", bpmnId);
                    jbpmn.put("bpmnName", bpmnName);
                    jbpmn.put("bpmnPath", bpmnPath);
                    jbpmns.put(jbpmn);
                }

                barString = bar.toString();
            }
        } catch (Exception e) {
            log.error("Failed to fetch BPMN archive: " + barPath);
        }

        return barString;
    }
}
