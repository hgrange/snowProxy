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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import org.apache.http.client.utils.URIUtils;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.DatatypeConverter;
import jakarta.json.JsonObject;

@Path("/svnow")

public class ServiceNowRequest {
    @GET


    @Produces(MediaType.APPLICATION_JSON)

    public String request() {
    	
      final Client client;
       //String url = "http://localhost:9080/test";
       String url = "https://dev181141.service-now.com/";
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
