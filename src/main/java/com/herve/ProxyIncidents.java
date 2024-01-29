// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package com.herve;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;


@RequestScoped
@Path("/")

public class ProxyIncidents  {
	
	 @Inject
	 private ReqRepDao reqrepDAO;
	 private ReqRep reqrep;
	 private Utils util = new Utils();
	
	static final Logger log = LoggerFactory.getLogger(ProxyIncidents.class);
	String url = System.getenv("SNOW_URL");
	String logsdir = System.getenv("SNOW_LOGS");
    
    @GET
    @Path("/nav_to.do")
    public Response nav(@Context HttpHeaders headers, @Context final UriInfo uriInfo) throws URISyntaxException, UnsupportedEncodingException {
    	
    	String path = uriInfo.getPath();
        String query = uriInfo.getRequestUri().getQuery().replaceAll("\\&", "%26");
        String uri = (url+path+"?"+query);
        URI sourceURI = new URI(uri);
    	  return Response.temporaryRedirect(sourceURI).build();
    }
    

    @GET
    @Path("/api/now/table/sys_user")
    @Transactional
    public String testConnection(@Context HttpHeaders headers, @Context UriInfo uriInfo) throws IOException, InterruptedException, ExecutionException, URISyntaxException {
    	return util.get(headers, uriInfo, "get-test-connection.log", reqrepDAO);
    }
    
    @GET
    @Path("/api/now/v2/table/change_request")

    public String getChangeRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo) throws IOException, InterruptedException, ExecutionException, URISyntaxException {
      return util.get(headers, uriInfo, "get-changeRequest.log", reqrepDAO);
    }	
    
    @GET
    @Path("/getreqrep")
 //   @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    
    public JsonObject getReqRep(@PathParam("req") String request) {   
    	 JsonObjectBuilder builder = Json.createObjectBuilder();
         ReqRep reqrep = reqrepDAO.readReqRep(request);
          if (reqrep != null) {
              builder.add("request", reqrep.getRequest()).add("response", reqrep.getResponse());
          }
          return builder.build();	
    }
    
        
    
    @GET
    @Path("{var:.+}")
    //@Produces(MediaType.APPLICATION_JSON)
    public String get(@Context HttpHeaders headers, @Context UriInfo uriInfo) throws IOException, InterruptedException, ExecutionException, URISyntaxException {
      return util.get(headers, uriInfo, "get.log", reqrepDAO);
    }
    
    @POST
    @Path("/api/x_ibm_waiops/aimanagerstory")
 //   @Consumes(MediaType.APPLICATION_JSON)
 //   @Produces(MediaType.APPLICATION_JSON)

    public String createIncident(String payLoad, @Context HttpHeaders headers, @Context UriInfo uriInfo ) throws IOException, InterruptedException, ExecutionException, URISyntaxException {
 

    	FileWriter fw = new FileWriter(logsdir+"/post-createIncident.log", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
      	

        String auth = headers.getRequestHeader("authorization").get(0);
        String path = uriInfo.getPath();
        String query = uriInfo.getRequestUri().getQuery();
     
         
        HttpClient httpClient = HttpClient.newHttpClient();
        URI sourceURI = new URI(url+path+"?"+query);
        HttpRequest request = HttpRequest.newBuilder(sourceURI).header("accept", "application/json")
                    .header("authorization", auth).POST(HttpRequest.BodyPublishers.ofString(payLoad)).build();
        
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> res = response.get();

        String bodyString = res.body();
            
        out.println(new Timestamp(System.currentTimeMillis())+" ---------- POST-createIncindent -------------");
        out.println("uri = "+sourceURI.toString());
        out.println("payLoad = "+ payLoad );
        out.println("status = "+ res.statusCode());
        out.println("rep = "+bodyString);

        out.close();
        bw.close();
        fw.close();
        return bodyString; 
       
    }

    @POST
    @Path("{var:.+}")
 //   @Consumes(MediaType.APPLICATION_JSON)
 //   @Produces(MediaType.APPLICATION_JSON)

    public String post(String payLoad, @Context HttpHeaders headers, @Context UriInfo uriInfo ) throws IOException, InterruptedException, ExecutionException, URISyntaxException {
  //  public String post(String payLoad) throws IOException, InterruptedException, ExecutionException, URISyntaxException {	
    	
    // payLoad = "{"story":{"topologyLink":"https://cpd-ibm-aiops.apps.ocp.159.8.91.9.nip.io/aiops/cfd95b7e-3bc7-4006-a4a8-a73a79c71255/resolution-hub/incidents/all/dfc6c1b1-29eb-43bc-a157-7ba164966569/topology",
    // "storyLink":"https://cpd-ibm-aiops.apps.ocp.159.8.91.9.nip.io/aiops/cfd95b7e-3bc7-4006-a4a8-a73a79c71255/resolution-hub/incidents/all/dfc6c1b1-29eb-43bc-a157-7ba164966569/overview",
    // "number":"38bb-ssnr","description":"Erroneous call rate is too high - ratings                                                                           ",
    // "id":"dfc6c1b1-29eb-43bc-a157-7ba164966569","detail":{"topologyDefinedApplications":[{"name":"RobotShop",
    // "detail":{"url":"https://cpd-ibm-aiops.apps.ocp.159.8.91.9.nip.io/aiops/default/services/zmWQy18cTO2N3OJlDMv40A/view"}}]},"state":"OPEN",
    // "title":"Erroneous call rate is too high - ratings                                                                           ","priority":1,
    // "associatedAlerts":[{"severity":5,"firstOccurrenceTime":"2023-11-03T16:58:33",
    // "summary":"Erroneous call rate is too high - ratings                                                                           ",
    // "lifecycleState":{"lastStateChangeTime":"2023-11-03T16:52:45","value":"open"},"resource":{"sourceId":"kubernetes","application":"robot-shop","name":"ratings-predictive","type":"host"},
    // "deduplicationKey":"{application=robot-shop, name=ratings-predictive, sourceId=kubernetes, type=host}-Instana Performance-","eventCount":1,"details":{},  ... more 8000 characters

    	FileWriter fw = new FileWriter(logsdir+"/post.log", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
      	

        String auth = headers.getRequestHeader("authorization").get(0);
        String path = uriInfo.getPath();
        String query = uriInfo.getRequestUri().getQuery();
     
         
        HttpClient httpClient = HttpClient.newHttpClient();
        URI sourceURI = new URI(url+path+"?"+query);
        HttpRequest request = HttpRequest.newBuilder(sourceURI).header("accept", "application/json")
                    .header("authorization", auth).POST(HttpRequest.BodyPublishers.ofString(payLoad)).build();
        
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> res = response.get();

        String bodyString = res.body();
        JSONObject json = new JSONObject(bodyString.trim());
          
            
        out.println(new Timestamp(System.currentTimeMillis())+" ---------- POST -------------");
        out.println("uri = "+sourceURI.toString());
        out.println("payLoad = "+ payLoad );
        out.println("status = "+ res.statusCode());
        out.println("rep = "+bodyString);
        //logger.log(Level.INFO, "rep-Get = "+json.toString());
            

        out.close();
        bw.close();
        fw.close();
        return bodyString; 
       
    }
    @PUT
    @Path("/setreqrep")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    
  
    public Response setReqRep( ReqRep reqrep) throws BadRequestException {
    	log.debug("reqrep = "+ reqrep); 

    	String request = reqrep.getRequest();
    	
    	ReqRep prevReqRep = reqrepDAO.readReqRep(request);
        if (prevReqRep == null) {
        	reqrepDAO.createReqRep(reqrep);
            return Response.status(Response.Status.CREATED).build();        
        }
       if (!reqrepDAO.findRequest(request).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("RequestResponse already exists").build();
        }

        reqrepDAO.updateReqRep(reqrep);
        return Response.status(Response.Status.NO_CONTENT).build();
    	
    	
    }
    

    @PUT
    @Path("{var:.+}")
 //   @Consumes(MediaType.APPLICATION_JSON)
 //   @Produces(MediaType.APPLICATION_JSON)

    public String put(String payLoad, @Context HttpHeaders headers, @Context UriInfo uriInfo ) throws IOException, InterruptedException, ExecutionException, URISyntaxException {

    	FileWriter fw = new FileWriter(logsdir+"/put.log", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
      	

        String auth = headers.getRequestHeader("authorization").get(0);
        String path = uriInfo.getPath();
        String query = uriInfo.getRequestUri().getQuery();
     
         
        HttpClient httpClient = HttpClient.newHttpClient();
        URI sourceURI = new URI(url+path+"?"+query);
        HttpRequest request = HttpRequest.newBuilder(sourceURI).header("accept", "application/json")
                    .header("authorization", auth).PUT(HttpRequest.BodyPublishers.ofString(payLoad)).build();
        
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> res = response.get();

        String bodyString = res.body();
        JSONObject json = new JSONObject(bodyString.trim());
          
            
        out.println(new Timestamp(System.currentTimeMillis())+" ---------- PUT -------------");
        out.println("uri = "+sourceURI.toString());
        out.println("body = "+ payLoad );
        out.println("status = "+ res.statusCode());
        out.println("rep = "+bodyString);
        //logger.log(Level.INFO, "rep-Get = "+json.toString());
            

        out.close();
        bw.close();
        fw.close();
        return bodyString; 
       
    }
    @PATCH
    @Path("{var:.+}")
 //   @Consumes(MediaType.APPLICATION_JSON)
 //   @Produces(MediaType.APPLICATION_JSON)

    public String patch(String payLoad, @Context HttpHeaders headers, @Context UriInfo uriInfo ) throws IOException, InterruptedException, ExecutionException, URISyntaxException {

    	FileWriter fw = new FileWriter(logsdir+"/patch.log", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
      	

        String auth = headers.getRequestHeader("authorization").get(0);
        String path = uriInfo.getPath();
        String query = uriInfo.getRequestUri().getQuery();
     
         
        HttpClient httpClient = HttpClient.newHttpClient();
        URI sourceURI = new URI(url+path+"?"+query);
        HttpRequest request = HttpRequest.newBuilder(sourceURI).header("accept", "application/json")
                    .header("authorization", auth).method("PATCH",HttpRequest.BodyPublishers.ofString(payLoad)).build();
        
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> res = response.get();

        String bodyString = res.body();
        JSONObject json = new JSONObject(bodyString.trim());
          
            
        out.println(new Timestamp(System.currentTimeMillis())+" ---------- PATCH -------------");
        out.println("uri = "+sourceURI.toString());
        out.println("body = "+ payLoad );
        out.println("status = "+ res.statusCode());
        out.println("rep = "+bodyString);
        //logger.log(Level.INFO, "rep-Get = "+json.toString());
            

        out.close();
        bw.close();
        fw.close();
        return bodyString; 
       
    }




}
