/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.pc.core.assets.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.registry.resource.services.utils.AddRolePermissionUtil;
import org.wso2.carbon.user.core.UserRealm;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public abstract class AssetResource {

    private static final Log log = LogFactory.getLog(AssetResource.class);

    /**
     * Append child element to parent element
     *
     * @param doc
     * @param parent
     * @param childName
     * @param childNS
     * @return
     */
    protected Element append(Document doc, Element parent, String childName, String childNS) {
        Element childElement = doc.createElementNS(childNS, childName);
        parent.appendChild(childElement);
        return childElement;
    }

    /**
     * Append child element text to parent element
     *
     * @param doc
     * @param parent
     * @param childName
     * @param childNS
     * @param text
     * @return
     */
    protected Element appendText(Document doc, Element parent, String childName, String childNS, String text) {
        Element childElement = doc.createElementNS(childNS, childName);
        childElement.setTextContent(text);
        parent.appendChild(childElement);
        return childElement;
    }

    /**
     * Convert xml document to String
     *
     * @param doc
     * @return
     * @throws TransformerException
     */
    protected String xmlToString(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }

    /**
     * Parse string to xml document
     *
     * @param xmlString
     * @return
     * @throws Exception
     */
    protected Document stringToXML(String xmlString) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlString)));
    }

    protected Document getXMLDocument(byte[] documentoXml) throws IOException, SAXException,
            ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(documentoXml));
    }

    /**
     * Set permission to given asset
     *
     * @param AssetRegistryPath
     * @param userName
     * @param assetName
     * @param assetVersion
     * @return
     * @throws ProcessCenterException
     */
    protected String setPermission(String AssetRegistryPath, String userName, String assetName, String assetVersion)
            throws ProcessCenterException {

        String status = "Failed to set permission";

        try {

            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry userRegistry = registryService.getGovernanceSystemRegistry();
                UserRealm userRealm = userRegistry.getUserRealm();
                String[] roles = userRealm.getUserStoreManager().getRoleListOfUser(userName);
                String path = AssetRegistryPath + assetName + "/" + assetVersion;
                for (String role : roles) {
                    if (role.equalsIgnoreCase("Internal/everyone") || role.equalsIgnoreCase("Internal/store") ||
                            role.equalsIgnoreCase("Internal/publisher")) {
                        continue;
                    } else {
                        //add read permission
                        AddRolePermissionUtil.addRolePermission(userRegistry, path, role, ProcessCenterConstants.READ,
                                ProcessCenterConstants.ALLOW);
                        //add write permission
                        AddRolePermissionUtil.addRolePermission(userRegistry, path, role, ProcessCenterConstants.WRITE,
                                ProcessCenterConstants.ALLOW);
                        //add authorize permission
                        AddRolePermissionUtil.addRolePermission(userRegistry, path, role, ProcessCenterConstants
                                .AUTHORIZE,
                                ProcessCenterConstants.ALLOW);
                    }
                }
                status = "Permission set successfully";
            }
        } catch (Exception e) {
            String errMsg = "Failed to update Permission";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
        return status;
    }
}
