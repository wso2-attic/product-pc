package org.wso2.carbon.processCenter.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.wso2.carbon.processCenter.core.ProcessStore;
import org.wso2.carbon.processCenter.core.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dilini on 2/8/16.
 */
@Path("/process")
public class ProcessInformationViewer {

    private Log log = LogFactory.getLog(ProcessInformationViewer.class);

    @POST
    @Path("/byName")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response searchProcessByName(ProcessFilter processFilter){
//        ProcessStoreService storeService = (ProcessStoreService) PrivilegedCarbonContext.
//                getThreadLocalCarbonContext().getOSGiService(ProcessStoreService.class, null);
//        ProcessStore store = storeService.getProcessStore();
        ProcessStore store = new ProcessStore();
        String filter = processFilter.getFilter();
        String filterType = processFilter.getFilterType();
        String userName = processFilter.getUserName();
        JSONArray result = store.getProcessInformationByName(filter, filterType, userName);
        if(result != null){
            return Response.ok(result.toString()).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/filters")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getAdvanceSearchResult(org.wso2.carbon.processCenter.core.models.Process process){
        ProcessStore store = new ProcessStore();
        String result = store.getAdvanceSearchResult(process, process.getUserName());
        if(!result.equals("{}")){
            return Response.ok(result).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/bookmark")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response bookMarkProcess(String processID){
        ProcessStore store = new ProcessStore();
        processID = processID.split("=")[1];
        int result = store.bookMarkProcess(processID);
        if(result >= 0){
            JSONArray object = new JSONArray();
            if(result == 1)
                object.put("true");
            else if(result == 0)
                object.put("false");
            return Response.ok(object.toString()).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("bookmarkedProcesses")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getBookmarkedProcessList(String user){
        ProcessStore store = new ProcessStore();
        user = user.split("=")[1];
        String result = store.getBookmarkedProcessList(user);
        if(result != null){
            return Response.ok(result).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("processText")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getProcessText(String processTextPath){
        ProcessStore store = new ProcessStore();
        processTextPath = processTextPath.split("=")[1];
        processTextPath = processTextPath.replaceAll("%2F", "/");
        String result = store.getProcessText(processTextPath);
        if(!"FAILED TO GET TEXT CONTENT".equals(result)){
            return Response.ok(new JSONArray().put(result).toString()).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("associations")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getAssociations(String resourcePath){
        ProcessStore store = new ProcessStore();
        resourcePath = resourcePath.split("=")[1];
        resourcePath = resourcePath.replaceAll("%2F", "/");
        String result = store.getSuccessorPredecessorSubProcessList(resourcePath);
        if(!"".equals(result)){
            return Response.ok(new JSONArray().put(result).toString()).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("bpmnModel")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getBPMNModel(String bpmnPath){
        ProcessStore store = new ProcessStore();
        bpmnPath = bpmnPath.split("=")[1];
        bpmnPath = bpmnPath.replaceAll("%2F", "/");
        String result = store.getBPMN(bpmnPath);
        if(!"".equals(result)){
            return Response.ok(new JSONArray().put(result).toString()).status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
