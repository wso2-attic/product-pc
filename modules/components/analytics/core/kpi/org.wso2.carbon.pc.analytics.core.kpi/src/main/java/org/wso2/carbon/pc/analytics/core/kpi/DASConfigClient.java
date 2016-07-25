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
package org.wso2.carbon.pc.analytics.core.kpi;

/**
 * Configure DAS configurations to publish data to DAS an do the analytics
 * (initiator class in the module)
 */

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.pc.analytics.core.kpi.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.core.kpi.internal.PCAnalyticsServerHolder;
import org.wso2.carbon.pc.analytics.core.kpi.utils.DASConfigurationUtils;
import org.wso2.carbon.pc.core.ProcessCenterException;
import org.wso2.securevault.SecretResolver;
import org.wso2.securevault.SecretResolverFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;

public class DASConfigClient {

    String streamName;
    String stremaVersion;
    String streamId;
    String streamDescription;
    String streamNickName;
    String receiverName;
    JSONArray processVariables;
    String dasUrl = null;
    private static final Log log = LogFactory.getLog(DASConfigClient.class);

    /**
     * Configure WSO2 DAS for analytics, by creating an Event Stream, Event Receiver for each process.
     * In the event stream a field for processInstanceId too would be set.
     *
     * @param dasConfigDetails Data given by the user for the configurations. Ex:
     *    {"processDefinitionId":"myProcess:1:27504","eventStreamName":"j_77_process_stream",
     *    "eventStreamVersion":"1.0.0","eventStreamDescription":"This is the event stream generated to configure process
     *    analytics with DAS, for the processj_77","eventStreamNickName":"j_77_process_stream","eventStreamId":
     *    "j_77_process_stream:1.0.0","eventReceiverName":"j_77_process_receiver","pcProcessId":"j:77",
     *    "processVariables":[{"name":"processInstanceId","type":"string","isAnalyzeData":"false","isDrillDownData":"false"},
     *    {"name":"valuesAvailability","type":"string","isAnalyzeData":"false","isDrillDownData":"false"},
     *    {"name":"custid","type":"string","isAnalyzeData":false,"isDrillDownData":false},
     *    {"name":"amount","type":"long","isAnalyzeData":false,"isDrillDownData":false},
     *    {"name":"confirm","type":"bool","isAnalyzeData":false,"isDrillDownData":false}]}
     * @throws ProcessCenterException
     * @throws RemoteException
     * @throws LogoutAuthenticationExceptionException
     */
    public void configDAS(String dasConfigDetails, String processName, String processVersion)
            throws ProcessCenterException, RemoteException, LogoutAuthenticationExceptionException {

        JSONObject dasConfigDetailsJOb = null;
        LoginAdminServiceClient loginServiceClient = null;
        String dasUsername = null;
        char[] dasPassword = null;

        try {
            //String requestHeader = "Basic ";
            OMElement configElement = DASConfigurationUtils.getConfigElement();
            SecretResolver secretResolver = SecretResolverFactory.create(configElement, false);
            /*OMElement analyticsElement = configElement
                    .getFirstChildWithName(new QName(AnalyticsConfigConstants.ANALYTICS));*/
            dasUsername = DASConfigurationUtils.getDASUserName();
            ///
            if (secretResolver != null && secretResolver.isInitialized()) {
                if (secretResolver.isTokenProtected(AnalyticsConfigConstants.SECRET_ALIAS_DAS_PASSWORD)) {
                    dasPassword = secretResolver.resolve(AnalyticsConfigConstants.SECRET_ALIAS_DAS_PASSWORD)
                            .toCharArray();
                } else {
                    dasPassword = DASConfigurationUtils.getDASPassword().toCharArray();
                }
            } else {
                dasPassword = DASConfigurationUtils.getDASPassword().toCharArray();
            }

            dasUrl = DASConfigurationUtils.getDASURL();
            dasConfigDetailsJOb = new JSONObject(dasConfigDetails);
            streamName = dasConfigDetailsJOb.getString(AnalyticsConfigConstants.EVENT_STREAM_NAME);
            stremaVersion = dasConfigDetailsJOb.getString(AnalyticsConfigConstants.EVENT_STREAM_VERSION);
            streamId = dasConfigDetailsJOb.getString(AnalyticsConfigConstants.EVENT_STREAM_ID);
            streamDescription = dasConfigDetailsJOb.getString(AnalyticsConfigConstants.EVENT_STREAM_DESCRIPTION);
            streamNickName = dasConfigDetailsJOb.getString(AnalyticsConfigConstants.EVENT_STREAM_NICK_NAME);
            receiverName = dasConfigDetailsJOb.getString(AnalyticsConfigConstants.EVENT_RECEIVER_NAME);
            processVariables = dasConfigDetailsJOb.getJSONArray(AnalyticsConfigConstants.PROCESS_VARIABLES);

            //login to DAS
            loginServiceClient = PCAnalyticsServerHolder.getInstance().getLoginAdminServiceClient();
            String session = loginServiceClient.authenticate(dasUsername, dasPassword);

            //remove the password from memory
            Arrays.fill(dasPassword, ' ');
            dasPassword = null;

            //create event stream // The payload data is as>> "process instance id, valueAvailability, actual process
            // variables list
            PCAnalyticsServerHolder.getInstance().getStreamAdminServiceClient()
                    .createEventStream(session, streamName, stremaVersion, streamId, streamNickName, streamDescription,
                            processVariables);
            if (log.isDebugEnabled()) {
                log.debug("Created the Event Stream: " + streamId + " in WSO2 DAS on :" + dasUrl);
            }

            //create event receiver
            PCAnalyticsServerHolder.getInstance().getReceiverAdminServiceClient()
                    .deployEventReceiverConfiguration(session, receiverName, streamId,
                            AnalyticsConfigConstants.WSO2_EVENT);
            if (log.isDebugEnabled()) {
                log.debug("Created the Event Receiver: " + receiverName + " for the " + streamId + " in WSO2 DAS");
            }

            //now send the REST Post call to the WSO2 BPS to communicate the analytics configuration details to the
            // BPS from PC
            BPSConfigRestClient.post(dasConfigDetails, processName, processVersion);

            //logging out from DAS Admin Services
            loginServiceClient.logOut();
        } catch (LoginAuthenticationExceptionException e) {
            String errMsg = "Error in Login to DAS at :" + dasUrl + "trying to login with username : " + dasUsername
                    + " and the given password";
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (LogoutAuthenticationExceptionException e) {
            String errMsg = "Error in Logout from DAS at :" + dasUrl;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (AxisFault | JSONException | XMLStreamException e) {
            String errMsg = "Error in DAS configuration, using :" + dasConfigDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (RemoteException e) {
            String errMsg = "Error in DAS configuration, using :" + dasConfigDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (IOException e) {
            String errMsg = "Error in DAS configuration, using :" + dasConfigDetails;
            log.error(errMsg, e);
            throw new ProcessCenterException(errMsg, e);
        } catch (ProcessCenterException e) {
            log.error(e.getMessage(), e);
            throw new ProcessCenterException(e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new ProcessCenterException(e.getMessage(), e);
        } finally {
            loginServiceClient.logOut();
        }
    }
}