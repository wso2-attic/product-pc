package org.wso2.carbon.pc.analytics.config;

/**
 * Created by samithac on 13/2/16.
 */
import org.apache.axis2.AxisFault;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.pc.analytics.config.clients.LoginAdminServiceClient;
import org.wso2.carbon.pc.analytics.config.clients.ReceiverAdminServiceClient;

import java.rmi.RemoteException;

public class DASConfigClient {
    /*public static void main(String[] args) throws RemoteException,
            LoginAuthenticationExceptionException,
            LogoutAuthenticationExceptionException {*/

    public DASConfigClient() {


    System.setProperty(
                "javax.net.ssl.trustStore",
                "/home/samithac/wso2-products/wso2das-3.0.0-SNAPSHOT/repository/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = "https://localhost:9443"; // "https://172.21.47.141:9445";

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

		/*StreamAdminServiceClient streamAdminServiceClient = new StreamAdminServiceClient(
				backEndUrl, session);
		System.out.println("getEventStreamNames::"
				+ streamAdminServiceClient.getGG());
		streamAdminServiceClient.doSomeTestingTasks();*/


        ReceiverAdminServiceClient receiverAdminServiceClient= null;
        try {
            receiverAdminServiceClient = new ReceiverAdminServiceClient(backEndUrl, session);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        receiverAdminServiceClient.deployEventReceiverConfiguration("TestEventReceiver5", "TestStream:1.0.0", "wso2event");
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