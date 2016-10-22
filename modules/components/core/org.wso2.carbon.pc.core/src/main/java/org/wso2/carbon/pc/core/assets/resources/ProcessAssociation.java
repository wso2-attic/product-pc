package org.wso2.carbon.pc.core.assets.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;

/**
 * Class related to inter-process associations - sub process, successor, predecessor
 */
public class ProcessAssociation {
    private static final Log log = LogFactory.getLog(ProcessAssociation.class);

    public void populateAssociations(Association[] associations, JSONArray jsonArray, UserRegistry reg,
            String resourcePath) throws Exception {
        for (Association association : associations) {
            String associationPath = association.getDestinationPath();
            if (associationPath.equals("/" + resourcePath)) {
                continue;
            }
            Resource associatedResource = reg.get(associationPath);
            String processContent = new String((byte[]) associatedResource.getContent());
            Document doc = Utils.stringToXML(processContent);
            Element rootElement = doc.getDocumentElement();
            Element overviewElement = (Element) rootElement.getElementsByTagName("overview").item(0);
            JSONObject associatedProcessDetails = new JSONObject();
            associatedProcessDetails.put("name", overviewElement.getElementsByTagName("name").item(0).getTextContent());
            associatedProcessDetails.put("path", associatedResource.getPath());
            associatedProcessDetails.put("id", associatedResource.getId());
            associatedProcessDetails.put("processId", associatedResource.getUUID());
            associatedProcessDetails
                    .put("version", overviewElement.getElementsByTagName("version").item(0).getTextContent());
            associatedProcessDetails.put("LCState", associatedResource.getProperty("registry.lifecycle." +
                    associatedResource.getProperty("registry.LC.name") + ".state"));
            jsonArray.put(associatedProcessDetails);
        }
    }

    public String getSucessorPredecessorSubprocessList(String resourcePath) {
        String resourceString = "";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessCenterConstants.GREG_PATH.length());

                JSONObject conObj = new JSONObject();
                JSONArray subprocessArray = new JSONArray();
                Association[] aSubprocesses = reg
                        .getAssociations(resourcePath, ProcessCenterConstants.SUBPROCESS_ASSOCIATION);
                populateAssociations(aSubprocesses, subprocessArray, reg, resourcePath);
                conObj.put("subprocesses", subprocessArray);

                JSONArray successorArray = new JSONArray();
                Association[] aSuccessors = reg
                        .getAssociations(resourcePath, ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
                populateAssociations(aSuccessors, successorArray, reg, resourcePath);
                conObj.put("successors", successorArray);

                JSONArray predecessorArray = new JSONArray();
                Association[] aPredecessors = reg
                        .getAssociations(resourcePath,
                                ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                populateAssociations(aPredecessors, predecessorArray, reg, resourcePath);
                conObj.put("predecessors", predecessorArray);

                resourceString = conObj.toString();
            }
        } catch (Exception e) {
            log.error("Failed to fetch Successor, Predecessor and Subprocess information of " + resourcePath, e);
        }
        return resourceString;
    }

    public boolean addSubprocess(String subprocessDetails, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(subprocessDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject subprocess = processInfo.getJSONObject("subprocess");
                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" + processVersion;

                if (subprocess != null) {
                    String subprocessPath = subprocess.getString("path");
                    reg.addAssociation(processAssetPath, subprocessPath, ProcessCenterConstants.SUBPROCESS_ASSOCIATION);
                    reg.addAssociation(subprocessPath, processAssetPath,
                            ProcessCenterConstants.PARENTPROCESS_ASSOCIATION);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to add subprocess: " + subprocessDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean addSuccessor(String successorDetails, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(successorDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject successor = processInfo.getJSONObject("successor");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;

                if (successor != null) {
                    reg.addAssociation(processAssetPath, successor.getString("path"),
                            ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
                    reg.addAssociation(successor.getString("path"), processAssetPath,
                            ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to add successor: " + successorDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean addPredecessor(String predecessorDetails, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(predecessorDetails);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject predecessor = processInfo.getJSONObject("predecessor");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;

                if (predecessor != null) {
                    reg.addAssociation(processAssetPath, predecessor.getString("path"),
                            ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                    reg.addAssociation(predecessor.getString("path"), processAssetPath,
                            ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to add predecessor: " + predecessorDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean deleteSubprocess(String deleteSubprocess, String user) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(deleteSubprocess);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject subprocess = processInfo.getJSONObject("deleteSubprocess");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;

                if (subprocess != null) {
                    reg.removeAssociation(processAssetPath, subprocess.getString("path"),
                            ProcessCenterConstants.SUBPROCESS_ASSOCIATION);
                    reg.removeAssociation(subprocess.getString("path"), processAssetPath,
                            ProcessCenterConstants.PARENTPROCESS_ASSOCIATION);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete subprocess: " + deleteSubprocess;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean deleteSuccessor(String deleteSuccessor, String user) throws ProcessCenterException,
            RegistryException, JSONException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(deleteSuccessor);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject successor = processInfo.getJSONObject("deleteSuccessor");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;

                if (successor != null) {
                    reg.removeAssociation(processAssetPath, successor.getString("path"),
                            ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
                    reg.removeAssociation(successor.getString("path"), processAssetPath,
                            ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete successor: " + deleteSuccessor;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

    public boolean deletePredecessor(String deletePredecessor, String user) throws ProcessCenterException,
            RegistryException, JSONException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject processInfo = new JSONObject(deletePredecessor);
                String processName = processInfo.getString("processName");
                String processVersion = processInfo.getString("processVersion");
                JSONObject predecessor = processInfo.getJSONObject("deletePredecessor");

                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;

                if (predecessor != null) {
                    reg.removeAssociation(processAssetPath, predecessor.getString("path"),
                            ProcessCenterConstants.PREDECESSOR_ASSOCIATION);
                    reg.removeAssociation(predecessor.getString("path"), processAssetPath,
                            ProcessCenterConstants.SUCCESSOR_ASSOCIATION);
                }
            }

        } catch (Exception e) {
            String errMsg = "Failed to delete predecessor: " + deletePredecessor;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return true;
    }

}
