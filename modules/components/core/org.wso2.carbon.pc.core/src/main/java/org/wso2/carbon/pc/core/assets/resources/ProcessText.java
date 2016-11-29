package org.wso2.carbon.pc.core.assets.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.audit.util.RegPermissionUtil;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

/**
 * Class for process text related operations
 */
public class ProcessText {
    private static final Log log = LogFactory.getLog(ProcessText.class);

    public boolean saveProcessText(String processName, String processVersion, String processText, String user)
            throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                RegPermissionUtil
                        .setPutPermission(registryService, user, ProcessCenterConstants.AUDIT.PROCESS_TEXT_PATH);

                // get process asset content
                String processPath = "processes/" + processName + "/" + processVersion;
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document doc = Utils.stringToXML(processContent);

                // store process text as a separate resource
                String processTextResourcePath = "processText/" + processName + "/" + processVersion;
                reg.addAssociation(processTextResourcePath, processPath, ProcessCenterConstants.ASSOCIATION_TYPE);

                if (processText != null && processText.length() > 0) {
                    Resource processTextResource = reg.newResource();
                    processTextResource.setContent(processText);
                    processTextResource.setMediaType("text/html");
                    reg.put(processTextResourcePath, processTextResource);
                    doc.getElementsByTagName("processtextpath").item(0).setTextContent(processTextResourcePath);
                } else {
                    reg.delete(processTextResourcePath);
                    doc.getElementsByTagName("processtextpath").item(0).setTextContent("NA");
                }

                // update process asset
                String newProcessContent = Utils.xmlToString(doc);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);
            }
        } catch (Exception e) {
            String errMsg = "Save process text error for " + processName + " - " + processVersion + " : " + processText;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public String getProcessText(String textPath) throws ProcessCenterException {
        String textContent = "FAILED TO GET TEXT CONTENT";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                Resource textResource = reg.get(textPath);
                textContent = new String((byte[]) textResource.getContent());
            }
        } catch (Exception e) {
            String errMsg = "Process text retrieving error: " + textPath;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return textContent;
    }

    /**
     * Delete process related text
     *
     * @param processName
     * @param processVersion
     * @return
     * @throws ProcessCenterException
     */
    public String deleteProcessText(String processName, String processVersion) throws ProcessCenterException {
        String processId = "";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                String processTextPath = ProcessCenterConstants.PROCESS_TEXT_PATH + processName + "/" + processVersion;
                if (reg.resourceExists(processTextPath)) {
                    //delete the processText resource
                    reg.delete(processTextPath);
                }

                //delete the processText resource's path defined in process.rxt
                String processPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
                if (reg.resourceExists(processPath)) {
                    Resource processResource = reg.get(processPath);

                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = Utils.stringToXML(processContent);
                    processXML.getElementsByTagName("processtextpath").item(0).setTextContent("NA");

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
            throw new ProcessCenterException(errMsg, e);
        }
        return processId;
    }
}
