package org.wso2.carbon.processCenter.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.processCenter.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.processCenter.core.models.Process;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Created by dilini on 1/18/16.
 */
public class ProcessStore {

    private Document stringToXML(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        return document;
    }

    public Process[] getProcessByName(String name){
        Process processes[] = new Process[2];
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            System.out.println("############## " + registryService);
            if (registryService != null) {
                UserRegistry reg = null;
                reg = registryService.getGovernanceSystemRegistry();
                String[] processPaths = GovernanceUtils.findGovernanceArtifacts("application/vnd.wso2-process+xml", reg);
                System.out.println("########### in" + processPaths.length);
                for (int i = 0; i < processPaths.length; i++) {
                    ///processes/abc/1.0.1
                    System.out.println("######### " + processPaths[i]);
                    String words[] = processPaths[i].split("/");
                    Process process = new Process(words[2], words[3]);
                    processes[i] = process;
                    System.out.println("######### " +processes[i].getName() + " " + processes[i].getVersion());
                }
                JSONArray result = new JSONArray();

                try {
                    for (String processPath : processPaths) {
                        Resource processResource = reg.get(processPath);
                        String processContent = new String((byte[]) processResource.getContent());
                        Document processXML = stringToXML(processContent);
                        String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                        String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();

                        JSONObject processJSON = new JSONObject();
                        processJSON.put("path", processPath);
                        processJSON.put("processid", processResource.getUUID());
                        processJSON.put("processname", processName);
                        processJSON.put("processversion", processVersion);
                        result.put(processJSON);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (RegistryException e) {
            e.printStackTrace();
        }
        return processes;
    }
}
