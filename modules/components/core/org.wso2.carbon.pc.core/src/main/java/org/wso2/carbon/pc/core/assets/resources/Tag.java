package org.wso2.carbon.pc.core.assets.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.util.Iterator;

/**
 * Class related to tags of attached to processes
 */
public class Tag {
    private static final Log log = LogFactory.getLog(Tag.class);

    public String getProcessTags() throws ProcessCenterException {

        String textContent = "FAILED TO GET PROCESS TAGS";

        try {
            JSONObject tagsObj = new JSONObject();

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                String[] processPaths = GovernanceUtils
                        .findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                for (String processPath : processPaths) {
                    Resource processResource = reg.get(processPath);

                    String processContent = new String((byte[]) processResource.getContent());
                    Document processXML = Utils.stringToXML(processContent);
                    String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                    String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();
                    NodeList processImages = processXML.getElementsByTagName("images");

                    String processImage = "";
                    if (processImages.getLength() != 0) {
                        Element imageElement = (Element) processImages.item(0);
                        processImage = imageElement.getElementsByTagName("thumbnail").item(0).getTextContent();
                    }

                    JSONObject processJSON = new JSONObject();
                    processJSON.put("path", processPath);
                    processJSON.put("processid", processResource.getUUID());
                    processJSON.put("processname", processName);
                    processJSON.put("processversion", processVersion);
                    processJSON.put("processImage", processImage);

                    org.wso2.carbon.registry.core.Tag[] tags = reg.getTags(processPath);
                    for (org.wso2.carbon.registry.core.Tag tag : tags) {

                        Iterator<String> keys = tagsObj.keys();

                        if (!keys.hasNext() || keys == null) {
                            JSONArray newTagArray = new JSONArray();
                            newTagArray.put(processJSON);
                            tagsObj.put(tag.getTagName(), newTagArray);
                            continue;
                        }

                        while (keys.hasNext()) {
                            String temp = (keys.next());

                            if (temp == tag.getTagName()) {
                                JSONArray processArray = ((JSONArray) tagsObj.get(temp));
                                processArray = processArray.put(processJSON);
                                tagsObj.put(temp, processArray);
                                break;

                            }
                            if (!keys.hasNext()) {
                                JSONArray newTagArray = new JSONArray();
                                newTagArray.put(processJSON);
                                tagsObj.put(tag.getTagName(), newTagArray);
                            }
                        }
                    }
                }

                textContent = tagsObj.toString();

            } else {
                String msg = "Registry service not available for retrieving processes.";
                throw new ProcessCenterException(msg);
            }
        } catch (Exception e) {
            String msg = "Process tags retrieving error";
            log.error(msg, e);
            throw new ProcessCenterException(msg, e);
        }
        return textContent;
    }

    /**
     * Get process related tags as a String in which the seperate tags are delimited by commas(,)
     * i.e.  tag1,tag2,tag3
     *
     * @param processName    name of the process
     * @param processVersion version of the process
     * @return process related tags as a String in which the separate tags are delimited by commas(,)
     * @throws RegistryException
     */
    public String getProcessTags(String processName, String processVersion) throws RegistryException {
        StringBuffer tagsSb = new StringBuffer();
        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            UserRegistry reg = registryService.getGovernanceSystemRegistry();
            String processPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;
            org.wso2.carbon.registry.core.Tag[] tags = reg.getTags(processPath);
            for (org.wso2.carbon.registry.core.Tag tag : tags) {
                tagsSb.append(ProcessCenterConstants.TAGS_FILE_TAG_SEPARATOR + tag.getTagName());
            }
        }
        return tagsSb.toString();
    }

    /**
     * Adds a new tag for a process asset at the publisher
     *
     * @param processDetails
     * @param user
     * @return true if the process tag update is a success
     * @throws ProcessCenterException
     */
    public boolean addNewTag(String processDetails, String user) throws ProcessCenterException {

        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            String processName = "FAILED TO APPLY TAG";
            try {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                JSONObject processInfo = new JSONObject(processDetails);

                processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String tagName = processInfo.getString("tag");
                String assetPath = "processes/" + processName + "/" + processVersion;

                reg.applyTag(assetPath, tagName);
            } catch (Exception e) {
                String msg = "Process update error:" + processName;
                log.error(msg, e);
                throw new ProcessCenterException(msg, e);
            }
        }
        return true;
    }

    /**
     * Removes tag for a process asset at the publisher
     *
     * @param processDetails
     * @param user
     * @return true is the process tag removal is a success
     * @throws ProcessCenterException
     */
    public boolean removeTag(String processDetails, String user) throws ProcessCenterException {

        RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
        if (registryService != null) {
            String processName = "FAILED TO REMOVE TAG";
            try {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);
                JSONObject processInfo = new JSONObject(processDetails);

                processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                String tagName = processInfo.getString("tag");
                String assetPath = "processes/" + processName + "/" + processVersion;

                reg.removeTag(assetPath, tagName);
            } catch (Exception e) {
                String msg = "Process update error:" + processName;
                log.error(msg, e);
                throw new ProcessCenterException(msg, e);
            }
        }
        return true;
    }
}
