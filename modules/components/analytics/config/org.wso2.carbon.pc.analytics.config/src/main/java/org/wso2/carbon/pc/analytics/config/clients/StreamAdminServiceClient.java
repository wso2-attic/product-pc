package org.wso2.carbon.pc.analytics.config.clients;

/**
 * Created by samithac on 13/2/16.
 */
import com.google.gson.JsonElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.netbeans.lib.cvsclient.commandLine.command.log;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse;
import org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames;

//import org.wso2.carbon.service.mgt.stub.ServiceAdminStub;
//import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaDataWrapper;
import java.rmi.RemoteException;

public class StreamAdminServiceClient {
	private final String serviceName = "EventStreamAdminService";
	private EventStreamAdminServiceStub serviceAdminStub;
	private String endPoint;
	ServiceClient serviceClient;
	Options option;
	private static final Log log = LogFactory.getLog(StreamAdminServiceClient.class);
	JSONObject streamDefinitionJsonOb;


	public StreamAdminServiceClient(String backEndUrl, String session, String streamName, String streamVersion, String streamId, String streamNickName, String streamDescription, JSONArray processVariablesJObArr)
			throws AxisFault {
		this.endPoint = backEndUrl + "/services/" + serviceName;
		serviceAdminStub = new EventStreamAdminServiceStub(endPoint);


		// Authenticate Your stub from sessionCooke
		serviceClient = serviceAdminStub._getServiceClient();
		option = serviceClient.getOptions();
		// option.setAction("urn:listServices");
		// System.out.println(option.getAction());
		// option.setTo(new EndpointReference(endPoint));
		option.setManageSession(true);
		option.setProperty(
				org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING,
				session);

		streamDefinitionJsonOb=new JSONObject();
		try {
			streamDefinitionJsonOb.put("streamId",streamId) ;
			streamDefinitionJsonOb.put("name",streamName);
			streamDefinitionJsonOb.put("version",streamVersion);
			streamDefinitionJsonOb.put("nickName",streamNickName);
			streamDefinitionJsonOb.put("description",streamDescription);
			//streamDefinitionJsonOb.pu

			JSONArray processVariablesJArr=new JSONArray();

			//setting process variables as payloadData into the eventStream definition
			streamDefinitionJsonOb.put("payloadData", processVariablesJObArr);
			log.info(streamDefinitionJsonOb.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}



	/*
	 * public void deleteService(String[] serviceGroup) throws RemoteException {
	 * serviceAdminStub.deleteServiceGroups(serviceGroup);
	 * 
	 * }
	 */
	public String getGG() {
		GetStreamNames gsn = new GetStreamNames();

		try {
			return serviceAdminStub.getStreamNames(gsn).toString();
			// return serviceAdminStub.get
		} catch (RemoteException e) {
			e.printStackTrace();
			return "ssss";
		}
	}

	/*
	 * public ServiceMetaDataWrapper listServices() throws RemoteException {
	 * return serviceAdminStub.listServices("ALL", "*", 0);
	 * 
	 * }
	 */

	public void createEventStream() {
		//String streamId=streamName+":"+streamVersion;
		/*GetAllEventStreamDefinitionDtoResponse gaesdr = null;
		try {
			gaesdr = serviceAdminStub
					.getAllEventStreamDefinitionDto(new GetAllEventStreamDefinitionDto());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("getAllEventStreamDefinitionDto:"
				+ gaesdr.toString());

		GetStreamDetailsForStreamId getStreamDetailsForStreamId = new GetStreamDetailsForStreamId();
		getStreamDetailsForStreamId
				.setStreamId("BPMN_Process_Instance_Data_Publish:1.0.0");
		GetStreamDetailsForStreamIdResponse getStreamDetailsForStreamIdResponse = null;
		try {
			getStreamDetailsForStreamIdResponse = serviceAdminStub
					.getStreamDetailsForStreamId(getStreamDetailsForStreamId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("getStreamDetailsForStreamId:"
				+ getStreamDetailsForStreamIdResponse.toString());*/

		AddEventStreamDefinitionAsString addEventStreamDefinitionAsString = new AddEventStreamDefinitionAsString();
		AddEventStreamDefinitionAsStringResponse addEventStreamDefinitionAsStringResponse = null;
		//addEventStreamDefinitionAsString
		//		.setStreamStringDefinition("{ \"streamId\": \"streamId\",  			  \"name\": \"org.wso2.test1\",    			  \"version\": \"1.0.0\",    			  \"nickName\": \"TestStream\",    			  \"description\": \"Test Stream\", \"metaData\": [    {      \"name\": \"ip\",      \"type\": \"STRING\"    }  ],  \"correlationData\": [    {      \"name\": \"id\",      \"type\": \"LONG\"    }  ],  \"payloadData\": [    {      \"name\": \"testMessage\",    \"type\": \"STRING\"   }  ]}");
		addEventStreamDefinitionAsString.setStreamStringDefinition(streamDefinitionJsonOb.toString());
		try {
			addEventStreamDefinitionAsStringResponse = serviceAdminStub
					.addEventStreamDefinitionAsString(addEventStreamDefinitionAsString);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Stream creation success:"
				+ addEventStreamDefinitionAsStringResponse.get_return());
	}

}