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
import org.wso2.carbon.registry.core.exceptions.ResourceNotFoundException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

/**
 * Class related to FlowChart resources of processes
 */
public class FlowChart {
    private static final Log log = LogFactory.getLog(FlowChart.class);

    /**
     * @param processName
     * @param processVersion
     * @param flowchartJson
     * @return the processId once the flowchart is saved
     */
    public String uploadFlowchart(String processName, String processVersion, String flowchartJson, String user)
            throws ProcessCenterException {

        String processId = "NA";
        if (log.isDebugEnabled())
            log.debug("Creating Flowchart...");
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                RegPermissionUtil
                        .setPutPermission(registryService, user, ProcessCenterConstants.AUDIT.PROCESS_FLOW_CHART_PATH);

                Resource flowchartContentResource = reg.newResource();
                flowchartContentResource.setContent(flowchartJson);
                flowchartContentResource.setMediaType("application/json");
                String flowchartContentPath = "flowchart/" + processName + "/" + processVersion;
                reg.put(flowchartContentPath, flowchartContentResource);
                String processPath = "processes/" + processName + "/" + processVersion;

                // update process by linking the flowchart asset
                Resource processAsset = reg.get(processPath);
                byte[] processContentBytes = (byte[]) processAsset.getContent();
                String processContent = new String(processContentBytes);
                Document processXMLContent = Utils.stringToXML(processContent);

                //set the flowchart content
                processXMLContent.getElementsByTagName("flowchart").item(0).getFirstChild()
                        .setTextContent(flowchartContentPath);

                String newProcessContent = Utils.xmlToString(processXMLContent);
                processAsset.setContent(newProcessContent);
                reg.put(processPath, processAsset);

                Resource storedProcessAsset = reg.get(processPath);
                processId = storedProcessAsset.getUUID();

                //add reg association
                reg.addAssociation(flowchartContentPath, processPath, ProcessCenterConstants.ASSOCIATION_TYPE);

            }
        } catch (Exception e) {
            String errMsg =
                    "Flow-chart uploading error for " + processName + " - " + processVersion + ":" + flowchartJson;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        if (log.isDebugEnabled())
            log.debug("Successfully uploaded the flowchart for process " + processName + "-" + processVersion);
        return processId;
    }

    /**
     * @param flowchartPath
     * @return the flowchart string of a process
     */
    public String getFlowchart(String flowchartPath) throws ProcessCenterException {
        String flowchartString = "NA";

        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                flowchartPath = flowchartPath.substring("/_system/governance/".length());
                try {
                    Resource flowchartAsset = reg.get(flowchartPath);
                    flowchartString = new String((byte[]) flowchartAsset.getContent());
                } catch (ResourceNotFoundException e) {
                    flowchartString = "NA";
                }
            }
        } catch (Exception e) {
            String values[] = flowchartPath.split("/");
            String errorMessage = "Failed to retrieve the flowchart for process " + values[1] + "-" + values[2];
            log.error(errorMessage, e);
            throw new ProcessCenterException(errorMessage);
        }
        if (log.isDebugEnabled())
            log.debug("Successfully retrieved the flowchart at path " + flowchartPath);
        return flowchartString;
    }

    /**
     * Delete a flowchart from the registry
     *
     * @param name
     * @param version
     */
    public void deleteFlowchart(String name, String version, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            String flowchartContentPath = "flowchart/" + name + "/" + version;
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                reg.delete(flowchartContentPath);

                String processPath = "processes/" + name + "/" + version;
                Resource processResource = reg.get(processPath);

                String processContent = new String((byte[]) processResource.getContent());
                Document processXML = Utils.stringToXML(processContent);
                processXML.getElementsByTagName("flowchart").item(0).getFirstChild().setTextContent("NA");

                String newProcessContent = Utils.xmlToString(processXML);
                processResource.setContent(newProcessContent);
                reg.put(processPath, processResource);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to upload the flowchart for process " + name + "-" + version;
            throw new ProcessCenterException(errorMessage, e);
        }
    }

}
