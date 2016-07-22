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

package org.wso2.carbon.pc.analytics.core.kpi.utils;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.pc.analytics.core.kpi.AnalyticsConfigConstants;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.carbon.pc.core.internal.ProcessCenterServerHolder;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.securevault.SecretResolver;
import org.wso2.securevault.SecretResolverFactory;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

import org.wso2.carbon.pc.core.ProcessStore;

/**
 * Utils file for Analytics configuration related properties
 */
public class DASConfigurationUtils {
    private static final Log log = LogFactory.getLog(DASConfigurationUtils.class);

    /**
     * Check whether analytics configurations (with DAS) is made for the respective process
     *
     * @param processName
     * @param processVersion
     * @return isDASAnalyticsConfigured
     * @throws ProcessCenterException
     */
    public static boolean isDASAnalyticsConfigured(String processName, String processVersion)
            throws ProcessCenterException {
        String processContent = null;
        ProcessStore ps = new ProcessStore();
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                org.wso2.carbon.registry.core.Resource resource = reg.get(processAssetPath);
                processContent = new String((byte[]) resource.getContent());
                Document doc = ps.stringToXML(processContent);

                Element rootElement = doc.getDocumentElement();
                Element propertiesElement = (Element) rootElement.getElementsByTagName("properties").item(0);

                if (propertiesElement.getElementsByTagName(AnalyticsConfigConstants.IS_DAS_CONFIGED_TAG).getLength() > 0
                        && propertiesElement.getElementsByTagName(AnalyticsConfigConstants.IS_DAS_CONFIGED_TAG).item(0)
                        .getTextContent().equals("true")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (RegistryException e) {
            String errMsg = "Governance Registry access error, wile checking isDasConfigedForAnalytics";
            throw new ProcessCenterException(errMsg, e);
        } catch (Exception e) {
            String errMsg = "Registry process.rxt String to XML conversion error, wile checking isDasConfigedForAnalytics ";
            throw new ProcessCenterException(errMsg, e);
        }
        return false;
    }

    /**
     * Set the Property isDasConfigedForAnalytics in the process related .rxt to flag that analytics configurations (with DAS) is made for the respective process
     *
     * @param processName
     * @param processVersion
     * @throws ProcessCenterException
     */
    public static void setPropertyDASAnalyticsConfigured(String processName, String processVersion)
            throws ProcessCenterException {
        String processContent = null;
        ProcessStore ps = new ProcessStore();
        try {
            RegistryService registryService = ProcessCenterServerHolder.getInstance().getRegistryService();
            if (registryService != null) {
                UserRegistry reg = registryService.getGovernanceSystemRegistry();
                String processAssetPath = ProcessCenterConstants.PROCESS_ASSET_ROOT + processName + "/" +
                        processVersion;
                org.wso2.carbon.registry.core.Resource resource = reg.get(processAssetPath);
                processContent = new String((byte[]) resource.getContent());
                Document doc = ps.stringToXML(processContent);

                Element rootElement = doc.getDocumentElement();
                Element propertiesElement = (Element) rootElement.getElementsByTagName("properties").item(0);

                //add a new property item element if it is not existing already
                if (propertiesElement.getElementsByTagName(AnalyticsConfigConstants.IS_DAS_CONFIGED_TAG).getLength()
                        == 0) {
                    ps.appendText(doc, propertiesElement, AnalyticsConfigConstants.IS_DAS_CONFIGED_TAG,
                            ProcessCenterConstants.MNS, "true");

                    String newProcessContent = ps.xmlToString(doc);
                    resource.setContent(newProcessContent);
                    reg.put(processAssetPath, resource);

                    if (log.isDebugEnabled()) {
                        log.debug("isDasConfigedForAnalytics property in process.rxt set as true");
                    }
                }
            }
        } catch (TransformerException | RegistryException e) {
            String errMsg = "Exception in setting property isDASAnalyticsConfigured in process.rxt for the process:"
                    + processName + ":" + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (Exception e) {
            String errMsg =
                    "Exception in setting property isDASAnalyticsConfigured in process.rxt while converting xml to string for the process:"
                            + processName + ":" + processVersion;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        }
    }

    /**
     * Get content of pc.xml file as a string
     *
     * @return the content of pc.xml file as a String
     * @throws IOException
     * @throws XMLStreamException
     */
    public static OMElement getConfigElement() throws IOException, XMLStreamException {
        String carbonConfigDirPath = CarbonUtils.getCarbonConfigDirPath();
        String pcConfigPath = carbonConfigDirPath + File.separator +
                AnalyticsConfigConstants.PC_CONFIGURATION_FILE_NAME;
        File configFile = new File(pcConfigPath);
        String configContent = FileUtils.readFileToString(configFile);
        return AXIOMUtil.stringToOM(configContent);
    }

    /**
     * @return DASUrl
     * @throws IOException
     * @throws XMLStreamException
     */
    public static String getDASURL() throws IOException, XMLStreamException {
        OMElement configElement = getConfigElement();
        OMElement analyticsElement = configElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.ANALYTICS));
        if (analyticsElement != null) {
            String baseUrl = analyticsElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.CONFIG_BASE_URL))
                    .getText();
            if (baseUrl != null && !baseUrl.isEmpty()) {
                if (baseUrl.endsWith(File.separator)) {
                    //baseUrl += File.separator;
                    return baseUrl.substring(0, baseUrl.length() - 1);
                }
                return baseUrl;
            }
        }
        return null;
    }

    public static String getBPSURL() throws IOException, XMLStreamException {
        OMElement configElement = getConfigElement();
        OMElement analyticsElement = configElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.ANALYTICS));
        if (analyticsElement != null) {
            String baseUrl = analyticsElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.BPS_BASE_URL))
                    .getText();
            if (baseUrl != null && !baseUrl.isEmpty()) {
                if (baseUrl.endsWith(File.separator)) {
                    //baseUrl += File.separator;
                    return baseUrl.substring(0, baseUrl.length() - 1);
                }
                return baseUrl;
            }
        }
        return null;
    }

    /**
     * @return AuthorizationHeader
     * @throws IOException
     * @throws XMLStreamException
     */
    public static String getAuthorizationHeader() throws IOException, XMLStreamException {
        OMElement configElement = getConfigElement();
        SecretResolver secretResolver = SecretResolverFactory.create(configElement, false);
        OMElement analyticsElement = configElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.ANALYTICS));

        String userName = null;
        char[] password=null;
        if (analyticsElement != null) {
            userName = analyticsElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.CONFIG_USER_NAME))
                    .getText();
            if (secretResolver != null && secretResolver.isInitialized()) {
                if (secretResolver.isTokenProtected(AnalyticsConfigConstants.SECRET_ALIAS_BPS_PASSWORD)) {
                    password = secretResolver.resolve(AnalyticsConfigConstants.SECRET_ALIAS_BPS_PASSWORD).toCharArray();
                } else {
                    password = analyticsElement
                            .getFirstChildWithName(new QName(AnalyticsConfigConstants.CONFIG_PASSWORD)).getText().toCharArray();
                }
            } else {
                password = analyticsElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.CONFIG_PASSWORD))
                        .getText().toCharArray();
            }
        }
        if (userName != null && password != null) {
            String headerPortion = userName + ":" + String.valueOf(password);
            byte[] encodedBytes = headerPortion.getBytes("UTF-8");
            String encodedString = DatatypeConverter.printBase64Binary(encodedBytes);
            //requestHeader += encodedString;
            return AnalyticsConfigConstants.REQUEST_HEADER_BASIC+" " + encodedString;
        }
        return null;
    }

    /**
     * Get DAS user name
     *
     * @return dasUserName
     * @throws IOException
     * @throws XMLStreamException
     */
    public static String getDASUserName() throws IOException, XMLStreamException {
        OMElement configElement = getConfigElement();
        OMElement analyticsElement = configElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.ANALYTICS));
        if (analyticsElement != null) {
            String dasUserName = analyticsElement
                    .getFirstChildWithName(new QName(AnalyticsConfigConstants.DAS_USER_NAME)).getText();
            if (dasUserName != null && !dasUserName.isEmpty()) {
                if (dasUserName.endsWith(File.separator)) {
                    return dasUserName.substring(0, dasUserName.length() - 1);
                }
                return dasUserName;
            }
        }
        return null;
    }

    /**
     * Get DAS Password
     *
     * @return dasPassword
     * @throws IOException
     * @throws XMLStreamException
     */
    public static String getDASPassword() throws IOException, XMLStreamException {
        OMElement configElement = getConfigElement();
        OMElement analyticsElement = configElement.getFirstChildWithName(new QName(AnalyticsConfigConstants.ANALYTICS));
        if (analyticsElement != null) {
            String dasPassword = analyticsElement
                    .getFirstChildWithName(new QName(AnalyticsConfigConstants.DAS_PASSWORD)).getText();
            if (dasPassword != null && !dasPassword.isEmpty()) {
                if (dasPassword.endsWith(File.separator)) {
                    return dasPassword.substring(0, dasPassword.length() - 1);
                }
                return dasPassword;
            }
        }
        return null;
    }
}