/*
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
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
package org.wso2.carbon.processCenter.api;



import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.openjpa.persistence.jest.JSONObject;
import org.json.JSONArray;
import org.wso2.carbon.processCenter.core.ProcessStore;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import  javax.activation.DataHandler;

//@Path("/processcenter/")
public class ProcessCenterService {


    // @POST
    //@Path("/deployedprocesses/")
    // @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "text/plain"})
    private static final Log log = LogFactory.getLog(ProcessCenterService.class);


    @POST
    @Path("/getprocesses/")
    @Produces({"application/xml", "application/json"})
    public javax.ws.rs.core.Response getProcesses() {


        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().getProcesses();
        if (result != null) {

            return javax.ws.rs.core.Response.ok(result).build();
            //   return javax.ws.rs.core.Response.status(200)
            // .header(Endpoint.SESSION_TOKEN, session.getToken())
            //   .entity(new GenericEntity<List<org.json.JSONObject>>(result) {}).build();
            // javax.ws.rs.core.Response.getWriter

        } else {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }

        //throw new ProcessCenterException(msg, e);

    }

    @POST
    @Path("/getassociations/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public javax.ws.rs.core.Response getSucessorPredecessorSubprocessList(String processPath) {

        log.info(processPath + "################################");
        String path = (processPath.split("="))[1];
        String actualPath = path.replaceAll("%2F", "/");
        log.info(actualPath);

        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().getSucessorPredecessorSubprocessList(actualPath);
        if (result != "") {

            return javax.ws.rs.core.Response.ok(result).build();

        } else {

            //no associations
            return javax.ws.rs.core.Response.ok().build();

        }

    }


    @POST
    @Path("/processdetails/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public javax.ws.rs.core.Response getProcessDetails(String processPath) {

        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().getProcessDetails((processPath.split("=")[1]).replaceAll("%2F", "/"));
        if (result != null) {

            return javax.ws.rs.core.Response.ok(result).build();

        } else {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }
    }


    @POST
    @Path("/createprocess/")
    @Produces({MediaType.APPLICATION_XML, "text/plain"})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public javax.ws.rs.core.Response createProcess(String processInfo) {

        //processInfo=%7B%22processName%22%3A%22sda%22%2C%22processVersion%22%3A%22asdda%22%2C%22processOwner%22%3A%22admin%22%2C%22processTags%22%3A%22%22%2C%22subprocess%22%3A%5B%5D%2C%22successor%22%3A%5B%5D%2C%22predecessor%22%3A%5B%5D%7D
        String p = (processInfo.split("="))[1];
        String pReplaced = ((((((p.replaceAll("%7B", "{")).replaceAll("%22", "'")).replaceAll("%3A", ":")).replaceAll("%2C", ",")).replaceAll("%7D", "}")).replaceAll("%5B", "[")).replaceAll("%5D", "]");
        log.info(pReplaced + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().createProcess(pReplaced);
        log.info(result + "$$$$$$$$$$$$$$$$$$$$$$$$$$");
        if (result != null) {

            return javax.ws.rs.core.Response.ok(result).build();

        } else {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }
    }

    @POST
    @Path("/saveprocesstext/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public javax.ws.rs.core.Response saveProcessText(Process process) {
        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().saveProcessText(process.getProcessName(), process.getProcessVersion(), process.getProcessText());
        if (result == "OK") {

            return javax.ws.rs.core.Response.ok().build();

        } else {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }

    }


    @POST
    @Path("/getprocesstext/")
    @Produces({MediaType.APPLICATION_XML, "text/plain"})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public javax.ws.rs.core.Response getProcessText(String textPath) {

        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().getProcessText((textPath.split("=")[1]).replaceAll("%2F", "/"));
        if (result != "FAILED TO GET TEXT CONTENT") {

            return javax.ws.rs.core.Response.ok(result).build();

        } else {
            return Response.serverError().build();


        }

    }

    @POST
    @Path("/getbpmn/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public javax.ws.rs.core.Response getBPMN(String bpmnPath) {

        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().getBPMN((bpmnPath.split("=")[1]).replaceAll("%2F", "/"));
        if (result != null) {

            return javax.ws.rs.core.Response.ok(result).build();

        } else {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }
//
//    public javax.ws.rs.core.Response getBPMN(String bpmnPath) {
//    }

    }

    @POST
    //@Produces(MediaType.TEXT_HTML)
   // @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/uploadbpmn/")
    public void createBPMN(@FormParam("bpmnProcessName") String bpmnName,
                             @FormParam("bpmnProcessVersion") String bpmnVersion,
                             MultipartBody multipartBody,
                             @Context HttpServletResponse servletResponse) {

        log.info(bpmnName+bpmnVersion+"***********************");

        List<Attachment> attachments = multipartBody.getAllAttachments();
        Attachment file = multipartBody.getAttachment("file");
       // DataHandler dataHandler = attachments.get(0).getDataHandler();

        try {
            //InputStream inputStream = dataHandler.getInputStream();

            InputStream inputStream = file.getObject(InputStream.class);

            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer);
            String theString = writer.toString();

            log.info(theString);
            String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().createBPMN(bpmnName, bpmnVersion, inputStream);

            if (result != "FAILED TO CREATE BPMN") {
                //return Response.ok(result).build();
                log.info("successfully added bpmn");
                servletResponse.sendRedirect("../../../bpmn-process-center-explorer/process-details?q="+result);


            } else {
              // servletResponse.sendRedirect("../../../bpmn-process-center-explorer/process-details?q="+result);
                servletResponse.sendError(servletResponse.SC_BAD_REQUEST, "bpmn file upload failed");
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage(), e);

            //		return buildErrorResponse(e);

            // servletResponse.sendRedirect("designer");


        }

    }
        // IncomingFile incomingFile = attachment.getObject(IncomingFile.class);
        //  InputStream inputStream = attachment.getObject(InputStream.class);




//    return Response.status(200)
//            .entity("addUser is called, name : " + name + ", age : " + age)
//    .build();




    @POST
    @Path("/updateversion/")
  //  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateVersion(ProcessVersion processVersion, @Context HttpServletResponse servletResponse){

        String result = org.wso2.carbon.processCenter.core.ProcessStore.getInstance().updateVersion(processVersion.getProcessPath(), processVersion.getUpdatedVersion());
        if (result != "") {

            try {
               // servletResponse.sendRedirect("../../../bpmn-process-center-explorer/process-details?q=" + result);

                return Response.ok(result).build();
            }catch (Exception e){
                log.error(e.getMessage(), e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

            }

        } else {
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();

        }
    }

    }