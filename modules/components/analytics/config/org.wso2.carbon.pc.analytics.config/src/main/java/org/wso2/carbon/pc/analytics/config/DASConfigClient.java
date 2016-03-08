package org.wso2.carbon.pc.analytics.config;

/**
 * Created by samithac on 13/2/16.
 */
import org.apache.axis2.AxisFault;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.event.receiver.stub.EventReceiverAdminServiceStub;
import org.wso2.carbon.pc.analytics.config.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.ReceiverAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.StreamAdminServiceClient;

import java.rmi.RemoteException;

public class DASConfigClient {
    /*public static void main(String[] args) throws RemoteException,
            LoginAuthenticationExceptionException,
            LogoutAuthenticationExceptionException {*/
   /* private LoginAdminServiceClient login;
    private String session;
    private StreamAdminServiceClient streamAdminServiceClient;
    private ReceiverAdminServiceClient receiverAdminServiceClient;*/

    String streamName;
    String stremaVersion;
    String streamId;
    String streamDescription;
    String streamNickName;
    String receiverName;
    JSONArray processVariablesJObArr;

    public DASConfigClient(){

    }

    public void configDAS(String processVariableDetails) {

        JSONObject processInfo = null;
        try {
            processInfo = new JSONObject(processVariableDetails);

            streamName = processInfo.getString("eventStreamName");
            stremaVersion = processInfo.getString("eventStreamVersion");
            streamId=processInfo.getString("eventStreamId");
            streamDescription=processInfo.getString("eventStreamDescription");
            streamNickName=processInfo.getString("eventStreamNickName");
            receiverName=processInfo.getString("eventReceiverName");
            processVariablesJObArr = processInfo.getJSONArray("processVariables");

            /*dasConfigData["eventStreamName"]=eventStreamName;
            dasConfigData["eventStreamVersion"]=eventStreamVersion;
            dasConfigData["eventStreamDescription"]=eventStreamDescription;
            dasConfigData["eventStreamNickName"]=eventStreamNickName;
            dasConfigData["eventStreamId"]=eventStreamId;

            dasConfigData["processVariables"]=processVariables;*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    System.setProperty(
                "javax.net.ssl.trustStore",
                "/home/samithac/wso2-products/wso2das-3.0.0-SNAPSHOT/repository/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = "https://localhost:9448"; // "https://172.21.47.141:9445";

        // login to DAS as admin
        LoginAdminServiceClient login = null;
        try {
            login = new LoginAdminServiceClient(backEndUrl);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        String session = null;
        try {
            session = login.authenticate("admin", "admin");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        }
        System.out.println(session);

        ////////////////
        /*try {
            EventReceiverAdminServiceStub eventReceiverAdminServiceStub = new EventReceiverAdminServiceStub("https://localhost:9448/services/EventReceiverAdminService");
            EventReceiverAdminServiceStub eventReceiverAdminServiceStub2 = new EventReceiverAdminServiceStub("https://localhost:9448/services/EventReceiverAdminService");

            //eventReceiverAdminServiceStub.
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }*/

        ///////////////
        //create event stream
        StreamAdminServiceClient streamAdminServiceClient = null;
        try {
            streamAdminServiceClient = new StreamAdminServiceClient(backEndUrl, session,streamName,stremaVersion,streamId,streamNickName,streamDescription, processVariablesJObArr);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        /*System.out.println("getEventStreamNames::"
				+ streamAdminServiceClient.getGG());*/
		streamAdminServiceClient.createEventStream();

        //create event receiver
        ReceiverAdminServiceClient receiverAdminServiceClient= null;
        receiverAdminServiceClient = new ReceiverAdminServiceClient(backEndUrl, session,receiverName,streamId,"wso2event");
        receiverAdminServiceClient.deployEventReceiverConfiguration();
        //receiverAdminServiceClient.deployEventReceiverConfiguration(eventReceiverName, streamNameWithVersion, eventAdapterType);
        //EventReceiverAdminServiceStub eventReceiverAdminServiceStub=new EventReceiverAdminServiceStub(backEndUrl);


        try {
            login.logOut();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LogoutAuthenticationExceptionException e) {
            e.printStackTrace();
        }
    }
}