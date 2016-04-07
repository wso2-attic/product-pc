package org.wso2.carbon.pc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.governance.api.common.GovernanceArtifactManager;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.common.AttributeSearchService;
import org.wso2.carbon.registry.common.ResourceData;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.indexing.IndexingConstants;
import org.wso2.carbon.registry.indexing.service.ContentSearchService;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathya on 3/24/16.
 */
public class PDFSearchService  {

    private static final Log log = LogFactory.getLog(PDFSearchService.class);

    public String search(String searchQuery, String username){

        String processString = null;
         ContentSearchService contentSearchService = ProcessCenterServerHolder.getInstance().getContentSearchService();
        AttributeSearchService attributeSearchService = ProcessCenterServerHolder.getInstance().getAttributeSearchService();
         try {

             PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(username);
             ResourceData[] data = contentSearchService.search(searchQuery);
             log.info(data[0].getResourcePath());

           //  contentSearchService.search(registry, "summary");
             Map<String, String> input = new HashMap<>();
             input.put(IndexingConstants.FIELD_MEDIA_TYPE, "application/pdf");
             input.put(IndexingConstants.FIELD_CONTENT, searchQuery);
             ResourceData[] pdfResources = attributeSearchService.search(input);

             if(pdfResources.length > 0 && pdfResources != null){

                 JSONArray result = new JSONArray();

                 RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
                 if (registryService != null) {
                     UserRegistry reg = registryService.getGovernanceSystemRegistry();


                     for(ResourceData pdf : pdfResources){

                         String resourcePath = pdf.getResourcePath();
                         Association[] associations = reg.getAllAssociations(resourcePath.substring("/_system/governance/".length()));
                         for(Association association : associations){
                             String destinationPath = association.getDestinationPath();
                             Resource processResource = reg.get(destinationPath);
                             String lifecycleState = processResource.getProperty("registry.lifecycle.SampleLifeCycle2.state");                             String mediaType = "application/vnd.wso2-process+xml";
                             if(processResource.getMediaType().equals(mediaType)){

                                 String processContent = new String((byte[]) processResource.getContent());
                                 Document processXML = stringToXML(processContent);
                                 String processName = processXML.getElementsByTagName("name").item(0).getTextContent();
                                 String processVersion = processXML.getElementsByTagName("version").item(0).getTextContent();

                                 JSONObject processJSON = new JSONObject();
                                 processJSON.put("processid", processResource.getUUID());
                                 processJSON.put("type", "process");
                                 processJSON.put("path", destinationPath);
                                 processJSON.put("mediaType", "application/vnd.wso2-process+xml");
                                 processJSON.put("processname", processName);
                                 processJSON.put("processversion", processVersion);
                                 processJSON.put("lifecyclestate", lifecycleState);

                                 result.put(processJSON);

                                 processString = result.toString();

                             }

                         }
                     }
                 }
             }
         }catch (Exception ex){
             log.error(ex.getMessage(), ex);
             ex.printStackTrace();
         }

        return processString;

     }


    private Document stringToXML(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        return document;
    }


}
