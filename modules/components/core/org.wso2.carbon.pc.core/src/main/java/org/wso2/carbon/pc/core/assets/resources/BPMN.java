package org.wso2.carbon.pc.core.assets.resources;


import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.wso2.carbon.pc.core.ProcessCenterConstants;

import java.util.HashMap;
import java.util.Map;

public class BPMN {
    private Document BPMNDocument;
    private Map<String, String> variableMap = new HashMap<>();

    public BPMN(Document BPMNDocument){
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
}