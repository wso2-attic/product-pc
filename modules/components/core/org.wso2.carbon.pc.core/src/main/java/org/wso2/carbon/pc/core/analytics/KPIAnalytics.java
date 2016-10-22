package org.wso2.carbon.pc.core.analytics;

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
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.pc.core.util.Utils;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * KPI Analytics related class.
 */
public class KPIAnalytics {
    private static final Log log = LogFactory.getLog(KPIAnalytics.class);

    /**
     * Get a list of configured process variables and their types, for analytics
     *
     * @param resourcePath process resource path
     * @return JSON Object in string representation, which includes the configured process variables for analytics
     * @throws ProcessCenterException Throws ProcessCenterException, if failed to get the process variables list
     */
    public String getProcessVariablesList(String resourcePath) throws ProcessCenterException {
        String resourceString = "";
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                resourcePath = resourcePath.substring(ProcessCenterConstants.GREG_PATH.length());
                Resource resourceAsset = reg.get(resourcePath);
                String resourceContent = new String((byte[]) resourceAsset.getContent());

                JSONObject procVariablesJob = new JSONObject();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(
                        new InputSource(new StringReader(resourceContent)));

                JSONArray variableArray = new JSONArray();

                procVariablesJob.put(ProcessCenterConstants.PROCESS_VARIABLES, variableArray);

                NodeList processVariableElements = ((Element) document.getFirstChild())
                        .getElementsByTagName(ProcessCenterConstants.PROCESS_VARIABLE);

                if (processVariableElements.getLength() != 0) {
                    for (int i = 0; i < processVariableElements.getLength(); i++) {
                        Element processVariableElement = (Element) processVariableElements.item(i);
                        String processVariableName = processVariableElement.getElementsByTagName("name").item(0)
                                .getTextContent();
                        String processVariableType = processVariableElement.getElementsByTagName("type").item(0)
                                .getTextContent();
                        String isAnalyzeData = processVariableElement
                                .getElementsByTagName(ProcessCenterConstants.IS_ANALYZE_DATA).item(0).getTextContent();
                        String isDrillDownVariable = processVariableElement
                                .getElementsByTagName(ProcessCenterConstants.IS_DRILL_DOWN_VARIABLE).item(0)
                                .getTextContent();

                        JSONObject processVariable = new JSONObject();
                        processVariable.put("name", processVariableName);
                        processVariable.put("type", processVariableType);
                        processVariable.put(ProcessCenterConstants.IS_ANALYZE_DATA, isAnalyzeData);
                        processVariable.put(ProcessCenterConstants.IS_DRILL_DOWN_VARIABLE, isDrillDownVariable);
                        variableArray.put(processVariable);
                    }
                }
                resourceString = procVariablesJob.toString();
            }
        } catch (Exception e) {
            String errMsg = "Failed to get the process variables list";
            throw new ProcessCenterException(errMsg, e);
        }
        return resourceString;
    }

    public void removeAllProcessVariables(String resourcePath) throws ProcessCenterException {
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                Resource resource = reg.get(resourcePath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                if (doc.getElementsByTagName(ProcessCenterConstants.PROCESS_VARIABLE).getLength() != 0) {
                    NodeList variableElements = ((Element) doc.getFirstChild())
                            .getElementsByTagName(ProcessCenterConstants.PROCESS_VARIABLE);
                    ArrayList<Node> delete = new ArrayList<Node>();

                    for (int i = 0; i < variableElements.getLength(); i++) {
                        Node node = variableElements.item(i);
                        NodeList childList = node.getChildNodes();

                        for (int x = 0; x < childList.getLength(); x++) {
                            Node child = childList.item(x);
                            // To search only "process_variable" children
                            if (child.getNodeType() == Node.ELEMENT_NODE &&
                                    child.getNodeName().equalsIgnoreCase("name")) {
                                // add to "to be deleted" list
                                delete.add(node);
                                break;
                            }
                        }
                    }
                    for(int i = 0; i < delete.size(); i++) {
                        Node node = delete.get(i);
                        node.getParentNode().removeChild(node);
                    }
                    String newDeleteVarProcessContent = Utils.xmlToString(doc);
                    resource.setContent(newDeleteVarProcessContent);
                    reg.put(resourcePath, resource);
                }
            }
        } catch (Exception e) {
            String errMsg = "Failed to delete all process variables";
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Save the process variables in process rxt which need to be configured for analytics
     *
     * @param processVariableDetails process variable details
     * @throws ProcessCenterException Throws ProcessCenterException, if Failed to save processVariables in process rxt
     */
    public void saveProcessVariables(String processVariableDetails) throws ProcessCenterException {
        String processContent = null;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();

                JSONObject processInfo = new JSONObject(processVariableDetails);
                String processName = processInfo.getString(ProcessCenterConstants.PROCESS_NAME);
                String processVersion = processInfo.getString(ProcessCenterConstants.PROCESS_VERSION);
                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                removeAllProcessVariables(processAssetPath);
                Resource resource = reg.get(processAssetPath);
                processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);
                JSONObject processVariablesJOb = processInfo.getJSONObject(ProcessCenterConstants.PROCESS_VARIABLES);

                Iterator<?> keys = processVariablesJOb.keys();
                //saving pracess variable name,type as sub elements
                while (keys.hasNext()) {
                    String variableName = (String) keys.next();
                    String[] varMetaData = processVariablesJOb.get(variableName).toString().split("##");
                    String variableType = varMetaData[0];
                    String isAnalyzeData = varMetaData[1];
                    String isDrillDownVariable = varMetaData[2];
                    Element rootElement = doc.getDocumentElement();
                    Element variableElement = Utils.append(doc, rootElement, ProcessCenterConstants.PROCESS_VARIABLE,
                            ProcessCenterConstants.MNS);
                    Utils.appendText(doc, variableElement, "name", ProcessCenterConstants.MNS, variableName);
                    Utils.appendText(doc, variableElement, "type", ProcessCenterConstants.MNS, variableType);
                    Utils.appendText(doc, variableElement, ProcessCenterConstants.IS_ANALYZE_DATA, ProcessCenterConstants.MNS,
                            isAnalyzeData);
                    Utils.appendText(doc, variableElement, ProcessCenterConstants.IS_DRILL_DOWN_VARIABLE,
                            ProcessCenterConstants.MNS, isDrillDownVariable);

                    String newProcessContent = Utils.xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);
                }
                log.info("Saved process variables to configure analytics");
                if (log.isDebugEnabled()) {
                    log.debug("Saved process variables to configure analytics.Saved info:" + processVariableDetails);
                }
            }
        } catch (Exception e) {
            String errMsg =
                    "Failed to save processVariables with info,\n" + processVariableDetails + "\n,to the process.rxt";
            throw new ProcessCenterException(e);
        }
    }

    /**
     * Get the configured event stream and receiver information (for analytics with DAS), of the process
     *
     * @param resourcePath path for the process resource, in governance registry
     * @return Information of Event Stream and Reciever, configured for the process
     * @throws ProcessCenterException Throws ProcessCenterException if failed to get the event stream and receiver info
     */
    public String getStreamAndReceiverInfo(String resourcePath) throws ProcessCenterException {
        String resourceString = "";
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
                JSONArray variableArray = new JSONArray();
                Element dasConfigInfoElement = (Element) ((Element) document.getFirstChild())
                        .getElementsByTagName(ProcessCenterConstants.ANALYTICS_CONFIG_INFO).item(0);
                String processDefinitionId = dasConfigInfoElement
                        .getElementsByTagName(ProcessCenterConstants.PROCESS_DEFINITION_ID).item(0).getTextContent();
                String eventStreamName = dasConfigInfoElement
                        .getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_NAME).item(0).getTextContent();
                String eventStreamVersion = dasConfigInfoElement
                        .getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_VERSION).item(0).getTextContent();
                String eventStreamDescription = dasConfigInfoElement
                        .getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_DESCRIPTION).item(0).getTextContent();
                String eventStreamNickName = dasConfigInfoElement
                        .getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_NICK_NAME).item(0).getTextContent();
                String eventReceiverName = dasConfigInfoElement
                        .getElementsByTagName(ProcessCenterConstants.EVENT_RECEIVER_NAME).item(0).getTextContent();

                JSONObject dasConfigInfoJOb = new JSONObject();
                dasConfigInfoJOb.put(ProcessCenterConstants.PROCESS_DEFINITION_ID, processDefinitionId);
                dasConfigInfoJOb.put(ProcessCenterConstants.EVENT_STREAM_NAME, eventStreamName);
                dasConfigInfoJOb.put(ProcessCenterConstants.EVENT_STREAM_VERSION, eventStreamVersion);
                dasConfigInfoJOb.put(ProcessCenterConstants.EVENT_STREAM_DESCRIPTION, eventStreamDescription);
                dasConfigInfoJOb.put(ProcessCenterConstants.EVENT_STREAM_NICK_NAME, eventStreamNickName);
                dasConfigInfoJOb.put(ProcessCenterConstants.EVENT_RECEIVER_NAME, eventReceiverName);
                resourceString = dasConfigInfoJOb.toString();
            }
        } catch (Exception e) {
            String errMsg = "Failed to get the event stream and receiver info";
            throw new ProcessCenterException(errMsg, e);
        }
        return resourceString;
    }

    /**
     * Save event stream and receiver information configured for analytics with DAS, for the particular process, in
     * governance registry in the process.rxt
     *
     * @param dasConfigData  Analytics configuration details
     * @param processName    process name
     * @param processVersion process version
     * @throws ProcessCenterException Throws ProcessCenterException, if failed to save das configuration details
     */
    public void saveStreamAndReceiverInfo(String dasConfigData, String processName, String processVersion)
            throws ProcessCenterException {
        String processContent = null;
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                JSONObject dasConfigDataJOb = new JSONObject(dasConfigData);
                String processDefinitionId = dasConfigDataJOb.getString(ProcessCenterConstants.PROCESS_DEFINITION_ID);
                String eventStreamName = dasConfigDataJOb.getString(ProcessCenterConstants.EVENT_STREAM_NAME);
                String eventStreamVersion = dasConfigDataJOb.getString(ProcessCenterConstants.EVENT_STREAM_VERSION);
                String eventStreamDescription = dasConfigDataJOb
                        .getString(ProcessCenterConstants.EVENT_STREAM_DESCRIPTION);
                String eventStreamNickName = dasConfigDataJOb.getString(ProcessCenterConstants.EVENT_STREAM_NICK_NAME);
                String eventReceiverName = dasConfigDataJOb.getString(ProcessCenterConstants.EVENT_RECEIVER_NAME);
                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                Resource resource = reg.get(processAssetPath);
                processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                Element rootElement = doc.getDocumentElement();
                if (doc.getElementsByTagName(ProcessCenterConstants.ANALYTICS_CONFIG_INFO).getLength() == 0) {
                    Element dasConfigInfoElement = Utils.append(doc, rootElement,
                            ProcessCenterConstants.ANALYTICS_CONFIG_INFO, ProcessCenterConstants.MNS);
                    Utils.appendText(doc, dasConfigInfoElement, ProcessCenterConstants.PROCESS_DEFINITION_ID,
                            ProcessCenterConstants.MNS, processDefinitionId);
                    Utils.appendText(doc, dasConfigInfoElement, ProcessCenterConstants.EVENT_STREAM_NAME,
                            ProcessCenterConstants.MNS, eventStreamName);
                    Utils.appendText(doc, dasConfigInfoElement, ProcessCenterConstants.EVENT_STREAM_VERSION,
                            ProcessCenterConstants.MNS, eventStreamVersion);
                    Utils.appendText(doc, dasConfigInfoElement, ProcessCenterConstants.EVENT_STREAM_DESCRIPTION,
                            ProcessCenterConstants.MNS, eventStreamDescription);
                    Utils.appendText(doc, dasConfigInfoElement, ProcessCenterConstants.EVENT_STREAM_NICK_NAME,
                            ProcessCenterConstants.MNS, eventStreamNickName);
                    Utils.appendText(doc, dasConfigInfoElement, ProcessCenterConstants.EVENT_RECEIVER_NAME,
                            ProcessCenterConstants.MNS, eventReceiverName);
                } else {
                    doc.getElementsByTagName(ProcessCenterConstants.PROCESS_DEFINITION_ID).item(0)
                            .setTextContent(processDefinitionId);
                    doc.getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_NAME).item(0)
                            .setTextContent(eventStreamName);
                    doc.getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_VERSION).item(0)
                            .setTextContent(eventStreamVersion);
                    doc.getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_DESCRIPTION).item(0)
                            .setTextContent(eventStreamDescription);
                    doc.getElementsByTagName(ProcessCenterConstants.EVENT_STREAM_NICK_NAME).item(0)
                            .setTextContent(eventStreamNickName);
                    doc.getElementsByTagName(ProcessCenterConstants.EVENT_RECEIVER_NAME).item(0)
                            .setTextContent(eventReceiverName);
                }

                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);
                if (log.isDebugEnabled()) {
                    log.debug("The Saved das configuration details in registry. Saved details : " + dasConfigData);
                }
            }
        } catch (TransformerException | JSONException | RegistryException e) {
            String errMsg =
                    "Failed to save das configuration details with info,\n" + dasConfigData + "\n,to the process.rxt";
            throw new ProcessCenterException(errMsg, e);
        } catch (Exception e) {
            String errMsg =
                    "Failed to save das configuration details with info,\n" + dasConfigData + "\n,to the process.rxt";
            throw new ProcessCenterException(errMsg, e);
        }
    }

    public void deleteProcessVariable(String deleteVariableDetails, String user) throws ProcessCenterException {
        try {
            RegistryService registryService =
                    ProcessCenterServerHolder.getInstance().getRegistryService();

            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceUserRegistry(user);

                JSONObject variableInfo = new JSONObject(deleteVariableDetails);
                String processName = variableInfo.getString("processName");
                String processVersion = variableInfo.getString("processVersion");
                JSONArray variableObjArray = variableInfo.getJSONArray(ProcessCenterConstants.PROCESS_VARIABLE_LIST);

                String processAssetPath =
                        ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                                processVersion;
                Resource resource = reg.get(processAssetPath);
                String processContent = new String((byte[]) resource.getContent());
                Document doc = Utils.stringToXML(processContent);

                NodeList variableElements = ((Element) doc.getFirstChild())
                        .getElementsByTagName(ProcessCenterConstants.PROCESS_VARIABLE);
                for (int i = 0; i < variableObjArray.length(); i++) {
                    String variableName =
                            variableObjArray.getJSONObject(i).getString(ProcessCenterConstants
                                    .VARIABLE_NAME);
                    String variableType =
                            variableObjArray.getJSONObject(i).getString(ProcessCenterConstants.VARIABLE_TYPE);
                    String isAnalyzedData = variableObjArray.getJSONObject(i)
                            .getString(ProcessCenterConstants.IS_ANALYZE_DATA);
                    String isDrillDownData = variableObjArray.getJSONObject(i)
                            .getString(ProcessCenterConstants.IS_DRILL_DOWN_DATA);

                    for (int j = 0; j < variableElements.getLength(); j++) {
                        Element variableElement = (Element) variableElements.item(j);
                        String varName = variableElement.getElementsByTagName("name").item(0)
                                .getTextContent();
                        String varType = variableElement.getElementsByTagName("type").item(0)
                                .getTextContent();
                        String varAnalyzedData = variableElement
                                .getElementsByTagName(ProcessCenterConstants.IS_ANALYZE_DATA).item(0).getTextContent();
                        String varDrillDownData = variableElement
                                .getElementsByTagName(ProcessCenterConstants.IS_DRILL_DOWN_VARIABLE).item(0)
                                .getTextContent();

                        if (varName.equals(variableName) && varType.equals(variableType) &&
                                varAnalyzedData.equals(isAnalyzedData) &&
                                varDrillDownData.equals(isDrillDownData)) {
                            variableElement.getParentNode().removeChild(variableElement);
                            break;
                        }
                    }
                }
                String newProcessContent = Utils.xmlToString(doc);
                resource.setContent(newProcessContent);
                reg.put(processAssetPath, resource);
            }
        } catch(Exception e) {
            String errMsg = "Failed to delete process variable list: " + deleteVariableDetails;
            throw new ProcessCenterException(errMsg,e);
        }
    }
}
