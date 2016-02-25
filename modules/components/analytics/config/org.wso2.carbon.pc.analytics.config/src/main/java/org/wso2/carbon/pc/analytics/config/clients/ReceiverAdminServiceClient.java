package org.wso2.carbon.pc.analytics.config.clients;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.event.receiver.stub.EventReceiverAdminServiceStub;
import org.wso2.carbon.event.receiver.stub.types.BasicInputAdapterPropertyDto;

//import das.config.service.stubs.EventReceiverAdminServiceStub;

public class ReceiverAdminServiceClient {
	private final String serviceName = "EventReceiverAdminService";
	private EventReceiverAdminServiceStub eventReceiverAdminServiceStub;
	//private EventReceiverAdminService eventReceiverAdminServiceStub;

	private String endPoint;

	public ReceiverAdminServiceClient(String backEndUrl, String sessionCookie)
			throws AxisFault {
		this.endPoint = backEndUrl + "/services/" + serviceName;
		eventReceiverAdminServiceStub = new EventReceiverAdminServiceStub(endPoint);
		// Authenticate Your stub from sessionCooke
		ServiceClient serviceClient;
		Options option;

		serviceClient = eventReceiverAdminServiceStub._getServiceClient();
		option = serviceClient.getOptions();
		// option.setAction("urn:listServices");
		// System.out.println(option.getAction());
		// option.setTo(new EndpointReference(endPoint));
		option.setManageSession(true);
		option.setProperty(
				org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
				sessionCookie);

	}

	public void deployEventReceiverConfiguration(String eventReceiverName,String streamNameWithVersion,String eventAdapterType) {
		
		/*try {
			eventReceiverAdminServiceStub=new EventReceiverAdminServiceStub();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*DeployTextEventReceiverConfiguration textEventReceiverConfiguration=new DeployTextEventReceiverConfiguration();

		textEventReceiverConfiguration.setEventReceiverName(eventReceiverName);
		textEventReceiverConfiguration.setStreamNameWithVersion(streamNameWithVersion);
		textEventReceiverConfiguration.setEventAdapterType(eventAdapterType);
		//textEventReceiverConfiguration.setMappingEnabled(false);
		
		BasicInputAdapterPropertyDto props[]=new BasicInputAdapterPropertyDto[1];
		props[0]=new BasicInputAdapterPropertyDto();	
		props[0].setKey("events.duplicated.in.cluster");
		props[0].setValue("false");
		
		textEventReceiverConfiguration.setInputPropertyConfiguration(props);
		
		*//*
		EventMappingPropertyDto[] mappingProps= new EventMappingPropertyDto[1];
		mappingProps[0]=new EventMappingPropertyDto();
		mappingProps[0].setName("customMapping");
		//mappingProps[0].setDefaultValue("disable");
		mappingProps[0].setType("wso2event");	
		mappingProps[0].setValueOf("disable");
		textEventReceiverConfiguration.setInputMappings(mappingProps); *//*
		
		//textEventReceiverConfiguration.setInputMappings();
		try {
			eventReceiverAdminServiceStub.deployTextEventReceiverConfiguration(textEventReceiverConfiguration);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//////////////////////////////////

		/*DeployXmlEventReceiverConfiguration xmlEventReceiverConfiguration=new DeployXmlEventReceiverConfiguration();
		xmlEventReceiverConfiguration.setEventAdapterType(eventAdapterType);
		xmlEventReceiverConfiguration.setEventReceiverName(eventReceiverName);
		xmlEventReceiverConfiguration.setStreamNameWithVersion(streamNameWithVersion);
		xmlEventReceiverConfiguration.setEventAdapterType(eventAdapterType);
		EventMappingPropertyDto[] x= new EventMappingPropertyDto[1];
		x[0]=new EventMappingPropertyDto();
		x[0].setName("customMapping");
		x[0].setDefaultValue("disable");
		x[0].setType("wso2event");
		
		xmlEventReceiverConfiguration.setInputMappings(x);
		//textEventReceiverConfiguration.setInputMappings();
		try {
			eventReceiverAdminServiceStub.deployXmlEventReceiverConfiguration(xmlEventReceiverConfiguration);
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/

		//////////////////////
		/*EventReceiverAdminServiceStub.DeployWso2EventReceiverConfiguration wso2EventReceiverConfiguration=new EventReceiverAdminServiceStub.DeployWso2EventReceiverConfiguration();
		wso2EventReceiverConfiguration.setEventReceiverName(eventReceiverName);
		wso2EventReceiverConfiguration.setEventAdapterType(eventAdapterType);
		wso2EventReceiverConfiguration.setFromStreamNameWithVersion(streamNameWithVersion);
		wso2EventReceiverConfiguration.setMappingEnabled(false);
		wso2EventReceiverConfiguration.setFromStreamNameWithVersion("");

		*//*BasicInputAdapterPropertyDto props[]=new BasicInputAdapterPropertyDto[1];
		props[0]=new BasicInputAdapterPropertyDto();
		props[0].setKey("events.duplicated.in.cluster");
		props[0].setValue("false");

		wso2EventReceiverConfiguration.setInputPropertyConfiguration(props);*//*

		*//*WSO2EventInputMapping wso2EventInputMapping = new WSO2EventInputMapping();
		wso2EventInputMapping.setCustomMappingEnabled(mappingEnabled);*//*

		EventReceiverAdminServiceStub.DeployWso2EventReceiverConfigurationResponse deployWso2EventReceiverConfigurationResponse;
		try {
			deployWso2EventReceiverConfigurationResponse=eventReceiverAdminServiceStub.deployWso2EventReceiverConfiguration(wso2EventReceiverConfiguration);

		} catch (RemoteException e) {
			e.printStackTrace();
		}*/


		///////////
		BasicInputAdapterPropertyDto props[]=new BasicInputAdapterPropertyDto[1];
		props[0]=new BasicInputAdapterPropertyDto();
		props[0].setKey("events.duplicated.in.cluster");
		props[0].setValue("false");
		try {
			eventReceiverAdminServiceStub.deployWso2EventReceiverConfiguration(eventReceiverName,streamNameWithVersion,eventAdapterType,null,null,null,props,false,"");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}
