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

import java.util.Properties;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;


@Path("/rest12345")
public class Proxy {
    @GET
    @Path("{var:.+}")
    @Produces(MediaType.APPLICATION_JSON)

    public String get(@Context UriInfo ui, @PathParam("var") String path) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        MultivaluedMap<String, String> pathParams = ui.getPathParameters();
    	String response = "{ \"data\":1919 }";
        return response;
    }
    
    @POST
    @Path("{var:.+}")
    @Produces(MediaType.APPLICATION_JSON)

    public String post(@Context UriInfo ui, @PathParam("var") String path, @Context HttpHeaders headers) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        MultivaluedMap<String, String> pathParams = ui.getPathParameters();
        MultivaluedMap<String, String> headersParams = headers.getRequestHeaders();
    	String response = "{ \"data\":1919 }";
        return response;
    }
    
    @PUT
    @Path("{var:.+}")
    @Produces(MediaType.APPLICATION_JSON)

    public String put(@Context UriInfo ui, @PathParam("var") String path, @Context HttpHeaders headers) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        MultivaluedMap<String, String> pathParams = ui.getPathParameters();
        MultivaluedMap<String, String> headersParams = headers.getRequestHeaders();
    	String response = "{ \"data\":1919 }";
        return response;
    }
    public String request(String url) {
    	
        final Client client;
         //String url = "http://localhost:9080/test";
         //String url = "https://dev181141.service-now.com/";
        //String url = "https://localhost:8080/";
        Response response = null;

          String json = null;
          try {
             
            client = ClientBuilder.newClient().register(new Authenticator("abel.tuter", "P4ssw0rd!"));
            //client = ClientBuilder.newClient().register(new Authenticator("aiops-topology-ibm-aiops-user", "wsa6cy0FZGnBuB+WQcCaaALS9yvuC6KSKtwW+H8kkws="));	 
            // Client client = ClientBuilder.newClient();
            
             WebTarget target = client.target(url);
             response = target.request().get();
             json = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);
             response.close();
             client.close();
                     
          } catch (Exception e) {
          	e.printStackTrace();
          }
          
          //String json = response.readEntity(String.class);
          //Jsonb JSONB = JsonbBuilder.create();
         // Properties sysProps = JSONB.fromJson(json, Properties.class);

         
          
          
          return json;
      }
}
